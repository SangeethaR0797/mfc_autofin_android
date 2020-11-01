package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.CustomerDetailsRes;
import model.vehicle_details.vehicle_category.stock_details.StockData;
import model.vehicle_details.vehicle_category.stock_details.StockDetailsReq;
import model.vehicle_details.vehicle_category.stock_details.StockResponse;
import model.vehicle_details.vehicle_category.stock_details.StockResponseData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;

import static retrofit_config.RetroBase.retrofitInterface;

public class VehRegNumAns extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = VehRegNumAns.class.getSimpleName();
    ImageView iv_vehDetails_backBtn;
    TextView tvVehCategoryQn, tvRegNoLbl;
    EditText etVehRegNo;

    long delay = 2000; // 2 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

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
        iv_vehDetails_backBtn.setOnClickListener(this);
        Runnable input_finish_checker = new Runnable() {
            public void run() {
                if (etVehRegNo.getText().toString().length() >= 10 && etVehRegNo.getText().toString().length() <= 15) {
                    if (System.currentTimeMillis() > (last_text_edit + delay - 1000)) {
                        validateRegNo(etVehRegNo.getText().toString());
                    }
                }
            }
        };

        etVehRegNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {
                //You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        }

    }

    private void validateRegNo(String finalString) {
        StockDetailsReq stockDetailsReq = new StockDetailsReq();
        stockDetailsReq.setUserId("242");
        stockDetailsReq.setUserType("Dealer");
        StockData stockData = new StockData();
        stockData.setVehicleNumber(finalString);
        stockDetailsReq.setData(stockData);

        retrofitInterface.getFromWeb(stockDetailsReq, Global_URLs.STOCK_DETAILS_BASE_URL + CommonStrings.STOCK_DETAILS_URL_END).enqueue(this);

            /*   CommonMethods.setValueAgainstKey(VehRegNumAns.this, "vehicle_reg_no", finalString);
            Intent intent = new Intent(VehRegNumAns.this, VehRegistrationYear.class);
            startActivity(intent);*/

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);

        StockResponse stockResponse = new Gson().fromJson(strRes, StockResponse.class);
        try {
            if (stockResponse != null && stockResponse.getStatus().toString().equals("true")) {

                if (stockResponse.getData() != null) {
                    CommonStrings.customVehDetails.setVehRegNum(etVehRegNo.getText().toString());
                    StockResponseData stockResponseData = stockResponse.getData();
                    CommonStrings.stockResData = stockResponseData;
                    Intent intent = new Intent(VehRegNumAns.this, VehRegistrationYear.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No Stock available", Toast.LENGTH_LONG).show();
                }
            }
        } catch (NullPointerException exception) {
            exception.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}