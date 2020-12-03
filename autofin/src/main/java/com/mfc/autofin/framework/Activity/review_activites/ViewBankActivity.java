package com.mfc.autofin.framework.Activity.review_activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;

import controller.SelectBankAdapter;
import kyc.DocumentUploadActivity;
import model.bank_models.BankListData;
import model.bank_models.BankListReqData;
import model.bank_models.BankListResponse;
import model.bank_models.RecBankListReq;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class ViewBankActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = ViewBankActivity.class.getSimpleName();
    private TextView tvMatchingBankLbl, tvCommonAppBarTitle;
    private RecyclerView rvSelectBank;
    private ImageView iv_common_bar_backBtn;
    private List<BankListData> bankList = new ArrayList<BankListData>();
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bank);
        initView();
        SpinnerManager.showSpinner(this);
        retrofitInterface.getFromWeb(getBankListReq(), CommonStrings.RECOMMENDED_BANK_URL).enqueue(this);
    }

    private Object getBankListReq() {
        RecBankListReq bankListReq = new RecBankListReq();
        bankListReq.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        bankListReq.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        BankListReqData reqData = new BankListReqData();
        reqData.setCustomerId(CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID));
        reqData.setCaseId(CommonMethods.getStringValueFromKey(this, CommonStrings.CASE_ID));
        bankListReq.setData(reqData);
        return bankListReq;
    }

    private void initView() {
        tvMatchingBankLbl = findViewById(R.id.tvMatchingBankLbl);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        rvSelectBank = findViewById(R.id.rvSelectBank);
        tvCommonAppBarTitle.setText("SELECT BANK");
        iv_common_bar_backBtn.setOnClickListener(this);
    }

    private void attachToAdapter(List<BankListData> listOfBank) {
        tvMatchingBankLbl.setText(listOfBank.size() + " " + getResources().getString(R.string.lbl_selected_bank));
        SelectBankAdapter selectBankAdapter = new SelectBankAdapter(ViewBankActivity.this, listOfBank);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSelectBank.setLayoutManager(layoutManager);
        rvSelectBank.setAdapter(selectBankAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_common_bar_backBtn) {
            CommonMethods.redirectToDashboard(this);
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        BankListResponse bankListResponse = new Gson().fromJson(strRes, BankListResponse.class);

        try {
            if (bankListResponse != null && bankListResponse.getStatus()) {
                if (bankListResponse.getData() != null) {
                    bankList = bankListResponse.getData();
                    attachToAdapter(bankList);
                } else {
                    CommonMethods.showToast(this, "No Data found");
                }
            } else {
                CommonMethods.showToast(this, "No Data found");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            startActivity(new Intent(ViewBankActivity.this, DocumentUploadActivity.class));
        }


    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onBackPressed()
    {
        CommonMethods.redirectToDashboard(this);

    }
}
