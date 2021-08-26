package fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidentialCity;
import com.mfc.autofin.framework.R;

import model.add_lead_details.AddLeadDetails;
import model.add_lead_details.AddLeadRequest;
import model.add_lead_details.AddLeadResponse;
import model.basic_details.BasicDetails;
import model.otp_models.CustomerMobile;
import model.otp_models.OTPRequest;
import model.otp_models.OTPResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class OTPBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, Callback<Object> {

    private static final String TAG = OTPBottomSheetFragment.class.getSimpleName();
    TextView tvCMobileNum, tvOTPTimer, tvResendOTPLbl;
    EditText etOTPVal, editText;
    ImageView iv_dialog_close;
    Button btnProceed;
    Activity activity;
    LinearLayout llEditPhoneNum;
    int remainingSecs = 30000;
    CountDownTimer otpTimer;

    public OTPBottomSheetFragment(Activity activity, EditText editText) {
        this.activity = activity;
        this.editText = editText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_o_t_p_bottom_sheet_list_dialog, container, false);
        initView(view);
        //loadTimer(remainingSecs, 0);
        otpTimer= new CountDownTimer(remainingSecs, 1000) {

            long milliSecondRemaining = 0;

            public void onTick(long millisUntilFinished) {
                milliSecondRemaining = millisUntilFinished;
                tvOTPTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {

                tvOTPTimer.setText("00");
                CommonStrings.customBasicDetails.setOtp("");
                Toast.makeText(activity, "Your Verification Code expired! Please try again.", Toast.LENGTH_LONG).show();
            }
        }.start();
        return view;
    }

    private void initView(View view) {
        tvCMobileNum = view.findViewById(R.id.tvCMobileNum);
        tvOTPTimer = view.findViewById(R.id.tvOTPTimer);
        tvResendOTPLbl = view.findViewById(R.id.tvResendOTPLbl);
        etOTPVal = view.findViewById(R.id.etOTPVal);
        iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        btnProceed = view.findViewById(R.id.btnProceed);
        llEditPhoneNum = view.findViewById(R.id.llEditPhoneNum);
        tvCMobileNum.setText(CommonStrings.customBasicDetails.getCustomerMobile());
        etOTPVal.setText(CommonStrings.customBasicDetails.getOtp());
        tvCMobileNum.setOnClickListener(this);
        tvResendOTPLbl.setOnClickListener(this);
        iv_dialog_close.setOnClickListener(this);
        llEditPhoneNum.setOnClickListener(this);
        btnProceed.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    public void loadTimer(int remainingSecs, int givenTag) {


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_dialog_close) {
            dismiss();
        } else if (v.getId() == R.id.llEditPhoneNum) {
            otpTimer.cancel();
            CommonStrings.customBasicDetails.setCustomerMobile("");
            editText.setText("");
            dismiss();
        } else if (v.getId() == R.id.tvResendOTPLbl) {
            if (!CommonStrings.customBasicDetails.getOtp().equals("")) {
                otpTimer.cancel();
            }
            SpinnerManager.showSpinner(activity);
            retrofitInterface.getFromWeb(getOTPRequest(), CommonStrings.OTP_URL_END).enqueue(this);
        } else if (v.getId() == R.id.btnProceed) {
            if (!etOTPVal.getText().toString().equals("") && etOTPVal.getText().toString().equalsIgnoreCase(CommonStrings.customBasicDetails.getOtp())) {
                otpTimer.cancel();
                CommonStrings.customBasicDetails.setOtp(etOTPVal.getText().toString());
                SpinnerManager.showSpinner(activity);
                retrofitInterface.getFromWeb(getAddLeadRequest(), Global.customerAPI_BaseURL+CommonStrings.ADD_LEAD_URL_END).enqueue(this);
            } else {
                Toast.makeText(activity, getString(R.string.wrong_otp), Toast.LENGTH_LONG).show();
                etOTPVal.setText("");
            }
        }

    }

    private OTPRequest getOTPRequest() {
        OTPRequest otpRequest = new OTPRequest();
        otpRequest.setUserId(CommonMethods.getStringValueFromKey(activity, CommonStrings.DEALER_ID_VAL));
        otpRequest.setUserType(CommonMethods.getStringValueFromKey(activity, CommonStrings.USER_TYPE_VAL));
        CustomerMobile customerMobile = new CustomerMobile();
        customerMobile.setCustomerMobile(CommonStrings.customBasicDetails.getCustomerMobile());
        otpRequest.setData(customerMobile);
        return otpRequest;
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

        SpinnerManager.hideSpinner(activity);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        String url = response.raw().request().url().toString();


        if (url.contains(CommonStrings.OTP_URL_END)) {
            OTPResponse otpResponse = new Gson().fromJson(strRes, OTPResponse.class);
            try {
                if (otpResponse != null && otpResponse.getStatus().toString().equals("true")) {

                    if (otpResponse.getData() != null) {
                        otpTimer.start();
                    }
                } else {
                    Toast.makeText(activity, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if (url.contains(CommonStrings.ADD_LEAD_URL_END)) {

            try {
                AddLeadResponse addLeadResponse = new Gson().fromJson(strRes, AddLeadResponse.class);

                if (addLeadResponse != null && addLeadResponse.getStatus()) {
                    Log.i(TAG, "onResponse: "+addLeadResponse.getData());
                    CommonMethods.setValueAgainstKey(activity, CommonStrings.CUSTOMER_ID, String.valueOf(addLeadResponse.getData()));
                    Log.i(TAG, "onResponse: "+CommonMethods.getStringValueFromKey(activity, CommonStrings.CUSTOMER_ID));
                    Toast.makeText(activity, addLeadResponse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(activity, ResidentialCity.class);
                    startActivity(intent);
                }
                /*else {
                    if(addLeadResponse.getMessage()!=null)
                    {
                        CommonMethods.showToast(activity, addLeadResponse.getMessage());
                    }
                    else
                    {
                        CommonMethods.showToast(activity, "Try Again");
                    }

                }*/

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    public AddLeadRequest getAddLeadRequest() {
        AddLeadRequest addLeadRequest = new AddLeadRequest();
        addLeadRequest.setData(getAddLeadDetails());
        addLeadRequest.setUserId(CommonMethods.getStringValueFromKey(activity, CommonStrings.DEALER_ID_VAL));
        addLeadRequest.setUserType(CommonMethods.getStringValueFromKey(activity, CommonStrings.USER_TYPE_VAL));
        return addLeadRequest;
    }

    public AddLeadDetails getAddLeadDetails() {
        AddLeadDetails addLeadDetails = new AddLeadDetails();
        BasicDetailsActivity basicDetailsActivity = new BasicDetailsActivity();
        addLeadDetails.setVehicleDetails(basicDetailsActivity.getVehicleDetails());
        addLeadDetails.setLoanDetails(basicDetailsActivity.getLoanDetails());
        addLeadDetails.setBasicDetails(getBasicDetails());
        return addLeadDetails;
    }

    public BasicDetails getBasicDetails() {
        BasicDetails basicDetails = new BasicDetails();
        basicDetails.setFullName(CommonStrings.customBasicDetails.getFullName());
        basicDetails.setCustomerMobile(CommonStrings.customBasicDetails.getCustomerMobile());
        basicDetails.setEmail(CommonStrings.customBasicDetails.getEmail());
        basicDetails.setOtp(CommonStrings.customBasicDetails.getOtp());
        basicDetails.setSalutation(CommonStrings.customBasicDetails.getSalutation());
        return basicDetails;
    }

}