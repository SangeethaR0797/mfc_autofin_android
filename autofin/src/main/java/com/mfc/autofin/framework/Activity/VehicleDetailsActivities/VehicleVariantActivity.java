package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
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
import model.ibb_models.VehVariantRes;
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

public class VehicleVariantActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static String TAG = VehicleVariantActivity.class.getSimpleName();
    TextView tvGivenVehModelVal, tvGivenVehModelEdit, tvSelectedVehVariant;
    ImageView iv_vehDetails_backBtn, iv_app_variant_search, svCloseButton;
    private Button btnNext;
    String strVehCategory="",strYear = "", strVehMake = "", strVehModel = "", strVariant;
    ListView lvVehListView;
    VehicleDetailsAdapter vehicleDetailsAdapter;
    SearchView svVehVariantDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_variant);
        Log.i(TAG, "onCreate: ");
        strVehCategory=CommonStrings.customLoanDetails.getLoanCategory();
        strYear = CommonStrings.customVehDetails.getRegistrationYear();
        strVehMake = CommonStrings.customVehDetails.getMake();
        strVehModel = CommonStrings.customVehDetails.getModel();


        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getVariant() != null && CommonStrings.customVehDetails.getVariant() != null) {
                strVariant = CommonStrings.customVehDetails.getVariant();
            }
            else if (CommonStrings.stockResData != null) {
                if (CommonStrings.stockResData.getIbbVariant() != null) {
                    strVariant = CommonStrings.stockResData.getIbbVariant();
                }
            } else {
                strVariant = "";
            }
        }
        else
        {
            if (CommonStrings.stockResData != null) {
                if (CommonStrings.stockResData.getIbbVariant() != null) {
                    strVariant = CommonStrings.stockResData.getIbbVariant();
                }
            } else {
                strVariant = "";
            }
        }

        initView();

        if (CommonMethods.isInternetWorking(VehicleVariantActivity.this)) {
            SpinnerManager.showSpinner(this);
            IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_VARIANT, CommonMethods.getStringValueFromKey(VehicleVariantActivity.this, "ibb_access_token"), CommonStrings.IBB_TAG, strYear, "0", strVehMake, strVehModel);
            retrofitInterface.getFromWeb(ibbVehDetailsReq, Global.ibb_base_url + IBB_VEH_DETAILS_END_POINT).enqueue(this);
        } else {
            Toast.makeText(VehicleVariantActivity.this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        tvGivenVehModelVal = findViewById(R.id.tvGivenVehModelVal);
        tvGivenVehModelEdit = findViewById(R.id.tvGivenVehModelEdit);
        tvSelectedVehVariant = findViewById(R.id.tvSelectedVehVariant);
        btnNext = findViewById(R.id.btnNext);
        tvGivenVehModelVal.setText(strVehModel);
        lvVehListView = findViewById(R.id.lvVehListView);
        svVehVariantDetails = findViewById(R.id.svVehVariantDetails);
        tvSelectedVehVariant.setText(strVariant);
        tvGivenVehModelEdit.setOnClickListener(this);
        tvSelectedVehVariant.setOnClickListener(this);
        iv_app_variant_search = findViewById(R.id.iv_app_variant_search);
        iv_app_variant_search.setOnClickListener(this);
        lvVehListView.setDivider(null);
        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);
        btnNext.setOnClickListener(this);


        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
        ImageView magImage = (ImageView) svVehVariantDetails.findViewById(magId);
        magImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        int searchCloseButtonId = svVehVariantDetails.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        svCloseButton = this.svVehVariantDetails.findViewById(searchCloseButtonId);
        svCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    svVehVariantDetails.setQuery("", false);
                }
                svVehVariantDetails.clearFocus();
                svVehVariantDetails.setVisibility(View.GONE);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            svVehVariantDetails.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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
        }

    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.iv_vehDetails_back) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else */
        if (v.getId() == R.id.tvGivenVehModelEdit) {
            finish();
        } else if (v.getId() == R.id.iv_app_variant_search) {
            if (lvVehListView.getVisibility() == View.VISIBLE) {
                svVehVariantDetails.setVisibility(View.VISIBLE);
            } else {
                CommonMethods.showToast(this, "Please open the List of Variants");
            }
        } else if (v.getId() == R.id.tvSelectedVehVariant) {
            if (CommonMethods.isInternetWorking(VehicleVariantActivity.this)) {
                SpinnerManager.showSpinner(this);
                IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_VARIANT, CommonMethods.getStringValueFromKey(VehicleVariantActivity.this, "ibb_access_token"), CommonStrings.IBB_TAG, strYear, "0", strVehMake, strVehModel);
                retrofitInterface.getFromWeb(ibbVehDetailsReq, Global.ibb_base_url + IBB_VEH_DETAILS_END_POINT).enqueue(this);
            } else {
                Toast.makeText(VehicleVariantActivity.this, "Please check your Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.btnNext) {
            try{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    if (!tvSelectedVehVariant.getText().toString().isEmpty()) {
                        CommonStrings.customVehDetails.setVariant(tvSelectedVehVariant.getText().toString());

                    if(strVehCategory.equals(getResources().getString(R.string.new_car)))

                        startActivity(new Intent(this, InterestedVehiclePriceActivity.class));
                    }

                    try{ if(strVehCategory.equals(getResources().getString(R.string.old_car)))
                    {
                        startActivity(new Intent(this, VehicleOwnerActivity.class));
                    }}
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }
                    } else {
                        Toast.makeText(VehicleVariantActivity.this, "Please select Vehicle Variant", Toast.LENGTH_LONG).show();
                    }


            }catch(Exception exception){exception.printStackTrace();}

        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

        SpinnerManager.hideSpinner(VehicleVariantActivity.this);
        String strVariantRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strVariantRes);
        try {
            {
                VehVariantRes variantRes = new Gson().fromJson(strVariantRes, VehVariantRes.class);
                /*if (variantRes.getStatus() == 200) {*/
                    if (variantRes.getVariant() != null) {
                        generateListView(variantRes.getVariant());
                    } else {
                        Log.i(TAG, "onResponse: No Variants found");
                    }
                /*}*/
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            CommonMethods.redirectToDashboard(this);

        }

    }

    private void generateListView(List<String> year) {
        lvVehListView.setVisibility(View.VISIBLE);
        vehicleDetailsAdapter = new VehicleDetailsAdapter(this, R.layout.custom_list_item_row, year, tvSelectedVehVariant, lvVehListView);
        lvVehListView.setAdapter(vehicleDetailsAdapter);
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
    @Override
    public void onBackPressed() {
    }
}