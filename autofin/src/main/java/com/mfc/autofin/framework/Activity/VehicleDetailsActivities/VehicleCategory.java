package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import utility.CommonStrings;

public class VehicleCategory extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    TextView tvVehCategoryQn;
    RadioGroup rgVehCategory;
    RadioButton rbNewCar, rbOldCar;
    ImageView iv_vehDetails_backBtn;
    private List<Category> vehicleCategoryList;

    private String TAG = VehicleCategory.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_category);
        Log.i(TAG, "onCreate: ");
        initView();
         //retrofitInterface.getFromWeb(VEH_CATEGORY_URL).enqueue(this);

    }

    private void initView() {

        tvVehCategoryQn = findViewById(R.id.tvVehCategoryQn);
        rgVehCategory = findViewById(R.id.rgVehCategory);
        rbOldCar = findViewById(R.id.rbOldCar);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        iv_vehDetails_backBtn.setOnClickListener(this);
        rbNewCar = findViewById(R.id.rbNewCar);
        rbOldCar.setOnClickListener(this);
        rbNewCar.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_back) {
            finish();
        } else if (v.getId() == R.id.rbNewCar) {
            if (rbNewCar.isChecked()) {
                CommonStrings.customVehDetails.setVehCategory(rbNewCar.getText().toString());
                Intent intent = new Intent(VehicleCategory.this, VehRegNumActivity.class);
                startActivity(intent);
            }
        } else if (v.getId() == R.id.rbOldCar) {
            if (rbOldCar.isChecked()) {
                CommonStrings.customVehDetails.setVehCategory(rbOldCar.getText().toString());
                Intent intent = new Intent(VehicleCategory.this, VehRegNumActivity.class);
                startActivity(intent);
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
}