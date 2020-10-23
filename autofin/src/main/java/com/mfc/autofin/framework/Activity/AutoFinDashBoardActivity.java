package com.mfc.autofin.framework.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import model.CustomerDetailsReq;
import model.CustomerDetailsRes;
import model.DealerData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit_services.DashboardService;
import retrofit_services.RetrofitInterface;

import static retrofit_config.RetroBase.retrofitInterface;

public class AutoFinDashBoardActivity extends AppCompatActivity  {

    private static String TAG=AutoFinDashBoardActivity.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofin_dashboard);
        getDashBoardInfo(AutoFinDashBoardActivity.this);
        //retrofitInterface.getFromWeb(getCustomerDetailsReq(),"employee-type").enqueue(AutoFinDashBoardActivity.this);
    }

    private void getDashBoardInfo(final Context mContext) {
        DashboardService.fetchDashBoardInfo(mContext, new RetrofitInterface() {
            @Override
            public void OnSuccess(Object obj) {
                Response<CustomerDetailsRes> mRes = (Response<CustomerDetailsRes>) obj;
                CustomerDetailsRes mData = mRes.body();

                String str = new Gson().toJson(mData, CustomerDetailsRes.class);
                Log.i(TAG, "OnSuccess: " + str);
            }

            @Override
            public void OnFailure(Throwable mThrowable) {

                Toast.makeText(mContext, mThrowable.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private CustomerDetailsReq getCustomerDetailsReq()
    {
        CustomerDetailsReq customerDetailsReq=new CustomerDetailsReq();
        DealerData dealerData=new DealerData();
        customerDetailsReq.setUserId("242");
        customerDetailsReq.setUserType("Dealer");
        dealerData.setType("Dealer");
        dealerData.setId("242");
        dealerData.setTabName("");

        return customerDetailsReq;
    }

   /* @Override
    public void onResponse(Call<Object> call, Response<Object> response)
    {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        CustomerDetailsRes createEventResponse = new Gson().fromJson(strRes, CustomerDetailsRes.class);
        try {
            if (createEventResponse.getStatus().toString().equals("true")) {
                Toast.makeText(AutoFinDashBoardActivity.this, (CharSequence) createEventResponse.getMessage(),Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }*/
}
