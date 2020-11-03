package fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.ResidentialCityActivity;
import com.mfc.autofin.framework.R;

import model.otp_models.CustomerMobile;
import model.otp_models.OTPRequest;
import model.otp_models.OTPResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class OTPBottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener, Callback<Object> {

    private static final String TAG = OTPBottomSheetFragment.class.getSimpleName();
    TextView tvCMobileNum, tvOTPTimer, tvResendOTPLbl;
    EditText etOTPVal;
    ImageView iv_dialog_close;
    Button btnProceed;
    Activity activity;

    public OTPBottomSheetFragment(Activity activity) {
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_o_t_p_bottom_sheet_list_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvCMobileNum = view.findViewById(R.id.tvCMobileNum);
        tvOTPTimer = view.findViewById(R.id.tvOTPTimer);
        tvResendOTPLbl = view.findViewById(R.id.tvResendOTPLbl);
        etOTPVal = view.findViewById(R.id.etOTPVal);
        iv_dialog_close = view.findViewById(R.id.iv_dialog_close);
        btnProceed = view.findViewById(R.id.btnProceed);
        tvCMobileNum.setText(CommonStrings.customBasicDetails.getCustomerMobile());
        iv_dialog_close.setOnClickListener(this);
        btnProceed.setOnClickListener(this);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                tvOTPTimer.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                tvOTPTimer.setText("00");
                CommonStrings.customBasicDetails.setOtp("");
                Toast.makeText(activity, "Your OTP expired! Please try again.", Toast.LENGTH_LONG).show();
            }
        }.start();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_dialog_close) {
            dismiss();
        } else if (v.getId() == R.id.etOTPVal) {
            dismiss();
        } else if (v.getId() == R.id.tvResendOTPLbl) {
            SpinnerManager.showSpinner(v.getContext());
            retrofitInterface.getFromWeb(getOTPRequest(), CommonStrings.OTP_URL_END).enqueue(this);
        } else if (v.getId() == R.id.btnProceed) {
            if (!etOTPVal.getText().toString().isEmpty() && etOTPVal.getText().toString().equalsIgnoreCase(CommonStrings.customBasicDetails.getOtp()))
                startActivity(new Intent(v.getContext(), ResidentialCityActivity.class));
            else {
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

        OTPResponse otpResponse = new Gson().fromJson(strRes, OTPResponse.class);
        try {
            if (otpResponse != null && otpResponse.getStatus().toString().equals("true")) {

                if (otpResponse.getData() != null) {
                    CommonStrings.customBasicDetails.setOtp(otpResponse.getData());
                }

            } else {
                Toast.makeText(activity, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();
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