package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

import static com.mfc.autofin.framework.R.drawable.navy_blue_outline;

public class VehRegNumActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvUserHaveVehNumQn;
    ImageView iv_vehDetails_backBtn;
    Button btnVehNumYes, btnVehNumNo;
    String vehRegNum="";
    Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_reg_num);
        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customVehDetails.getHaveVehicleNumber())
            {
                vehRegNum="Yes";
            }
            else
            {
                vehRegNum="No";
            }
        }
        initView();
    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        tvUserHaveVehNumQn = findViewById(R.id.tvUserHaveVehNumQn);
        btnVehNumYes = findViewById(R.id.btnVehNumYes);
        btnVehNumNo = findViewById(R.id.btnVehNumNo);
        btnNext = findViewById(R.id.btnNext);
        if(vehRegNum.equalsIgnoreCase("Yes"))
        {
            CommonMethods.highLightSelectedButton(VehRegNumActivity.this, btnVehNumYes);
            CommonMethods.deHighLightButton(VehRegNumActivity.this, btnVehNumNo);

        }
        else if(vehRegNum.equalsIgnoreCase("No"))
        {
            CommonMethods.highLightSelectedButton(VehRegNumActivity.this,btnVehNumNo);
            CommonMethods.deHighLightButton(VehRegNumActivity.this,btnVehNumYes);

        }
        iv_vehDetails_backBtn.setOnClickListener(this);
        btnVehNumYes.setOnClickListener(this);
        btnVehNumNo.setOnClickListener(this);
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.GONE);
        }
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            finish();
        } else if (v.getId() == R.id.btnVehNumYes) {
            CommonMethods.highLightSelectedButton(VehRegNumActivity.this, btnVehNumYes);
            CommonMethods.deHighLightButton(VehRegNumActivity.this, btnVehNumNo);
            vehRegNum=btnVehNumYes.getText().toString();

            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }

        } else if (v.getId() == R.id.btnVehNumNo) {
            CommonMethods.highLightSelectedButton(VehRegNumActivity.this, btnVehNumNo);
            CommonMethods.deHighLightButton(VehRegNumActivity.this, btnVehNumYes);
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        }
        else if (v.getId() == R.id.btnNext) {
            if(!vehRegNum.isEmpty())
            moveToNextPage();
            else
                CommonMethods.showToast(this,"Please select anyone option");
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void moveToNextPage()
    {
        if(vehRegNum.equalsIgnoreCase("YES"))
        {
            CommonStrings.customVehDetails.setHaveVehicleNumber(true);
            Intent intent = new Intent(VehRegNumActivity.this, VehRegNumAns.class);
            startActivity(intent);
        }
        else if(vehRegNum.equalsIgnoreCase("NO"))
        {
            CommonStrings.customVehDetails.setHaveVehicleNumber(false);
            CommonStrings.customVehDetails.setVehicleNumber("NA");
            CommonMethods.setValueAgainstKey(this,CommonStrings.CAR_HAVE_REG_NO,btnVehNumNo.getText().toString());
            Intent intent = new Intent(VehRegNumActivity.this, VehRegistrationYear.class);
            startActivity(intent);
        }
    }
}