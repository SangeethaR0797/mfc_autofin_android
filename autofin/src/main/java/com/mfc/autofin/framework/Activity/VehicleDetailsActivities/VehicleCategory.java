package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import controller.DashboardAdapter;

public class VehicleCategory extends AppCompatActivity implements View.OnClickListener {

    TextView tvVehCategoryQn;
    RadioGroup rgVehCategory;
    RadioButton radioBtnNewCar, radioBtnOldCar, radioBtnNewBike;
    ImageView iv_vehDetails_backBtn;

    private String TAG=VehicleCategory.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_category);
        Log.i(TAG, "onCreate: ");
        initView();
    }

    private void initView() {

        tvVehCategoryQn = findViewById(R.id.tvVehCategoryQn);
        rgVehCategory = findViewById(R.id.rgVehCategory);
        radioBtnNewCar = findViewById(R.id.radioBtnNewCar);
        radioBtnNewBike = findViewById(R.id.radioBtnNewBike);
        radioBtnOldCar = findViewById(R.id.radioBtnOldCar);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);

        iv_vehDetails_backBtn.setOnClickListener(this);
        radioBtnNewCar.setOnClickListener(this);
        radioBtnNewBike.setOnClickListener(this);
        radioBtnOldCar.setOnClickListener(this);
        iv_vehDetails_backBtn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            Intent intent = new Intent(VehicleCategory.this, AutoFinDashBoardActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.radioBtnOldCar) {
            if (radioBtnOldCar.isChecked()) {
                Intent intent = new Intent(VehicleCategory.this, VehRegNumActivity.class);
                startActivity(intent);
            }
        }

    }
}