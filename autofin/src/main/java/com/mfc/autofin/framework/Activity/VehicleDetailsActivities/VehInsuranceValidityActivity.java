package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.LastYearSalesOrTurnOver;
import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.StartDateOfBusinessOrProfessionActivity;
import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import utility.CommonMethods;
import utility.CommonStrings;

@SuppressLint("NewApi")
public class VehInsuranceValidityActivity extends AppCompatActivity implements View.OnClickListener {


    TextView tvGivenInsurance, tvGivenVehInsuranceVal, tvGivenInsuranceAmountEdit, tvInsuranceValidityLbl, tvInsuranceValidityDate;
    ImageView iv_vehDetails_backBtn;
    Calendar insuranceCal = Calendar.getInstance();
    LinearLayout llCalendarView;
    DatePickerDialog vehInsuranceDate;
    DatePicker vehInsurancePicker;
    String strInsuranceValidity = "", strInsuranceType = "";
    Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_insurance);


        if (CommonStrings.stockResData != null) {
            if (CommonStrings.stockResData.getInsuranceValidity() != null) {
                strInsuranceValidity = CommonStrings.stockResData.getInsurance();
            }
        }
        if (CommonStrings.customVehDetails != null) {
            if (CommonStrings.customVehDetails.getInsuranceType() != null) {
                strInsuranceType = CommonStrings.customVehDetails.getInsuranceType();
            }
        } else {
            strInsuranceType = "";
        }

        if(CommonStrings.IS_OLD_LEAD)
        {
            if (!CommonStrings.customVehDetails.getInsuranceValidity().isEmpty())
            {
                strInsuranceValidity=CommonStrings.customVehDetails.getInsuranceValidity().substring(0, 10);
            }
        }
        initView();
    }

    @SuppressLint("NewApi")
    private void initView() {
        tvGivenInsurance = findViewById(R.id.tvGivenInsurance);
        tvGivenVehInsuranceVal = findViewById(R.id.tvGivenVehInsuranceVal);
        tvGivenInsuranceAmountEdit = findViewById(R.id.tvGivenInsuranceAmountEdit);
        tvInsuranceValidityLbl = findViewById(R.id.tvInsuranceValidityLbl);
        tvInsuranceValidityDate = findViewById(R.id.tvInsuranceValidityDate);
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_back);
        llCalendarView = findViewById(R.id.llCalendarView);
        vehInsurancePicker = findViewById(R.id.vehInsurancePicker);
        btnNext=findViewById(R.id.btnNext);
        tvGivenVehInsuranceVal.setText(strInsuranceType);
        llCalendarView.setOnClickListener(this);
        iv_vehDetails_backBtn.setVisibility(View.INVISIBLE);
        tvGivenInsuranceAmountEdit.setOnClickListener(this);
        tvInsuranceValidityLbl.setOnClickListener(this);
        if(!strInsuranceValidity.isEmpty())
        {
            tvInsuranceValidityDate.setText(strInsuranceValidity);
        }
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenInsuranceAmountEdit) {
            finish();
        } else if (v.getId() == R.id.tvInsuranceValidityLbl) {
            showDatePickerDialog();
        }
        else if (v.getId() == R.id.btnNext) {
            if (!strInsuranceValidity.isEmpty()) {
                moveToNextPage();
            }
            else
            {
                CommonMethods.showToast(this,"Please select anyone option");
            }
        }
    }
    private void showDatePickerDialog() {
        Date todayDate = Calendar.getInstance().getTime();

        String day = (String) DateFormat.format("dd", todayDate);
        String monthNumber = (String) DateFormat.format("MM", todayDate);
        String year = (String) DateFormat.format("yyyy", todayDate);

        int cMonth = Integer.parseInt(monthNumber), cDay = Integer.parseInt(day), cYear = Integer.parseInt(year);

        vehInsuranceDate = new DatePickerDialog(VehInsuranceValidityActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthName = new DateFormatSymbols().getMonths()[month];
                String insValidityDate = dayOfMonth + "-" + monthName + "-" + year;
                tvInsuranceValidityDate.setText(insValidityDate);
                strInsuranceValidity=dayOfMonth + " " + monthName + " " + year;
                if(!CommonStrings.IS_OLD_LEAD)
                    moveToNextPage();
            }
        }, cYear, cMonth, cDay);
        vehInsuranceDate.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        vehInsuranceDate.show();
    }

    @Override
    public void onBackPressed() {
    }

    private void moveToNextPage()
    {
            CommonStrings.customVehDetails.setInsuranceValidity(strInsuranceValidity);
            Intent intent = new Intent(VehInsuranceValidityActivity.this, BasicDetailsActivity.class);
            startActivity(intent);

    }

}