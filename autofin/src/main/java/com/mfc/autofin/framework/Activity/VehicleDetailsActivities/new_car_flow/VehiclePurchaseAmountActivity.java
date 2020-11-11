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
import com.mfc.autofin.framework.Activity.PersonalDetails.MonthlyIncome;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.CarHaveLoanCurrentlyActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;

public class VehiclePurchaseAmountActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isNewCar=false;
    private String strPreviousVal = "";
    private ImageView iv_vehDetails_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvVehPurchaseAmount;
    private EditText etVehPurchaseAmount;
    private Button btnNext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_purchase_amount);
        if(CommonStrings.customVehDetails.getVehCategory().equals(getResources().getString(R.string.new_car)))
        {
            isNewCar=true;
        }
        if(isNewCar)
        {
            if (!CommonMethods.getStringValueFromKey(this, CommonStrings.ROAD_PRICE).equals("")) {
                strPreviousVal = CommonMethods.getStringValueFromKey(this, CommonStrings.ROAD_PRICE);
            } else {
                strPreviousVal = "";
            }

        }
        else
        {
            if (!String.valueOf(CommonStrings.customVehDetails.getOwnership()).equals("")) {
                strPreviousVal = String.valueOf(CommonStrings.customVehDetails.getOwnership());
            } else {
                strPreviousVal = "";
            }
        }
               initView();
    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvVehPurchaseAmount = findViewById(R.id.tvVehPurchaseAmount);
        etVehPurchaseAmount = findViewById(R.id.etVehPurchaseAmount);
        btnNext = findViewById(R.id.btnNext);
        if(isNewCar)
        {
            tvGivenLbl.setText(getResources().getString(R.string.lbl_interested_vehicle_price));
        }
        else
        {
            tvGivenLbl.setText(getResources().getString(R.string.lbl_veh_ownership));
        }
        tvVehPurchaseAmount.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_vehDetails_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {

            try{

                if(isNewCar)
                {
                    if (!etVehPurchaseAmount.getText().toString().equals("")) {
                        CommonMethods.setValueAgainstKey(this, CommonStrings.VEH_PURCHASE_AMOUNT, etVehPurchaseAmount.getText().toString());
                        startActivity(new Intent(this, InsuredAmountActivity.class));
                    } else {
                        CommonMethods.showToast(this, "Please enter vehicle purchase amount");
                    }
                }
                else
                    {
                        if (!etVehPurchaseAmount.getText().toString().equals("")) {
                            CommonMethods.setValueAgainstKey(this, CommonStrings.VEH_PURCHASE_AMOUNT, etVehPurchaseAmount.getText().toString());
                            startActivity(new Intent(this, CarHaveLoanCurrentlyActivity.class));
                        } else {
                            CommonMethods.showToast(this, "Please enter vehicle purchase amount");
                        }
                    }

            }catch(Exception exception){exception.printStackTrace();}
        }
    }
}