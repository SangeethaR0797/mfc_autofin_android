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

public class MonthlyIncome extends AppCompatActivity implements View.OnClickListener {

    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvMonthlyIncomeLbl, tvMonthlyIncome, tvErrorMessage;
    ImageView iv_personal_details_backBtn;
    private View belowETView;
    private String strEducation = "", strMonthlyIncome = "";
    private EditText etMonthlyIncomeAmount;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_income);
        if (CommonStrings.customPersonalDetails.getEducation().length() > 0) {
            strEducation = CommonStrings.customPersonalDetails.getEducation();
        } else {
            strEducation = "";
        }

        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvMonthlyIncomeLbl=findViewById(R.id.tvMonthlyIncomeLbl);
        tvMonthlyIncome = findViewById(R.id.tvMonthlyIncome);
        etMonthlyIncomeAmount = findViewById(R.id.etMonthlyIncomeAmount);
        btnNext = findViewById(R.id.btnNext);
        belowETView = findViewById(R.id.belowETView);
        etMonthlyIncomeAmount.setOnClickListener(this);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_education));
        tvGivenPreviousVal.setText(strEducation);
        tvGivenValEdit.setOnClickListener(this);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etMonthlyIncomeAmount.getText().toString().equals("")) {
                strMonthlyIncome = etMonthlyIncomeAmount.getText().toString();
                CommonStrings.customPersonalDetails.setSalaryPerMonth(Double.parseDouble(strMonthlyIncome));
                Intent intent = new Intent(this, NumOFExistingLoanActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvMonthlyIncomeLbl .getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, strMonthlyIncome);
                startActivity(intent);
            } else {
                belowETView.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.monthly_income_error_message));
            }
        } else if (v.getId() == R.id.etMonthlyIncomeAmount) {
            belowETView.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));

            if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }

    }
    @Override
    public void onBackPressed() {

    }
}