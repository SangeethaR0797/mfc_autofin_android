package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.R;

import utility.CommonStrings;

public class VehPostInspectionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenVehLoan, tvGivenVehLoanVal, tvGivenVehLoanEdit, tvVehValuationPostInspLbl, tvPostInspectionAmountLbl;
    EditText etPostInspectionAmount;
    Button btnVehPostInspectionYes, btnVehPostInspectionNo, btnNext;
    LinearLayout llVehPostInspection;
    ImageView iv_vehDetails_backBtn;
    String strDoesCarHaveLoan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_post_inspection);
        if (CommonStrings.customVehDetails.getDoesCarHaveLoan()) {
            strDoesCarHaveLoan = "Yes";
        } else {
            strDoesCarHaveLoan = "No";
        }
        initView();
    }

    private void initView() {
        tvGivenVehLoan = findViewById(R.id.tvGivenVehLoan);
        tvGivenVehLoanVal = findViewById(R.id.tvGivenVehLoanVal);
        tvGivenVehLoanEdit = findViewById(R.id.tvGivenVehLoanEdit);
        tvVehValuationPostInspLbl = findViewById(R.id.tvVehValuationPostInspLbl);
        tvPostInspectionAmountLbl = findViewById(R.id.tvPostInspectionAmountLbl);
        etPostInspectionAmount = findViewById(R.id.etPostInspectionAmount);
        btnVehPostInspectionYes = findViewById(R.id.btnVehPostInspectionYes);
        btnVehPostInspectionNo = findViewById(R.id.btnVehPostInspectionNo);
        btnNext = findViewById(R.id.btnNext);
        llVehPostInspection = findViewById(R.id.llVehPostInspection);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvGivenVehLoanVal.setText(strDoesCarHaveLoan);
        iv_vehDetails_backBtn.setOnClickListener(this);
        tvGivenVehLoanEdit.setOnClickListener(this);
        btnVehPostInspectionYes.setOnClickListener(this);
        btnVehPostInspectionNo.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenVehLoanEdit) {
            finish();
        } else if (v.getId() == R.id.btnVehPostInspectionYes) {
            llVehPostInspection.setVisibility(View.VISIBLE);
            etPostInspectionAmount.setFocusable(true);
            etPostInspectionAmount.setEnabled(true);
        } else if (v.getId() == R.id.btnVehPostInspectionNo) {
            llVehPostInspection.setVisibility(View.GONE);
            etPostInspectionAmount.setText("");
        } else if (v.getId() == R.id.btnNext) {
            if (llVehPostInspection.getVisibility() == View.VISIBLE) {
                if (!etPostInspectionAmount.getText().toString().isEmpty()) {
                    CommonStrings.customVehDetails.setPostevaluation(btnVehPostInspectionYes.getText().toString() + " (" + etPostInspectionAmount.getText().toString() + " )");
                    Intent intent = new Intent(VehPostInspectionActivity.this, VehValidInsuranceActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Please enter Post valuation amount", Toast.LENGTH_LONG).show();

                }
            } else {
                CommonStrings.customVehDetails.setPostevaluation(btnVehPostInspectionNo.getText().toString());
                Intent intent = new Intent(VehPostInspectionActivity.this, VehValidInsuranceActivity.class);
                startActivity(intent);
            }
        }
    }
}