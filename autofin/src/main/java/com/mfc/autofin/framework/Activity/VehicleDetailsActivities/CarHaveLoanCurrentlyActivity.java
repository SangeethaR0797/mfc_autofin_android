package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

public class CarHaveLoanCurrentlyActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenVehOwnership, tvGivenVehOwnershipVal, tvGivenVehOwnershipEdit;
    Button btnCarHaveLoan, btnCarHaveNoLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carhave_loan_currently);
        initView();
    }

    private void initView() {
        tvGivenVehOwnership = findViewById(R.id.tvGivenVehOwnership);
        tvGivenVehOwnershipVal = findViewById(R.id.tvGivenVehOwnershipVal);
        tvGivenVehOwnershipEdit = findViewById(R.id.tvGivenVehOwnershipEdit);
        btnCarHaveLoan = findViewById(R.id.btnCarHaveLoan);
        btnCarHaveNoLoan = findViewById(R.id.btnCarHaveNoLoan);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenVehOwnershipEdit) {
            finish();
        } else if (v.getId() == R.id.btnCarHaveLoan) {
            Intent intent = new Intent(CarHaveLoanCurrentlyActivity.this, VehPostInspectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnCarHaveNoLoan) {
            Intent intent = new Intent(CarHaveLoanCurrentlyActivity.this, VehPostInspectionActivity.class);
            startActivity(intent);
        }
    }

}