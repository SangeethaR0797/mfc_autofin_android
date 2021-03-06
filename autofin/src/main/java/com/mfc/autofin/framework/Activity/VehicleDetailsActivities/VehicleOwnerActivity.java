package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.new_car_flow.VehiclePurchaseAmountActivity;
import com.mfc.autofin.framework.R;

import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;

public class VehicleOwnerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    TextView tvGivenVehVariantVal, tvGivenVehVariantEdit;
    RadioButton radioBtn01, radioBtn02, radioBtn03, radioBtn04, radioBtn05;
    ImageView iv_vehDetails_backBtn;
    String strVariantVal = "",strOwnership="";
    private double noOfOwner=0;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_owner);
        strVariantVal = CommonStrings.customVehDetails.getVariant();
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getOwnership() != 0) {
                noOfOwner = CommonStrings.customVehDetails.getOwnership();
                strOwnership=CommonMethods.getFormattedAmount(noOfOwner);
            }
        }

        initView();

    }

    private void initView() {
        tvGivenVehVariantVal = findViewById(R.id.tvGivenVehVariantVal);
        tvGivenVehVariantEdit = findViewById(R.id.tvGivenVehVariantEdit);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        radioBtn01 = findViewById(R.id.radioBtn01);
        radioBtn02 = findViewById(R.id.radioBtn02);
        radioBtn03 = findViewById(R.id.radioBtn03);
        radioBtn04 = findViewById(R.id.radioBtn04);
        radioBtn05 = findViewById(R.id.radioBtn05);
        btnNext=findViewById(R.id.btnNext);
        tvGivenVehVariantVal.setText(strVariantVal);
        tvGivenVehVariantEdit.setOnClickListener(this);
        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);
        if(noOfOwner==1)
        {
            radioBtn01.setChecked(true);
            radioBtn02.setChecked(false);
            radioBtn03.setChecked(false);
            radioBtn04.setChecked(false);
            radioBtn05.setChecked(false);
        }
        else if(noOfOwner==2)
        {
            radioBtn02.setChecked(true);
            radioBtn01.setChecked(false);
            radioBtn03.setChecked(false);
            radioBtn04.setChecked(false);
            radioBtn05.setChecked(false);
        }
        else if(noOfOwner==3)
        {
            radioBtn03.setChecked(true);
            radioBtn02.setChecked(false);
            radioBtn01.setChecked(false);
            radioBtn04.setChecked(false);
            radioBtn05.setChecked(false);
        }
        else if(noOfOwner==4)
        {
            radioBtn04.setChecked(true);
            radioBtn03.setChecked(false);
            radioBtn02.setChecked(false);
            radioBtn01.setChecked(false);
            radioBtn05.setChecked(false);
        }
        else if(noOfOwner==5)
        {
            radioBtn05.setChecked(true);
            radioBtn03.setChecked(false);
            radioBtn02.setChecked(false);
            radioBtn01.setChecked(false);
            radioBtn04.setChecked(false);
        }
        radioBtn01.setOnCheckedChangeListener(this);
        radioBtn02.setOnCheckedChangeListener(this);
        radioBtn03.setOnCheckedChangeListener(this);
        radioBtn04.setOnCheckedChangeListener(this);
        radioBtn05.setOnCheckedChangeListener(this);

        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
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
                strOwnership=radioBtn01.getText().toString();
                if(!CommonStrings.IS_OLD_LEAD)
                {
                    moveToNextPage();
                }
            }
            if (button.getId() == R.id.radioBtn02) {
                radioBtn01.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn05.setChecked(false);
                strOwnership=radioBtn02.getText().toString();
                if(!CommonStrings.IS_OLD_LEAD)
                {
                    moveToNextPage();
                }
            }
            if (button.getId() == R.id.radioBtn03) {
                radioBtn02.setChecked(false);
                radioBtn01.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn05.setChecked(false);
                strOwnership=radioBtn03.getText().toString();
                if(!CommonStrings.IS_OLD_LEAD)
                {
                    moveToNextPage();
                }
            }
            if (button.getId() == R.id.radioBtn04) {
                radioBtn02.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn01.setChecked(false);
                radioBtn05.setChecked(false);
                strOwnership=radioBtn04.getText().toString();
                if(!CommonStrings.IS_OLD_LEAD)
                {
                    moveToNextPage();
                }
            }
            if (button.getId() == R.id.radioBtn05) {
                radioBtn02.setChecked(false);
                radioBtn03.setChecked(false);
                radioBtn04.setChecked(false);
                radioBtn01.setChecked(false);
                strOwnership=radioBtn05.getText().toString();
                if(!CommonStrings.IS_OLD_LEAD)
                {
                    moveToNextPage();
                }
            }
        }


    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenVehVariantEdit) {
            finish();
        }else if (v.getId() == R.id.btnNext) {
            if(!strOwnership.isEmpty())
            {
                moveToNextPage();
            }
            else
            {
                CommonMethods.showToast(this,"Please select anyone option");
            }
        }
    }


    private void moveToNextPage() {
        CommonStrings.customVehDetails.setOwnership(Integer.valueOf(strOwnership));
        CommonStrings.customVehDetails.setOnRoadPrice(Double.parseDouble("0"));
        Intent intent = new Intent(VehicleOwnerActivity.this, VehiclePurchaseAmountActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
    }
}