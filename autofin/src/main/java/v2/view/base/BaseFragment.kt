package v2.view.base

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R
import v2.model.dto.VehicleAddUpdateDTO
import v2.view.VehicleSelectionFragDirections


public open class BaseFragment : Fragment() {

    //region screen Navigation

    public fun navigateFromDashBoard(id:Int)
    {
        view?.let {
                Navigation.findNavController(it).navigate(id)
        }
    }

    public fun navigateToStockResFrag()
    {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.stockAPIFragment)
        }
    }

    public fun navigateToAddOrUpdateVehicleDetails(vehicleAddUpdateDTO: VehicleAddUpdateDTO) {
        val directions = VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(vehicleAddUpdateDTO!!)
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