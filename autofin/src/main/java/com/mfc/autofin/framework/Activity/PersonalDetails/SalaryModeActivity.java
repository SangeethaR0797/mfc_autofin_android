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
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit;
    private RadioGroup rgSalMode;
    private RadioButton rbCashSalary, rbChequeSal, rbTransferAndDeposit;
    private String strYearOfExperience = "";
    private LinearLayout llBankSelectionInSalMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_mode);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.YEARS_OF_EXPERIENCE).equals("")) {
            strYearOfExperience = CommonMethods.getStringValueFromKey(this, CommonStrings.YEARS_OF_EXPERIENCE);
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        rgSalMode = findViewById(R.id.rgSalMode);
        rbCashSalary = findViewById(R.id.rbCashSalary);
        rbChequeSal = findViewById(R.id.rbChequeSal);
        rbTransferAndDeposit = findViewById(R.id.rbTransferAndDeposit);
        llBankSelectionInSalMode = findViewById(R.id.llBankSelectionInSalMode);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_year_of_experience));
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenPreviousVal.setText(strYearOfExperience);
        tvGivenValEdit.setOnClickListener(this);
        rbCashSalary.setOnClickListener(this);
        rbChequeSal.setOnClickListener(this);
        rbTransferAndDeposit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.rbCashSalary) {
                try{
                    if (rbCashSalary.isChecked()) {

                        CommonMethods.setValueAgainstKey(this, CommonStrings.SALARY_MODE, rbCashSalary.getText().toString());
                    startActivity(new Intent(this, ReviewActivity.class));
                    }

                }catch(Exception exception){exception.printStackTrace();}

        } else if (v.getId() == R.id.rbChequeSal) {
            try{  if (rbChequeSal.isChecked()) {

                CommonMethods.setValueAgainstKey(this, CommonStrings.SALARY_MODE, rbChequeSal.getText().toString());
                startActivity(new Intent(this, ReviewActivity.class));
            }
                }
            catch(Exception exception){exception.printStackTrace();}

        } else if (v.getId() == R.id.rbTransferAndDeposit) {
            try{
                if (rbTransferAndDeposit.isChecked()) {
                //llBankSelectionInSalMode.setVisibility(View.VISIBLE);

                    CommonMethods.setValueAgainstKey(this, CommonStrings.SALARY_MODE, rbTransferAndDeposit.getText().toString());
                    startActivity(new Intent(this, ReviewActivity.class));
                }}
                catch(Exception exception){exception.printStackTrace();}


        }
    }
}