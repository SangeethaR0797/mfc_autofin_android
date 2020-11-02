package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;

public class VehicleOwnerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    TextView tvGivenVehVariantVal, tvGivenVehVariantEdit;
    RadioButton radioBtn01, radioBtn02, radioBtn03, radioBtn04, radioBtn05;
    ImageView iv_vehDetails_backBtn;
    String strVariantVal="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner);
        strVariantVal=CommonStrings.customVehDetails.getVariant();
        initView();
    }

    private void initView() {
        tvGivenVehVariantVal = findViewById(R.id.tvGivenVehVariantVal);
        tvGivenVehVariantEdit = findViewById(R.id.tvGivenVehVariantEdit);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        radioBtn01 = findViewById(R.id.radioBtn01);
        radioBtn02 = findViewById(R.id.radioBtn02);
        radioBtn03 = findViewById(R.id.radioBtn03);
        radioBtn04 = findViewById(R.id.radioBtn04);
        radioBtn05 = findViewById(R.id.radioBtn05);
        tvGivenVehVariantVal.setText(strVariantVal);
        tvGivenVehVariantEdit.setOnClickListener(this);
        iv_vehDetails_backBtn.setOnClickListener(this);
        radioBtn01.setOnCheckedChangeListener(this);
        radioBtn02.setOnCheckedChangeListener(this);
        radioBtn03.setOnCheckedChangeListener(this);
        radioBtn04.setOnCheckedChangeListener(this);
        radioBtn05.setOnCheckedChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenVehVariantEdit) {
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            if (button.getId() == R.id.radioBtn01) {
                radioBtn02.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn05.setChecked(false);
                moveToNextPage(radioBtn01.getText().toString());
            }
            if (button.getId() == R.id.radioBtn02) {
                radioBtn01.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn05.setChecked(false);
                moveToNextPage(radioBtn02.getText().toString());
            }
            if (button.getId() == R.id.radioBtn03) {
                radioBtn02.setChecked(false);
                radioBtn01.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn05.setChecked(false);
                moveToNextPage(radioBtn03.getText().toString());
            }
            if (button.getId() == R.id.radioBtn04) {
                radioBtn02.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn01.setChecked(false);
                radioBtn05.setChecked(false);
                moveToNextPage(radioBtn04.getText().toString());
            }
            if (button.getId() == R.id.radioBtn05) {
                radioBtn02.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn01.setChecked(false);
                moveToNextPage(radioBtn05.getText().toString());
            }
        }


    }

    private void moveToNextPage(String strOwner) {
        CommonStrings.customVehDetails.setOwnership(Integer.valueOf(strOwner));
        Intent intent = new Intent(VehicleOwnerActivity.this, CarHaveLoanCurrentlyActivity.class);
        startActivity(intent);
    }
}