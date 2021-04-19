package v2.view.other_activity

import android.os.Bundle
import android.text.Editable
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
import v2.model.request.Get_IBB_MasterDetailsRequest
import v2.model.response.Get_IBB_MasterDetailsResponse
import v2.model_view.IBB.IBB_MasterViewModel
import v2.service.utility.ApiResponse
import v2.view.adapter.StringDataRecyclerViewAdapter
import v2.view.callBackInterface.itemClickCallBack


class CarBasicDetailsActivity : AppCompatActivity(), itemClickCallBack {
    var mCurrentCalFor: String = ""
    var mSelectedYear: String = ""

    var mSelectedMake: String = ""
    var mSelectedModel: String = ""
    var mSelectedVariant: String = ""
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
            manageBackpress()
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


        iBB_MasterViewModel = ViewModelProvider(this@CarBasicDetailsActivity).get(
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
        manageBackpress()
    }

    private fun callYearApiData() {
        tvSelectedText.text = ""
        tvSelectLabel.text = "Select Registration Year"
        var request = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, CommonStrings.YEAR, "0", "app", null, null, null, null)
        iBB_MasterViewModel!!.getIBB_MasterDetails(request, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }

    private fun callMakeApiData() {
        tvSelectedText.text = mSelectedYear
        tvSelectLabel.text = "Select Make"
        var request = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, CommonStrings.MAKE, "0", "app", mSelectedYear, null, null, null)
        iBB_MasterViewModel!!.getIBB_MasterDetails(request, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }

    private fun callModelApiData() {
        tvSelectedText.text = mSelectedYear + "-" + mSelectedMake
        tvSelectLabel.text = "Select Model"
        var request = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, CommonStrings.MODEL, "0", "app", mSelectedYear, null, mSelectedMake, null)
        iBB_MasterViewModel!!.getIBB_MasterDetails(request, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }

    private fun callVariantApiData() {
        tvSelectedText.text = mSelectedYear + "-" + mSelectedMake + "-" + mSelectedModel
        tvSelectLabel.text = "Select Variant"
        var request = getIBBMasterDetailsRequest(CommonStrings.IBB_TOKEN_VALUE, CommonStrings.VARIANT, "0", "app", mSelectedYear, null, mSelectedMake, mSelectedModel)
        iBB_MasterViewModel!!.getIBB_MasterDetails(request, Global.ibb_base_url + CommonStrings.IBB_VEH_DETAILS_END_POINT)

    }


    private fun manageBackpress() {
        if (mCurrentCalFor.equals(CommonStrings.YEAR)) {
            mSelectedYear = "";
            mSelectedMake = "";
            mSelectedModel = "";
            mSelectedVariant = "";
            onBackPressed()
        } else if (mCurrentCalFor.equals(CommonStrings.MAKE)) {
            mSelectedMake = "";
            mSelectedModel = "";
            mSelectedVariant = "";
            callYearApiData()
        } else if (mCurrentCalFor.equals(CommonStrings.MODEL)) {
            mSelectedModel = "";
            mSelectedVariant = "";
            callMakeApiData()
        } else if (mCurrentCalFor.equals(CommonStrings.VARIANT)) {
            mSelectedVariant = "";
            callModelApiData()
        }

    }

    private fun getIBBMasterDetailsRequest(access_token: String? = null,
                                           mfor: String? = null,
                                           pricefor: String? = null,
                                           tag: String? = null,
                                           year: String? = null,
                                           month: String? = null,
                                           make: String? = null,
                                           model: String? = null): Get_IBB_MasterDetailsRequest? {
        mCurrentCalFor = mfor!!;
        return Get_IBB_MasterDetailsRequest(access_token, mfor, pricefor, tag, year, month, make, model)
    }

    private fun onIBB_MasterDetails(mApiResponse: ApiResponse) {
        when (mApiResponse.status) {
            ApiResponse.Status.LOADING -> {
            }
            ApiResponse.Status.SUCCESS -> {

                val masterResponse: Get_IBB_MasterDetailsResponse? = mApiResponse.data as Get_IBB_MasterDetailsResponse?
                var dataValue: List<String> = listOf()

                if (mCurrentCalFor.equals(CommonStrings.YEAR)) {
                    dataValue = masterResponse!!.year
                } else if (mCurrentCalFor.equals(CommonStrings.MAKE)) {
                    dataValue = masterResponse!!.make
                } else if (mCurrentCalFor.equals(CommonStrings.MODEL)) {
                    dataValue = masterResponse!!.model
                } else if (mCurrentCalFor.equals(CommonStrings.VARIANT)) {
                    dataValue = masterResponse!!.variant
                }

                reviewAdapter = StringDataRecyclerViewAdapter(dataValue, this@CarBasicDetailsActivity)
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

        if (mCurrentCalFor.equals(CommonStrings.YEAR)) {
            mSelectedYear = value
            callMakeApiData()

        } else if (mCurrentCalFor.equals(CommonStrings.MAKE)) {
            mSelectedMake = value
            callModelApiData()

        } else if (mCurrentCalFor.equals(CommonStrings.MODEL)) {
            mSelectedModel = value
            callVariantApiData()
        } else if (mCurrentCalFor.equals(CommonStrings.VARIANT)) {
            mSelectedVariant = value
            //Call Back Result
        }


    }
}