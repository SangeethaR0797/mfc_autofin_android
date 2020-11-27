package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.R;

import model.basic_details.BasicDetails;
import utility.CommonMethods;
import utility.CommonStrings;

public class VehValidInsuranceActivity extends AppCompatActivity implements View.OnClickListener {


    private static String TAG = VehValidInsuranceActivity.class.getSimpleName();
    TextView tvGivenVehPostInspection, tvGivenVehPostInspectionVal, tvGivenVehPostInspectionEdit, tvValidInsuranceLbl, tvInsuranceAmountLbl;
    private EditText etInsuranceAmount;
    Button btnValidInsurance, btnValidInsuranceNo, btnNext;
    ImageView iv_vehDetails_backBtn;
    LinearLayout llVehInsuranceAmount;
    String strPostInspectionVal = "", strInsurance = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_valid_insurance);
        Log.i(TAG, "onCreate: ");

        if (!CommonMethods.getStringValueFromKey(this, "post_inspection_amount").equals("")) {
            strPostInspectionVal = CommonMethods.getStringValueFromKey(this, "post_inspection_amount");
        } else {
            strPostInspectionVal = "";
        }
        if (CommonStrings.stockResData != null) {
            if (CommonStrings.stockResData.getInsurance() != null) {
                strInsurance = CommonStrings.stockResData.getInsurance();
            }
        } else {
            strInsurance = "";
        }

        initView();
    }

    private void initView() {
        tvGivenVehPostInspection = findViewById(R.id.tvGivenVehPostInspection);
        tvGivenVehPostInspectionVal = findViewById(R.id.tvGivenVehPostInspectionVal);
        tvGivenVehPostInspectionEdit = findViewById(R.id.tvGivenVehPostInspectionEdit);
        tvValidInsuranceLbl = findViewById(R.id.tvValidInsuranceLbl);
        tvInsuranceAmountLbl = findViewById(R.id.tvInsuranceAmountLbl);
        etInsuranceAmount = findViewById(R.id.etInsuranceAmount);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        btnValidInsurance = findViewById(R.id.btnValidInsurance);
        btnValidInsuranceNo = findViewById(R.id.btnValidInsuranceNo);
        llVehInsuranceAmount = findViewById(R.id.llVehInsuranceAmount);
        btnNext = findViewById(R.id.btnNext);
        tvGivenVehPostInspectionVal.setText(strPostInspectionVal);

        if (strInsurance.equalsIgnoreCase("Yes")) {
            CommonMethods.highLightSelectedButton(VehValidInsuranceActivity.this, btnValidInsurance);
            CommonMethods.deHighLightButton(VehValidInsuranceActivity.this, btnValidInsuranceNo);
            llVehInsuranceAmount.setVisibility(View.VISIBLE);
        } else {
            CommonMethods.highLightSelectedButton(VehValidInsuranceActivity.this, btnValidInsuranceNo);
            CommonMethods.deHighLightButton(VehValidInsuranceActivity.this, btnValidInsurance);
        }

        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);
        tvGivenVehPostInspectionEdit.setOnClickListener(this);
        btnValidInsurance.setOnClickListener(this);
        btnValidInsuranceNo.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenVehPostInspectionEdit) {
            finish();
        } else if (v.getId() == R.id.btnValidInsurance) {
            strInsurance = "Yes";
            llVehInsuranceAmount.setVisibility(View.VISIBLE);
            CommonMethods.highLightSelectedButton(this, btnValidInsurance);
            CommonMethods.deHighLightButton(this, btnValidInsuranceNo);
            etInsuranceAmount.setFocusable(true);
            etInsuranceAmount.setEnabled(true);
        } else if (v.getId() == R.id.btnValidInsuranceNo) {
            CommonMethods.highLightSelectedButton(this, btnValidInsuranceNo);
            CommonMethods.deHighLightButton(this, btnValidInsurance);
            llVehInsuranceAmount.setVisibility(View.GONE);
        } else if (v.getId() == R.id.btnNext) {
            if (llVehInsuranceAmount.getVisibility() == View.VISIBLE && etInsuranceAmount.getText().toString().equals("")) {
                Toast.makeText(this, "Please enter Insurance Amount", Toast.LENGTH_LONG).show();
            } else if (llVehInsuranceAmount.getVisibility() == View.VISIBLE && !etInsuranceAmount.getText().toString().equals("")) {
                CommonStrings.customVehDetails.setInsurance(true);
                CommonStrings.customVehDetails.setInsuranceAmount(etInsuranceAmount.getText().toString());
                Intent intent = new Intent(VehValidInsuranceActivity.this, InsuranceTypeActivity.class);
                startActivity(intent);
            } else {
                CommonStrings.customVehDetails.setInsurance(false);
                CommonStrings.customVehDetails.setInsuranceAmount("0");
                CommonStrings.customVehDetails.setInsuranceType("NA");
                CommonStrings.customVehDetails.setInsuranceValidity("NA");
                Intent intent = new Intent(VehValidInsuranceActivity.this, BasicDetailsActivity.class);
                startActivity(intent);
            }
        }
    }
    @Override
    public void onBackPressed() {
    }
}