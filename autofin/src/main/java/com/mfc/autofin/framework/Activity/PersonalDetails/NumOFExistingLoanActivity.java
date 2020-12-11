package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class NumOFExistingLoanActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvNumOfExistingLoanLbl;
    private RadioButton rbLoan01, rbLoan02, rbLoan03, rbLoan04, rbLoan05, rbLoan06, rbNoPendingLoan;
    private Intent intent;
    private String strPreviousLbl = "", strPreviousVal = "", strExistingLoan = "";
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_o_f_existing_loan);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customLoanDetails.getNoOfExistingLoans() != null && !CommonStrings.customLoanDetails.getNoOfExistingLoans().isEmpty()) {
                strExistingLoan = CommonMethods.removeDecimal(Double.parseDouble(CommonStrings.customLoanDetails.getNoOfExistingLoans()));
            }
        }

        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvNumOfExistingLoanLbl = findViewById(R.id.tvNumOfExistingLoanLbl);
        btnNext = findViewById(R.id.btnNext);
        rbLoan01 = findViewById(R.id.rbLoan01);
        rbLoan02 = findViewById(R.id.rbLoan02);
        rbLoan03 = findViewById(R.id.rbLoan03);
        rbLoan04 = findViewById(R.id.rbLoan04);
        rbLoan05 = findViewById(R.id.rbLoan05);
        rbLoan06 = findViewById(R.id.rbLoan06);
        rbNoPendingLoan = findViewById(R.id.rbNoPendingLoan);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
        if (strExistingLoan.equalsIgnoreCase("0")) {
            rbNoPendingLoan.setChecked(true);
            rbLoan01.setChecked(false);
            rbLoan02.setChecked(false);
            rbLoan03.setChecked(false);
            rbLoan04.setChecked(false);
            rbLoan05.setChecked(false);
            rbLoan06.setChecked(false);
        } else if (strExistingLoan.equalsIgnoreCase("1") || strExistingLoan.equalsIgnoreCase("01")) {

            rbNoPendingLoan.setChecked(false);
            rbLoan01.setChecked(true);
            rbLoan02.setChecked(false);
            rbLoan03.setChecked(false);
            rbLoan04.setChecked(false);
            rbLoan05.setChecked(false);
            rbLoan06.setChecked(false);
        } else if (strExistingLoan.equalsIgnoreCase("2") || strExistingLoan.equalsIgnoreCase("02")) {
            rbLoan02.setChecked(true);
            rbNoPendingLoan.setChecked(false);
            rbLoan01.setChecked(false);
            rbLoan03.setChecked(false);
            rbLoan04.setChecked(false);
            rbLoan05.setChecked(false);
            rbLoan06.setChecked(false);
        } else if (strExistingLoan.equalsIgnoreCase("3") || strExistingLoan.equalsIgnoreCase("03")) {
            rbLoan03.setChecked(true);
            rbLoan02.setChecked(false);
            rbNoPendingLoan.setChecked(false);
            rbLoan01.setChecked(false);
            rbLoan04.setChecked(false);
            rbLoan05.setChecked(false);
            rbLoan06.setChecked(false);
        } else if (strExistingLoan.equalsIgnoreCase("4") || strExistingLoan.equalsIgnoreCase("04")) {
            rbLoan04.setChecked(true);
            rbLoan02.setChecked(false);
            rbNoPendingLoan.setChecked(false);
            rbLoan01.setChecked(false);
            rbLoan03.setChecked(false);
            rbLoan05.setChecked(false);
            rbLoan06.setChecked(false);
        } else if (strExistingLoan.equalsIgnoreCase("5") || strExistingLoan.equalsIgnoreCase("05")) {
            rbLoan02.setChecked(false);
            rbNoPendingLoan.setChecked(false);
            rbLoan01.setChecked(false);
            rbLoan03.setChecked(false);
            rbLoan04.setChecked(false);
            rbLoan05.setChecked(true);
            rbLoan06.setChecked(false);
        } else if (strExistingLoan.equalsIgnoreCase("6") || strExistingLoan.equalsIgnoreCase("06")) {

            rbLoan02.setChecked(false);
            rbNoPendingLoan.setChecked(false);
            rbLoan01.setChecked(false);
            rbLoan03.setChecked(false);
            rbLoan04.setChecked(false);
            rbLoan05.setChecked(false);
            rbLoan06.setChecked(true);
        }
        rbLoan02.setOnCheckedChangeListener(this);
        rbLoan01.setOnCheckedChangeListener(this);
        rbLoan03.setOnCheckedChangeListener(this);
        rbLoan04.setOnCheckedChangeListener(this);
        rbLoan05.setOnCheckedChangeListener(this);
        rbLoan06.setOnCheckedChangeListener(this);
        rbNoPendingLoan.setOnCheckedChangeListener(this);
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!strExistingLoan.isEmpty()) {
                moveToNextPage();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if (isChecked) {
            if (button.getId() == R.id.rbLoan01) {
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                strExistingLoan = rbLoan01.getText().toString();
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            } else if (button.getId() == R.id.rbLoan02) {
                rbLoan01.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                strExistingLoan = rbLoan02.getText().toString();
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            } else if (button.getId() == R.id.rbLoan03) {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                strExistingLoan = rbLoan03.getText().toString();
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            } else if (button.getId() == R.id.rbLoan04) {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                strExistingLoan = rbLoan04.getText().toString();
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            } else if (button.getId() == R.id.rbLoan05) {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                strExistingLoan = rbLoan05.getText().toString();
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            } else if (button.getId() == R.id.rbLoan06) {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                strExistingLoan = rbLoan06.getText().toString();
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            } else if (button.getId() == R.id.rbNoPendingLoan) {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                strExistingLoan = "0";
                if (!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            }
        }
    }

    private void moveToNextPage() {
        CommonStrings.customLoanDetails.setNoOfExistingLoans(strExistingLoan);
        Intent intent = new Intent(this, LoanRequiredActivity.class);
        intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvNumOfExistingLoanLbl.getText().toString());
        intent.putExtra(CommonStrings.PREVIOUS_VALUE, strExistingLoan);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}