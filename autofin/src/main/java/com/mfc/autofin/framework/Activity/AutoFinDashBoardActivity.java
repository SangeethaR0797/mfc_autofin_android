package com.mfc.autofin.framework.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleCategory;
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
import utility.CustomFonts;

import static retrofit_config.RetroBase.retrofitInterface;

public class AutoFinDashBoardActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private TextView tvTotalLeadsCount, tvTotalLeadsLabel, tvOpenLeadsCount, tvOpenLeadsLabel, tvBankLeadsCount, tvBankLeadsLabel, tvClosedLeadsCount, tvClosedLeadsLabel, tvLeadTypeLabel;
    RecyclerView leads_recyclerview;
    LinearLayout llTotalLeads, llOpenLeads, llBankLeads, llClosedLeads, llAppBar;
    FloatingActionButton fab_add_lead;
    ImageView ivSearch, closeButton;
    SearchView searchView;
    DashboardAdapter dashboardAdapter;
    private static String TAG = AutoFinDashBoardActivity.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofin_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        initViews();
        retrofitInterface.getFromWeb(getCustomerDetailsReq(""), "customer-listing").enqueue(this);
    }

    private void initViews() {
        tvTotalLeadsCount = findViewById(R.id.tvTotalLeadsCount);
        tvTotalLeadsLabel = findViewById(R.id.tvTotalLeadsLabel);
        tvOpenLeadsCount = findViewById(R.id.tvOpenLeadsCount);
        tvOpenLeadsLabel = findViewById(R.id.tvOpenLeadsLabel);
        tvBankLeadsCount = findViewById(R.id.tvBankLeadsCount);
        tvBankLeadsLabel = findViewById(R.id.tvBankLeadsLabel);
        tvClosedLeadsCount = findViewById(R.id.tvClosedLeadsCount);
        tvClosedLeadsLabel = findViewById(R.id.tvClosedLeadsLabel);
        tvLeadTypeLabel = findViewById(R.id.tvLeadTypeLabel);
        leads_recyclerview = findViewById(R.id.leads_recyclerview);
        llAppBar = findViewById(R.id.llAppBar);
        llTotalLeads = findViewById(R.id.llTotalLeads);
        llOpenLeads = findViewById(R.id.llOpenLeads);
        llClosedLeads = findViewById(R.id.llClosedLeads);
        llBankLeads = findViewById(R.id.llBankLeads);
        fab_add_lead = findViewById(R.id.fab_add_lead);
        ivSearch = findViewById(R.id.iv_app_bar_search);
        searchView = findViewById(R.id.searchView);
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        closeButton = this.searchView.findViewById(searchCloseButtonId);

        llBankLeads.setOnClickListener(this);
        llTotalLeads.setOnClickListener(this);
        llClosedLeads.setOnClickListener(this);
        llOpenLeads.setOnClickListener(this);
        fab_add_lead.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        tvLeadTypeLabel.setText(R.string.lbl_all_leads);
        tvLeadTypeLabel.setTypeface(CustomFonts.getRobotoRegularTF(AutoFinDashBoardActivity.this));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchView.setQuery("", false);
                searchView.clearFocus();
                searchView.setVisibility(View.GONE);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                dashboardAdapter.filter(query);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                dashboardAdapter.filter(newText);
                return false;
            }
        });

    }


    private CustomerDetailsReq getCustomerDetailsReq(String tabName) {
        CustomerDetailsReq customerDetailsReq = new CustomerDetailsReq();
        DealerData dealerData = new DealerData();
        customerDetailsReq.setUserId("242");
        customerDetailsReq.setUserType("Dealer");
        dealerData.setType("Dealer");
        dealerData.setId("242");
        dealerData.setTabName(tabName);
        customerDetailsReq.setData(dealerData);
        return customerDetailsReq;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.llOpenLeads) {
            retrofitInterface.getFromWeb(getCustomerDetailsReq("Open"), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_open_leads);
        } else if (v.getId() == R.id.llClosedLeads) {
            retrofitInterface.getFromWeb(getCustomerDetailsReq("RTO"), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_closed_leads);
        } else if (v.getId() == R.id.llBankLeads) {
            retrofitInterface.getFromWeb(getCustomerDetailsReq("Bank"), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_bank_leads);
        } else if (v.getId() == R.id.llTotalLeads) {
            retrofitInterface.getFromWeb(getCustomerDetailsReq(""), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_all_leads);
        } else if (v.getId() == R.id.fab_add_lead) {
            Intent intent = new Intent(AutoFinDashBoardActivity.this, VehicleCategory.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv_app_bar_search) {
            searchView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        CustomerDetailsRes createEventResponse = new Gson().fromJson(strRes, CustomerDetailsRes.class);
        try {
            if (createEventResponse.getStatus().toString().equals("true")) {
                setLeadTabCount(createEventResponse.getData());
                attachAdapter(createEventResponse.getData());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    private void attachAdapter(List<CustomerData> data) {
        try {
            dashboardAdapter = new DashboardAdapter(AutoFinDashBoardActivity.this, data);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            leads_recyclerview.setLayoutManager(layoutManager);
            leads_recyclerview.setAdapter(dashboardAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void setLeadTabCount(List<CustomerData> data) {
        int openLeadsSize = 0, totalLeadsSize = 0, bankLeadSize = 0, closedLeadSize = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getMainStatus().equalsIgnoreCase("open")) {
                openLeadsSize++;
            } else if (data.get(i).getMainStatus().equalsIgnoreCase("RTO")) {
                closedLeadSize++;
            } else if (data.get(i).getMainStatus().equalsIgnoreCase("Bank")) {
                bankLeadSize++;
            }

        }
        tvTotalLeadsCount.setText(""+data.size());
        tvBankLeadsCount.setText(""+bankLeadSize);
        tvClosedLeadsCount.setText(""+closedLeadSize);
        tvOpenLeadsCount.setText(""+openLeadsSize);
    }
}
