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
    private String strPurchaseAmount = "",strInsurance="",insuranceAmount="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insured_amount);
        if (!String.valueOf(CommonStrings.customVehDetails.getVehicleSellingPrice()).equals("")) {
            strPurchaseAmount = String.valueOf(CommonStrings.customVehDetails.getVehicleSellingPrice());
        }

        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customVehDetails.getInsurance()) {
                strInsurance="Yes";
                if (CommonStrings.customVehDetails.getInsuranceAmount() != 0) {
                    double insAmount = CommonStrings.customVehDetails.getInsuranceAmount();
                    if(!CommonMethods.getFormattedString(insAmount).isEmpty())
                    insuranceAmount =CommonMethods.getFormattedString(insAmount);
                    else
                        insuranceAmount="0";
                }
            }
            else
            {
                strInsurance="No";
            }
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
        iv_vehDetails_back.setVisibility(View.INVISIBLE);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_vehicle_purchase_amount));
        tvGivenPreviousVal.setText(strPurchaseAmount);

        if(!insuranceAmount.isEmpty())
        {
            etInsuredAmount.setText(insuranceAmount);
        }
        tvGivenValEdit.setOnClickListener(this);
        tvInsuredAmount.setTypeface(CustomFonts.getRobotoRegularTF(this));
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            try {
                if (!etInsuredAmount.getText().toString().equals("")) {
                    String insuranceAmount= etInsuredAmount.getText().toString();
                    CommonStrings.customVehDetails.setInsuranceAmount(Double.parseDouble(insuranceAmount));
                    startActivity(new Intent(this, BasicDetailsActivity.class));
                } else {
                    CommonMethods.showToast(this, "Please enter Insured Amount");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }
    @Override
    public void onBackPressed() {
    }
}
