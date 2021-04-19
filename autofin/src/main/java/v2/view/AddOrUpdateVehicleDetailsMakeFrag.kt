package v2.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddOrUpdateVehicleDetailsMakeFrag.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddOrUpdateVehicleDetailsMakeFrag : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var ivBack: ImageView
    lateinit var tvTitle: TextView
    lateinit var tvSelectedText: TextView

    lateinit var llOwnership: LinearLayout
    lateinit var llKilometresDriven: LinearLayout
    lateinit var llFuleType: LinearLayout

    lateinit var llVehiclePrice: LinearLayout
    lateinit var btnNext: Button

    lateinit var rvOwnership: RecyclerView
    lateinit var rvKilometresDriven: RecyclerView
    lateinit var rvFuleType: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_fragment_veh_make, container, false)
        ivBack = view.findViewById(R.id.iv_back)
        tvTitle = view.findViewById(R.id.tv_title)
        tvSelectedText = view.findViewById(R.id.tv_selected_text)

        llOwnership = view.findViewById(R.id.ll_ownership)
        llKilometresDriven = view.findViewById(R.id.ll_kilometres_driven)
        llFuleType = view.findViewById(R.id.ll_fule_type)
        llVehiclePrice = view.findViewById(R.id.ll_vehicle_price)

        rvOwnership = view.findViewById(R.id.rv_ownership)
        rvKilometresDriven = view.findViewById(R.id.rv_kilometres_driven)
        rvFuleType = view.findViewById(R.id.rv_fule_type)


        btnNext = view.findViewById(R.id.btn_next)


        addEvent();
        return view
    }

    fun addEvent() {
        ivBack.setOnClickListener(View.OnClickListener { activity?.onBackPressed() })

        btnNext.setOnClickListener(View.OnClickListener { })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment VehMakeFrag.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                AddOrUpdateVehicleDetailsMakeFrag().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}