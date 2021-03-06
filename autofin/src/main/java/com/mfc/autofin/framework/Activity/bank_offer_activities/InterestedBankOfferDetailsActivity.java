package com.mfc.autofin.framework.Activity.bank_offer_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.PersonalDetails.InterestedBankOfferActivity;
import com.mfc.autofin.framework.R;

import model.bank_models.InterestedBankOfferData;
import model.bank_models.InterestedBankOfferReq;
import model.bank_models.InterestedBankOfferRes;
import model.bank_models.InterestedBankOfferResData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;

import static retrofit_config.RetroBase.retrofitInterface;

public class InterestedBankOfferDetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {


    private static final String TAG = InterestedBankOfferDetailsActivity.class.getSimpleName();
    ImageView iv_common_bar_backBtn,ivBankIcon;
    TextView tvCommonAppBarTitle, tvLoanAmountLbl, tvBankOfferedLoanAmount, tvEMILbl, tvBankOfferedEMIAmount, tvTenureLbl, tvTenurePeriod, tvRateOfInterestLbl, tvRateOfInterest, tvChangeBankLbl;
    Button btnApplyNow;
    String strBankName="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_bank_offer_details);
        retrofitInterface.getFromWeb(getInterestedBankOfferReq(), Global.customer_bank_baseURL+CommonStrings.INTERESTED_BANK_OFFER_URL).enqueue(this);
        initView();
    }

    private InterestedBankOfferReq getInterestedBankOfferReq() {
        InterestedBankOfferReq interestedBankOfferReq = new InterestedBankOfferReq();
        interestedBankOfferReq.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        interestedBankOfferReq.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        InterestedBankOfferData interestedBankOfferData = new InterestedBankOfferData();
        interestedBankOfferData.setCaseId(CommonMethods.getStringValueFromKey(this,CommonStrings.CASE_ID));
        interestedBankOfferReq.setData(interestedBankOfferData);
        return interestedBankOfferReq;

    }

    private void initView() {
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        ivBankIcon=findViewById(R.id.ivBankIcon);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tvLoanAmountLbl = findViewById(R.id.tvLoanAmountLbl);
        tvBankOfferedLoanAmount = findViewById(R.id.tvBankOfferedLoanAmount);
        tvEMILbl = findViewById(R.id.tvEMILbl);
        tvBankOfferedEMIAmount = findViewById(R.id.tvBankOfferedEMIAmount);
        tvTenureLbl = findViewById(R.id.tvTenureLbl);
        tvTenurePeriod = findViewById(R.id.tvTenurePeriod);
        tvRateOfInterestLbl = findViewById(R.id.tvRateOfInterestLbl);
        tvRateOfInterest = findViewById(R.id.tvRateOfInterest);
        tvChangeBankLbl = findViewById(R.id.tvChangeBankLbl);
        btnApplyNow = findViewById(R.id.btnApplyNowLoan);
        tvCommonAppBarTitle.setText("INTERESTED BANK OFFER");
        iv_common_bar_backBtn.setOnClickListener(this);
        btnApplyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_common_bar_backBtn) {
            finish();
        } else if (v.getId() == R.id.btnApplyNowLoan) {
            startActivity(new Intent(this, InterestedBankOfferActivity.class));
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        InterestedBankOfferRes interestedBankOfferRes = new Gson().fromJson(strRes, InterestedBankOfferRes.class);
        if (interestedBankOfferRes != null && interestedBankOfferRes.getStatus()) {
            try {
           InterestedBankOfferResData resData=interestedBankOfferRes.getData();
           setBankImage(resData.getBankName());
           String strEMI="",strTenure="",strROI="",strLoanAmount="";
           strEMI=getResources().getString(R.string.rupees_symbol)+" "+CommonMethods.getFormattedAmount(Double.parseDouble(resData.getEmi()));
           strLoanAmount=getResources().getString(R.string.rupees_symbol)+" "+CommonMethods.getFormattedAmount(Double.parseDouble(resData.getLoanAmount()));
           if(Integer.parseInt(resData.getTenure())>1)
           {
               strTenure=resData.getTenure()+" "+"Months";
           }
           else
           {
               strTenure=resData.getTenure()+" "+"Month";
           }
           strROI=CommonMethods.getFormattedDouble(Double.parseDouble(resData.getRateOfIntrest()))+" %";
           tvBankOfferedEMIAmount.setText(strEMI);
           tvBankOfferedLoanAmount.setText(strLoanAmount);
           tvTenurePeriod.setText(strTenure);
           tvRateOfInterest.setText(strROI);
           strBankName=resData.getBankName();
            }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }

        }
    }

    private void setBankImage(String bankName)
    {
        if(bankName.contains("HDFC"))
        {
            ivBankIcon.setBackground(null);
            ivBankIcon.setBackground(getResources().getDrawable(R.drawable.ic_hdfc));
        }
        else  if(bankName.contains("ICICI"))
        {
            ivBankIcon.setBackground(null);
            ivBankIcon.setBackground(getResources().getDrawable(R.drawable.ic_icici));
        }
        else  if(bankName.contains("AXIS"))
        {
            ivBankIcon.setBackground(null);
            ivBankIcon.setBackground(getResources().getDrawable(R.drawable.ic_axis));
        }
        else  if(bankName.contains("SBI"))
        {
            ivBankIcon.setBackground(null);
            ivBankIcon.setBackground(getResources().getDrawable(R.drawable.ic_sbi));
        }
        else
        {
            ivBankIcon.setBackground(null);
            ivBankIcon.setBackground(getResources().getDrawable(R.drawable.ic_outline_image_search_24));
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
