package com.mfc.autofin.framework.Activity.bank_offer_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.PersonalDetails.InterestedBankOfferActivity;
import com.mfc.autofin.framework.R;

public class InterestedBankOfferDetailsActivity extends AppCompatActivity implements View.OnClickListener {


    ImageView iv_common_bar_backBtn;
    TextView tvCommonAppBarTitle,tvLoanAmountLbl,tvBankOfferedLoanAmount,tvEMILbl,tvBankOfferedEMIAmount,tvTenureLbl,tvTenurePeriod,tvRateOfInterestLbl,tvRateOfInterest,tvChangeBankLbl;
    Button btnApplyNow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_bank_offer_details);
        initView();
    }

    private void initView() {
        iv_common_bar_backBtn=findViewById(R.id.iv_common_bar_backBtn);
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        tvLoanAmountLbl=findViewById(R.id.tvLoanAmountLbl);
        tvBankOfferedLoanAmount=findViewById(R.id.tvBankOfferedLoanAmount);
        tvEMILbl=findViewById(R.id.tvEMILbl);
        tvBankOfferedEMIAmount=findViewById(R.id.tvBankOfferedEMIAmount);
        tvTenureLbl=findViewById(R.id.tvTenureLbl);
        tvTenurePeriod=findViewById(R.id.tvTenurePeriod);
        tvRateOfInterestLbl=findViewById(R.id.tvRateOfInterestLbl);
        tvRateOfInterest=findViewById(R.id.tvRateOfInterest);
        tvChangeBankLbl=findViewById(R.id.tvChangeBankLbl);
        btnApplyNow=findViewById(R.id.btnApplyNow);
        tvCommonAppBarTitle.setText("INTERESTED BANK OFFER");
        iv_common_bar_backBtn.setOnClickListener(this);
        btnApplyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_common_bar_backBtn)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            startActivity(new Intent(this, InterestedBankOfferActivity.class));
        }
    }
}
