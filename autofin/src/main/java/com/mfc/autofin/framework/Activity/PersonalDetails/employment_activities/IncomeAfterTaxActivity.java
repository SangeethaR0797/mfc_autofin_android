package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.CurrentOrganizationActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.SalaryModeActivity;
import com.mfc.autofin.framework.R;

import model.personal_details_models.EmploymentTypeData;
import utility.CommonMethods;
import utility.CommonStrings;

public class IncomeAfterTaxActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_personal_details_backBtn;
    private TextView tvIncomeAfterTaxLbl, tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvErrorMessage;
    private EditText etIncomeAfterTax;
    private View belowETYearsOE;
    private Button btnNext;
    private String strPreviousLbl = "", strPreviousVal = "", strJoiningDate = "", strIncomeAfterTax = "";
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_after_tax);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.cusEmpDetails.getIncomeAfterTax()!=0)
            {
                strIncomeAfterTax=CommonMethods.removeDecimal(CommonStrings.cusEmpDetails.getIncomeAfterTax());
            }
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvIncomeAfterTaxLbl = findViewById(R.id.tvIncomeAfterTaxLbl);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etIncomeAfterTax = findViewById(R.id.etIncomeAfterTax);
        belowETYearsOE = findViewById(R.id.belowETYearsOE);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        if(!strIncomeAfterTax.isEmpty())
        {
            etIncomeAfterTax.setText(strIncomeAfterTax);
        }
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etIncomeAfterTax.getText().toString().isEmpty()) {
                strIncomeAfterTax = etIncomeAfterTax.getText().toString();
                    CommonStrings.cusEmpDetails.setIncomeAfterTax(Double.parseDouble(strIncomeAfterTax));
                    Intent intent = new Intent(this, LastYearDepreciationActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvIncomeAfterTaxLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, strIncomeAfterTax);
                    startActivity(intent);

            } else {
                belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText("Please enter your income after tax");
            }

        } else if (v.getId() == R.id.etIncomeAfterTax) {
            belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));
            if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onBackPressed() {

    }
}
