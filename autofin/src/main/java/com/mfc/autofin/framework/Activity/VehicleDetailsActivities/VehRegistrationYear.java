package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.util.List;

import controller.VehicleDetailsAdapter;
import model.custom_model.ReviewData;
import model.ibb_models.IBBVehDetailsReq;
import model.ibb_models.VehRegYearRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CommonURLs;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.IBB_VEH_DETAILS_END_POINT;

public class VehRegistrationYear extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = VehRegistrationYear.class.getSimpleName();
    private TextView tvGivenRegNumLbl, tvGivenRegNoVal, tvVehRegNumEdit, tvRegYearLbl, tvRegYear;
    private ImageView iv_vehDetails_backBtn, iv_year_search, svCloseButton;
    ListView lvVehListView;
    String previousVal = "", strYear = "";
    //SearchView svVehDetails;
    VehicleDetailsAdapter vehicleDetailsAdapter;
    private Button btnNext;
    private boolean isNewCarFlow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_registration_year);


        if (CommonStrings.customLoanDetails.getLoanCategory().equals(getString(R.string.new_car))) {
            isNewCarFlow = true;
            previousVal = CommonStrings.customLoanDetails.getLoanCategory();
        } else {
            previousVal = CommonStrings.customVehDetails.getVehicleNumber();
        }

        if (CommonStrings.stockResData != null) {
            if (CommonStrings.stockResData.getYear() != null)
                strYear = String.valueOf(CommonStrings.stockResData.getYear());
            else
                strYear = "";
        }
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getRegistrationYear() != null && !CommonStrings.customVehDetails.getRegistrationYear().isEmpty()) {

                    if (CommonStrings.customVehDetails.getRegistrationYear().contains("."))
                        strYear = CommonStrings.customVehDetails.getRegistrationYear().substring(0, 4);
                    else
                        strYear = CommonStrings.customVehDetails.getRegistrationYear();

            }
        }


        initView();

        if (CommonMethods.isInternetWorking(VehRegistrationYear.this)) {
            SpinnerManager.showSpinner(VehRegistrationYear.this);
            IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_YEAR, CommonMethods.getStringValueFromKey(VehRegistrationYear.this, "ibb_access_token"), CommonStrings.IBB_TAG);
            retrofitInterface.getFromWeb(ibbVehDetailsReq, Global.ibb_base_url + IBB_VEH_DETAILS_END_POINT).enqueue(this);
        } else {
            Toast.makeText(VehRegistrationYear.this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {

        tvGivenRegNumLbl = findViewById(R.id.tvGivenRegNumLbl);
        tvGivenRegNoVal = findViewById(R.id.tvGivenRegNoVal);
        tvVehRegNumEdit = findViewById(R.id.tvVehRegNumEdit);
        tvRegYearLbl = findViewById(R.id.tvRegYearLbl);
        tvRegYear = findViewById(R.id.tvRegYear);
        lvVehListView = findViewById(R.id.lvVehListView);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);
        if (isNewCarFlow) {
            tvGivenRegNumLbl.setText("VEHICLE CATEGORY");
        } else {
            tvGivenRegNumLbl.setText("REGISTRATION NO.");
        }
        tvGivenRegNoVal.setText(previousVal);
        lvVehListView.setDivider(null);
        if (!strYear.isEmpty()) {
            tvRegYear.setText(strYear);
        }
        btnNext = findViewById(R.id.btnNext);
        iv_vehDetails_backBtn.setOnClickListener(this);
        tvVehRegNumEdit.setOnClickListener(this);
        tvRegYear.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvVehRegNumEdit) {
            finish();
        } else if (v.getId() == R.id.tvRegYear) {
            SpinnerManager.showSpinner(VehRegistrationYear.this);
            IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_YEAR, CommonMethods.getStringValueFromKey(VehRegistrationYear.this, "ibb_access_token"), CommonStrings.IBB_TAG);
            retrofitInterface.getFromWeb(ibbVehDetailsReq, Global.ibb_base_url + IBB_VEH_DETAILS_END_POINT).enqueue(this);
        } else if (v.getId() == R.id.btnNext) {
            if (!tvRegYear.getText().toString().equals("")) {
                CommonStrings.customVehDetails.setRegistrationYear(tvRegYear.getText().toString());
                Intent intent = new Intent(this, VehicleMakeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select Manufactured Year", Toast.LENGTH_LONG).show();
            }

        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(VehRegistrationYear.this);
        String strYearRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strYearRes);
        try {
            {
                VehRegYearRes yearRes = new Gson().fromJson(strYearRes, VehRegYearRes.class);
                if (yearRes.getStatus() == 200) {
                    if (yearRes.getYear() != null) {
                        generateListView(yearRes.getYear());
                    } else {
                        Log.i(TAG, "onResponse: No Years found");
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CommonMethods.redirectToDashboard(this);
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    private void generateListView(List<String> year) {
        lvVehListView.setVisibility(View.VISIBLE);
        vehicleDetailsAdapter = new VehicleDetailsAdapter(this, R.layout.custom_list_item_row, year, tvRegYear, lvVehListView);
        lvVehListView.setAdapter(vehicleDetailsAdapter);
    }

    @Override
    public void onBackPressed() {
    }
}