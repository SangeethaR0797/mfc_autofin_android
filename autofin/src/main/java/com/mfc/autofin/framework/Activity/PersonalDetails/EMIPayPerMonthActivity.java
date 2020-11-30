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
    private Intent intent;
    private String strPreviousLbl="",strPreviousVal="",strMonthlyEMI="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_m_i_pay_per_month);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
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
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
        etMonthlyEMIAmount.setOnClickListener(this);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!etMonthlyEMIAmount.getText().toString().isEmpty())
            {
                strMonthlyEMI=etMonthlyEMIAmount.getText().toString();
                CommonStrings.customPersonalDetails.setTotalEMIPaid(Double.parseDouble(strMonthlyEMI));
                Intent intent = new Intent(this, EmploymentTypeActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvMonthlyEMILbl.getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, strMonthlyEMI);
                startActivity(intent);
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

    @Override
    public void onBackPressed() {
    }
}