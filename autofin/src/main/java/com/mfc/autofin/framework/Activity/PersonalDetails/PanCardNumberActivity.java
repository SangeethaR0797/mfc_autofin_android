package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.CommonMethods;
import utility.CommonStrings;

public class PanCardNumberActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl,tvGivenPreviousVal,tvGivenValEdit,tvPanCardLbl,tvEnterPanCardNo,tvErrorMessage;
    private EditText etPanCardNumber;
    private View belowETPANNo;
    private Button btnNext;
    private String strLoanAmount="",strPanNumber="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pan_card_number);
        if(!CommonMethods.getStringValueFromKey(this, CommonStrings.LOAN_REQUIRED).isEmpty())
        {
            strLoanAmount=CommonMethods.getStringValueFromKey(this, CommonStrings.LOAN_REQUIRED);
        }
        else
        {
            strLoanAmount="";
        }
        initView();
    }

    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvPanCardLbl=findViewById(R.id.tvPanCardLbl);
        tvEnterPanCardNo=findViewById(R.id.tvEnterPanCardNo);
        etPanCardNumber=findViewById(R.id.etPanCardNumber);
        belowETPANNo=findViewById(R.id.belowETPANNo);
        btnNext=findViewById(R.id.btnNext);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_loan_required));
        tvGivenPreviousVal.setText(strLoanAmount);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        etPanCardNumber.setOnClickListener(this);
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
            if(!etPanCardNumber.getText().toString().isEmpty())
            {
                strPanNumber=etPanCardNumber.getText().toString();
               if(isPanNumberValid(strPanNumber))
               {
                   CommonMethods.setValueAgainstKey(this,CommonStrings.PAN_CARD_NUMBER,strPanNumber);
                   startActivity(new Intent(this,BankNamesActivity.class));
               }
               else
               {
                   belowETPANNo.setBackgroundColor(getResources().getColor(R.color.error_red));
                   tvErrorMessage.setVisibility(View.VISIBLE);
                   tvErrorMessage.setText(getResources().getString(R.string.please_enter_valid_pan_number));
               }
            }
            else
            {
                belowETPANNo.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.please_enter_pan_number));
            }
        }
        else if(v.getId()==R.id.etPanCardNumber)
        {
            belowETPANNo.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));
            if(tvErrorMessage.getVisibility()==View.VISIBLE)
            {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }

    }

    private boolean isPanNumberValid(String panNum)
    {
        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");

        Matcher matcher = pattern.matcher(panNum);
        if (matcher.matches())
            return true;
        else
            return false;

    }

   }