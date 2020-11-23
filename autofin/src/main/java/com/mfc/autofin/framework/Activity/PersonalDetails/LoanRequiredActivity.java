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

public class LoanRequiredActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvLoanRequiredLbl, tvLoanRequired, tvErrorMessage;
    private EditText etLoanRequiredAmount;
    private Button btnNext;
    private View belowETViewLoanAmount;
    private String strMonthlyEMI = "", strLoanAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_required);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.MONTHLY_EMI).isEmpty()) {
            strMonthlyEMI = CommonMethods.getStringValueFromKey(this, CommonStrings.MONTHLY_EMI);
        } else {
            strMonthlyEMI = "";
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvLoanRequiredLbl = findViewById(R.id.tvLoanRequiredLbl);
        tvLoanRequired = findViewById(R.id.tvLoanRequired);
        etLoanRequiredAmount = findViewById(R.id.etLoanRequiredAmount);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        belowETViewLoanAmount = findViewById(R.id.belowETViewLoanAmount);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_total_emi));
        tvGivenPreviousVal.setText(strMonthlyEMI);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        etLoanRequiredAmount.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        /*if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else*/
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etLoanRequiredAmount.getText().toString().isEmpty()) {
                strLoanAmount = etLoanRequiredAmount.getText().toString();
                CommonMethods.setValueAgainstKey(this, CommonStrings.LOAN_REQUIRED, strLoanAmount);
                startActivity(new Intent(this, EMIPayPerMonthActivity.class));
            } else {
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.loan_required_error_message));
                belowETViewLoanAmount.setBackgroundColor(getResources().getColor(R.color.error_red));
            }
        } else if (v.getId() == R.id.etLoanRequiredAmount) {
            belowETViewLoanAmount.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));

            if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }

    }
}