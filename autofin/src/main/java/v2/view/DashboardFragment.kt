package v2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R


class DashboardFragment : Fragment(), View.OnClickListener {

    lateinit var btnRegisteredList:Button
    lateinit var btnNewVehicle:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        btnRegisteredList=view.findViewById(R.id.btnRegisteredList)
        btnNewVehicle=view.findViewById(R.id.btnNewVehicle)
        btnRegisteredList.setOnClickListener(this)
        btnNewVehicle.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnRegisteredList -> {
                    Navigation.findNavController(v).navigate(R.id.registeredListFragmentNav)
                }
                R.id.btnNewVehicle -> {
                    Navigation.findNavController(v).navigate(R.id.newVehicleFragment)
                }
            }
        }
    }

    companion object {

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

}