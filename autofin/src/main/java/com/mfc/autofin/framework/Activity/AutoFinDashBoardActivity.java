package com.mfc.autofin.framework.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.util.List;
import controller.DashboardAdapter;
import model.CustomerData;
import model.CustomerDetailsReq;
import model.CustomerDetailsRes;
import model.DealerData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static retrofit_config.RetroBase.retrofitInterface;

public class AutoFinDashBoardActivity extends AppCompatActivity implements Callback<Object> {

    private TextView tvTotalLeadsCount,tvTotalLeadsLabel,tvOpenLeadsCount,tvOpenLeadsLabel,tvBankLeadsCount,tvBankLeadsLabel,tvClosedLeadsCount,tvClosedLeadsLabel,tvLeadTypeLabel;
    RecyclerView leads_recyclerview;
    private static String TAG=AutoFinDashBoardActivity.class.getSimpleName();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofin_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initViews();
        retrofitInterface.getFromWeb(getCustomerDetailsReq(),"customer-listing").enqueue(this);
    }

    private void initViews()
    {
        tvTotalLeadsCount=findViewById(R.id.tvTotalLeadsCount);
        tvTotalLeadsLabel=findViewById(R.id.tvTotalLeadsLabel);
        tvOpenLeadsCount=findViewById(R.id.tvOpenLeadsCount);
        tvOpenLeadsLabel=findViewById(R.id.tvOpenLeadsLabel);
        tvBankLeadsCount=findViewById(R.id.tvBankLeadsCount);
        tvBankLeadsLabel=findViewById(R.id.tvBankLeadsLabel);
        tvClosedLeadsCount=findViewById(R.id.tvClosedLeadsCount);
        tvClosedLeadsLabel=findViewById(R.id.tvClosedLeadsLabel);
        tvLeadTypeLabel=findViewById(R.id.tvLeadTypeLabel);
        leads_recyclerview=findViewById(R.id.leads_recyclerview);

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
        customerDetailsReq.setData(dealerData);
        return customerDetailsReq;
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response)
    {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        CustomerDetailsRes createEventResponse = new Gson().fromJson(strRes, CustomerDetailsRes.class);
        try {
            if (createEventResponse.getStatus().toString().equals("true")) {
                Toast.makeText(AutoFinDashBoardActivity.this, (CharSequence) createEventResponse.getMessage(),Toast.LENGTH_SHORT).show();
                attachAdapter(createEventResponse.getData());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    private void attachAdapter(List<CustomerData> data)
    {
        try {
            DashboardAdapter rdmDealerListAdapter = new DashboardAdapter(AutoFinDashBoardActivity.this, data);
            leads_recyclerview.setAdapter(rdmDealerListAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}
