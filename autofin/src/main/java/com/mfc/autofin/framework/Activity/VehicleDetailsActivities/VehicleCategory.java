package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

import java.util.List;

import model.vehicle_details.vehicle_category.Category;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;

public class VehicleCategory extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    TextView tvVehCategoryQn;
    RadioGroup rgVehCategory;
    RadioButton rbNewCar, rbOldCar;
    ImageView iv_vehDetails_back;
    private List<Category> vehicleCategoryList;
    Button btnNext;

    private String TAG = VehicleCategory.class.getSimpleName();
    private String vehCategory = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autofin_vehicle_category);
        Log.i(TAG, "onCreate: ");


        if (CommonStrings.IS_OLD_LEAD) {
            Log.i(TAG, "onCreate: " + CommonStrings.customLoanDetails.getLoanCategory());
            vehCategory = CommonStrings.customLoanDetails.getLoanCategory();
            Log.i(TAG, "initView: " + vehCategory);
        }

        initView();
    }

    private void initView() {

        tvVehCategoryQn = findViewById(R.id.tvVehCategoryQn);
        rgVehCategory = findViewById(R.id.rgVehCategory);
        rbOldCar = findViewById(R.id.rbOldCar);
        iv_vehDetails_back = findViewById(R.id.iv_vehDetails_back);
        iv_vehDetails_back.setOnClickListener(this);
        rbNewCar = findViewById(R.id.rbNewCar);
        btnNext = findViewById(R.id.btnNext);
        Log.i(TAG, "initView: " + vehCategory);

        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.INVISIBLE);
        }
        if (!vehCategory.isEmpty()) {
            if (vehCategory.equalsIgnoreCase(rbNewCar.getText().toString())) {
                Log.i(TAG, "initView: 2" + vehCategory);
                rbNewCar.setChecked(true);

            } else if (vehCategory.equalsIgnoreCase(rbOldCar.getText().toString())) {
                Log.i(TAG, "initView: 3" + vehCategory);
                rbOldCar.setChecked(true);

            }

        }
        if (btnNext.getVisibility() == View.VISIBLE) {
            btnNext.setOnClickListener(this);
        }
        rbOldCar.setOnClickListener(this);
        rbNewCar.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            finish();
        } else if (v.getId() == R.id.rbNewCar) {
            try {
                if (rbNewCar.isChecked()) {
                    vehCategory = rbNewCar.getText().toString();
                    CommonStrings.customLoanDetails.setLoanCategory(vehCategory);
                    CommonStrings.customVehDetails.setHaveVehicleNumber(false);
                    CommonStrings.customVehDetails.setVehicleNumber("NA");
                    Intent intent = new Intent(VehicleCategory.this, VehRegistrationYear.class);
                    startActivity(intent);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (v.getId() == R.id.rbOldCar) {

            try {
                if (rbOldCar.isChecked()) {
                    vehCategory = rbOldCar.getText().toString();
                    CommonStrings.customLoanDetails.setLoanCategory(vehCategory);
                    Intent intent = new Intent(VehicleCategory.this, VehRegNumActivity.class);
                    startActivity(intent);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (v.getId() == R.id.btnNext) {
            if (!vehCategory.isEmpty()) {
                CommonStrings.customLoanDetails.setLoanCategory(vehCategory);
                Intent intent = new Intent(VehicleCategory.this, VehRegNumActivity.class);
                startActivity(intent);
            }
            else
            {
               CommonMethods.showToast(this,"Please select anyone value");
            }
        }


    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
       /* String res = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + res);
        VehicleCategoryRes vehicleCategoryRes = new Gson().fromJson(res, VehicleCategoryRes.class);
        try {
            if (vehicleCategoryRes != null) {
                if (vehicleCategoryRes.getData() != null) {
                    VehData vehData = vehicleCategoryRes.getData();
                    vehicleCategoryList.addAll(vehData.getCategories());
                    setVehicleCategoryList();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
*/

    }

    private void setVehicleCategoryList() {
        for (int i = 0; i < vehicleCategoryList.size(); i++) {
            RadioButton radioButton = new RadioButton(VehicleCategory.this, null, R.style.auto_fin_tab_label_style);
            rbOldCar.setText(vehicleCategoryList.get(i).getDisplayLabel());
            final RadioButton[] rb = new RadioButton[5];
            RadioGroup rg = new RadioGroup(this); //create the RadioGroup
            rg.setOrientation(RadioGroup.HORIZONTAL);//or RadioGroup.VERTICAL
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}