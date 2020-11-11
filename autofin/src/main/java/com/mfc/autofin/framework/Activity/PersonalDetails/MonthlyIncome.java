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

    private TextView tvGivenLbl,tvGivenPreviousVal,tvGivenValEdit,tvMonthlyIncome,tvErrorMessage;
    ImageView iv_personal_details_backBtn;
    private View belowETView;
    private String strUserDOB="",strMonthlyIncome="";
    private EditText etMonthlyIncomeAmount;
    private Button btnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_income);
        if(CommonMethods.getStringValueFromKey(this,CommonStrings.USER_DOB).length()>0)
        {
           strUserDOB= CommonMethods.getStringValueFromKey(this,CommonStrings.USER_DOB);
        }
        else
        {
            strUserDOB="";
        }

        initView();
    }

    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvMonthlyIncome=findViewById(R.id.tvMonthlyIncome);
        etMonthlyIncomeAmount=findViewById(R.id.etMonthlyIncomeAmount);
        btnNext=findViewById(R.id.btnNext);
        belowETView=findViewById(R.id.belowETView);
        etMonthlyIncomeAmount.setOnClickListener(this);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_dob));
        tvGivenPreviousVal.setText(strUserDOB);
        tvGivenValEdit.setOnClickListener(this);
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
            if(!etMonthlyIncomeAmount.getText().toString().equals(""))
            {
                strMonthlyIncome=etMonthlyIncomeAmount.getText().toString();
                CommonMethods.setValueAgainstKey(MonthlyIncome.this,CommonStrings.MONTHLY_INCOME,strMonthlyIncome);
                startActivity(new Intent(MonthlyIncome.this,EMIPayPerMonthActivity.class));
            }
           else
            {
                belowETView.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.monthly_income_error_message));
            }
        }
        else if(v.getId()==R.id.etMonthlyIncomeAmount)
        {
            belowETView.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));

            if(tvErrorMessage.getVisibility()==View.VISIBLE)
            {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }

    }
}