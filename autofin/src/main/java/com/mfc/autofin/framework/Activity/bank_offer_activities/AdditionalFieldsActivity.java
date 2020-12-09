package com.mfc.autofin.framework.Activity.bank_offer_activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;

import controller.AdditionalFieldAdapter;
import model.CustomerDetailsReq;
import model.add_lead_details.AddLeadRequest;
import model.add_lead_details.CustomerDetailsRequest;
import model.add_lead_details.CustomerID;
import model.addtional_fields.AdditionFields;
import model.addtional_fields.AdditionalFieldData;
import model.addtional_fields.AdditionalFieldResponse;
import model.addtional_fields.BankName;
import model.addtional_fields.CustAdditionalData;
import model.addtional_fields.CustAdditionalResponse;
import model.addtional_fields.GetAdditionFieldsReq;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class AdditionalFieldsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {
    private static final String TAG = AdditionalFieldsActivity.class.getSimpleName();
    private ImageView iv_common_bar_backBtn;
    private TextView tvCommonAppBarTitle, tvReferenceLbl;
    private LinearLayout llRefContent;
    private RecyclerView rvAdditionalFields;
    private List<AdditionFields> additionFieldsList = new ArrayList<AdditionFields>();
    private List<CustAdditionalData> custAdditionalList = new ArrayList<CustAdditionalData>();
    private Button btnNext;
    private String mUserId = "", mUserType = "", mAppName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_fields);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mUserId = data.getString(AutoFinConstants.DEALER_ID);
            mUserType = data.getString(AutoFinConstants.USER_TYPE);
            mAppName = data.getString(AutoFinConstants.APP_NAME);

            CommonMethods.setValueAgainstKey(this, CommonStrings.DEALER_ID_VAL, mUserId);
            CommonMethods.setValueAgainstKey(this, CommonStrings.USER_TYPE_VAL, mUserType);
            CommonMethods.setValueAgainstKey(this, CommonStrings.APP_NAME_VAL, mAppName);

        }

        retrofitInterface.getFromWeb(getCustomerAdditionalReq(), Global.customerDetails_BaseURL + CommonStrings.CUSTOMER_ADDITIONAL_FIELDS).enqueue(this);

        if (CommonStrings.IS_OLD_LEAD) {
                     retrofitInterface.getFromWeb(getAdditionalFieldReq(), Global.customerDetails_BaseURL + CommonStrings.GET_ADDITIONAL_FIELDS).enqueue(this);

        } else {
            retrofitInterface.getFromWeb(getAdditionalFieldReq(), Global.customerDetails_BaseURL + CommonStrings.CUSTOMER_ADDITIONAL_FIELDS).enqueue(this);}
        initView();
    }

    private CustomerDetailsRequest getCustomerAdditionalReq() {
        CustomerDetailsRequest customerDetailsReq = new CustomerDetailsRequest();
        customerDetailsReq.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        customerDetailsReq.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        CustomerID customerID = new CustomerID();
        customerID.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID)));
        customerDetailsReq.setData(customerID);
        return customerDetailsReq;
    }

    private GetAdditionFieldsReq getAdditionalFieldReq() {
        GetAdditionFieldsReq additionFieldsReq = new GetAdditionFieldsReq();
        additionFieldsReq.setUserId(mUserId);
        additionFieldsReq.setUserType(mUserType);
        BankName bankName = new BankName();
        bankName.setBankName("HDFC Bank");
        additionFieldsReq.setData(bankName);
        return additionFieldsReq;
    }

    private void initView() {
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tvReferenceLbl = findViewById(R.id.tvReferenceLbl);
        llRefContent = findViewById(R.id.llRefContent);
        rvAdditionalFields = findViewById(R.id.rvAdditionalFields);
        iv_common_bar_backBtn.setVisibility(View.INVISIBLE);
        tvCommonAppBarTitle.setText("REFERENCE");
        tvReferenceLbl.setText(getResources().getString(R.string.lbl_reference_name));
        btnNext = findViewById(R.id.btnNext);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_common_bar_backBtn) {

        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);

        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        if (url.contains(CommonStrings.GET_ADDITIONAL_FIELDS)) {
            try {
                AdditionalFieldResponse additionalFieldResponse = new Gson().fromJson(strRes, AdditionalFieldResponse.class);
                if (additionalFieldResponse.getStatus() && additionalFieldResponse != null) {
                    if (additionalFieldResponse.getData() != null) {
                        additionFieldsList.addAll(additionalFieldResponse.getData());
                        attachToAdapter();
                    } else {
                        additionalFieldResponse.getMessage();
                    }
                } else {
                    CommonMethods.showToast(this, "No data found,Please try again!");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if (url.contains(CommonStrings.CUSTOMER_ADDITIONAL_FIELDS)) {
            try {
                CustAdditionalResponse custAdditionalResponse = new Gson().fromJson(strRes, CustAdditionalResponse.class);
                if (custAdditionalResponse.getStatus() && custAdditionalResponse != null) {
                    if (custAdditionalResponse.getData() != null) {
                        custAdditionalList.addAll(custAdditionalResponse.getData());

                    } else {
                        custAdditionalResponse.getMessage();
                    }
                } else {
                    CommonMethods.showToast(this, "No data found,Please try again!");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }
    }

    private void attachToAdapter() {

        AdditionalFieldAdapter additionalFieldAdapter;
        if(CommonStrings.IS_OLD_LEAD)
        {
            additionalFieldAdapter = new AdditionalFieldAdapter(AdditionalFieldsActivity.this, additionFieldsList,custAdditionalList,btnNext);
        }
        else
        {
            additionalFieldAdapter = new AdditionalFieldAdapter(AdditionalFieldsActivity.this, additionFieldsList,btnNext);

        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAdditionalFields.setLayoutManager(layoutManager);
        rvAdditionalFields.setAdapter(additionalFieldAdapter);
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
