package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import utility.CommonMethods;
import utility.CommonStrings;

public class CityMonthAndYearActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CityMonthAndYearActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvMovedToCity, tvWhenMovedToCityLbl;
    ImageView iv_residential_details_backBtn;
    LinearLayout llMonthAndYear;
    String strCity = "", strYear = "",strMovedToCCity="";
    DatePicker datePicker;
    Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moved_to_city_m_y);
        if (CommonStrings.customResDetails.getCustomerCity() != null)
            strCity = CommonStrings.customResDetails.getCustomerCity();

        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customResDetails.getMoveInCityYear()!=null && !CommonStrings.customResDetails.getMoveInCityYear().isEmpty())
            {
                strMovedToCCity= CommonStrings.customResDetails.getMoveInCityYear();
            }
        }
        initView();
    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvWhenMovedToCityLbl = findViewById(R.id.tvWhenMovedToCityLbl);
        tvMovedToCity = findViewById(R.id.tvMovedToCity);
        llMonthAndYear = findViewById(R.id.llMonthAndYear);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        iv_residential_details_backBtn = findViewById(R.id.iv_residential_details_backBtn);
        tvGivenPreviousVal.setText(strCity);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_residential_city));
        iv_residential_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        btnNext=findViewById(R.id.btnNext);
        tvWhenMovedToCityLbl.setOnClickListener(this);
        if(!strMovedToCCity.isEmpty())
        {
            tvMovedToCity.setText(strMovedToCCity);
        }
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
        }
        tvMovedToCity.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvWhenMovedToCityLbl) {
            showDatePickerDialog();
        }
        else if (v.getId() == R.id.btnNext) {
            if (tvGivenPreviousVal.getText().toString() != "" && tvMovedToCity.getText().toString() != "") {
                moveToNextScreen();
            } else {
                CommonMethods.showToast(this, "Please select Month and Year");
            }
        }
    }

    private void showDatePickerDialog() {
        Date todayDate = Calendar.getInstance().getTime();

        String day = (String) DateFormat.format("dd", todayDate);
        String monthNumber = (String) DateFormat.format("MM", todayDate);
        String year = (String) DateFormat.format("yyyy", todayDate);

        int cMonth = Integer.parseInt(monthNumber), cDay = Integer.parseInt(day), cYear = Integer.parseInt(year);

        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(CityMonthAndYearActivity.this,
                AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthName = new DateFormatSymbols().getMonths()[month];
                tvMovedToCity.setText(monthName + " " + year);
                moveToNextScreen();
            }
        }, cYear, cMonth, cDay) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            }
        };
        monthDatePickerDialog.setTitle("Select MONTH & YEAR");
        monthDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        monthDatePickerDialog.show();
    }

    private void moveToNextScreen() {
            CommonStrings.customResDetails.setMoveInCityYear(tvMovedToCity.getText().toString());
            startActivity(new Intent(this, CurrentResidenceMonthAndYearActivity.class));
    }

    @Override
    public void onBackPressed() {

    }
}