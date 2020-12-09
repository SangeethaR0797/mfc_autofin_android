package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.review_activites.ReviewActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class SalaryModeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvSalaryModeLbl;
    private RadioGroup rgSalMode;
    private RadioButton rbCashSalary, rbChequeSal, rbTransferAndDeposit;
    private String strYearOfExperience = "", strPreviousLbl = "", strPreviousVal = "",salMode="";
    private LinearLayout llBankSelectionInSalMode;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_mode);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.cusEmpDetails.getSalaryMode()!=null && !CommonStrings.cusEmpDetails.getSalaryMode().isEmpty())
            {
                salMode=CommonStrings.cusEmpDetails.getSalaryMode();
            }
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvSalaryModeLbl=findViewById(R.id.tvSalaryModeLbl);
        rgSalMode = findViewById(R.id.rgSalMode);
        rbCashSalary = findViewById(R.id.rbCashSalary);
        rbChequeSal = findViewById(R.id.rbChequeSal);
        rbTransferAndDeposit = findViewById(R.id.rbTransferAndDeposit);
        llBankSelectionInSalMode = findViewById(R.id.llBankSelectionInSalMode);
        tvGivenLbl.setText(strPreviousLbl);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenPreviousVal.setText(strPreviousVal);
        if(!salMode.isEmpty())
        {
            if(salMode.equalsIgnoreCase(rbCashSalary.getText().toString()))
            {
                rbCashSalary.setChecked(true);
                rbChequeSal.setChecked(false);
                rbTransferAndDeposit.setChecked(false);
            }
            else if(salMode.equalsIgnoreCase(rbChequeSal.getText().toString()))
            {
                rbCashSalary.setChecked(false);
                rbChequeSal.setChecked(true);
                rbTransferAndDeposit.setChecked(false);
            }
            else if(salMode.equalsIgnoreCase(rbTransferAndDeposit.getText().toString()))
            {
                rbCashSalary.setChecked(false);
                rbChequeSal.setChecked(false);
                rbTransferAndDeposit.setChecked(true);
            }
        }
        tvGivenValEdit.setOnClickListener(this);
        rbCashSalary.setOnClickListener(this);
        rbChequeSal.setOnClickListener(this);
        rbTransferAndDeposit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.rbCashSalary) {
            try {
                if (rbCashSalary.isChecked()) {

                    CommonStrings.cusEmpDetails.setSalaryMode(rbCashSalary.getText().toString());
                    Intent intent = new Intent(this, PanCardNumberActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvSalaryModeLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, rbCashSalary.getText().toString());
                    startActivity(intent);                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if (v.getId() == R.id.rbChequeSal) {
            try {
                if (rbChequeSal.isChecked()) {

                    CommonStrings.cusEmpDetails.setSalaryMode(rbChequeSal.getText().toString());
                    Intent intent = new Intent(this, PanCardNumberActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvSalaryModeLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, rbChequeSal.getText().toString());
                    startActivity(intent);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if (v.getId() == R.id.rbTransferAndDeposit) {
            try {
                if (rbTransferAndDeposit.isChecked()) {
                    CommonStrings.cusEmpDetails.setSalaryMode(rbTransferAndDeposit.getText().toString());
                    Intent intent = new Intent(this, PanCardNumberActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvSalaryModeLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, rbTransferAndDeposit.getText().toString());
                    startActivity(intent);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }


        }
    }
}