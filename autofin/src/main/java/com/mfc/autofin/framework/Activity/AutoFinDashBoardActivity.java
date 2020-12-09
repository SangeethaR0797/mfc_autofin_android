package com.mfc.autofin.framework.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import model.ibb_models.AccessTokenRequest;
import model.ibb_models.AccessTokenRes;
import model.token.GetTokenReq;
import model.token.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;
import utility.CommonURLs;
import utility.DataEncrypt;
import utility.Global;
import utility.RobotoMedium;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class AutoFinDashBoardActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    public static boolean progress = true;

    private TextView tvLeadTypeLabel;
    private RobotoMedium tvTotalLeadsCount, tvTotalLeadsLabel, tvOpenLeadsCount, tvOpenLeadsLabel, tvBankLeadsCount, tvBankLeadsLabel, tvClosedLeadsCount, tvClosedLeadsLabel;
    RecyclerView leads_recyclerview;
    LinearLayout llTotalLeads, llOpenLeads, llBankLeads, llClosedLeads, llAppBar;
    FloatingActionButton fab_add_lead;
    ImageView ivSearch, closeButton;
    SearchView searchView;
    DashboardAdapter dashboardAdapter;
    private static String TAG = AutoFinDashBoardActivity.class.getSimpleName();
    private ImageView exitAutofin;
    private static final String UTF_8 = "UTF-8";
    private String mUserId = "";
    private String mUserType = "";
    private String mAppName = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofin_dashboard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        fetchIntentData();

        if (CommonMethods.isInternetWorking(this)) {
            SpinnerManager.showSpinner(this);
            retrofitInterface.getFromWeb(getTokenRequest(), Global.customerDetails_BaseURL+CommonStrings.TOKEN_URL_END).enqueue(this);
        } else {
            Toast.makeText(this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }
        initViews();
    }

    private GetTokenReq getTokenRequest()
    {
        GetTokenReq getTokenReq=new GetTokenReq();
        getTokenReq.setUserId(mUserId);
        getTokenReq.setUserType(mUserType);
        getTokenReq.setRequestFrom("OMS");
        getTokenReq.setData("GenerateToken");
        return getTokenReq;
    }

    private void fetchIntentData() {
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mUserId = data.getString(AutoFinConstants.DEALER_ID);
            mUserType = data.getString(AutoFinConstants.USER_TYPE);
            mAppName = data.getString(AutoFinConstants.APP_NAME);

            // Added by Sangeetha
            CommonMethods.setValueAgainstKey(this, CommonStrings.DEALER_ID_VAL, mUserId);
            CommonMethods.setValueAgainstKey(this, CommonStrings.USER_TYPE_VAL, mUserType);
            CommonMethods.setValueAgainstKey(this, CommonStrings.APP_NAME_VAL, mAppName);
        }
    }

    private AccessTokenRequest getIBBAccessTokenReq() {
        AccessTokenRequest accessTokenRequest = new AccessTokenRequest();
        accessTokenRequest.setUsername(CommonStrings.IBB_USERNAME);
        accessTokenRequest.setPassword(CommonStrings.IBB_PASSWORD);
        return accessTokenRequest;
    }

    @SuppressLint("NewApi")
    private void initViews() {
        tvTotalLeadsCount = (RobotoMedium) findViewById(R.id.tvTotalLeadsCount);
        tvTotalLeadsLabel = (RobotoMedium) findViewById(R.id.tvTotalLeadsLabel);
        tvOpenLeadsCount = (RobotoMedium) findViewById(R.id.tvOpenLeadsCount);
        tvOpenLeadsLabel = (RobotoMedium) findViewById(R.id.tvOpenLeadsLabel);
        tvBankLeadsCount = (RobotoMedium) findViewById(R.id.tvBankLeadsCount);
        tvBankLeadsLabel = (RobotoMedium) findViewById(R.id.tvBankLeadsLabel);
        tvClosedLeadsCount = (RobotoMedium) findViewById(R.id.tvClosedLeadsCount);
        tvClosedLeadsLabel = (RobotoMedium) findViewById(R.id.tvClosedLeadsLabel);
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
        exitAutofin = findViewById(R.id.exitAutofin);
        int searchCloseButtonId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        closeButton = this.searchView.findViewById(searchCloseButtonId);

        llBankLeads.setOnClickListener(this);
        llTotalLeads.setOnClickListener(this);
        llClosedLeads.setOnClickListener(this);
        llOpenLeads.setOnClickListener(this);
        fab_add_lead.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        exitAutofin.setOnClickListener(this);

        tvLeadTypeLabel.setText(R.string.lbl_all_leads);
        tvLeadTypeLabel.setTypeface(CustomFonts.getRobotoRegularTF(AutoFinDashBoardActivity.this));
        closeButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
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
        customerDetailsReq.setUserId(mUserId);
        customerDetailsReq.setUserType(mUserType);
        dealerData.setType(mUserType);
        dealerData.setId(mUserId);
        dealerData.setTabName(tabName);
        customerDetailsReq.setData(dealerData);
        return customerDetailsReq;
    }

    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.llOpenLeads) {
            highLightTab(llOpenLeads, tvOpenLeadsCount, tvOpenLeadsLabel);
            deHighLightTab(llTotalLeads, tvTotalLeadsCount, tvTotalLeadsLabel);
            deHighLightTab(llBankLeads, tvBankLeadsCount, tvBankLeadsLabel);
            deHighLightTab(llClosedLeads, tvClosedLeadsCount, tvClosedLeadsLabel);
            retrofitInterface.getFromWeb(getCustomerDetailsReq("Open"), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_open_leads);
        } else if (v.getId() == R.id.llClosedLeads) {
            deHighLightTab(llOpenLeads, tvOpenLeadsCount, tvOpenLeadsLabel);
            deHighLightTab(llTotalLeads, tvTotalLeadsCount, tvTotalLeadsLabel);
            deHighLightTab(llBankLeads, tvBankLeadsCount, tvBankLeadsLabel);
            highLightTab(llClosedLeads, tvClosedLeadsCount, tvClosedLeadsLabel);
            retrofitInterface.getFromWeb(getCustomerDetailsReq("RTO"), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_closed_leads);
        } else if (v.getId() == R.id.llBankLeads) {
            deHighLightTab(llOpenLeads, tvOpenLeadsCount, tvOpenLeadsLabel);
            deHighLightTab(llTotalLeads, tvTotalLeadsCount, tvTotalLeadsLabel);
            highLightTab(llBankLeads, tvBankLeadsCount, tvBankLeadsLabel);
            deHighLightTab(llClosedLeads, tvClosedLeadsCount, tvClosedLeadsLabel);
            retrofitInterface.getFromWeb(getCustomerDetailsReq("Bank"), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_bank_leads);
        } else if (v.getId() == R.id.llTotalLeads) {
            deHighLightTab(llOpenLeads, tvOpenLeadsCount, tvOpenLeadsLabel);
            highLightTab(llTotalLeads, tvTotalLeadsCount, tvTotalLeadsLabel);
            deHighLightTab(llBankLeads, tvBankLeadsCount, tvBankLeadsLabel);
            deHighLightTab(llClosedLeads, tvClosedLeadsCount, tvClosedLeadsLabel);
            retrofitInterface.getFromWeb(getCustomerDetailsReq(""), "customer-listing").enqueue(this);
            tvLeadTypeLabel.setText(R.string.lbl_all_leads);
        } else if (v.getId() == R.id.fab_add_lead) {
            CommonStrings.IS_OLD_LEAD=false;
            CommonMethods.clearData();
            Intent intent = new Intent(AutoFinDashBoardActivity.this, VehicleCategory.class);
            startActivity(intent);
        } else if (v.getId() == R.id.iv_app_bar_search) {
            searchView.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.exitAutofin) {
            finish();
        }

    }

    private void highLightTab(LinearLayout lLayout, TextView textView, TextView textView1) {
        lLayout.setBackgroundColor(getResources().getColor(R.color.navy_blue));
        textView.setTextColor(getResources().getColor(R.color.white));
        textView1.setTextColor(getResources().getColor(R.color.white));
    }

    private void deHighLightTab(LinearLayout lLayout, TextView textView, TextView textView1) {
        lLayout.setBackgroundColor(getResources().getColor(R.color.white));
        textView.setTextColor(getResources().getColor(R.color.very_dark_blue));
        textView1.setTextColor(getResources().getColor(R.color.very_dark_blue));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        if (url.contains(CommonStrings.TOKEN_URL_END)) {
            TokenResponse tokenRes = new Gson().fromJson(strRes, TokenResponse.class);
            try {
                if (tokenRes != null && tokenRes.getStatus()) {
                    if (tokenRes.getData() != null) {
                        CommonMethods.setValueAgainstKey(AutoFinDashBoardActivity.this, "encrypt_token", tokenRes.getData());
                        Log.i(TAG, "onResponse: " + tokenRes.getData());
                        storeToken(tokenRes.getData());
                        SpinnerManager.showSpinner(this);
                        retrofitInterface.getFromWeb(getCustomerDetailsReq(""), Global.customerAPI_BaseURL+CommonStrings.CUSTOMER_DETAILS_URL_END).enqueue(this);
                        retrofitInterface.getFromWeb(getIBBAccessTokenReq(), Global.ibb_base_url+CommonStrings.IBB_ACCESS_TOKEN_URL_END).enqueue(this);

                    } else {
                        Toast.makeText(this, "No access token available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Error occurred", Toast.LENGTH_LONG).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        else if (url.contains(CommonStrings.CUSTOMER_DETAILS_URL_END)) {
            CustomerDetailsRes customerDetailsRes = new Gson().fromJson(strRes, CustomerDetailsRes.class);
            try {
                if (customerDetailsRes != null && customerDetailsRes.getStatus().toString().equals("true")) {

                    if (customerDetailsRes.getData().getCount() != null) {
                        tvTotalLeadsCount.setText(customerDetailsRes.getData().getCount().getAll() + "");
                        tvOpenLeadsCount.setText(customerDetailsRes.getData().getCount().getOpen() + "");
                        tvBankLeadsCount.setText(customerDetailsRes.getData().getCount().getBank() + "");
                        tvClosedLeadsCount.setText(customerDetailsRes.getData().getCount().getClosed() + "");
                    } else {
                        Toast.makeText(this, "No Count Data available", Toast.LENGTH_LONG).show();

                    }

                    if (customerDetailsRes.getData().getCustomers() != null) {
                        attachAdapter(customerDetailsRes.getData().getCustomers());
                    } else {
                        Toast.makeText(this, "No Customer Data available", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
        else if (url.contains(CommonStrings.IBB_ACCESS_TOKEN_URL_END)) {
            AccessTokenRes accessTokenRes = new Gson().fromJson(strRes, AccessTokenRes.class);
            try {
                if (accessTokenRes != null && accessTokenRes.getStatus().equalsIgnoreCase("success")) {
                    if (accessTokenRes.getAccessToken() != null) {
                        CommonMethods.setValueAgainstKey(AutoFinDashBoardActivity.this, "ibb_access_token", accessTokenRes.getAccessToken());
                        Log.i(TAG, "onResponse: " + accessTokenRes.getAccessToken());
                    } else {
                        Toast.makeText(this, "No access token available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "Error occurred", Toast.LENGTH_LONG).show();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void storeToken(String data)
    {
        DataEncrypt dataEncrypt=new DataEncrypt();
        try {
            /*final String decryptedToken = new String(dataEncrypt.decrypt(data),UTF_8).trim();*/
            /*CommonMethods.setValueAgainstKey(this,"token",decryptedToken);*/
            String decValue=dataEncrypt.decrypt(data);
            //String decryptedData = String.valueOf(Base64.decode(decValue, Base64.NO_WRAP));
            CommonStrings.TOKEN_VALUE=decValue;
            Log.i(TAG, "storeToken: "+CommonStrings.TOKEN_VALUE);
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

    @Override
    protected void onResume() {
        super.onResume();
        SpinnerManager.showSpinner(this);
        retrofitInterface.getFromWeb(getCustomerDetailsReq(""), CommonStrings.CUSTOMER_DETAILS_URL_END).enqueue(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SpinnerManager.showSpinner(this);
        retrofitInterface.getFromWeb(getCustomerDetailsReq(""), CommonStrings.CUSTOMER_DETAILS_URL_END).enqueue(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
