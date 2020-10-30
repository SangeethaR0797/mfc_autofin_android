package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.ibb_models.IBBVehDetailsReq;
import model.ibb_models.VehRegYearRes;
import model.ibb_models.VehVariantRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.IBB_VEH_DETAILS_END_POINT;

public class VehicleVariantActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static String TAG=VehicleVariantActivity.class.getSimpleName();
    TextView tvGivenVehModelVal,tvGivenVehModelEdit,tvSelectedVehVariant;
    ImageView iv_app_variant_search;
    String strVehMake="",strVehModel="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_variant);
        strVehMake=CommonMethods.getStringValueFromKey(VehicleVariantActivity.this,"veh_reg_make");
        strVehModel= CommonMethods.getStringValueFromKey(VehicleVariantActivity.this,"veh_reg_model");
        initView();

    }

    private void initView()
    {
        tvGivenVehModelVal=findViewById(R.id.tvGivenVehModelVal);
        tvGivenVehModelEdit=findViewById(R.id.tvGivenVehModelEdit);
        tvSelectedVehVariant=findViewById(R.id.tvSelectedVehVariant);
        tvGivenVehModelVal.setText(strVehModel);
        tvGivenVehModelEdit.setOnClickListener(this);
        tvSelectedVehVariant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
     if(v.getId()==R.id.tvGivenVehModelEdit)
     {
         Intent intent=new Intent(VehicleVariantActivity.this, VehicleModelActivity.class);
         startActivity(intent);
     }
     else if(v.getId()==R.id.tvSelectedVehVariant)
     {
         SpinnerManager.showSpinner(this);
         IBBVehDetailsReq ibbVehDetailsReq=new IBBVehDetailsReq(CommonStrings.IBB_VARIANT, CommonMethods.getStringValueFromKey(VehicleVariantActivity.this,"ibb_access_token"),CommonStrings.IBB_TAG,CommonMethods.getStringValueFromKey(VehicleVariantActivity.this,"veh_reg_year"),"0",strVehMake,strVehModel);
         retrofitInterface.getFromWeb(ibbVehDetailsReq, Global_URLs.IBB_BASE_URL+IBB_VEH_DETAILS_END_POINT).enqueue(this);
     }

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

        SpinnerManager.hideSpinner(VehicleVariantActivity.this);
        String strVariantRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strVariantRes);
        try {
            {
                VehVariantRes variantRes=new Gson().fromJson(strVariantRes,VehVariantRes.class);
                if(variantRes.getStatus()==200)
                {
                    if(variantRes.getVariant()!=null)
                    {
                        generateListView(variantRes.getVariant());
                    }
                    else
                    {
                        Log.i(TAG, "onResponse: No Years found");
                    }
                }}
        }catch(Exception exception)
        {
            exception.printStackTrace();
            Intent intent=new Intent(VehicleVariantActivity.this, AutoFinDashBoardActivity.class);
            startActivity(intent);
        }

    }

    private void generateListView(List<String> year)
    {

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}