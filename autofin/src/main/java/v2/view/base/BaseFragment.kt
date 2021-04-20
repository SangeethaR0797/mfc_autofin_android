package v2.view.base

import android.view.Gravity
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import v2.model.dto.VehicleAddUpdateDTO
import v2.view.VehicleSelectionFragDirections


public open class BaseFragment : Fragment() {

    public fun navigateToAddOrUpdateVehicleDetails(vehicleAddUpdateDTO: VehicleAddUpdateDTO) {
        val directions = VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(vehicleAddUpdateDTO!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }

    public fun showToast(message: String) {
        val toast = Toast.makeText(activity, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }
}