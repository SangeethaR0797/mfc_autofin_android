package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import controller.VehicleDetailsAdapter;
import model.ibb_models.IBBVehDetailsReq;
import model.ibb_models.VehRegYearRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.IBB_VEH_DETAILS_END_POINT;
import static utility.CommonStrings.IBB_YEAR;
import static utility.CommonStrings.VEH_CATEGORY_URL;

public class VehRegistrationYear extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = VehRegistrationYear.class.getSimpleName();
    private TextView tvGivenRegNumLbl, tvGivenRegNoVal, tvVehRegNumEdit, tvRegYearLbl, tvRegYear;
    private ImageView iv_year_search, svCloseButton;
    ListView lvVehListView;
    String regNoVal = "", strYear = "";
    SearchView svVehDetails;
    VehicleDetailsAdapter vehicleDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_registration_year);
        regNoVal = CommonMethods.getStringValueFromKey(VehRegistrationYear.this, CommonStrings.VEH_REG_NO);
        if (CommonStrings.stockResData != null) {
            if (CommonStrings.stockResData.getYear() != null)
                strYear = String.valueOf(CommonStrings.stockResData.getYear());
            else
                strYear = "";
        }
        initView();

    }

    private void initView() {
        tvGivenRegNumLbl = findViewById(R.id.tvGivenRegNumLbl);
        tvGivenRegNoVal = findViewById(R.id.tvGivenRegNoVal);
        tvVehRegNumEdit = findViewById(R.id.tvVehRegNumEdit);
        tvRegYearLbl = findViewById(R.id.tvRegYearLbl);
        tvRegYear = findViewById(R.id.tvRegYear);
        iv_year_search = findViewById(R.id.iv_year_search);
        lvVehListView = findViewById(R.id.lvVehListView);
        svVehDetails = findViewById(R.id.svVehDetails);
        tvGivenRegNoVal.setText(regNoVal);
        lvVehListView.setDivider(null);
        tvRegYear.setText(strYear);


        int searchCloseButtonId = svVehDetails.getContext().getResources()
                .getIdentifier("android:id/search_close_btn", null, null);

        svCloseButton = this.svVehDetails.findViewById(searchCloseButtonId);
        svCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                svVehDetails.setQuery("", false);
                svVehDetails.clearFocus();
                svVehDetails.setVisibility(View.GONE);
            }
        });

        svVehDetails.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

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

        tvVehRegNumEdit.setOnClickListener(this);
        tvRegYear.setOnClickListener(this);
        iv_year_search.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvVehRegNumEdit) {
            Intent intent = new Intent(VehRegistrationYear.this, VehRegNumAns.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tvRegYear) {
            if (CommonMethods.isInternetWorking(VehRegistrationYear.this)) {
                SpinnerManager.showSpinner(VehRegistrationYear.this);
                IBBVehDetailsReq ibbVehDetailsReq = new IBBVehDetailsReq(CommonStrings.IBB_YEAR, CommonMethods.getStringValueFromKey(VehRegistrationYear.this, "ibb_access_token"), CommonStrings.IBB_TAG);
                retrofitInterface.getFromWeb(ibbVehDetailsReq, Global_URLs.IBB_BASE_URL + IBB_VEH_DETAILS_END_POINT).enqueue(this);
            } else {
                Toast.makeText(VehRegistrationYear.this, "PLease check your Interner Connection", Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.iv_year_search) {
            svVehDetails.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(VehRegistrationYear.this, AutoFinDashBoardActivity.class);
            startActivity(intent);
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

    /*void onItemClick(AdapterView<?> adapterView, View view, int i, long l)     {

        String Exercise = String.valueOf(adapterView.getItemAtPosition(i));
        result = (textView3.getText().toString() + "\n" + Exercise;
        textView3.setText(result);

    }
*/
}