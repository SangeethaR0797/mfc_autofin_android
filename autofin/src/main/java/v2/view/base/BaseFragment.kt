package v2.view.base

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import utility.CommonStrings
import v2.FinalOTPFragmentDirections
import v2.help.CurrencyData
import v2.model.dto.AddLeadRequest
import v2.model.dto.CustomLoanProcessCompletedData
import v2.model.response.CustomerDetailsResponse
import v2.model.response.StockDetails
import v2.model.response.master.KYCDocumentResponse
import v2.service.utility.ApiResponse
import v2.service.utility.ErrorUtils
import v2.view.*
import v2.view.callBackInterface.DatePickerCallBack
import v2.view.other_activity.MasterDataSelectionActivity
import v2.view.other_activity.VehBasicDetailsActivity
import v2.view.utility_view.SelectedBankOfferStatusFragmentDirections
import v2.view.utility_view.StockAPIFragmentDirections
import v2.view.utility_view.WebViewActivity
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


public open class BaseFragment : Fragment() {
    override fun onResume() {
        super.onResume()

    }

    var customerId = ""
    var alertDialog: AlertDialog? = null

    var cal = Calendar.getInstance()
    private lateinit var datePickerCallBack: DatePickerCallBack
    public val DATE_FORMATE_DDMMYYYY = "dd/MM/yyyy"
    public val DATE_FORMATE_DDMMMYYYY = "dd MMM yyyy"
    public val DATE_FORMATE_YYYYMMDD = "yyyy-MM-dd"

    //region DatePicker
    public fun getBackDateFromTodayDate(yearBack: Int): Date {
        val cal = Calendar.getInstance()
        val today = cal.time
        cal.add(Calendar.YEAR, -yearBack) // to get previous year add -1
        return cal.time
    }

    public fun getTodayDate(): Date {
        val cal = Calendar.getInstance()
        return cal.time
    }


    public fun stringToDateString(
        value: String,
        sourceDateFormat: String,
        targetDateFormat: String
    ): String {
        val date = SimpleDateFormat(sourceDateFormat).parse(value)
        return SimpleDateFormat(targetDateFormat).format(date)
    }

