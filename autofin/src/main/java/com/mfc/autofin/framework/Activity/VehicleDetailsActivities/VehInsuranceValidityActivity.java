package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import utility.CommonMethods;
import utility.CommonStrings;

public class VehInsuranceValidityActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {


    TextView tvGivenInsurance, tvGivenVehInsuranceVal, tvGivenInsuranceAmountEdit, tvInsuranceValidityLbl, tvInsuranceValidityDate;
    ImageView iv_vehDetails_backBtn;
    String strInsurance = "", strInsuranceAmount = "", strInsuranceValidity = "";
    Calendar insuranceCal = Calendar.getInstance();
    CalendarView cvInsuranceValidity;
    LinearLayout llCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_insurance);

        if (CommonStrings.customVehDetails.getInsurance()) {
            strInsurance = "Yes";
            if (CommonStrings.customVehDetails.getInsuranceAmount().equals("0")) {
                strInsuranceAmount = CommonStrings.customVehDetails.getInsuranceAmount();

            } else {
                strInsurance = "No";
            }
        } else {
            strInsurance = "No";
        }
        if (CommonStrings.stockResData != null) {
            if (CommonStrings.stockResData.getInsuranceValidity() != null) {
                strInsuranceValidity = CommonStrings.stockResData.getInsurance();
            }
        } else {
            strInsuranceValidity = "dd / mm / yyyy";
        }

        initView();
    }

    private void initView() {
        tvGivenInsurance = findViewById(R.id.tvGivenInsurance);
        tvGivenVehInsuranceVal = findViewById(R.id.tvGivenVehInsuranceVal);
        tvGivenInsuranceAmountEdit = findViewById(R.id.tvGivenInsuranceAmountEdit);
        tvInsuranceValidityLbl = findViewById(R.id.tvInsuranceValidityLbl);
        tvInsuranceValidityDate = findViewById(R.id.tvInsuranceValidityDate);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        cvInsuranceValidity = findViewById(R.id.cvInsuranceValidity);
        llCalendarView = findViewById(R.id.llCalendarView);
        tvGivenVehInsuranceVal.setText(strInsurance + " ( " + getString(R.string.rupees_symbol) + " " + strInsuranceAmount + " ) ");
        llCalendarView.setOnClickListener(this);
        iv_vehDetails_backBtn.setOnClickListener(this);
        tvGivenInsuranceAmountEdit.setOnClickListener(this);
        tvInsuranceValidityLbl.setOnClickListener(this);
        tvInsuranceValidityDate.setText(strInsuranceValidity);
        cvInsuranceValidity.setMinDate(System.currentTimeMillis() - 1000);
        cvInsuranceValidity.setOnDateChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenInsuranceAmountEdit) {
            finish();
        } else if (v.getId() == R.id.tvInsuranceValidityLbl) {
            llCalendarView.setVisibility(View.VISIBLE);
            //showDatePickerDialog();
        }
    }


    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String monthName = new SimpleDateFormat("MMMM").format(view.getDate());
        String insValidityDate = dayOfMonth + " " + monthName + " " + year;
        tvInsuranceValidityDate.setText(insValidityDate);
        CommonStrings.customVehDetails.setInsuranceValidity(dayOfMonth + " " + monthName + " " + year);
        Intent intent = new Intent(VehInsuranceValidityActivity.this, InsuranceTypeActivity.class);
        startActivity(intent);
    }
}