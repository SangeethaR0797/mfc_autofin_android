package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.IncomeAfterTaxActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.LastYearSalesOrTurnOver;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class LastYearProfitActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl,tvGivenPreviousVal, tvGivenValEdit, tvLastYearProfit,tvLastYearProfitVal,tvErrorMessage;
    private EditText etLastYearProfitVal;
    private View belowETYearsOE;
    private Button btnNext;
    private String strPreviousLbl="",strPreviousVal="", strLastYearSales = "";
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_year_profit);
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
        tvLastYearProfit=findViewById(R.id.tvLastYearProfit);
        tvLastYearProfitVal=findViewById(R.id.tvLastYearProfitVal);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        etLastYearProfitVal=findViewById(R.id.etLastYearProfitVal);
        belowETYearsOE=findViewById(R.id.belowETYearsOE);
        btnNext=findViewById(R.id.btnNext);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
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
            try{
                if (!etLastYearProfitVal.getText().toString().isEmpty()) {
                    strLastYearSales = etLastYearProfitVal.getText().toString();
                    CommonStrings.cusEmpDetails.setLastYearProfit(Double.parseDouble(strLastYearSales));
                    Intent intent=new Intent(this, LastYearSalesOrTurnOver.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL,tvLastYearProfit.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE,strLastYearSales);
                    startActivity(intent);
                } else {
                    belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.error_red));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText("Please enter Last year Profit");
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onBackPressed() {

    }
}
