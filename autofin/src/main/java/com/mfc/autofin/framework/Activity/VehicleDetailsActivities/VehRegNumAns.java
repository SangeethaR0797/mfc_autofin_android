package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import model.vehicle_details.vehicle_category.stock_details.StockData;
import model.vehicle_details.vehicle_category.stock_details.StockDetailsReq;
import model.vehicle_details.vehicle_category.stock_details.StockResponse;
import model.vehicle_details.vehicle_category.stock_details.StockResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class VehRegNumAns extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = VehRegNumAns.class.getSimpleName();
    ImageView iv_vehDetails_backBtn;
    TextView tvVehCategoryQn, tvRegNoLbl;
    EditText etVehRegNo;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_reg_num_ans);
        initView();
    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvVehCategoryQn = findViewById(R.id.tvVehCategoryQn);
        tvRegNoLbl = findViewById(R.id.tvRegNoLbl);
        etVehRegNo = findViewById(R.id.etVehRegNo);
        btnNext = findViewById(R.id.btnNext);
        iv_vehDetails_backBtn.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            validate();
        }

    }

    private void validate() {
        String regNo = etVehRegNo.getText().toString();
        if (!regNo.isEmpty()) {
            if (regNo.length() >= 10 && regNo.length() < 15) {
                callStockAPI(regNo);
            } else {
                Toast.makeText(this, getString(R.string.incorrect_reg_no), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.reg_no_empty_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void callStockAPI(String strRegNo) {
        StockDetailsReq stockDetailsReq = new StockDetailsReq();
        stockDetailsReq.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        stockDetailsReq.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        StockData stockData = new StockData();
        stockData.setVehicleNumber(strRegNo);
        stockDetailsReq.setData(stockData);

        if (CommonMethods.isInternetWorking(this)) {
            SpinnerManager.showSpinner(this);
            retrofitInterface.getFromWeb(stockDetailsReq, Global_URLs.STOCK_DETAILS_BASE_URL + CommonStrings.STOCK_DETAILS_URL_END).enqueue(this);
        } else {
            Toast.makeText(this, getString(R.string.please_check_internet_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

        SpinnerManager.hideSpinner(this);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);

        StockResponse stockResponse = new Gson().fromJson(strRes, StockResponse.class);
        try {
            if (stockResponse != null && stockResponse.getStatus().toString().equals("true")) {

                if (stockResponse.getData() != null) {
                    CommonStrings.customVehDetails.setVehRegNum(etVehRegNo.getText().toString());
                    StockResponseData stockResponseData = stockResponse.getData();
                    CommonStrings.stockResData = stockResponseData;
                    Toast.makeText(this, "Vehicle Number verified successfully!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(VehRegNumAns.this, VehRegistrationYear.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, getString(R.string.no_stock_available), Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, getString(R.string.no_stock_available), Toast.LENGTH_LONG).show();
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {
        Toast.makeText(this, getString(R.string.please_try_again), Toast.LENGTH_LONG).show();

    }
}