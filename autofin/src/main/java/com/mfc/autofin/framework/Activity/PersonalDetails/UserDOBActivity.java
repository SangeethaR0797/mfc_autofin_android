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
import android.widget.Button;
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
    String strResidenceType = "",strDOB="";
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_d_o_b);
        if (!CommonStrings.customResDetails.getResidenceType().isEmpty()) {
            strResidenceType = CommonStrings.customResDetails.getResidenceType();
        }

        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customPersonalDetails.getBirthDate()!=null && !CommonStrings.customPersonalDetails.getBirthDate().isEmpty())
            {
                strDOB= CommonStrings.customPersonalDetails.getBirthDate().substring(0, 10);
            }
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
        btnNext=findViewById(R.id.btnNext);
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        if(!strDOB.isEmpty())
        {
            tvDOB.setText(strDOB);
        }
        iv_personal_details_backBtn.setOnClickListener(this);
        tvDOBLbl.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        if(CommonStrings.IS_OLD_LEAD)
        {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        }
        else
        {
            btnNext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            CommonMethods.redirectToDashboard(this);
            CommonMethods.clearData();
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvDOBLbl) {
            showDatePickerDialog();
        }else if (v.getId() == R.id.btnNext) {
            if(!strDOB.isEmpty())
            {
                moveToNextScreen();
            }
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
                    strDOB=tvDOB.getText().toString();
                    if(!CommonStrings.IS_OLD_LEAD)
                    moveToNextScreen();
                }

            }
        }, cYear, cMonth, cDay);

        dobDatePicker.getDatePicker().setMaxDate(c.getTimeInMillis());
        dobDatePicker.show();
    }


    private void moveToNextScreen() {
        CommonStrings.customPersonalDetails.setBirthDate(strDOB);
        Intent intent = new Intent(this, GenderActivity.class);
        //intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL,tvDOBLbl.getText());
        //intent.putExtra(CommonStrings.PREVIOUS_VALUE,tvDOB.getText());
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        CommonMethods.redirectToDashboard(UserDOBActivity.this);
    }
}