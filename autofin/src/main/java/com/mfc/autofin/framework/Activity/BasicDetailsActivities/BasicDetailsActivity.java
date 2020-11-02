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
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehRegNumAns;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehRegistrationYear;
import com.mfc.autofin.framework.R;

import fragments.OTPBottomSheetFragment;
import model.otp_models.CustomerMobile;
import model.otp_models.OTPRequest;
import model.otp_models.OTPResponse;
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
    TextView tvGivenInsType, tvGivenInsTypeVal, tvGivenInsTypeEdit, tvNameLbl, tvEmailLbl, tvPhoneNumLbl;
    EditText etName, etEmailId, etPhoneNumber;
    Button btnNext;
    String strInsuranceType = "";
    String strName = "", strEmail = "", strPhoneNum = "";

    private ImageView iv_vehDetails_backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        strInsuranceType = CommonStrings.customVehDetails.getInsuranceType();
        initView();
    }

    private void initView() {
        tvGivenInsType = findViewById(R.id.tvGivenInsType);
        tvGivenInsTypeVal = findViewById(R.id.tvGivenInsTypeVal);
        tvGivenInsTypeEdit = findViewById(R.id.tvGivenInsTypeEdit);
        tvNameLbl = findViewById(R.id.tvNameLbl);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvEmailLbl = findViewById(R.id.tvEmailLbl);
        tvPhoneNumLbl = findViewById(R.id.tvPhoneNumLbl);
        etName = findViewById(R.id.etName);
        etEmailId = findViewById(R.id.etEmailId);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnNext = findViewById(R.id.btnNext);
        tvGivenInsTypeVal.setText(strInsuranceType);
        tvNameLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvEmailLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvPhoneNumLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        etName.setTypeface(CustomFonts.getRobotoMedium(this));
        etEmailId.setTypeface(CustomFonts.getRobotoMedium(this));
        etPhoneNumber.setTypeface(CustomFonts.getRobotoMedium(this));
        btnNext.setTypeface(CustomFonts.getRobotoMedium(this));
        tvGivenInsTypeEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        iv_vehDetails_backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenInsValidityEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (validate()) {
                CommonStrings.customBasicDetails.setFullName(strName);
                CommonStrings.customBasicDetails.setEmail(strEmail);
                CommonStrings.customBasicDetails.setCustomerMobile(strPhoneNum);
                SpinnerManager.showSpinner(this);
                retrofitInterface.getFromWeb(getOTPRequest(), CommonStrings.OTP_URL_END).enqueue(this);
            }
        } else if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
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
        if (!strName.isEmpty() && strName.matches("^[a-z A-Z ]*$")) {
            if (!strEmail.isEmpty() && strEmail.matches(emailPattern)) {
                if (!strPhoneNum.isEmpty() && strPhoneNum.matches("^[6-9]\\d{9}$")) {
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
}