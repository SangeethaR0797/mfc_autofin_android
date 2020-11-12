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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_reg_num);
        initView();
    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        tvUserHaveVehNumQn = findViewById(R.id.tvUserHaveVehNumQn);
        btnVehNumYes = findViewById(R.id.btnVehNumYes);
        btnVehNumNo = findViewById(R.id.btnVehNumNo);
        iv_vehDetails_backBtn.setOnClickListener(this);
        btnVehNumYes.setOnClickListener(this);
        btnVehNumNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            finish();
        } else if (v.getId() == R.id.btnVehNumYes) {
            CommonMethods.highLightSelectedButton(VehRegNumActivity.this, btnVehNumYes);
            CommonMethods.deHighLightButton(VehRegNumActivity.this, btnVehNumNo);
            Intent intent = new Intent(VehRegNumActivity.this, VehRegNumAns.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnVehNumNo) {
            CommonStrings.customVehDetails.setVehRegNum("NA");
            CommonMethods.highLightSelectedButton(VehRegNumActivity.this, btnVehNumNo);
            CommonMethods.deHighLightButton(VehRegNumActivity.this, btnVehNumYes);
            Intent intent = new Intent(VehRegNumActivity.this, VehRegistrationYear.class);
            startActivity(intent);
        }
    }
}