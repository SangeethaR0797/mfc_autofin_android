package v2.view.base

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import v2.model.dto.VehicleAddUpdateDTO
import v2.view.VehicleSelectionFragDirections

public open class BaseFragment : Fragment() {

    public fun navigateAddOrUpdateVehicleDetails(vehicleAddUpdateDTO: VehicleAddUpdateDTO) {
        val directions = VehicleSelectionFragDirections.actionVehicleSelectionFrag2ToAddOrUpdateVehicleDetailsMakeFrag(vehicleAddUpdateDTO!!)
        view?.let {
            Navigation.findNavController(it).navigate(directions)
        }
    }
}