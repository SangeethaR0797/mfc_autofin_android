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

public class EMIPayPerMonthActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvGivenLbl,tvGivenPreviousVal,tvGivenValEdit,tvMonthlyEMILbl,tvMonthlyEMI,tvErrorMessage;
    private EditText etMonthlyEMIAmount;
    private View belowETViewEMI;
    private ImageView iv_personal_details_backBtn;
    private Button btnNext;
    String strMonthlyIncome="",strMonthlyEMI="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_m_i_pay_per_month);
        if(!CommonMethods.getStringValueFromKey(this, CommonStrings.MONTHLY_INCOME).isEmpty())
        {
            strMonthlyIncome=CommonMethods.getStringValueFromKey(this, CommonStrings.MONTHLY_INCOME);
        }
        else
        {
            strMonthlyIncome="";
        }
        initView();
    }

    private void initView()
    {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvMonthlyEMILbl=findViewById(R.id.tvMonthlyEMILbl);
        tvMonthlyEMI=findViewById(R.id.tvMonthlyEMI);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        etMonthlyEMIAmount=findViewById(R.id.etMonthlyEMIAmount);
        belowETViewEMI=findViewById(R.id.belowETViewEMI);
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        btnNext=findViewById(R.id.btnNext);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_monthly_income));
        tvGivenPreviousVal.setText(strMonthlyIncome);
        tvGivenValEdit.setOnClickListener(this);
        etMonthlyEMIAmount.setOnClickListener(this);
        iv_personal_details_backBtn.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!etMonthlyEMIAmount.getText().toString().isEmpty())
            {
                strMonthlyEMI=etMonthlyEMIAmount.getText().toString();
                CommonMethods.setValueAgainstKey(EMIPayPerMonthActivity.this,CommonStrings.MONTHLY_EMI,strMonthlyEMI);
                startActivity(new Intent(EMIPayPerMonthActivity.this,LoanRequiredActivity.class));
            }
            else
            {
                belowETViewEMI.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.monthly_emi_error_message));
            }
        }
        else if(v.getId()==R.id.etMonthlyIncomeAmount)
        {
            belowETViewEMI.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));

            if(tvErrorMessage.getVisibility()==View.VISIBLE)
            {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }


    }
}