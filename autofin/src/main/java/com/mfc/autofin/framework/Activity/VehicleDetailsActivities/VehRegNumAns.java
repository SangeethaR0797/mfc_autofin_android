package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

public class VehRegNumAns extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_vehDetails_backBtn;
    TextView tvVehCategoryQn, tvRegNoLbl;
    EditText etVehRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_reg_num_ans);
        initView();
    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvVehCategoryQn = findViewById(R.id.tvVehCategoryQn);
        tvRegNoLbl = findViewById(R.id.tvRegNoLbl);
        etVehRegNo = findViewById(R.id.etVehRegNo);
        iv_vehDetails_backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            Intent intent = new Intent(VehRegNumAns.this, VehRegNumActivity.class);
            startActivity(intent);
        }

    }
}