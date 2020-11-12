package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import android.content.Intent;
import android.os.Bundle;
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

    TextView tvGivenVehOwnership, tvGivenVehOwnershipVal, tvGivenVehOwnershipEdit;
    Button btnCarHaveLoan, btnCarHaveNoLoan;
    ImageView iv_vehDetails_backBtn;
    String purchaseAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carhave_loan_currently);
        purchaseAmount = CommonMethods.getStringValueFromKey(this,CommonStrings.VEH_PURCHASE_AMOUNT);
        initView();
    }

    private void initView() {
        tvGivenVehOwnership = findViewById(R.id.tvGivenVehOwnership);
        tvGivenVehOwnershipVal = findViewById(R.id.tvGivenVehOwnershipVal);
        tvGivenVehOwnershipEdit = findViewById(R.id.tvGivenVehOwnershipEdit);
        btnCarHaveLoan = findViewById(R.id.btnCarHaveLoan);
        btnCarHaveNoLoan = findViewById(R.id.btnCarHaveNoLoan);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvGivenVehOwnershipVal.setText(purchaseAmount);
        iv_vehDetails_backBtn.setOnClickListener(this);
        tvGivenVehOwnershipEdit.setOnClickListener(this);
        btnCarHaveLoan.setOnClickListener(this);
        btnCarHaveNoLoan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));

        } else if (v.getId() == R.id.tvGivenVehOwnershipEdit) {
            finish();
        } else if (v.getId() == R.id.btnCarHaveLoan) {
            CommonMethods.highLightSelectedButton(this, btnCarHaveLoan);
            CommonMethods.deHighLightButton(this, btnCarHaveNoLoan);
            CommonStrings.customVehDetails.setDoesCarHaveLoan(true);
            /*ValuationReportBSD valuationReportBSD = new ValuationReportBSD(this);
            valuationReportBSD.show(getSupportFragmentManager(), "Validation Report");*/
            Intent intent = new Intent(CarHaveLoanCurrentlyActivity.this, VehPostInspectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.btnCarHaveNoLoan) {
            CommonMethods.highLightSelectedButton(this, btnCarHaveNoLoan);
            CommonMethods.deHighLightButton(this, btnCarHaveLoan);
            CommonStrings.customVehDetails.setDoesCarHaveLoan(false);
            Intent intent = new Intent(this, VehPostInspectionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Done!!!", Toast.LENGTH_SHORT).show();
    }
}