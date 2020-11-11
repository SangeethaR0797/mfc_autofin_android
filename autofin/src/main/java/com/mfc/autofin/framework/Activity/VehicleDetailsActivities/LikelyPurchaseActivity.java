package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class LikelyPurchaseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLikePurchaseTitle, tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit;
    private RadioGroup rgLikelyPurchase;
    private RadioButton rbWithinAWeek, rbWithinAMonth, rbAfterAMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likely_purchase);
        initView();
    }

    private void initView() {
        rgLikelyPurchase = findViewById(R.id.rgLikelyPurchase);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        rbWithinAWeek = findViewById(R.id.rbWithinAWeek);
        rbWithinAMonth = findViewById(R.id.rbWithinAMonth);
        rbAfterAMonth = findViewById(R.id.rbAfterAMonth);
        tvGivenLbl.setText("VARIANT");
        tvGivenPreviousVal.setText(CommonStrings.customVehDetails.getVariant());
        tvGivenValEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            startActivity(new Intent(this, VehicleVariantActivity.class));
        } else if (v.getId() == R.id.rbWithinAWeek) {
            if (rbWithinAWeek.isChecked()) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.LIKELY_PURCHASE_DATE, rbWithinAWeek.getText().toString());
                /*Intent intent = new Intent(LikelyPurchaseActivity.this, VehRegNumActivity.class);
                startActivity(intent);*/
            }
        } else if (v.getId() == R.id.rbWithinAWeek) {
            if (rbWithinAWeek.isChecked()) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.LIKELY_PURCHASE_DATE, rbWithinAMonth.getText().toString());
                /*Intent intent = new Intent(LikelyPurchaseActivity.this, VehRegNumActivity.class);
                startActivity(intent);*/
            }
        }
        else if (v.getId() == R.id.rbAfterAMonth) {
            if (rbAfterAMonth.isChecked()) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.LIKELY_PURCHASE_DATE, rbAfterAMonth.getText().toString());
                /*Intent intent = new Intent(LikelyPurchaseActivity.this, VehRegNumActivity.class);
                startActivity(intent);*/
            }
        }
    }
}