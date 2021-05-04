package v2.view.base

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R
import v2.help.CurrencyData
import v2.model.dto.AddLeadRequest
import v2.model.response.StockDetails
import v2.view.AddOrUpdateVehicleDetailsMakeFragDirections
import v2.view.VehicleSelectionFragDirections
import v2.view.other_activity.VehBasicDetailsActivity
import v2.view.utility_view.StockAPIFragmentArgs
import v2.view.utility_view.StockAPIFragmentDirections
import java.text.NumberFormat
import java.util.*


public open class BaseFragment : Fragment() {

    public var caseId=""
    //region validation

    public fun isValidVehicleRegNo(vehicleRegNo: String): Boolean {
        return Regex(pattern = "[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}").matches(vehicleRegNo)
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
        var formattedRegNum = regNo.substring(0, 2) + separator + regNo.substring(2, 4) + separator + regNo.substring(4, 6) + separator + regNo.substring(6, 10)
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
        val stockDirection = VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToStockAPIFragment(stockDetails)
        view?.let {
            Navigation.findNavController(it).navigate(stockDirection)
        }
    }

    public fun navigateVehBasicDetailsActivity(requestCode: Int) {
        val carBasicDetailsActivity = Intent(activity, VehBasicDetailsActivity::class.java)
        startActivityForResult(carBasicDetailsActivity, requestCode)
    }

    public fun navigateToAddOrUpdateVehicleDetails(addLeadRequest: AddLeadRequest) {
        val directions= VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(addLeadRequest)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navigateToAddLeadFragment(addLeadRequest: AddLeadRequest) {
        val directions = AddOrUpdateVehicleDetailsMakeFragDirections.actionAddOrUpdateVehicleDetailsMakeFragToAddLeadDetailsFrag(addLeadRequest!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun stockToAddLeadFragment(addLeadRequest: AddLeadRequest) {
        val directions = StockAPIFragmentDirections.actionStockAPIFragmentToAddLeadDetailsFrag(addLeadRequest!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun navToSoftOffer() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.softOfferFragment2)
        }
    }

    //endregion screen Navigation

    //region message
    fun showToast(message: String) {
        hideSoftKeyboard()
        /*  val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
          toast.setGravity(Gravity.CENTER, 0, 0)
          toast.show()*/

        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.v2_toast_layout, activity?.findViewById(R.id.toast_layout_root) as ViewGroup?)

        val image: ImageView = layout.findViewById<View>(R.id.image) as ImageView
        // image.visibility = View.GONE
        val text = layout.findViewById<View>(R.id.text) as TextView
        text.text = message

        val toast = Toast(activity)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
    //endregion message

    //region keyboard function
    open fun hideSoftKeyboard() {
        try {
            if (activity?.currentFocus != null) {
                val inputMethodManager = activity?.getSystemService(
                        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                        activity?.currentFocus!!.windowToken, 0)
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
                        Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                        view.windowToken, 0)
            } else {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        } catch (e: Exception) {
        }
    }
//endregion keyboard function
}