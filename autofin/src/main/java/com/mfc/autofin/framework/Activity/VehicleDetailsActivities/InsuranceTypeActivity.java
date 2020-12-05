package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.R;

import controller.DashboardAdapter;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;

public class InsuranceTypeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenInsValidity, tvGivenInsValidityVal, tvGivenInsValidityEdit, tvVehTypeQn;
    RadioGroup rgVehType;
    RadioButton radioBtnComprehensive, radioBtnThirdParty;
    ImageView iv_vehDetails_backBtn;
    String strInsurance = "", strInsuranceAmount = "", strInsuranceType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insurance_type);
        if (CommonStrings.customVehDetails.getInsurance()) {
            strInsurance = "Yes";
            if (!String.valueOf(CommonStrings.customVehDetails.getInsuranceAmount()).equals("")) {
                strInsuranceAmount = String.valueOf(CommonStrings.customVehDetails.getInsuranceAmount());
            } else {
                strInsurance = "No";
            }
        } else {
            strInsurance = "No";
        }

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
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        tvGivenInsValidityVal.setText(strInsurance + " ( " + getString(R.string.rupees_symbol) + " " + strInsuranceAmount + " ) ");
        if (strInsuranceType.equalsIgnoreCase(radioBtnComprehensive.getText().toString())) {
            radioBtnComprehensive.setChecked(true);
        } else if (strInsuranceType.equalsIgnoreCase(radioBtnThirdParty.getText().toString())) {
            radioBtnThirdParty.setChecked(true);
        }
        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);
        tvGivenInsValidityEdit.setOnClickListener(this);
        radioBtnComprehensive.setOnClickListener(this);
        radioBtnThirdParty.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenInsValidityEdit) {
            finish();
        } else if (v.getId() == R.id.radioBtnComprehensive) {
            CommonStrings.customVehDetails.setInsuranceType(radioBtnComprehensive.getText().toString());
            Intent intent = new Intent(InsuranceTypeActivity.this, VehInsuranceValidityActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.radioBtnThirdParty) {
            CommonStrings.customVehDetails.setInsuranceType(radioBtnThirdParty.getText().toString());
            Intent intent = new Intent(InsuranceTypeActivity.this, VehInsuranceValidityActivity.class);
            startActivity(intent);
        }
    }
    @Override
    public void onBackPressed() {
    }
}