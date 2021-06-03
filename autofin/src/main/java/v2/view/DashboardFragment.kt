package v2.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global
import v2.model.response.master.KYCDocumentResponse
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.base.BaseFragment


class DashboardFragment : BaseFragment(), View.OnClickListener {

    lateinit var btnRegisteredList:Button
    lateinit var btnNewVehicle:Button
    lateinit var kycDocumentViewModel: MasterViewModel
     private val hardCodedCustomerId="1724"
    private val hardCodedCaseId="0242210415000012"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kycDocumentViewModel = ViewModelProvider(this).get(MasterViewModel::class.java)
        kycDocumentViewModel.getKYCDocumentLiveData().observe(requireActivity()) { mApiResponse: ApiResponse? ->
            onGetKYCDocumentResponse(mApiResponse!!)
        }


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
                    kycDocumentViewModel.getKYCDocumentResponse(Global.baseURL + CommonStrings.KYC_UPLOAD_URL_END_POINT + hardCodedCustomerId)
                }
                R.id.btnNewVehicle -> {
                    navigateFromDashBoard(R.id.vehicleSelectionFrag2)
                }
            }
        }
    }


    private fun onGetKYCDocumentResponse(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                showProgressDialog(requireContext())
            }
            ApiResponse.Status.SUCCESS -> {
                hideProgressDialog()

                val kycDocumentRes: KYCDocumentResponse = mApiResponse.data as KYCDocumentResponse
                if (kycDocumentRes.statusCode == "100") {
                    if (kycDocumentRes.data.groupedDoc.isNotEmpty() || kycDocumentRes.data.nonGroupedDoc.isNotEmpty())
                    {
                        val directions=DashboardFragmentDirections.actionDashboardFragmentToDocumentUploadFragment(hardCodedCustomerId,kycDocumentRes,hardCodedCaseId)
                        view.let {
                            it?.let { it1 -> Navigation.findNavController(it1).navigate(directions) }
                        }
                    }
                } else {
                    navigateToBankOfferStatus()
                }


            }
            ApiResponse.Status.ERROR -> {
                hideProgressDialog()
                Log.i("SoftOfferFragment", ": " + ApiResponse.Status.ERROR)

            }
            else -> {
                Log.i("SoftOfferFragment", ": ")
            }
        }

    }

    companion object {

        fun newInstance(): DashboardFragment {
            return DashboardFragment()
        }
    }

}