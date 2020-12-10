package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.ValuationReportBSD;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class CarHaveLoanCurrentlyActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CarHaveLoanCurrentlyActivity.class.getSimpleName();
    TextView tvGivenVehOwnership, tvGivenVehOwnershipVal, tvGivenVehOwnershipEdit;
    Button btnCarHaveLoan, btnCarHaveNoLoan;
    ImageView iv_vehDetails_back;
    String purchaseAmount = "", strCarHaveLoan = "";
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carhave_loan_currently);
        Log.i(TAG, "onCreate: "+CommonStrings.customVehDetails.getVehicleSellingPrice());
        double d=CommonStrings.customVehDetails.getVehicleSellingPrice();
        Log.i(TAG, "onCreate: "+d);
        purchaseAmount =CommonMethods.getFormattedString(d);
        Log.i(TAG, "onCreate: "+purchaseAmount);
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.customVehDetails.getDoesCarHaveLoan()) {
                strCarHaveLoan = "YES";
            } else {
                strCarHaveLoan = "NO";
            }
        }

        initView();
    }

    private void initView() {
        tvGivenVehOwnership = findViewById(R.id.tvGivenVehOwnership);
        tvGivenVehOwnershipVal = findViewById(R.id.tvGivenVehOwnershipVal);
        tvGivenVehOwnershipEdit = findViewById(R.id.tvGivenVehOwnershipEdit);
        btnCarHaveLoan = findViewById(R.id.btnCarHaveLoan);
        btnCarHaveNoLoan = findViewById(R.id.btnCarHaveNoLoan);
        iv_vehDetails_back = findViewById(R.id.iv_vehDetails_back);
        tvGivenVehOwnershipVal.setText(purchaseAmount);
        iv_vehDetails_back.setVisibility(View.INVISIBLE);
        btnNext = findViewById(R.id.btnNext);
        if (!strCarHaveLoan.isEmpty()) {
            if (strCarHaveLoan.equalsIgnoreCase("YES")) {
                CommonMethods.highLightSelectedButton(this, btnCarHaveLoan);
                CommonMethods.deHighLightButton(this, btnCarHaveNoLoan);
            } else if (strCarHaveLoan.equalsIgnoreCase("NO")) {
                CommonMethods.highLightSelectedButton(this, btnCarHaveNoLoan);
                CommonMethods.deHighLightButton(this, btnCarHaveLoan);
            }
        }
        tvGivenVehOwnershipEdit.setOnClickListener(this);
        btnCarHaveLoan.setOnClickListener(this);
        btnCarHaveNoLoan.setOnClickListener(this);
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenVehOwnershipEdit) {
            finish();
        } else if (v.getId() == R.id.btnCarHaveLoan) {
            CommonMethods.highLightSelectedButton(this, btnCarHaveLoan);
            CommonMethods.deHighLightButton(this, btnCarHaveNoLoan);
            strCarHaveLoan=btnCarHaveLoan.getText().toString();
        } else if (v.getId() == R.id.btnCarHaveNoLoan) {
            CommonMethods.highLightSelectedButton(this, btnCarHaveNoLoan);
            CommonMethods.deHighLightButton(this, btnCarHaveLoan);
            strCarHaveLoan=btnCarHaveNoLoan.getText().toString();
        } else if (v.getId() == R.id.btnNext) {
            if(!strCarHaveLoan.isEmpty())
            moveToNextPage();
            else
                CommonMethods.showToast(this,"Please select anyone option");

        }

    }

    private void moveToNextPage()
    {
        if(strCarHaveLoan.equalsIgnoreCase(btnCarHaveLoan.getText().toString()))
        {
            CommonStrings.customVehDetails.setDoesCarHaveLoan(true);
            Intent intent = new Intent(CarHaveLoanCurrentlyActivity.this, VehPostInspectionActivity.class);
            startActivity(intent);
        }
        else if(strCarHaveLoan.equalsIgnoreCase(btnCarHaveNoLoan.getText().toString()))
        {
            CommonStrings.customVehDetails.setDoesCarHaveLoan(false);
            Intent intent = new Intent(this, VehPostInspectionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }


}