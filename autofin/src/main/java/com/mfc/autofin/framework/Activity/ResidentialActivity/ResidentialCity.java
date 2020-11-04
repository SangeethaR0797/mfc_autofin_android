package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import utility.CommonStrings;

public class ResidentialCity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAppBarTitle, tvGivenOTP, tvGivenOTPVal, tvGivenOTPEdit;
    ImageView iv_residential_details_backBtn;
    String otpVal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residential_city2);
        if (!CommonStrings.customBasicDetails.getOtp().isEmpty()) {
            otpVal = CommonStrings.customBasicDetails.getOtp();
        }
        initViews();
    }

    private void initViews() {
        tvAppBarTitle = findViewById(R.id.tvAppBarTitle);
        tvGivenOTP = findViewById(R.id.tvGivenOTP);
        tvGivenOTPVal = findViewById(R.id.tvGivenOTPVal);
        tvGivenOTPEdit = findViewById(R.id.tvGivenOTPEdit);
        iv_residential_details_backBtn = findViewById(R.id.iv_residential_details_backBtn);
        tvGivenOTPVal.setText(otpVal);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_residential_details_backBtn) {
            finish();
        }
    }
}