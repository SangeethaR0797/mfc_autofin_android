package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import controller.VehicleDetailsAdapter;
import model.ibb_models.IBBVehDetailsReq;
import model.ibb_models.VehMakeRes;
import model.ibb_models.VehModelRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.IBB_VEH_DETAILS_END_POINT;

public class VehicleModelActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = VehicleModelActivity.class.getSimpleName();
    TextView tvGivenRegMake, tvGivenVehMakeVal, tvGivenVehMakeEdit, tvSelectedVehModel;
    ImageView iv_app_model_search, svCloseButton;
    String strVehMake = "";
    ListView lvVehListView;
    SearchView svVehModelDetails;
    VehicleDetailsAdapter vehicleDetailsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_model);
        strVehMake = CommonMethods.getStringValueFromKey(VehicleModelActivity.this, "veh_reg_make");
        initView();
    }

    private void initView() {
        tvGivenRegMake = findViewById(R.id.tvGivenRegMake);
        tvGivenVehMakeVal = findViewById(R.id.tvGivenVehMakeVal);
        tvGivenVehMakeEdit = findViewById(R.id.tvGivenVehMakeEdit);
        tvSelectedVehModel = findViewById(R.id.tvSelectedVehModel);
        iv_app_model_search = findViewById(R.id.iv_app_model_search);
        lvVehListView = findViewById(R.id.lvVehListView);
        tvGivenVehMakeVal.setText(strVehMake);
        svVehModelDetails = findViewById(R.id.svVehModelDetails);
        lvVehListView.setDivider(null);
        int searchCloseButtonId = svVehModelDetails.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);

        svCloseButton = this.svVehModelDetails.findViewById(searchCloseButtonId);
        svCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                svVehModelDetails.setQuery("", false);
                svVehModelDetails.clearFocus();
                svVehModelDetails.setVisibility(View.GONE);
            }
        });

        svVehModelDetails.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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


        tvGivenVehMakeEdit.setOnClickListener(this);
        tvSelectedVehModel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenVehMakeEdit) {
            Intent intent = new Intent(VehicleModelActivity.this, VehicleMakeActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tvSelectedVehModel) {
            SpinnerManager.showSpinner(this);
            IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_MODEL, CommonMethods.getStringValueFromKey(VehicleModelActivity.this, "ibb_access_token"), CommonStrings.IBB_TAG, CommonMethods.getStringValueFromKey(VehicleModelActivity.this, "veh_reg_year"), "0", strVehMake);
            retrofitInterface.getFromWeb(ibbVehDetailsReq, Global_URLs.IBB_BASE_URL + IBB_VEH_DETAILS_END_POINT).enqueue(this);
        } else if (v.getId() == R.id.iv_app_make_search) {

        }

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(VehicleModelActivity.this);
        String strModelRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strModelRes);
        try {
            {
                VehModelRes makeRes = new Gson().fromJson(strModelRes, VehModelRes.class);
                if (makeRes.getStatus() == 200) {
                    if (makeRes.getModel() != null) {
                        generateListView(makeRes.getModel());
                    } else {
                        Log.i(TAG, "onResponse: No Model found");
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Intent intent = new Intent(VehicleModelActivity.this, AutoFinDashBoardActivity.class);
            startActivity(intent);
        }

    }

    private void generateListView(List<String> year) {
        lvVehListView.setVisibility(View.VISIBLE);
        vehicleDetailsAdapter = new VehicleDetailsAdapter(this, R.layout.custom_list_item_row, year, tvSelectedVehModel, lvVehListView);
        lvVehListView.setAdapter(vehicleDetailsAdapter);
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}