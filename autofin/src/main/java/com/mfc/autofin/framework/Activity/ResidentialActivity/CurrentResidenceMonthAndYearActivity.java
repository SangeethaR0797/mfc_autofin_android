package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import utility.CommonMethods;
import utility.CommonStrings;

public class CurrentResidenceMonthAndYearActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvMovedToCRes, tvWhenMovedToCurrentCityLbl;
    LinearLayout llMonthAndYearMovedToCRes;
    ImageView iv_residential_details_backBtn;
    String strMYofCCity = "", strYOfCCity = "";
    int crYear = 0000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moved_to_current_city);
        if (CommonMethods.getStringValueFromKey(this, CommonStrings.MOVED_TO_CCITY).length() > 0) {
            strMYofCCity = CommonMethods.getStringValueFromKey(this, CommonStrings.MOVED_TO_CCITY);
            strYOfCCity = getMYear(strMYofCCity);
        } else {
            strMYofCCity = "";
        }
        initView();
    }

    private String getMYear(String strMYofCCity) {
        String[] arr = strMYofCCity.split("\\s");
        String year = arr[1];
        return year;
    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvMovedToCRes = findViewById(R.id.tvMovedToCRes);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_when_did_you_moved_to_the_city));
        tvWhenMovedToCurrentCityLbl = findViewById(R.id.tvWhenMovedToCurrentCityLbl);
        llMonthAndYearMovedToCRes = findViewById(R.id.llMonthAndYearMovedToCRes);
        iv_residential_details_backBtn = findViewById(R.id.iv_residential_details_backBtn);
        tvGivenPreviousVal.setText(strMYofCCity);
        iv_residential_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        tvWhenMovedToCurrentCityLbl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        /*if (v.getId() == R.id.iv_residential_details_backBtn) {
            finish();
        } else*/
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvWhenMovedToCurrentCityLbl) {
            showDatePickerDialog();
        }
    }

    private void showDatePickerDialog() {
        Date todayDate = Calendar.getInstance().getTime();
        String day = (String) DateFormat.format("dd", todayDate);
        String monthNumber = (String) DateFormat.format("MM", todayDate);
        String year = (String) DateFormat.format("yyyy", todayDate);

        int cMonth = Integer.parseInt(monthNumber), cDay = Integer.parseInt(day), cYear = Integer.parseInt(year);

        DatePickerDialog monthDatePickerDialog = new DatePickerDialog(CurrentResidenceMonthAndYearActivity.this,
                AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String monthName = new DateFormatSymbols().getMonths()[month];
                crYear = year;
                tvMovedToCRes.setText(monthName + " " + year);
                moveToNextScreen();
            }
        }, cYear, cMonth, cDay) {
            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
            }
        };
        monthDatePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        monthDatePickerDialog.setTitle("Select MONTH & YEAR");
        monthDatePickerDialog.show();
    }

    private void moveToNextScreen() {
        if (tvGivenPreviousVal.getText().toString() != "" && tvMovedToCRes.getText().toString() != "") {
            try {
                if (crYear >= Integer.parseInt(strYOfCCity)) {
                    CommonMethods.setValueAgainstKey(this, CommonStrings.MOVED_TO_CRESIDENCE, tvMovedToCRes.getText().toString());
                    startActivity(new Intent(this, ResidenceTypeActivity.class));
                } else {
                    CommonMethods.showToast(this, "Selected Year should be Same or Later");
                }
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            } catch (Exception exception) {

            }

        } else {
            CommonMethods.showToast(this, "Please select Month and Year");
        }
    }
}