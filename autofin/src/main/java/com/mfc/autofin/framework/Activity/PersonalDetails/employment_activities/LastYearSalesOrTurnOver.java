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
import com.mfc.autofin.framework.Activity.PersonalDetails.SalaryModeActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class LastYearSalesOrTurnOver extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl,tvGivenPreviousVal, tvGivenValEdit, tvLastYearSales,tvLastYearSalesVal,tvErrorMessage;
    private EditText  etLastYearSalesVal;
    private View belowETYearsOE;
    private Button btnNext;
    private String strPreviousLbl="",strPreviousVal="", strLastYearSales = "";
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_year_sales);
        try
        {
            intent=getIntent();
            strPreviousLbl=intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal=intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        }catch (Exception exception){exception.printStackTrace();}

        initView();
    }

    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvLastYearSales=findViewById(R.id.tvLastYearSales);
        tvLastYearSalesVal=findViewById(R.id.tvLastYearSalesVal);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        etLastYearSalesVal=findViewById(R.id.etLastYearSalesVal);
        belowETYearsOE=findViewById(R.id.belowETYearsOE);
        btnNext=findViewById(R.id.btnNext);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        if(CommonStrings.cusEmpDetails.getEmploymentType().equals(getResources().getString(R.string.lbl_business_owner)))
        {
            tvLastYearSales.setText(getResources().getString(R.string.lbl_last_year_sales));
        }
        else
        {
            tvLastYearSales.setText(getResources().getString(R.string.lbl_last_year_turn_over));
        }
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            try{
                if (!etLastYearSalesVal.getText().toString().isEmpty()) {
                    strLastYearSales = etLastYearSalesVal.getText().toString();
                    CommonStrings.cusEmpDetails.setLastYearTurnOver(strLastYearSales);
                    Intent intent=new Intent(this, IncomeAfterTaxActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL,tvLastYearSales.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE,strLastYearSales);
                    startActivity(intent);
                } else {
                    belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.error_red));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(getResources().getString(R.string.please_enter_year_of_experience));
                }
            }catch(Exception exception){exception.printStackTrace();}

        } else if (v.getId() == R.id.etLastYearSalesVal) {
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
