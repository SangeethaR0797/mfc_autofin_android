package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.PersonalDetails.SalaryModeActivity;
import com.mfc.autofin.framework.Activity.review_activites.ReviewActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class LikelyPurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLikePurchaseTitle, tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit;
    private RadioGroup rgLikelyPurchase;
    private RadioButton rbWithinAWeek, rbWithinAMonth, rbAfterAMonth;
    private String strPreviousLbl = "", strPreviousVal = "";
    private Intent intent;
    private ImageView iv_personal_details_backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likely_purchase);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        initView();
    }

    private void initView() {
        tvLikePurchaseTitle=findViewById(R.id.tvLikePurchaseTitle);
        rgLikelyPurchase = findViewById(R.id.rgLikelyPurchase);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        rbWithinAWeek = findViewById(R.id.rbWithinAWeek);
        rbWithinAMonth = findViewById(R.id.rbWithinAMonth);
        rbAfterAMonth = findViewById(R.id.rbAfterAMonth);
        rbWithinAWeek.setOnClickListener(this);
        rbWithinAMonth.setOnClickListener(this);
        rbAfterAMonth.setOnClickListener(this);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            startActivity(new Intent(this, VehicleVariantActivity.class));
        } else if (v.getId() == R.id.rbWithinAWeek) {
            if (rbWithinAWeek.isChecked()) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.LIKELY_PURCHASE_DATE, rbWithinAWeek.getText().toString());
                    Intent intent = new Intent(LikelyPurchaseActivity.this, ReviewActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvLikePurchaseTitle.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, rbWithinAWeek.getText().toString());
                    startActivity(intent);

            }
        } else if (v.getId() == R.id.rbWithinAMonth) {
            if (rbWithinAMonth.isChecked()) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.LIKELY_PURCHASE_DATE, rbWithinAMonth.getText().toString());
                Intent intent = new Intent(LikelyPurchaseActivity.this, ReviewActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvLikePurchaseTitle.getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, rbWithinAMonth.getText().toString());
                startActivity(intent);

            }
        } else if (v.getId() == R.id.rbAfterAMonth) {
            if (rbAfterAMonth.isChecked()) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.LIKELY_PURCHASE_DATE, rbAfterAMonth.getText().toString());
                Intent intent = new Intent(LikelyPurchaseActivity.this, ReviewActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvLikePurchaseTitle.getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, rbAfterAMonth.getText().toString());
                startActivity(intent);
            }
        }
    }
    @Override
    public void onBackPressed() {

    }
}