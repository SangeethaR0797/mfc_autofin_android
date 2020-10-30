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
import model.ibb_models.VehMakeRes;
import model.ibb_models.VehRegYearRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.IBB_VEH_DETAILS_END_POINT;

public class VehicleMakeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object>
{

    private static final String TAG =VehicleMakeActivity.class.getSimpleName() ;
    private TextView tvGivenRegYear,tvGivenRegYearVal,tvGivenRegYearEdit,tvSelectedVehMake;
    private ImageView iv_app_make_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_make);

        initView();
    }

    private void initView()
    {
        tvGivenRegYear=findViewById(R.id.tvGivenRegYear);
        tvGivenRegYearVal=findViewById(R.id.tvGivenRegYearVal);
        tvGivenRegYearEdit=findViewById(R.id.tvGivenRegYearEdit);
        tvSelectedVehMake=findViewById(R.id.tvSelectedVehMake);
        iv_app_make_search=findViewById(R.id.iv_app_make_search);
        tvGivenRegYearEdit.setOnClickListener(this);
        tvSelectedVehMake.setOnClickListener(this);
        iv_app_make_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
    if(v.getId()==R.id.tvGivenRegYearEdit)
    {
        Intent intent=new Intent(VehicleMakeActivity.this, VehRegistrationYear.class);
        startActivity(intent);
    }
    else if(v.getId()==R.id.tvSelectedVehMake)
    {
        SpinnerManager.showSpinner(this);
        IBBVehDetailsReq ibbVehDetailsReq=new IBBVehDetailsReq(CommonStrings.IBB_MAKE, CommonMethods.getStringValueFromKey(VehicleMakeActivity.this,"ibb_access_token"),CommonStrings.IBB_TAG,CommonMethods.getStringValueFromKey(VehicleMakeActivity.this,"veh_reg_year"),"0");
        retrofitInterface.getFromWeb(ibbVehDetailsReq, Global_URLs.IBB_BASE_URL+IBB_VEH_DETAILS_END_POINT).enqueue(this);
    }
    else if(v.getId()==R.id.iv_app_make_search)
    {

    }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(VehicleMakeActivity.this);
        String strMakeRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strMakeRes);
        try {
            {
                VehMakeRes makeRes=new Gson().fromJson(strMakeRes,VehMakeRes.class);
                if(makeRes.getStatus()==200)
                {
                    if(makeRes.getMake()!=null)
                    {
                        generateListView(makeRes.getMake());
                    }
                    else
                    {
                        Log.i(TAG, "onResponse: No Make found");
                    }
                }}
        }catch(Exception exception)
        {
            exception.printStackTrace();
            Intent intent=new Intent(VehicleMakeActivity.this, AutoFinDashBoardActivity.class);
            startActivity(intent);
        }

    }

    private void generateListView(List<String> year)
    {
        CommonMethods.setValueAgainstKey(this,"veh_reg_make","AUDI");
        Intent intent=new Intent(VehicleMakeActivity.this, VehicleModelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}