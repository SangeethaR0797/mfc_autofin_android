package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import model.residential_models.CityData;
import model.residential_models.ResidentialPinCodeRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.RES_CITY_URL;

public class ResidentialCity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    TextView tvAppBarTitle, tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvRegCityLbl, tvPincodeLbl, tvStateName, tvSelectedCity, tvCityName;
    EditText etResPinCode, etAddressLine1, etAddressLine2, etLandmark;
    Button btnPinCodeCheck, btnNext;
    ImageView iv_residential_details_backBtn;
    String basicDetailsVal = "", strPinCode = "", strCity="",strState="",strAdd1="",strAdd2="",strLandMark="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residential_city2);
        if (CommonStrings.customBasicDetails.getOtp() != "") {
            basicDetailsVal = CommonStrings.customBasicDetails.getSalutation()+" "+CommonStrings.customBasicDetails.getFullName() + " | " + CommonStrings.customBasicDetails.getEmail() + " | " + CommonStrings.customBasicDetails.getCustomerMobile();
        }
        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customResDetails.getPincode()!=null && !CommonStrings.customResDetails.getPincode().isEmpty())
            {
                strPinCode=CommonStrings.customResDetails.getPincode();
            }
            if(CommonStrings.customResDetails.getCustomerCity()!=null && !CommonStrings.customResDetails.getCustomerCity().isEmpty())
            {
                strCity=CommonStrings.customResDetails.getCustomerCity();
            }
            if(CommonStrings.customResDetails.getCustomerState()!=null && !CommonStrings.customResDetails.getCustomerState().isEmpty())
            {
                strState=CommonStrings.customResDetails.getCustomerState();
            }
            if(CommonStrings.customResDetails.getAddressLine1()!=null && !CommonStrings.customResDetails.getAddressLine1().isEmpty())
            {
                strAdd1=CommonStrings.customResDetails.getAddressLine1();
            }
            if(CommonStrings.customResDetails.getAddressLine2()!=null && !CommonStrings.customResDetails.getAddressLine2().isEmpty())
            {
                strAdd2=CommonStrings.customResDetails.getAddressLine2();
            }
            if(CommonStrings.customResDetails.getAddressLine3()!=null && !CommonStrings.customResDetails.getAddressLine3().isEmpty())
            {
                strLandMark=CommonStrings.customResDetails.getAddressLine3();
            }
        }
        initViews();
    }

    private void initViews() {
        tvAppBarTitle = findViewById(R.id.tvAppBarTitle);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvSelectedCity = findViewById(R.id.tvSelectedCity);
        iv_residential_details_backBtn = findViewById(R.id.iv_residential_details_backBtn);
        tvRegCityLbl = findViewById(R.id.tvRegCityLbl);
        tvPincodeLbl = findViewById(R.id.tvPincodeLbl);
        tvStateName = findViewById(R.id.tvStateName);
        tvCityName = findViewById(R.id.tvCityName);
        etResPinCode = findViewById(R.id.etResPinCode);
        etAddressLine1 = findViewById(R.id.etAddressLine1);
        etAddressLine2 = findViewById(R.id.etAddressLine2);
        etLandmark = findViewById(R.id.etLandmark);
        btnPinCodeCheck = findViewById(R.id.btnPinCodeCheck);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(getString(R.string.title_basic_details));
        tvGivenPreviousVal.setText(basicDetailsVal);
        if(!strPinCode.isEmpty())
        {
            etResPinCode.setText(strPinCode);
        }
        if(!strCity.isEmpty())
        {
            tvSelectedCity.setText(strCity);
        }
        if(!strState.isEmpty())
        {
            tvStateName.setText(strState);
        }
        if(!strAdd1.isEmpty())
        {
            etAddressLine1.setText(strAdd1);
        }
        if(!strAdd2.isEmpty())
        {
            etAddressLine2.setText(strAdd2);
        }
        if(!strLandMark.isEmpty())
        {
            etLandmark.setText(strLandMark);
        }
        tvGivenValEdit.setOnClickListener(this);
        iv_residential_details_backBtn.setOnClickListener(this);
        btnPinCodeCheck.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_residential_details_backBtn) {
            CommonMethods.redirectToDashboard(this);
            CommonMethods.clearData();
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnPinCodeCheck) {
            strPinCode = etResPinCode.getText().toString();
            if (strPinCode != "") {
                SpinnerManager.showSpinner(this);
                retrofitInterface.getFromWeb(Global.stock_details_base_url+RES_CITY_URL + etResPinCode.getText().toString()).enqueue(this);
            } else if (strPinCode != "") {
                Toast.makeText(this, getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
            } else if (strPinCode.length() != 6) {
                Toast.makeText(this, getResources().getString(R.string.please_enter_proper_pincode), Toast.LENGTH_SHORT).show();
            }
        } else if (v.getId() == R.id.btnNext) {
            if (!strPinCode.equals("")) {
                if (!tvStateName.getText().toString().equals("") && !strCity.equals("") && !etAddressLine1.getText().toString().equals("") && !etAddressLine2.getText().toString().equals("") && !etLandmark.getText().toString().equals("")) {
                    CommonStrings.customResDetails.setAddressLine1(etAddressLine1.getText().toString());
                    CommonStrings.customResDetails.setAddressLine2(etAddressLine2.getText().toString());
                    CommonStrings.customResDetails.setAddressLine3(etLandmark.getText().toString());
                    startActivity(new Intent(this, CityMonthAndYearActivity.class));
                } else {
                    CommonMethods.showToast(ResidentialCity.this, "Please fill all the fields");
                }
            } else {
                Toast.makeText(this, "Please enter Valid Pincode", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes = new Gson().toJson(response.body());
        ResidentialPinCodeRes pinCodeRes = new Gson().fromJson(strRes, ResidentialPinCodeRes.class);
        try {
            if (pinCodeRes != null && pinCodeRes.getStatus()) {
                if (pinCodeRes.getData() != null) {

                    CityData cityData = pinCodeRes.getData();
                    if (cityData.getCity() != null) {
                        CommonStrings.customResDetails.setCustomerCity(cityData.getCity());
                        strCity = CommonStrings.customResDetails.getCustomerCity();
                        tvStateName.setText(cityData.getState());
                        tvSelectedCity.setText(strCity);
                        CommonStrings.customResDetails.setPincode(cityData.getPincode());
                        CommonStrings.customResDetails.setCustomerState(cityData.getState());
                    } else {
                        Toast.makeText(this, pinCodeRes.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, pinCodeRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, pinCodeRes.getMessage(), Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}