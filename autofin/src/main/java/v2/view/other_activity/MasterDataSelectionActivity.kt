package v2.view.other_activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mfc.autofin.framework.R
import utility.CommonStrings
import utility.Global

import v2.model.dto.AddLeadRequest

import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.request.add_lead.AddLeadData
import v2.model.request.add_lead.VehicleDetails
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model_view.IBB.IBB_MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.StringDataRecyclerViewAdapter
import v2.view.callBackInterface.itemClickCallBack


class MasterDataSelectionActivity : AppCompatActivity(), itemClickCallBack {

    var iBB_MasterViewModel: IBB_MasterViewModel? = null
    lateinit var ivBack: ImageView
    lateinit var tvSelectedText: TextView
    lateinit var tvSelectLabel: TextView
    lateinit var etSearch: EditText
    lateinit var llSearch: LinearLayout
    lateinit var rvResult: RecyclerView
    var reviewAdapter: StringDataRecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v2_activity_basic_data_selection)
        ivBack = findViewById(R.id.iv_back)
        tvSelectedText = findViewById(R.id.tv_selected_text)
        tvSelectLabel = findViewById(R.id.tv_select_label)
        etSearch = findViewById(R.id.et_search)
        llSearch = findViewById(R.id.ll_search)
        rvResult = findViewById(R.id.rv_result)

        llSearch.setOnClickListener(View.OnClickListener {
            etSearch.requestFocus()

        })
        ivBack.setOnClickListener(View.OnClickListener {
            finish()
        })

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                       count: Int) {
                if (s != "") {
                    //do your work here
                }
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (TextUtils.isEmpty(etSearch.text)) {
                    reviewAdapter?.filter?.filter("")
                } else {
                    reviewAdapter?.filter?.filter(etSearch.text)
                }
            }
        })


        iBB_MasterViewModel = ViewModelProvider(this@MasterDataSelectionActivity).get(
                IBB_MasterViewModel::class.java
        )


        iBB_MasterViewModel!!.getIBB_MasterDetailsLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onIBB_MasterDetails(
                            mApiResponse!!
                    )
                })

        callYearApiData()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun callYearApiData() {
        etSearch.inputType = (InputType.TYPE_CLASS_NUMBER)
        tvSelectedText.text = ""
        tvSelectLabel.text = "Select Registration Year"
        var request = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, CommonStrings.YEAR, "0", "app", null, null, null, null)
        iBB_MasterViewModel!!.getIBB_MasterDetails(request, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }


    private fun getIBBMasterDetailsRequest(access_token: String? = null,
                                           mfor: String? = null,
                                           pricefor: String? = null,
                                           tag: String? = null,
                                           year: String? = null,
                                           month: String? = null,
                                           make: String? = null,
                                           model: String? = null): Get_IBB_MasterDetailsRequest? {

        return Get_IBB_MasterDetailsRequest(access_token, mfor, pricefor, tag, year, month, make, model)
    }

    private fun onIBB_MasterDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {

                val masterResponse: Get_IBB_MasterDetailsResponse? = mApiResponse.data as Get_IBB_MasterDetailsResponse?
                var dataValue: List<String> = listOf()

                dataValue = masterResponse!!.year

                reviewAdapter = StringDataRecyclerViewAdapter(dataValue, this@MasterDataSelectionActivity)
                val layoutManager = LinearLayoutManager(this)
                rvResult.setLayoutManager(layoutManager)
                rvResult.setAdapter(reviewAdapter)
            }
            ApiResponse.Status.ERROR -> {

            }
        }
    }

    override fun itemClick(item: Any?, position: Int) {
        var value: String = item as String
        val intent = Intent()
        intent.putExtra(CommonStrings.SELECTED_DATA, vehicleAddUpdateDTO)
        setResult(CommonStrings.RESULT_CODE, intent)
        finish()
    }
}