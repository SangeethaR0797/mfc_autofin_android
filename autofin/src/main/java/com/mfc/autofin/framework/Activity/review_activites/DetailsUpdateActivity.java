package com.mfc.autofin.framework.Activity.review_activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import model.add_lead_details.CustomerDetails;
import model.add_lead_details.CustomerDetailsRequest;
import model.add_lead_details.CustomerDetailsResponse;
import model.add_lead_details.CustomerID;
import model.add_lead_details.LoanDetails;
import model.basic_details.BasicData;
import model.basic_details.BasicDetailsReq;
import model.basic_details.BasicDetailsResponse;
import model.basic_details.BasicVehDetails;
import model.basic_details.EmploymentDetails;
import model.basic_details.PersonalDetailsData;
import model.basic_details.ReferenceDetails;
import model.basic_details.ResidentialDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;

import static retrofit_config.RetroBase.retrofitInterface;

public class DetailsUpdateActivity extends AppCompatActivity implements Callback<Object> {
    private static final String TAG = DetailsUpdateActivity.class.getSimpleName();
    TextView tvCommonAppBarTitle, tvUpdateTitle, tvMatchingWithBankLbl;
    private String userId = "", userType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details_update);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tvCommonAppBarTitle.setText("UPDATING...");
        userId = CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL);
        userType = CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL);
        retrofitInterface.getFromWeb(getBasicDetailsRequest(), Global.customerAPI_BaseURL+CommonStrings.ADD_BASIC_DETAILS_URL).enqueue(this);
        initView();
    }

    private BasicDetailsReq getBasicDetailsRequest() {
        BasicDetailsReq basicDetailsReq = new BasicDetailsReq();
        basicDetailsReq.setUserId(userId);
        basicDetailsReq.setUserType(userType);
        basicDetailsReq.setData(getBasicData());
        return basicDetailsReq;
    }

    private BasicData getBasicData() {
        BasicData basicData = new BasicData();
        Log.i(TAG, "getBasicData: "+CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID));
        basicData.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID)));
        basicData.setEmploymentDetails(getEmploymentDetails());
        basicData.setLoanDetails(getLoanDetails());
        basicData.setPersonalDetails(getPersonalDetails());
        basicData.setReferenceDetails(getReferenceDetails());
        basicData.setResidentialDetails(getResidentialDetails());
        basicData.setVehicleDetails(getVehicleDetails());
        return basicData;
    }

    private BasicVehDetails getVehicleDetails() {
        BasicVehDetails basicVehDetails = new BasicVehDetails();
        basicVehDetails.setLikelyPurchaseDate(CommonStrings.customVehDetails.getLikelyPurchaseDate());
        return basicVehDetails;
    }

    private ResidentialDetails getResidentialDetails() {
        ResidentialDetails residentialDetails =CommonStrings.customResDetails;
        return residentialDetails;
    }

    private ReferenceDetails getReferenceDetails() {
        ReferenceDetails referenceDetails = new ReferenceDetails();
        referenceDetails.setReferenceName("");
        referenceDetails.setRefMobileNumber("");
        referenceDetails.setRelationship("");
        return referenceDetails;
    }

    private PersonalDetailsData getPersonalDetails() {
        PersonalDetailsData personalDetailsData = CommonStrings.customPersonalDetails;
       return personalDetailsData;
    }

    private LoanDetails getLoanDetails() {
        LoanDetails loanDetails =CommonStrings.customLoanDetails;
        return loanDetails;
    }

    private EmploymentDetails getEmploymentDetails() {
        EmploymentDetails employmentDetails = CommonStrings.cusEmpDetails;
        return employmentDetails;
    }

    private void initView() {
        tvUpdateTitle = findViewById(R.id.tvUpdateTitle);
        tvMatchingWithBankLbl = findViewById(R.id.tvMatchingWithBankLbl);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        if (url.contains("add-basic-details")) {
            BasicDetailsResponse updateDetailsRes = new Gson().fromJson(strRes, BasicDetailsResponse.class);
            if (updateDetailsRes != null && updateDetailsRes.getStatus()) {
                try {
                    CommonMethods.showToast(this, updateDetailsRes.getMessage());
                    CommonMethods.setValueAgainstKey(this, CommonStrings.CUSTOMER_ID, updateDetailsRes.getData().toString());
                    retrofitInterface.getFromWeb(getCustomerDetailsReq(),Global.customerAPI_BaseURL+ CommonStrings.CUSTOMER_DETAILS_URL).enqueue(this);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        } else if (url.contains("customer-details")) {
            CustomerDetailsResponse customerDetailsRes = new Gson().fromJson(strRes, CustomerDetailsResponse.class);
            if (customerDetailsRes != null && customerDetailsRes.getStatus()) {
                try {
                    if (customerDetailsRes.getData() != null) {
                        CustomerDetails customerDetails = customerDetailsRes.getData();
                        if (customerDetails.getCaseId() != null && !customerDetails.getCaseId().equals("")) {
                            CommonMethods.setValueAgainstKey(this, CommonStrings.CASE_ID, customerDetails.getCaseId());
                            CommonMethods.showToast(this, customerDetails.getCaseId());
                            Log.i(TAG, "onResponse: " + customerDetails.getCaseId());
                            //startActivity(new Intent(DetailsUpdateActivity.this, DocumentUploadActivity.class));

                            startActivity(new Intent(DetailsUpdateActivity.this, ViewBankActivity.class));
                        } else {
                            CommonMethods.showToast(this, "No Case-Id found");
                        }
                    }

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    private CustomerDetailsRequest getCustomerDetailsReq() {
        CustomerDetailsRequest customerDetailsReq = new CustomerDetailsRequest();
        customerDetailsReq.setUserId(userId);
        customerDetailsReq.setUserType(userType);
        CustomerID customerID = new CustomerID();
        customerID.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID)));
        customerDetailsReq.setData(customerID);
        return customerDetailsReq;
    }

}
