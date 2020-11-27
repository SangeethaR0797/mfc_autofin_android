package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import utility.CommonMethods;
import utility.CommonStrings;

@SuppressLint("NewApi")
public class UserDOBActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvDOBLbl, tvDOB;
    LinearLayout llDOBCalendarView;
    ImageView iv_personal_details_backBtn;
    DatePickerDialog dobDatePicker;
    DatePicker datePicker;
    CalendarView cvUserDOB;
    String strResidenceType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_d_o_b);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.RESIDENCE_TYPE).isEmpty()) {
            strResidenceType = CommonMethods.getStringValueFromKey(this, CommonStrings.RESIDENCE_TYPE);
        }
        initView();
    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_residence_type).toUpperCase());
        tvDOBLbl = findViewById(R.id.tvDOBLbl);
        tvGivenPreviousVal.setText(strResidenceType);
        datePicker = findViewById(R.id.dobDatePicker);
        llDOBCalendarView = findViewById(R.id.llDOBCalendarView);
        tvDOB = findViewById(R.id.tvDOB);
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvDOBLbl.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(UserDOBActivity.this, AutoFinDashBoardActivity.class));
            CommonMethods.clearData();
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvDOBLbl) {
            showDatePickerDialog();
        }
    }


    private void showDatePickerDialog() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -18);
        Date todayDate = c.getTime();

        String day = (String) DateFormat.format("dd", todayDate);
        String monthNumber = (String) DateFormat.format("MM", todayDate);
        String year = (String) DateFormat.format("yyyy", todayDate);

        int cMonth = Integer.parseInt(monthNumber),
                cDay = Integer.parseInt(day),
                cYear = Integer.parseInt(year);

        dobDatePicker = new DatePickerDialog(UserDOBActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String monthName = new DateFormatSymbols().getMonths()[month];
                Calendar userAge = new GregorianCalendar(year, month, dayOfMonth);
                Calendar minAdultAge = new GregorianCalendar();
                minAdultAge.add(Calendar.YEAR, -18);
                if (minAdultAge.before(userAge)) {
                    CommonMethods.showToast(UserDOBActivity.this, "Please enter proper Date of Birth");
                } else {
                    tvDOB.setText(dayOfMonth + " " + monthName + " " + year);
                    moveToNextScreen();
                }

            }
        }, cYear, cMonth, cDay);

        dobDatePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        dobDatePicker.show();
    }


    private void moveToNextScreen() {
        CommonMethods.setValueAgainstKey(this, CommonStrings.USER_DOB, tvDOB.getText().toString());
        Intent intent = new Intent(this, GenderActivity.class);
        //intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL,tvDOBLbl.getText());
        //intent.putExtra(CommonStrings.PREVIOUS_VALUE,tvDOB.getText());
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}