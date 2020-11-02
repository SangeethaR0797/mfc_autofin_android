package com.mfc.autofin.framework.Activity.BasicDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.R;

import utility.CommonStrings;
import utility.CustomFonts;

public class BasicDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenInsType, tvGivenInsTypeVal, tvGivenInsTypeEdit, tvNameLbl, tvEmailLbl, tvPhoneNumLbl;
    EditText etName, etEmailId, etPhoneNumber;
    Button btnNext;
    String strInsuranceType = "";
    String strName = "", strEmail = "", strPhoneNum = "";


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
        tvNameLbl = findViewById(R.id.tvNameLbl);
        tvEmailLbl = findViewById(R.id.tvEmailLbl);
        tvPhoneNumLbl = findViewById(R.id.tvPhoneNumLbl);
        etName = findViewById(R.id.etName);
        etEmailId = findViewById(R.id.etEmailId);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnNext = findViewById(R.id.btnNext);
        tvGivenInsTypeVal.setText(strInsuranceType);
        tvNameLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvEmailLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvPhoneNumLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        etName.setTypeface(CustomFonts.getRobotoMedium(this));
        etEmailId.setTypeface(CustomFonts.getRobotoMedium(this));
        etPhoneNumber.setTypeface(CustomFonts.getRobotoMedium(this));
        btnNext.setTypeface(CustomFonts.getRobotoMedium(this));
        tvGivenInsTypeEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenInsValidityEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (validate()) {
                CommonStrings.customBasicDetails.setFullName(strName);
                CommonStrings.customBasicDetails.setEmail(strEmail);
                CommonStrings.customBasicDetails.setCustomerMobile(strPhoneNum);
                startActivity(new Intent(this, ResidentialCityActivity.class));
            }
        }
    }

    private boolean validate() {
        boolean validDetails = false;
        strName = etName.getText().toString();
        strEmail = etEmailId.getText().toString().trim();
        strPhoneNum = etPhoneNumber.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!strName.isEmpty() && strName.matches("^[a-z A-Z ]*$")) {
            if (!strEmail.isEmpty() && strEmail.matches(emailPattern)) {
                if (!strPhoneNum.isEmpty() && strPhoneNum.matches("^[6-9]\\d{9}$")) {
                    validDetails = true;
                } else {
                    validDetails = false;
                    Toast.makeText(this, getString(R.string.enter_valid_phone_number), Toast.LENGTH_LONG).show();
                }

            } else {
                validDetails = false;
                Toast.makeText(this, getString(R.string.enter_valid_emailid), Toast.LENGTH_LONG).show();
            }
        } else {
            validDetails = false;
            Toast.makeText(this, getString(R.string.enter_valid_name), Toast.LENGTH_LONG).show();
        }

        return validDetails;

    }
}