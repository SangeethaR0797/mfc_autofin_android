package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import utility.CommonMethods;

public class VehicleOwnerActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenVehVariantVal, tvGivenVehVariantEdit;
    RadioButton radioBtn01, radioBtn02, radioBtn03, radioBtn04, radioBtn05;
    ImageView iv_vehDetails_backBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner);
        initView();
    }

    private void initView() {
        tvGivenVehVariantVal = findViewById(R.id.tvGivenVehVariantVal);
        tvGivenVehVariantEdit = findViewById(R.id.tvGivenVehVariantEdit);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvGivenVehVariantVal.setText(CommonMethods.getStringValueFromKey(VehicleOwnerActivity.this, "veh_variant"));
        tvGivenVehVariantEdit.setOnClickListener(this);
        iv_vehDetails_backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenVehVariantEdit) {
            finish();
        } else if (v.getId() == R.id.radioBtn01) {
            Intent intent = new Intent(VehicleOwnerActivity.this, CarHaveLoanCurrentlyActivity.class);
            startActivity(intent);
        }
    }

}