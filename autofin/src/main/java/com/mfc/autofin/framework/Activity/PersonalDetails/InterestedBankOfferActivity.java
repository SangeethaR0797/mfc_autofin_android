package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import model.BankProcessResponse;
import model.add_lead_details.CustomerDetailsRequest;
import model.add_lead_details.CustomerID;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;

import static retrofit_config.RetroBase.retrofitInterface;

public class InterestedBankOfferActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = InterestedBankOfferActivity.class.getSimpleName();
    ImageView iv_common_bar_backBtn;
    TextView tvCommonAppBarTitle, tv_hi_cname, tvInterestedBankOfferCaseId, tv_will_contact_you_lbl, tv_check_list_2_lbl, tv_check_list_3_lbl, tvLbl;
    Button btnUpdate, btnTrackLoan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_bank_offer);
        initView();
    }

    private void initView() {
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tv_hi_cname = findViewById(R.id.tv_hi_cname);
        tvInterestedBankOfferCaseId = findViewById(R.id.tvInterestedBankOfferCaseId);
        tv_will_contact_you_lbl = findViewById(R.id.tv_will_contact_you_lbl);
        tv_check_list_2_lbl = findViewById(R.id.tv_check_list_2_lbl);
        tv_check_list_3_lbl = findViewById(R.id.tv_check_list_3_lbl);
        tvLbl = findViewById(R.id.tvLbl);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnTrackLoan = findViewById(R.id.btnTrackLoan);
        tv_hi_cname.setText(CommonStrings.customBasicDetails.getFullName());
        tvInterestedBankOfferCaseId.setText("Your Case Id is " + CommonMethods.getStringValueFromKey(this, CommonStrings.CASE_ID));
        tvCommonAppBarTitle.setText("INTERESTED BANK OFFER");
        iv_common_bar_backBtn.setVisibility(View.INVISIBLE);
        btnUpdate.setOnClickListener(this);
        btnTrackLoan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_common_bar_backBtn) {

        } else if (v.getId() == R.id.btnUpdate) {
            CommonMethods.showToast(this, "Yet to implement");
        } else if (v.getId() == R.id.btnTrackLoan) {
           // retrofitInterface.getFromWeb(getCustomerDetailsReq(), Global.customer_bank_baseURL+ CommonStrings.PROCESS_WITH_BANK_URL_END).enqueue(this);
            CommonMethods.redirectToDashboard(this);
        }
    }
    private CustomerDetailsRequest getCustomerDetailsReq() {
        CustomerDetailsRequest customerDetailsReq = new CustomerDetailsRequest();
        customerDetailsReq.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        customerDetailsReq.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        CustomerID customerID = new CustomerID();
        customerID.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID)));
        customerDetailsReq.setData(customerID);
        return customerDetailsReq;
    }


    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
            BankProcessResponse bankProcessResponse = new Gson().fromJson(strRes, BankProcessResponse.class);
            try {
                if(bankProcessResponse.getStatus())
                {
                    if(bankProcessResponse.getMessage()!=null) {
                        CommonMethods.showToast(this, bankProcessResponse.getMessage().toString());
                        CommonMethods.redirectToDashboard(this);
                    }
                    else
                    {
                        CommonMethods.showToast(this, "Processing with Bank");
                        CommonMethods.redirectToDashboard(this);
                    }
                }
                else
                {
                    CommonMethods.showToast(this, "Something went wrong! Please try again.");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onBackPressed() {

    }
}
