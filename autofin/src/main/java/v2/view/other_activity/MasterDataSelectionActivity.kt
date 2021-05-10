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

import v2.model.dto.DataSelectionDTO

import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.response.BankListResponse
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model_view.IBB.IBB_MasterViewModel
import v2.model_view.MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.MasterDataRecyclerViewAdapter
import v2.view.callBackInterface.itemClickCallBack
import java.util.ArrayList


class MasterDataSelectionActivity : AppCompatActivity(), itemClickCallBack {

    var iBB_MasterViewModel: IBB_MasterViewModel? = null
    lateinit var masterViewModel: MasterViewModel

    lateinit var ivBack: ImageView
    lateinit var tvScreenTitle: TextView
    lateinit var tvSelectedText: TextView
    lateinit var tvSelectLabel: TextView
    lateinit var etSearch: EditText
    lateinit var llSearch: LinearLayout
    lateinit var rvResult: RecyclerView
    var reviewAdapter: MasterDataRecyclerViewAdapter? = null
    var SELECTED_DATA_TYPE_CALL: String? = ""
    lateinit var llProgress: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SELECTED_DATA_TYPE_CALL = intent.getStringExtra(CommonStrings.SELECTED_DATA_TYPE);

        setContentView(R.layout.v2_master_data_selection)
        tvScreenTitle = findViewById(R.id.tv_screen_title)
        llProgress = findViewById(R.id.ll_progress)
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

        masterViewModel = ViewModelProvider(this).get(
                MasterViewModel::class.java
        )
        masterViewModel.getBankListLiveData()
                .observe(this, { mApiResponse: ApiResponse? ->
                    onBankList(
                            mApiResponse!!
                    )
                })


        if (!TextUtils.isEmpty(SELECTED_DATA_TYPE_CALL)) {
            if (SELECTED_DATA_TYPE_CALL.equals(CommonStrings.YEAR)) {
                callYearApiData()
            } else if (SELECTED_DATA_TYPE_CALL.equals(CommonStrings.BANK_DATA_CALL)) {
                callBankListApiData()
            }
        } else {
            Toast.makeText(baseContext, "Invalid data type", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun callYearApiData() {
        etSearch.inputType = (InputType.TYPE_CLASS_NUMBER)
        tvSelectedText.text = ""
        tvSelectLabel.text = "Select Registration Year"
        tvSelectLabel.visibility = View.GONE
        tvSelectedText.visibility = View.GONE
        etSearch.setHint("Year")
        tvScreenTitle.setHint("Year")
        var request = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, CommonStrings.YEAR, "0", "app", null, null, null, null)
        iBB_MasterViewModel!!.getIBB_MasterDetails(request, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }

    private fun callBankListApiData() {
        etSearch.inputType = (InputType.TYPE_TEXT_VARIATION_PERSON_NAME)
        tvSelectedText.text = ""
        tvSelectLabel.text = "Search Bank"
        etSearch.setHint("Search Bank")
        tvScreenTitle.setHint("Search Bank")
        tvSelectLabel.visibility = View.GONE
        tvSelectedText.visibility = View.GONE
        masterViewModel.getBankList(Global.customerDetails_BaseURL + CommonStrings.BANK_LIST_END_POINT)

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
                llProgress.visibility=View.GONE
            }
            ApiResponse.Status.SUCCESS -> {
                llProgress.visibility=View.GONE
                val masterResponse: Get_IBB_MasterDetailsResponse? = mApiResponse.data as Get_IBB_MasterDetailsResponse?
                var dataValue: List<String> = listOf()

                dataValue = masterResponse!!.year
                val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

                dataValue.forEachIndexed { index, s ->
                    list.add(DataSelectionDTO(s, null, s, false, null))

                }


                reviewAdapter = MasterDataRecyclerViewAdapter(list, this@MasterDataSelectionActivity)
                val layoutManager = LinearLayoutManager(this)
                rvResult.setLayoutManager(layoutManager)
                rvResult.setAdapter(reviewAdapter)
            }
            ApiResponse.Status.ERROR -> {
                llProgress.visibility=View.GONE
            }
        }
    }

    private fun onBankList(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
                llProgress.visibility=View.VISIBLE
            }
            ApiResponse.Status.SUCCESS -> {
                llProgress.visibility=View.GONE
                val response: BankListResponse? = mApiResponse.data as BankListResponse?
                setBankListDetails(response!!)

            }
            ApiResponse.Status.ERROR -> {
                llProgress.visibility=View.GONE
            }
        }
    }

    private fun setBankListDetails(bankListResponse: BankListResponse) {
        val list: ArrayList<DataSelectionDTO> = arrayListOf<DataSelectionDTO>()

        bankListResponse.data!!.forEachIndexed { index, value ->

            list.add(DataSelectionDTO(value, null, value, false, null))

        }

        reviewAdapter = MasterDataRecyclerViewAdapter(list, this@MasterDataSelectionActivity)
        val layoutManager = LinearLayoutManager(this)
        rvResult.setLayoutManager(layoutManager)
        rvResult.setAdapter(reviewAdapter)
    }


    override fun itemClick(item: Any?, position: Int) {
        var value: DataSelectionDTO = item as DataSelectionDTO
        val intent = Intent()
        intent.putExtra(CommonStrings.SELECTED_DATA, value)
        intent.putExtra(CommonStrings.SELECTED_DATA_TYPE, SELECTED_DATA_TYPE_CALL)
        setResult(CommonStrings.RESULT_CODE, intent)
        finish()
    }
}