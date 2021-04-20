package  v2.view.utility_view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R

class StockAPIFragment : Fragment(),View.OnClickListener {

    lateinit var tvVehMake: TextView
    lateinit var tvVehModelVariant: TextView
    lateinit var tvVehDetailsDesc: TextView
    lateinit var tvVehRegNum: TextView
    lateinit var ibEditVehDetails: ImageButton
    lateinit var btnVehicleReg: Button

    private lateinit var viewModel: StockAPIViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view= inflater.inflate(R.layout.stock_a_p_i_fragment, container, false)
        initViews(view)
        return view
    }

    private fun initViews(view: View?) {
        tvVehMake= view?.findViewById(R.id.tvVehMake)!!
        tvVehModelVariant= view?.findViewById(R.id.tvVehModelVariant)!!
        tvVehDetailsDesc= view?.findViewById(R.id.tvVehDetailsDesc)!!
        tvVehRegNum= view?.findViewById(R.id.tvVehRegNum)!!
        ibEditVehDetails= view?.findViewById(R.id.ibEditVehDetails)!!
        btnVehicleReg= view?.findViewById(R.id.btnVehicleReg)!!

        ibEditVehDetails.setOnClickListener(this)
        btnVehicleReg.setOnClickListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StockAPIViewModel::class.java)
    }

    override fun onClick(v: View?) {
if(v!=null)
{
    when(v.id)
    {
        R.id.ibEditVehDetails->
        {
            Navigation.findNavController(v).navigate(R.id.registeredListFragmentNav)
        }
        R.id.btnVehicleReg->
        {
            Navigation.findNavController(v).navigate(R.id.registeredListFragmentNav)
        }

    }
}
    }

}