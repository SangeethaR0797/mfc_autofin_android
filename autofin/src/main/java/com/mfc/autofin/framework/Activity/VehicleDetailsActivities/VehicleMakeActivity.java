package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.util.List;

import controller.VehicleDetailsAdapter;
import model.ibb_models.IBBVehDetailsReq;
import model.ibb_models.VehMakeRes;
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

public class VehicleMakeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = VehicleMakeActivity.class.getSimpleName();
    private TextView tvGivenRegYear, tvGivenRegYearVal, tvGivenRegYearEdit, tvSelectedVehMake;
    private ImageView iv_vehDetails_backBtn, iv_app_make_search, svCloseButton;
    private Button btnNext;
    ListView lvVehListView;
    SearchView svVehMakeDetails;
    VehicleDetailsAdapter vehicleDetailsAdapter;
    String strYear = "", strMake = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_make);
        strYear = CommonStrings.customVehDetails.getRegistrationYear();

        if (CommonStrings.stockResData != null) {
            if (CommonStrings.stockResData.getIbbMake() != null) {
                strMake = CommonStrings.stockResData.getIbbMake();
            }
        } else {
            strMake = "";
        }
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getMake() != null && CommonStrings.customVehDetails.getMake() != null) {
                strMake = CommonStrings.customVehDetails.getMake();
            }
        }


        initView();
        if (CommonMethods.isInternetWorking(VehicleMakeActivity.this)) {
            SpinnerManager.showSpinner(this);
            IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_MAKE, CommonMethods.getStringValueFromKey(VehicleMakeActivity.this, "ibb_access_token"), CommonStrings.IBB_TAG, strYear, "0");
            retrofitInterface.getFromWeb(ibbVehDetailsReq, Global.ibb_base_url+ IBB_VEH_DETAILS_END_POINT).enqueue(this);

        } else {
            Toast.makeText(VehicleMakeActivity.this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    @SuppressLint("NewApi")
    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        tvGivenRegYear = findViewById(R.id.tvGivenRegYear);
        tvGivenRegYearVal = findViewById(R.id.tvGivenRegYearVal);
        tvGivenRegYearEdit = findViewById(R.id.tvGivenRegYearEdit);
        tvSelectedVehMake = findViewById(R.id.tvSelectedVehMake);
        iv_app_make_search = findViewById(R.id.iv_app_make_search);
        lvVehListView = findViewById(R.id.lvVehListView);
        svVehMakeDetails = findViewById(R.id.svVehMakeDetails);
        btnNext = findViewById(R.id.btnNext);
        lvVehListView.setDivider(null);
        tvGivenRegYearVal.setText(strYear);
        tvSelectedVehMake.setText(strMake);
        iv_vehDetails_backBtn.setOnClickListener(this);
        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);

        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) svVehMakeDetails.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        int searchCloseButtonId = svVehMakeDetails.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);

        svCloseButton = this.svVehMakeDetails.findViewById(searchCloseButtonId);
        svCloseButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                svVehMakeDetails.setQuery("", false);
                svVehMakeDetails.clearFocus();
                svVehMakeDetails.setVisibility(View.GONE);
            }
        });

        svVehMakeDetails.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                vehicleDetailsAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                vehicleDetailsAdapter.getFilter().filter(newText);
                return false;
            }
        });

        btnNext.setOnClickListener(this);
        tvGivenRegYearEdit.setOnClickListener(this);
        tvSelectedVehMake.setOnClickListener(this);
        iv_app_make_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            CommonMethods.redirectToDashboard(this);
        } else if (v.getId() == R.id.tvGivenRegYearEdit) {
            finish();
        } else if (v.getId() == R.id.tvSelectedVehMake) {
            if (CommonMethods.isInternetWorking(VehicleMakeActivity.this)) {
                SpinnerManager.showSpinner(this);
                IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_MAKE, CommonMethods.getStringValueFromKey(VehicleMakeActivity.this, "ibb_access_token"), CommonStrings.IBB_TAG, strYear, "0");
                retrofitInterface.getFromWeb(ibbVehDetailsReq, Global.ibb_base_url + IBB_VEH_DETAILS_END_POINT).enqueue(this);

            } else {
                Toast.makeText(VehicleMakeActivity.this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.iv_app_make_search) {
            if (lvVehListView.getVisibility() == View.VISIBLE) {
                svVehMakeDetails.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(VehicleMakeActivity.this, "Please select ListView", Toast.LENGTH_LONG).show();

            }
        } else if (v.getId() == R.id.btnNext) {
            if (!tvSelectedVehMake.getText().toString().equals("")) {
                if (tvSelectedVehMake.getText().toString().equalsIgnoreCase(strMake)) {
                    CommonStrings.customVehDetails.setMake(tvSelectedVehMake.getText().toString());
                    Intent intent = new Intent(this, VehicleModelActivity.class);
                    startActivity(intent);
                } else {
                    CommonStrings.stockResData.setModel("");
                    CommonStrings.customVehDetails.setMake(tvSelectedVehMake.getText().toString());
                    Intent intent = new Intent(this, VehicleModelActivity.class);
                    startActivity(intent);
                }
            } else {
                Toast.makeText(VehicleMakeActivity.this, "Please select Vehicle Make", Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(VehicleMakeActivity.this);
        String strMakeRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strMakeRes);
        try {
            {
                VehMakeRes makeRes = new Gson().fromJson(strMakeRes, VehMakeRes.class);
                if (makeRes.getStatus() == 200) {
                    if (makeRes.getMake() != null) {
                        generateListView(makeRes.getMake());
                    } else {
                        Log.i(TAG, "onResponse: No Make found");
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CommonMethods.redirectToDashboard(this);
        }

    }

    private void generateListView(List<String> year) {
        lvVehListView.setVisibility(View.VISIBLE);
        vehicleDetailsAdapter = new VehicleDetailsAdapter(this, R.layout.custom_list_item_row, year, tvSelectedVehMake, lvVehListView);
        lvVehListView.setAdapter(vehicleDetailsAdapter);
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
    @Override
    public void onBackPressed() {
    }
}