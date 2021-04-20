package v2.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R
import utility.CommonStrings
import v2.model.dto.VehicleAddUpdateDTO
import v2.view.other_activity.VehBasicDetailsActivity

public class VehicleSelectionFrag : Fragment(), View.OnClickListener {

    lateinit var etVehRegNum: EditText
    lateinit var btnVehicleReg: Button
    lateinit var tvSearchCar: TextView

    var regNoVal: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_selection, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {
        etVehRegNum = view?.findViewById(R.id.etVehRegNum)!!
        btnVehicleReg = view.findViewById(R.id.btnVehicleReg)
        tvSearchCar = view.findViewById(R.id.tvSearchCar)
        tvSearchCar.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnVehicleReg -> {
                    if (etVehRegNum.text.isNotEmpty()) {
                        regNoVal = etVehRegNum.text.toString()
                        checkRegNoAvailable()
                    } else {
                        Toast.makeText(activity, "Please enter Vehicle Registration Number", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.tvSearchCar -> {


                    val carBasicDetailsActivity = Intent(activity, VehBasicDetailsActivity::class.java)

                    startActivityForResult(carBasicDetailsActivity, CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE)

                }

            }
        }

    }

    private fun checkRegNoAvailable() {
        if (isValidRegNo()) {
            Toast.makeText(activity, "Valid RegNo", Toast.LENGTH_SHORT).show()
            // need to write API call to check given reg no is available

        } else {
            Toast.makeText(activity, "Please enter valid Registration Number", Toast.LENGTH_SHORT).show()
        }

    }

    private fun isValidRegNo(): Boolean {
        return Regex(pattern = "[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$/n").matches(regNoVal)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CommonStrings.CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE && resultCode == CommonStrings.RESULT_CODE) {
            var vehicleAddUpdateDTO: VehicleAddUpdateDTO? = data?.getParcelableExtra(CommonStrings.VEHICLE_DATA)

            var year: String? = vehicleAddUpdateDTO?.year


            view?.let { Navigation.findNavController(it).navigate(R.id.addOrUpdateVehicleDetailsMakeFrag) }
        }

    }
}