    val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePicker, year: Int, monthOfYear: Int,
            dayOfMonth: Int
        ) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat(DATE_FORMATE_DDMMYYYY, Locale.US)
            val sdfValue = SimpleDateFormat(DATE_FORMATE_YYYYMMDD, Locale.US)
            var dateDisplayValue: String = sdf.format(cal.getTime())
            var dateValue: String = sdfValue.format(cal.getTime())

            if (datePickerCallBack != null) {
                datePickerCallBack.dateSelected(dateDisplayValue, dateValue)
            }
        }
    }

    public fun callDatePickerDialog(
        lastSelectedDateValue: String?,
        minDate: Date?,
        maxDate: Date?,
        datepickerCallBack: DatePickerCallBack
    ) {
        datePickerCallBack = datepickerCallBack
        var d: Int? = cal.get(Calendar.DAY_OF_MONTH)
        var m: Int? = cal.get(Calendar.MONTH)
        var y: Int? = cal.get(Calendar.YEAR)
        try {
            if (!TextUtils.isEmpty(lastSelectedDateValue)) {
                d = lastSelectedDateValue!!.subSequence(8, 10).toString().toInt()
                m = lastSelectedDateValue!!.subSequence(5, 7).toString().toInt()
                y = lastSelectedDateValue!!.subSequence(0, 4).toString().toInt()
            }
        } catch (e: IndexOutOfBoundsException) {

        }
        var datePickerDialog: DatePickerDialog?
        datePickerDialog = context?.let { DatePickerDialog(it, dateSetListener, y!!, m!!, d!!) }
        if (minDate != null) {
            datePickerDialog!!.datePicker.minDate = minDate.time
        }
        if (maxDate != null) {
            datePickerDialog!!.datePicker.maxDate = maxDate.time
        }

        datePickerDialog!!.show()

    }
    //endregion DatePicker
    //region validation

    public fun isValidVehicleRegNo(vehicleRegNo: String): Boolean {
        val expression = "[a-zA-Z]{2}[0-9]{2}[a-zA-Z]{2}[0-9]{4}"
        //return Regex(pattern = expression).matches(vehicleRegNo)

        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(vehicleRegNo)
        return matcher.matches()
    }

    public fun validName(name: String): Boolean {
        val expression = "[a-zA-Z]"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(name)
        return matcher.matches()
    }


    public fun isEmailValid(email: String?): Boolean {
        val expression =
            "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    public fun isValidPanNo(panNumber: String): Boolean {
        val expression = "[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}"
        //return Regex(pattern = expression).matches(panNumber)

        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(panNumber)
        return matcher.matches()
    }
    //endregion validation

    //region utility function

    public fun getAmountInWords(value: String): String {
        if (!TextUtils.isEmpty(value)) {
            return CurrencyData.convertToIndianCurrency(value)
        } else {
            return ""
        }
    }

    public fun formatAmount(value: String): String {
        if (!TextUtils.isEmpty(value)) {
            var format: NumberFormat? = NumberFormat.getInstance(Locale.US)
            return format!!.format(value.toLong())
        } else {
            return ""
        }
    }

    public fun unformatAmount(value: String): String {
        if (!TextUtils.isEmpty(value)) {
            return value.replace(",", "")
        } else {
            return ""
        }
    }

    public fun formatOwner(owner: String): String {
        return when {
            owner == "1" -> {
                "1st"
            }
            owner == "2" -> {
                "2nd"
            }
            owner == "3" -> {
                "3rd"
            }
            else -> owner + "th"
        }
    }

    public fun formatVehNum(regNo: String): String {
        val separator = "-"
        var formattedRegNum =
            regNo.substring(0, 2) + separator + regNo.substring(2, 4) + separator + regNo.substring(
                4,
                6
            ) + separator + regNo.substring(6, 10)
        return formattedRegNum
    }


    //endregion utility function

    //region screen Navigation

    public fun navigateFromDashBoard(id: Int) {
        view?.let {
            Navigation.findNavController(it).navigate(id)
        }
    }

    public fun navigateToStockResFrag(stockDetails: StockDetails) {
        val stockDirection =
            VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToStockAPIFragment(
                stockDetails
            )
        view?.let {
            Navigation.findNavController(it).navigate(stockDirection)
        }
    }

    public fun navigateVehBasicDetailsActivity(requestCode: Int) {
        val carBasicDetailsActivity = Intent(activity, VehBasicDetailsActivity::class.java)
        startActivityForResult(carBasicDetailsActivity, requestCode)
    }

    public fun navigateMasterDataSelectionActivity(requestCode: Int, dataType: String) {
        val activity = Intent(activity, MasterDataSelectionActivity::class.java)
        activity.putExtra(CommonStrings.SELECTED_DATA_TYPE, dataType)
        startActivityForResult(activity, requestCode)
    }

    public fun navigateToAddOrUpdateVehicleDetails(addLeadRequest: AddLeadRequest) {
        val directions =
            VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(
                addLeadRequest!!
            )
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }


    public fun navigateToMobileNumber() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_addOrUpdateVehicleDetailsMakeFrag_to_addLeadDetailsFrag)
        }
    }

    public fun navigateDashboardTop() {
        view?.let {
            Navigation.findNavController(it)
                .navigate(R.id.action_to_dashboard_top)
        }
    }

    public fun navigateToAddLeadFragment(
        addLeadRequest: AddLeadRequest,
        customerId: Int,
        mobile: String?
    ) {
        val directions =
            AddOrUpdateVehicleDetailsMakeFragDirections.actionAddOrUpdateVehicleDetailsMakeFragToAddLeadDetailsFrag(
                addLeadRequest!!, customerId, mobile
            )
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun stockToAddLeadFragment(
        addLeadRequest: AddLeadRequest, customerId: Int,
        mobile: String?
    ) {
        val directions =
            StockAPIFragmentDirections.actionStockAPIFragmentToAddLeadDetailsFrag(
                addLeadRequest!!,
                customerId,
                mobile
            )
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navToSoftOfferFromApplicationListFragment(
        customerDetailsResponse: CustomerDetailsResponse,
        customerId: String
    ) {
        val directions =
            ApplicationDetailsFragmentDirections.actionAddLeadDetailsFragToSoftOfferFragment2(
                customerDetailsResponse!!,
                customerId
            )
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }


    public fun navToSoftOffer(
        customerDetailsResponse: CustomerDetailsResponse,
        customerId: String,
        fragmentTag: String
    ) {
        when (fragmentTag) {
            CommonStrings.ADD_LEAD_FRAGMENT_TAG -> {
                val directions =
                    AddLeadDetailsFragDirections.actionAddLeadDetailsFragToSoftOfferFragment2(
                        customerDetailsResponse!!,
                        customerId
                    )
                view?.let {
                    Navigation.findNavController(it).navigate(directions)
                }

            }
            CommonStrings.APPLICATION_LIST_FRAGMENT_TAG -> {
                val directions =
                    ApplicationListFragmentDirections.actionApplicationListFragmentToSoftOfferFragment2(
                        customerDetailsResponse!!,
                        customerId
                    )
                view?.let {
                    Navigation.findNavController(it).navigate(directions)
                }

            }
            CommonStrings.APPLICATION_LEADS_FRAGMENT_TAG -> {
                val directions =
                    ApplicationDetailsFragmentDirections.actionAddLeadDetailsFragToSoftOfferFragment2(
                        customerDetailsResponse!!,
                        customerId
                    )
                view?.let {
                    Navigation.findNavController(it).navigate(directions)
                }

            }
        }
    }


    public fun navigateToKYCDocumentUpload(
        customerId: String,
        kycDocumentData: KYCDocumentResponse,
        caseID: String,
        customerData: CustomerDetailsResponse
    ) {
        val directions =
            AddressAndAdditionalFieldsFragmentDirections.actionAddressAndAdditionalFieldsFragmentToDocumentUploadFragment(
                customerId,
                kycDocumentData,
                caseID,
                customerData
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }
    }

    public fun navigateToKYCDocumentUploadFromApplicationList(
        customerId: String,
        kycDocumentData: KYCDocumentResponse,
        caseID: String,
        customerData: CustomerDetailsResponse
    ) {
        val directions =
            ApplicationListFragmentDirections.actionApplicationListFragmentToDocumentUploadFragment(
                customerId,
                kycDocumentData,
                caseID,
                customerData
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }
    }

    public fun navigateToAddressAdditionalFields(
        custId: Int,
        customerDetails: CustomerDetailsResponse
    ) {
        val directions =
            ApplicationListFragmentDirections.actionApplicationListFragmentToAddressAndAdditionalFieldsFragment2(
                custId,
                customerDetails
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }

    }

    public fun navigateToBankOfferStatusFromApplicationListFrag(
        custId: Int,
        customerDetails: CustomerDetailsResponse
    ) {

        val directions =
            ApplicationListFragmentDirections.actionApplicationListFragmentToSelectedBankOfferStatusFragment(
                custId.toString(),
                customerDetails
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }

    }

    public fun navigateToBankOfferStatus(
        custId: String,
        customerDetails: CustomerDetailsResponse,
        fragment: String
    ) {

        if (fragment == "AddressAdditionalFields") {
            val directions =
                AddressAndAdditionalFieldsFragmentDirections.actionAddressAndAdditionalFieldsFragmentToSelectedBankOfferStatusFragment(
                    custId,
                    customerDetails
                )
            view.let {
                it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
            }
        } else if (fragment == "ADD_LEAD") {
            val directions =
                AddLeadDetailsFragDirections.actionAddLeadDetailsFragToSelectedBankOfferStatusFragment(
                    custId,
                    customerDetails
                )
            view.let {
                it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
            }
        } else {
            val directions =
                DocumentUploadFragmentDirections.actionDocumentUploadFragmentToSelectedBankOfferStatusFragment(
                    custId,
                    customerDetails
                )
            view.let {
                it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
            }
        }

    }

    public fun navigateToApplicationDetails(custId: Int) {
        val directions =
            AddLeadDetailsFragDirections.actionAddLeadDetailsFragToApplicationDetailsFragment(custId)
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }
    }

    public fun navigateToBankSuccessPageFromSoftOffer(loanProcessCompletedData: CustomLoanProcessCompletedData) {
        val directions =
            ApplicationListFragmentDirections.actionApplicationListFragmentToBankSuccessMessageFragment2(
                loanProcessCompletedData
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }
    }

    public fun navigateToFinalOTPFragment(
        customerId: String,
        mobileNum: String,
        loanProcessCompletedData: CustomLoanProcessCompletedData
    ) {
        val directions =
            SelectedBankOfferStatusFragmentDirections.actionSelectedBankOfferStatusFragmentToFinalOTPFragment(
                customerId,
                mobileNum,
                loanProcessCompletedData
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }
    }


    public fun navigateToSuccessFragment(loanProcessCompletedData: CustomLoanProcessCompletedData) {
        val directions =
            FinalOTPFragmentDirections.actionFinalOTPFragmentToBankSuccessMessageFragment(
                loanProcessCompletedData
            )
        view.let {
            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
        }
    }


    public fun openWebViewActivity(title: String?, url: String?) {
        val intent = Intent(activity, WebViewActivity::class.java)
        intent.putExtra(CommonStrings.URL, url)
        intent.putExtra(CommonStrings.TITLE, title)
        startActivity(intent)
    }

    public fun navigateNoticeBoardAndNotificationFragment(screenType: String) {
        val directions =
            DashboardFragmentDirections.actionNoticeBoardAndNotificationFragment(screenType!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateApplicationListFragment(screenType: String, screenStatus: String?) {
        val directions =
            DashboardFragmentDirections.actionApplicationListFragment(screenType!!, screenStatus)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateToAddressAndAdditionalFieldsFragment(
        customerId: Int,
        customerDetails: CustomerDetailsResponse
    ) {
        val directions =
            SoftOfferFragmentDirections.actionSoftOfferFragment2ToAddressAndAdditionalFieldsFragment(
                customerId,
                customerDetails
            )
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateApplicationDetailsFragment(customerId: Int) {
        val directions =
            ApplicationListFragmentDirections.actionApplicationDetailsFragment(customerId)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateBankFeaturesAndChargesFragment(bankName: String, bankId: Int) {
        val directions =
            DashboardFragmentDirections.actionBankFeaturesAndChargesFragment(bankId, bankName!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateToLeadDetailsFromFinalScreen(customerId: Int) {
        // view?.let { Navigation.findNavController(it).navigate(R.id.action_bankSuccessMessageFragment_to_dashboardFragment) }
        val directions =
            BankSuccessMessageFragmentDirections.actionBankSuccessMessageFragmentToApplicationDetailsFragment(
                customerId
            )
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateToLeadDetails(customerId: Int) {
        val directions =
            ApplicationListFragmentDirections.actionApplicationDetailsFragment(customerId)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }
    //endregion screen Navigation

    //region message
    fun showToast(message: String) {
        try {


            hideSoftKeyboard()
            /*  val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
              toast.setGravity(Gravity.CENTER, 0, 0)
              toast.show()*/

            val inflater = layoutInflater!!
            val layout: View = inflater.inflate(
                R.layout.v2_toast_layout,
                activity?.findViewById(R.id.toast_layout_root) as ViewGroup?
            )

            val image: ImageView = layout.findViewById<View>(R.id.image) as ImageView
            // image.visibility = View.GONE
            val text = layout.findViewById<View>(R.id.text) as TextView
            text.text = message

            val toast = Toast(activity)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.duration = Toast.LENGTH_LONG
            toast.view = layout
            toast.show()
        } catch (e: java.lang.Exception) {

        }
    }
    //endregion message

    //region keyboard function
    open fun hideSoftKeyboard() {
        try {
            if (activity?.currentFocus != null) {
                val inputMethodManager = activity?.getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    activity?.currentFocus!!.windowToken, 0
                )
            } else {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        } catch (e: Exception) {
        }
    }

    open fun hideSoftKeyboard(view: View?) {
        try {
            if (view != null) {
                val inputMethodManager = activity?.getSystemService(
                    Activity.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken, 0
                )
            } else {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        } catch (e: Exception) {
        }
    }

    open fun showKeyBoardByForced() {
        try {
            val inputMethodManager = activity?.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } catch (e: Exception) {

        }

    }
//endregion keyboard function

    open fun parseCommonResponse(apiResponseOtp: ApiResponse) {
        when (apiResponseOtp.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {
            }
            ApiResponse.Status.ERROR -> {
                val apiError = ErrorUtils.parseThrowable(apiResponseOtp.error)
                if (apiError.getCode() == 0) {
                } else {
                    if (!TextUtils.isEmpty(apiError.getMessage())) {
                        showToast(apiError.getMessage()!!)
                    }
                }
            }
            else -> {
            }
        }
    }


    // Progress Dialog region starts

    private fun getAlertDialog(
        context: Context,
        layout: Int,
        setCancellationOnTouchOutside: Boolean
    ): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val customLayout: View =
            layoutInflater.inflate(layout, null)
        builder.setView(customLayout)
        val dialog = builder.create()
        dialog.setCanceledOnTouchOutside(setCancellationOnTouchOutside)
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        return dialog
    }

    fun showProgressDialog(context: Context): AlertDialog {
        if (alertDialog == null) {
            alertDialog = getAlertDialog(
                context,
                R.layout.layout_progress_dialog,
                setCancellationOnTouchOutside = false
            )
        }
        alertDialog!!.show()
        hideSoftKeyboard()
        return alertDialog!!
    }

    fun hideProgressDialog() {
        if (alertDialog != null && alertDialog!!.isShowing)
            alertDialog!!.dismiss()
    }


    // Progress Dialog region ends

    // Region Utility methods start

    private fun changeVectorFillColor(imgView: ImageView, id: Int) {
        DrawableCompat.setTint(imgView.getDrawable(), ContextCompat.getColor(requireContext(), id));
    }


    // Region Utility methods ends

    public fun setKeyBoardShowHideEvent(keyboardVisibilityEventListener: KeyboardVisibilityEventListener) {
        setEventListener(
            requireActivity(),
            viewLifecycleOwner,
            keyboardVisibilityEventListener
        )
    }

    fun makeCallOfMobileNumber(mobileNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + mobileNumber)
        startActivity(intent)
    }


    //region permission
    public fun checkCallPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }


    public fun askCallPermissions() {
        Log.d("Camera Permission Check", "Comes into Permission Check method")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CALL_PHONE),
            1
        )
    }
    //endregion permission

}