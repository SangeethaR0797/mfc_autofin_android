package com.mfc.autofin.framework.Activity.BasicDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.R;

import utility.CommonStrings;

public class BasicDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenInsType, tvGivenInsTypeVal, tvGivenInsTypeEdit;
    String strInsuranceType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        strInsuranceType = CommonStrings.customVehDetails.getInsuranceType();
        initView();
        Toast.makeText(this, "This page is Under Development", Toast.LENGTH_SHORT).show();
    }

    private void initView() {
        tvGivenInsType = findViewById(R.id.tvGivenInsType);
        tvGivenInsTypeVal = findViewById(R.id.tvGivenInsTypeVal);
        tvGivenInsTypeEdit = findViewById(R.id.tvGivenInsTypeEdit);
        tvGivenInsTypeVal.setText(strInsuranceType);
        tvGivenInsTypeEdit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenInsValidityEdit) {
            finish();
        }
    }
}