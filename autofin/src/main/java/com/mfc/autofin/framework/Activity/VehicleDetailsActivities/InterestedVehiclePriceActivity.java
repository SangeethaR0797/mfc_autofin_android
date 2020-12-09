package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.new_car_flow.VehiclePurchaseAmountActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;

public class InterestedVehiclePriceActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvRoadPriceLbl;
    private EditText etOnRoadPrice;
    private ImageView iv_vehDetails_back;
    private Button btnNext;
    private double roadPrice=0;
    private String strRoadPrice="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_vehicle_price);
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getOnRoadPrice() != 0) {
                roadPrice = CommonStrings.customVehDetails.getOnRoadPrice();
                String result =CommonMethods.getFormattedDouble(roadPrice);
                strRoadPrice= result.replaceAll("[-+.^:,]","");
            }
        }

        initView();
    }

    private void initView() {
        iv_vehDetails_back=findViewById(R.id.iv_vehDetails_back);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvRoadPriceLbl=findViewById(R.id.tvRoadPriceLbl);
        btnNext=findViewById(R.id.btnNext);
        tvGivenLbl.setText("VARIANT");
        etOnRoadPrice=findViewById(R.id.etOnRoadPrice);
        if(!strRoadPrice.isEmpty())
        {
            etOnRoadPrice.setText(strRoadPrice);
        }
        tvRoadPriceLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        tvGivenPreviousVal.setText(CommonStrings.customVehDetails.getVariant());
        iv_vehDetails_back.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_vehDetails_back)
        {
            startActivity(new Intent(this,VehicleVariantActivity.class));
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            try{
                if(etOnRoadPrice.getText().toString()!="")
                {
                    CommonStrings.customVehDetails.setOnRoadPrice(Double.parseDouble(etOnRoadPrice.getText().toString()));
                    startActivity(new Intent(this, VehiclePurchaseAmountActivity.class));
                }
                else
                {
                    CommonMethods.showToast(this,"Please enter on road price");
                }
            }catch (Exception exception)
            {
                exception.printStackTrace();
            }

        }

    }
    @Override
    public void onBackPressed() {
    }
}
