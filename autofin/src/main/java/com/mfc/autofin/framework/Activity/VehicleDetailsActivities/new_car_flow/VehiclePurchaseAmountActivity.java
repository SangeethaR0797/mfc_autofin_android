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
    private String strPreviousVal = "",strPurchaseAmount="";
    private ImageView iv_vehDetails_back;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvVehPurchaseAmount;
    private EditText etVehPurchaseAmount;
    private Button btnNext;
    private double purchaseAmount=0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_purchase_amount);
        if(CommonStrings.customLoanDetails.getLoanCategory().equals(getResources().getString(R.string.new_car)))
        {
            isNewCar=true;
        }
        if(isNewCar)
        {
            if (!String.valueOf(CommonStrings.customVehDetails.getOnRoadPrice()).equals("")) {
                strPreviousVal = String.valueOf(CommonStrings.customVehDetails.getOnRoadPrice());
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

        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getVehicleSellingPrice() != 0) {
                purchaseAmount = CommonStrings.customVehDetails.getVehicleSellingPrice();
                String result =CommonMethods.getFormattedDouble(purchaseAmount);
                strPurchaseAmount=result.replaceAll("[-+.^:,]","");
            }
        }

        initView();
    }

    private void initView() {
        iv_vehDetails_back = findViewById(R.id.iv_vehDetails_back);
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
        if(!strPurchaseAmount.isEmpty())
        {
            etVehPurchaseAmount.setText(strPurchaseAmount);

        }
        tvVehPurchaseAmount.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_vehDetails_back.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

       if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {

            try{

                if(isNewCar)
                {
                    if (!etVehPurchaseAmount.getText().toString().equals("")) {
                        CommonStrings.customVehDetails.setVehicleSellingPrice(Double.parseDouble(etVehPurchaseAmount.getText().toString()));
                        startActivity(new Intent(this, InsuredAmountActivity.class));
                    } else {
                        CommonMethods.showToast(this, "Please enter vehicle purchase amount");
                    }
                }
                else
                    {
                        if (!etVehPurchaseAmount.getText().toString().equals("")) {
                            CommonStrings.customVehDetails.setVehicleSellingPrice(Double.parseDouble(etVehPurchaseAmount.getText().toString()));
                            startActivity(new Intent(this, CarHaveLoanCurrentlyActivity.class));
                        } else {
                            CommonMethods.showToast(this, "Please enter vehicle purchase amount");
                        }
                    }

            }catch(Exception exception){exception.printStackTrace();}
        }
    }
    @Override
    public void onBackPressed() {
    }
}
