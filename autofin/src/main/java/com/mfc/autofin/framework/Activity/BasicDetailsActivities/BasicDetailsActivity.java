package com.mfc.autofin.framework.Activity.BasicDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;

import fragments.OTPBottomSheetFragment;
import model.add_lead_details.LoanDetails;
import model.basic_details.BasicDetails;
import model.basic_details.SalutationData;
import model.basic_details.SalutationResponse;
import model.basic_details.SalutationType;
import model.otp_models.CustomerMobile;
import model.otp_models.OTPRequest;
import model.otp_models.OTPResponse;
import model.vehicle_details.vehicle_category.VehicleDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class BasicDetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object>, AdapterView.OnItemSelectedListener {

    private static final String TAG = BasicDetailsActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvNameLbl, tvEmailLbl, tvPhoneNumLbl;
    EditText etName, etEmailId, etPhoneNumber;
    Button btnNext;
    String strPreviousScreenVal = "";
    String strName = "", strEmail = "", strPhoneNum = "";
    private Spinner spSalutation;
    boolean isNewCarFlow = false;
    private ImageView iv_basic_details_backBtn;
    private List<String> salutationTypeList=new ArrayList<>();
    String salutation="";
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        if (CommonStrings.customLoanDetails.getLoanCategory().equals(getResources().getString(R.string.new_car))) {
            isNewCarFlow = true;
            if (!CommonStrings.customVehDetails.getVehicleSellingPrice().equals("")) {
                strPreviousScreenVal = CommonMethods.getStringValueFromKey(this, CommonStrings.VEH_INSURED_AMOUNT);
            } else {
                strPreviousScreenVal = "";
            }
        } else {
            isNewCarFlow = false;
            if (CommonStrings.customVehDetails.getInsurance()) {
                if (CommonStrings.customVehDetails.getInsuranceType() != null) {
                    strPreviousScreenVal = CommonStrings.customVehDetails.getInsuranceValidity();
                } else {
                    strPreviousScreenVal = "";
                }
            } else {
                strPreviousScreenVal = "No";
            }


        }
        retrofitInterface.getFromWeb(CommonStrings.GET_SALUTATION_URL).enqueue(this);
        initView();
    }

    private void initView() {
        iv_basic_details_backBtn = findViewById(R.id.iv_basic_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvNameLbl = findViewById(R.id.tvNameLbl);
        tvEmailLbl = findViewById(R.id.tvEmailLbl);
        tvPhoneNumLbl = findViewById(R.id.tvPhoneNumLbl);
        spSalutation = findViewById(R.id.spSalutation);
        etName = findViewById(R.id.etName);
        etEmailId = findViewById(R.id.etEmailId);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnNext = findViewById(R.id.btnNext);
        if (isNewCarFlow) {
            tvGivenLbl.setText(getString(R.string.lbl_insured_amount));
        } else {
            if (strPreviousScreenVal.equals("No")) {
                tvGivenLbl.setText(getString(R.string.lbl_insurance_on_vehicle));
            } else {
                tvGivenLbl.setText(getString(R.string.vehicle_insurance_validity));
            }
        }
        tvGivenPreviousVal.setText(strPreviousScreenVal);
        tvNameLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvEmailLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvPhoneNumLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        etName.setTypeface(CustomFonts.getRobotoMedium(this));
        etEmailId.setTypeface(CustomFonts.getRobotoMedium(this));
        etPhoneNumber.setTypeface(CustomFonts.getRobotoMedium(this));
        btnNext.setTypeface(CustomFonts.getRobotoMedium(this));
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        iv_basic_details_backBtn.setOnClickListener(this);
        spSalutation.setOnItemSelectedListener(this);



    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (validate()) {
                CommonStrings.customBasicDetails.setFullName(strName);
                CommonStrings.customBasicDetails.setEmail(strEmail);
                CommonStrings.customBasicDetails.setCustomerMobile(strPhoneNum);
                CommonStrings.customBasicDetails.setSalutation(salutation);
                SpinnerManager.showSpinner(this);
                retrofitInterface.getFromWeb(getOTPRequest(), CommonStrings.OTP_URL_END).enqueue(this);
            }
        } else if (v.getId() == R.id.iv_basic_details_backBtn) {
            CommonMethods.redirectToDashboard(BasicDetailsActivity.this);
            CommonMethods.clearData();
        }
    }

    private OTPRequest getOTPRequest() {
        OTPRequest otpRequest = new OTPRequest();
        otpRequest.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        otpRequest.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        CustomerMobile customerMobile = new CustomerMobile();
        customerMobile.setCustomerMobile(CommonStrings.customBasicDetails.getCustomerMobile());
        otpRequest.setData(customerMobile);
        return otpRequest;
    }

    private boolean validate() {
        boolean validDetails = false;
        strName = etName.getText().toString();
        strEmail = etEmailId.getText().toString().trim();
        strPhoneNum = etPhoneNumber.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!strName.equals("") && strName.matches("^[a-z A-Z ]*$")) {
            if (!strEmail.equals("") && strEmail.matches(emailPattern)) {
                if (!strPhoneNum.equals("") && strPhoneNum.matches("^[6-9]\\d{9}$")) {
                    validDetails = true;
                } else {
                    validDetails = false;
                    Toast.makeText(this, getString(R.string.enter_valid_phone_number), Toast.LENGTH_LONG).show();
                }

            } else {
                validDetails = false;
                Toast.makeText(this, getString(R.string.enter_valid_emailid), Toast.LENGTH_LONG).show();
            }
        } else {
            validDetails = false;
            Toast.makeText(this, getString(R.string.enter_valid_name), Toast.LENGTH_LONG).show();
        }

        return validDetails;

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);

        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);

        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        if (url.contains(CommonStrings.OTP_URL_END)) {
            OTPResponse otpResponse = new Gson().fromJson(strRes, OTPResponse.class);
            try {
                if (otpResponse != null && otpResponse.getStatus().toString().equals("true")) {

                    if (otpResponse.getData() != null) {
                        CommonStrings.customBasicDetails.setOtp(otpResponse.getData());
                        OTPBottomSheetFragment bottomSheetFragment = new OTPBottomSheetFragment(this, etPhoneNumber);
                        bottomSheetFragment.show(getSupportFragmentManager(), "ModalBottomSheet");
                    }
                } else {
                    Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (url.contains(CommonStrings.GET_SALUTATION_URL)) {
            SalutationResponse salRes = new Gson().fromJson(strRes, SalutationResponse.class);
            try {
                if (salRes != null && salRes.getStatus()) {

                    if (salRes.getData() != null) {
                        SalutationData salutationData=salRes.getData();
                        if(salutationData.getTypes()!=null)
                        {
                            List<SalutationType> salTypeList=salutationData.getTypes();
                            for(int i=0;i<salTypeList.size();i++)
                            {
                                salutationTypeList.add(salTypeList.get(i).getValue());
                            }
                            adapter = new ArrayAdapter(BasicDetailsActivity.this,android.R.layout.simple_spinner_item,salutationTypeList);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spSalutation.setAdapter(adapter);
                        }
                        else
                        {
                            Toast.makeText(this, "No Salutation found", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }


    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    public VehicleDetails getVehicleDetails() {
        VehicleDetails vehicleDetails = new VehicleDetails();
        try {

            vehicleDetails.setHaveVehicleNumber(CommonStrings.customVehDetails.getHaveVehicleNumber());
            vehicleDetails.setVehicleNumber(CommonStrings.customVehDetails.getVehicleNumber());
            vehicleDetails.setRegistrationYear(CommonStrings.customVehDetails.getRegistrationYear());
            vehicleDetails.setMake(CommonStrings.customVehDetails.getMake());
            vehicleDetails.setModel(CommonStrings.customVehDetails.getModel());
            vehicleDetails.setVariant(CommonStrings.customVehDetails.getVariant());
            vehicleDetails.setOwnership(CommonStrings.customVehDetails.getOwnership());
            vehicleDetails.setVehicleSellingPrice(CommonStrings.customVehDetails.getVehicleSellingPrice());
            vehicleDetails.setDoesCarHaveLoan(CommonStrings.customVehDetails.getDoesCarHaveLoan());
            vehicleDetails.setValuationPrice(CommonStrings.customVehDetails.getValuationPrice());
            vehicleDetails.setOnRoadPrice(CommonStrings.customVehDetails.getOnRoadPrice());

            if (CommonStrings.customVehDetails.getInsurance()) {
                vehicleDetails.setInsurance(CommonStrings.customVehDetails.getInsurance());
                vehicleDetails.setInsuranceAmount(CommonStrings.customVehDetails.getInsuranceAmount());
                vehicleDetails.setInsuranceType(CommonStrings.customVehDetails.getInsuranceType());
                vehicleDetails.setInsuranceValidity(CommonStrings.customVehDetails.getInsuranceValidity());

            } else {
                vehicleDetails.setInsurance(CommonStrings.customVehDetails.getInsurance());
                vehicleDetails.setInsuranceType(CommonStrings.customVehDetails.getInsuranceType());
                vehicleDetails.setInsuranceAmount(CommonStrings.customVehDetails.getInsuranceAmount());
                vehicleDetails.setInsuranceValidity(CommonStrings.customVehDetails.getInsuranceValidity());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return vehicleDetails;
    }

    public LoanDetails getLoanDetails() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setLoanCategory(CommonStrings.customLoanDetails.getLoanCategory());
        return loanDetails;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        salutation=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}