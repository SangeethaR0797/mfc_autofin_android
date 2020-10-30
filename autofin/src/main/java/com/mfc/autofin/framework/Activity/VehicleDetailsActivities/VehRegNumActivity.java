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
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvUserHaveVehNumQn = findViewById(R.id.tvUserHaveVehNumQn);
        btnVehNumYes = findViewById(R.id.btnVehNumYes);
        btnVehNumNo = findViewById(R.id.btnVehNumNo);
        iv_vehDetails_backBtn.setOnClickListener(this);
        btnVehNumYes.setOnClickListener(this);
        btnVehNumNo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.btnVehNumYes) {
            btnVehNumYes.setBackgroundResource(R.drawable.navy_blue_outline);
            btnVehNumNo.setBackgroundResource(R.drawable.grey_box_1dp);
            btnVehNumYes.setTextColor(getResources().getColor(R.color.navy_blue));
            btnVehNumNo.setTextColor(getResources().getColor(R.color.grey_color));
            Intent intent = new Intent(VehRegNumActivity.this, VehRegNumAns.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnVehNumNo) {
            btnVehNumNo.setBackgroundResource(R.drawable.navy_blue_outline);
            btnVehNumYes.setBackgroundResource(R.drawable.grey_box_1dp);
            btnVehNumYes.setTextColor(getResources().getColor(R.color.grey_color));
            btnVehNumNo.setTextColor(getResources().getColor(R.color.navy_blue));
            Intent intent = new Intent(VehRegNumActivity.this, VehRegistrationYear.class);
            startActivity(intent);
        }
    }
}