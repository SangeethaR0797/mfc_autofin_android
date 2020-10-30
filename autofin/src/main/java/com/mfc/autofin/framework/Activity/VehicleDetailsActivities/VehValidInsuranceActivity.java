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

import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class VehValidInsuranceActivity extends AppCompatActivity implements View.OnClickListener {


    private static String TAG = VehValidInsuranceActivity.class.getSimpleName();
    TextView tvGivenVehPostInspection, tvGivenVehPostInspectionVal, tvGivenVehPostInspectionEdit, tvValidInsuranceLbl, tvInsuranceAmountLbl;
    EditText etPostInspectionAmount;
    Button btnValidInsurance, btnValidInsuranceNo, btnNext;
    ImageView iv_vehDetails_backBtn;
    LinearLayout llVehInsuranceAmount;
    String strInsurance="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_valid_insurance);
        Log.i(TAG, "onCreate: ");
        if(CommonStrings.stockResData!=null)
        {
            if(CommonStrings.stockResData.getInsurance()!=null)
            {
                strInsurance=CommonStrings.stockResData.getInsurance();
            }
        }else
        {
            strInsurance="";
        }

        initView();
    }

    private void initView() {
        tvGivenVehPostInspection = findViewById(R.id.tvGivenVehPostInspection);
        tvGivenVehPostInspectionVal = findViewById(R.id.tvGivenVehPostInspectionVal);
        tvGivenVehPostInspectionEdit = findViewById(R.id.tvGivenVehPostInspectionEdit);
        tvValidInsuranceLbl = findViewById(R.id.tvValidInsuranceLbl);
        tvInsuranceAmountLbl = findViewById(R.id.tvInsuranceAmountLbl);
        etPostInspectionAmount = findViewById(R.id.etPostInspectionAmount);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        btnValidInsurance = findViewById(R.id.btnValidInsurance);
        btnValidInsuranceNo = findViewById(R.id.btnValidInsuranceNo);
        llVehInsuranceAmount = findViewById(R.id.llVehInsuranceAmount);
        btnNext = findViewById(R.id.btnNext);
        if(strInsurance.equalsIgnoreCase("Yes"))
        {
            highLightSelectedButton(btnValidInsurance);
            dehighlightButton(btnValidInsuranceNo);
            llVehInsuranceAmount.setVisibility(View.VISIBLE);
        }
        else
        {
            highLightSelectedButton(btnValidInsuranceNo);
            dehighlightButton(btnValidInsurance);
        }


        iv_vehDetails_backBtn.setOnClickListener(this);
        tvGivenVehPostInspectionEdit.setOnClickListener(this);
        etPostInspectionAmount.setOnClickListener(this);
        btnValidInsurance.setOnClickListener(this);
        btnValidInsuranceNo.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    private void dehighlightButton(Button button)
    {
        button.setBackground(getResources().getDrawable(R.drawable.grey_box_1dp));

    }

    private void highLightSelectedButton(Button button)
    {
        button.setBackground(getResources().getDrawable(R.drawable.navy_blue_outline));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenVehPostInspectionEdit) {
            finish();
        } else if (v.getId() == R.id.btnValidInsurance) {
            llVehInsuranceAmount.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btnValidInsuranceNo) {
            llVehInsuranceAmount.setVisibility(View.GONE);
            Intent intent = new Intent(VehValidInsuranceActivity.this, VehInsuranceValidityActivity.class);
            startActivity(intent);

        } else if (v.getId() == R.id.btnNext) {
            if (llVehInsuranceAmount.getVisibility() == View.VISIBLE && etPostInspectionAmount.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter Insurance Amount", Toast.LENGTH_LONG).show();
            } else if (llVehInsuranceAmount.getVisibility() == View.VISIBLE && !etPostInspectionAmount.getText().toString().isEmpty()) {
                CommonMethods.setValueAgainstKey(this, "veh_insurance_amount", etPostInspectionAmount.getText().toString());
                Intent intent = new Intent(VehValidInsuranceActivity.this, VehInsuranceValidityActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(VehValidInsuranceActivity.this, VehInsuranceValidityActivity.class);
                startActivity(intent);
            }
        }
    }
}