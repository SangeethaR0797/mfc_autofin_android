package v2.view.base

import android.app.Activity
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import v2.model.dto.AddLeadRequest
import v2.view.VehicleSelectionFragDirections
import java.text.NumberFormat
import java.util.*


public open class BaseFragment : Fragment() {
    //region validation
    public fun isValidVehicleRegNo(vehicleRegNo: String): Boolean {
        return Regex(pattern = "[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}").matches(vehicleRegNo)
    }
    //

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
    public fun navigateToAddOrUpdateVehicleDetails(addLeadRequest: AddLeadRequest) {
        val directions = VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(addLeadRequest!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    //endregion screen Navigation

    //region message
    fun showToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
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