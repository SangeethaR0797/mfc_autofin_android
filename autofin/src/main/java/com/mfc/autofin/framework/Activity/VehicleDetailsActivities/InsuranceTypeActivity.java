package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.R;

public class InsuranceTypeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenInsValidity, tvGivenInsValidityVal, tvGivenInsValidityEdit, tvVehTypeQn;
    RadioGroup rgVehType;
    RadioButton radioBtnComprehensive, radioBtnThirdParty;
    ImageView iv_vehDetails_backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_type);
        iniView();
    }

    private void iniView() {
        tvGivenInsValidity = findViewById(R.id.tvGivenInsValidity);
        tvGivenInsValidityVal = findViewById(R.id.tvGivenInsValidityVal);
        tvGivenInsValidityEdit = findViewById(R.id.tvGivenInsValidityEdit);
        tvVehTypeQn = findViewById(R.id.tvVehTypeQn);
        rgVehType = findViewById(R.id.rgVehType);
        radioBtnComprehensive = findViewById(R.id.radioBtnComprehensive);
        radioBtnThirdParty = findViewById(R.id.radioBtnThirdParty);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        iv_vehDetails_backBtn.setOnClickListener(this);
        tvGivenInsValidityEdit.setOnClickListener(this);
        rgVehType.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenInsValidityEdit) {
            finish();
        } else if (v.getId() == R.id.radioBtnThirdParty) {
            Intent intent = new Intent(InsuranceTypeActivity.this, BasicDetailsActivity.class);
            startActivity(intent);
        }
    }
}