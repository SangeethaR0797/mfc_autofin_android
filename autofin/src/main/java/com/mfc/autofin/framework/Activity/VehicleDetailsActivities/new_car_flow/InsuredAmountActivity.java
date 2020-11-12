package com.mfc.autofin.framework.Activity.VehicleDetailsActivities.new_car_flow;

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
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.R;

import model.basic_details.BasicDetails;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;

public class InsuredAmountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvInsuredAmount;
    private EditText etInsuredAmount;
    private ImageView iv_vehDetails_back;
    private Button btnNext;
    private String strPurchaseAmount = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insured_amount);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.VEH_PURCHASE_AMOUNT).equals("")) {
            strPurchaseAmount = CommonMethods.getStringValueFromKey(this, CommonStrings.VEH_PURCHASE_AMOUNT);
        }
        initView();
    }

    private void initView() {
        iv_vehDetails_back = findViewById(R.id.iv_vehDetails_back);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvInsuredAmount = findViewById(R.id.tvInsuredAmount);
        etInsuredAmount = findViewById(R.id.etInsuredAmount);
        btnNext = findViewById(R.id.btnNext);
        iv_vehDetails_back.setOnClickListener(this);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_insured_amount));
        tvGivenPreviousVal.setText(strPurchaseAmount);
        tvGivenValEdit.setOnClickListener(this);
        tvInsuredAmount.setTypeface(CustomFonts.getRobotoRegularTF(this));
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            try {
                if (!etInsuredAmount.getText().toString().equals("")) {
                    CommonMethods.setValueAgainstKey(this, CommonStrings.VEH_INSURED_AMOUNT, etInsuredAmount.getText().toString());
                    startActivity(new Intent(this, BasicDetailsActivity.class));
                } else {
                    CommonMethods.showToast(this, "Please enter Insured Amount");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
}
