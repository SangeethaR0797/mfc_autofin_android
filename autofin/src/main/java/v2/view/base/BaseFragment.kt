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
import v2.model.dto.AddLeadRequest
import v2.model.response.StockResponse
import v2.view.VehicleSelectionFragDirections
import v2.view.other_activity.VehBasicDetailsActivity
import java.text.NumberFormat
import java.util.*


public open class BaseFragment : Fragment() {


    //region validation

    public fun isValidVehicleRegNo(vehicleRegNo: String): Boolean {
        return Regex(pattern = "[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}").matches(vehicleRegNo)
    }
    //endregion validation

    //region utility function
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
    //endregion utility function

    //region screen Navigation

    public fun navigateFromDashBoard(id:Int)
    {
        view?.let {
                Navigation.findNavController(it).navigate(id)
        }
    }

    public fun navigateToStockResFrag(stockDetails:StockResponse)
    {
        val stockDirection=VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToStockAPIFragment(stockDetails)
        view?.let {
            Navigation.findNavController(it).navigate(stockDirection)
        }
    }

    public fun navigateVehBasicDetailsActivity(requestCode: Int) {
        val carBasicDetailsActivity = Intent(activity, VehBasicDetailsActivity::class.java)
        startActivityForResult(carBasicDetailsActivity, requestCode)
    }

    public fun navigateToAddOrUpdateVehicleDetails(addLeadRequest: AddLeadRequest) {
        val directions = VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(addLeadRequest!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
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
        image.visibility = View.GONE
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