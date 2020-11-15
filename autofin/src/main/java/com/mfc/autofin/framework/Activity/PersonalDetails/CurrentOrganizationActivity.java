package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;

public class CurrentOrganizationActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvErrorMessage, tvOrgName;
    private EditText etOrganizationName;
    private Button btnNext;
    private View belowOrgName;
    private String strExistingLoan = "", strOrgName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_organization);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.NO_OF_EXISTING_LOAN).isEmpty()) {
            strExistingLoan = CommonMethods.getStringValueFromKey(this, CommonStrings.NO_OF_EXISTING_LOAN);
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvOrgName = findViewById(R.id.tvOrgName);
        etOrganizationName = findViewById(R.id.etOrganizationName);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_how_many_existing_loan));
        tvGivenPreviousVal.setText(strExistingLoan);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        belowOrgName = findViewById(R.id.belowOrgName);
        tvGivenValEdit.setOnClickListener(this);
        tvOrgName.setTypeface(CustomFonts.getRobotoRegularTF(this));
        iv_personal_details_backBtn.setOnClickListener(this);
        etOrganizationName.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etOrganizationName.getText().toString().equals("")) {
                strOrgName = etOrganizationName.getText().toString();
                CommonMethods.setValueAgainstKey(this, CommonStrings.CURRENT_ORG_NAME, strOrgName);
                startActivity(new Intent(this, JODOCurrentOrgActivity.class));
            } else {
                belowOrgName.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText("Please enter name of the organization you are working for!");
            }
        } else if (v.getId() == R.id.etOrganizationName) {
            belowOrgName.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));
            if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                tvErrorMessage.setVisibility(View.VISIBLE);
            }
        }
    }
}