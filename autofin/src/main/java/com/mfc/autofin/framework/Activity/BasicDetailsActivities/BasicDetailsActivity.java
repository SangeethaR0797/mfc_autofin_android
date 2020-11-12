package com.mfc.autofin.framework.Activity.BasicDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehRegNumAns;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehRegistrationYear;
import com.mfc.autofin.framework.R;

import fragments.OTPBottomSheetFragment;
import model.add_lead_details.LoanDetails;
import model.basic_details.BasicDetails;
import model.otp_models.CustomerMobile;
import model.otp_models.OTPRequest;
import model.otp_models.OTPResponse;
import model.vehicle_details.vehicle_category.VehicleDetails;
import model.vehicle_details.vehicle_category.stock_details.StockResponse;
import model.vehicle_details.vehicle_category.stock_details.StockResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class BasicDetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = BasicDetailsActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvNameLbl, tvEmailLbl, tvPhoneNumLbl;
    EditText etName, etEmailId, etPhoneNumber;
    Button btnNext;
    String strPreviousScreenVal = "";
    String strName = "", strEmail = "", strPhoneNum = "";
    boolean isNewCarFlow=false;
    private ImageView iv_basic_details_backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        if(CommonStrings.customVehDetails.getVehCategory().equals(getResources().getString(R.string.new_car)))
        {
            isNewCarFlow=true;
            if(!CommonMethods.getStringValueFromKey(this,CommonStrings.VEH_PURCHASE_AMOUNT).equals(""))
            {
                strPreviousScreenVal=CommonMethods.getStringValueFromKey(this,CommonStrings.VEH_INSURED_AMOUNT);
            }
            else
                {
                    strPreviousScreenVal="";
                }
        }
        else
        {
            isNewCarFlow=false;
            if (CommonStrings.customVehDetails.getInsuranceType() != null) {
                strPreviousScreenVal = CommonStrings.customVehDetails.getInsuranceType();
            } else {
                strPreviousScreenVal = "";
            }

        }

        initView();
    }

    private void initView() {
        iv_basic_details_backBtn = findViewById(R.id.iv_basic_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvNameLbl = findViewById(R.id.tvNameLbl);
        tvEmailLbl = findViewById(R.id.tvEmailLbl);
        tvPhoneNumLbl = findViewById(R.id.tvPhoneNumLbl);
        etName = findViewById(R.id.etName);
        etEmailId = findViewById(R.id.etEmailId);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnNext = findViewById(R.id.btnNext);
        if(isNewCarFlow)
        {
            tvGivenLbl.setText(getString(R.string.lbl_insured_amount));
        }
        else
        {
            tvGivenLbl.setText(getString(R.string.lbl_insurance_on_vehicle));
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
                SpinnerManager.showSpinner(this);
                retrofitInterface.getFromWeb(getOTPRequest(), CommonStrings.OTP_URL_END).enqueue(this);
            }
        } else if (v.getId() == R.id.iv_basic_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
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
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);

        OTPResponse otpResponse = new Gson().fromJson(strRes, OTPResponse.class);
        try {
            if (otpResponse != null && otpResponse.getStatus().toString().equals("true")) {

                if (otpResponse.getData() != null) {
                    CommonStrings.customBasicDetails.setOtp(otpResponse.getData());
                    OTPBottomSheetFragment bottomSheetFragment = new OTPBottomSheetFragment(this);
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


    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    public VehicleDetails getVehicleDetails() {
        VehicleDetails vehicleDetails = new VehicleDetails();
        try {

            vehicleDetails.setHaveVehicleNumber(false);
            vehicleDetails.setVehicleNumber("NA");
            vehicleDetails.setDoesCarHaveLoan(CommonStrings.customVehDetails.getDoesCarHaveLoan());
            vehicleDetails.setHaveVehicleNumber(CommonStrings.customVehDetails.getHaveVehicleNumber());
            vehicleDetails.setVehicleNumber(CommonStrings.customVehDetails.getVehicleNumber());
            vehicleDetails.setMake(CommonStrings.customVehDetails.getMake());
            vehicleDetails.setModel(CommonStrings.customVehDetails.getModel());
            vehicleDetails.setVariant(CommonStrings.customVehDetails.getVariant());
            vehicleDetails.setRegistrationYear(CommonStrings.customVehDetails.getRegistrationYear());
            vehicleDetails.setOwnership(CommonStrings.customVehDetails.getOwnership());
            vehicleDetails.setDoesCarHaveLoan(CommonStrings.customVehDetails.getDoesCarHaveLoan());
            if (CommonStrings.customVehDetails.getInsurance()) {
                vehicleDetails.setInsurance(CommonStrings.customVehDetails.getInsurance());
                vehicleDetails.setInsuranceType(CommonStrings.customVehDetails.getInsuranceType());
                vehicleDetails.setInsuranceAmount(CommonStrings.customVehDetails.getInsuranceAmount());
                //vehicleDetails.setInsuranceValidity(CommonMethods.getIVDateInFormat(CommonStrings.customVehDetails.getInsuranceValidity()));
                vehicleDetails.setInsuranceValidity("2020-01-01");

            } else {
                vehicleDetails.setInsurance(CommonStrings.customVehDetails.getInsurance());
                vehicleDetails.setInsuranceType(CommonStrings.customVehDetails.getInsuranceType());
                vehicleDetails.setInsuranceAmount(CommonStrings.customVehDetails.getInsuranceAmount());
                Log.i(TAG, "getVehicleDetails: " + CommonStrings.customVehDetails.getInsurance());
                Log.i(TAG, "getVehicleDetails: " + CommonStrings.customVehDetails.getInsuranceType());
                Log.i(TAG, "getVehicleDetails: " + CommonStrings.customVehDetails.getInsuranceAmount());
                vehicleDetails.setInsuranceValidity("2020-01-01");
                Log.i(TAG, "getVehicleDetails: " + CommonStrings.customVehDetails.getInsuranceValidity());


            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return vehicleDetails;
    }

    public LoanDetails getLoanDetails() {
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setLoanCategory(CommonStrings.customVehDetails.getVehCategory());
        return loanDetails;
    }
}