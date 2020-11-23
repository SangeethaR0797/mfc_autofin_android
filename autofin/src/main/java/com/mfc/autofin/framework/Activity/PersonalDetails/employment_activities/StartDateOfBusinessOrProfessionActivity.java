package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import model.bank_models.InterestedBankOfferRes;
import utility.CommonMethods;
import utility.CommonStrings;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class StartDateOfBusinessOrProfessionActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvStartDateOfBOPLbl, tvStartDateOfBOPVal;
    private String strEmpType = "", strPreviousLbl = "", strPreviousVal = "", strStartedDate = "";
    LinearLayout llStartDateOfBOP, llBusinessCalendarView;
    private Intent intent;
    DatePickerDialog startDateOfBOP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_date_of_business);

        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (CommonStrings.cusEmpDetailsModel.getEmpType() != null && !CommonStrings.cusEmpDetailsModel.getEmpType().isEmpty()) {
            strEmpType = CommonStrings.cusEmpDetailsModel.getEmpType();
        }
        initView();
    }

    @SuppressLint("NewApi")
    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvStartDateOfBOPLbl = findViewById(R.id.tvStartDateOfBOPLbl);
        tvStartDateOfBOPVal = findViewById(R.id.tvStartDateOfBOPValue);
        llStartDateOfBOP = findViewById(R.id.llStartDateOfBOP);
        llBusinessCalendarView = findViewById(R.id.llBusinessCalendarView);
        if (strEmpType.equals(getResources().getString(R.string.lbl_business_owner))) {
            tvStartDateOfBOPLbl.setText(getResources().getString(R.string.lbl_starting_date_of_business));
        } else {
            tvStartDateOfBOPLbl.setText(getResources().getString(R.string.lbl_starting_date_of_profession));
        }
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        tvStartDateOfBOPLbl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvStartDateOfBOPLbl) {
            //llBusinessCalendarView.setVisibility(View.VISIBLE);
            showDatePickerDialog();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void showDatePickerDialog() {
        Date todayDate = Calendar.getInstance().getTime();

        String day = (String) DateFormat.format("dd", todayDate);
        String monthNumber = (String) DateFormat.format("MM", todayDate);
        String year = (String) DateFormat.format("yyyy", todayDate);

        int cMonth = Integer.parseInt(monthNumber), cDay = Integer.parseInt(day), cYear = Integer.parseInt(year);

        startDateOfBOP = new DatePickerDialog(StartDateOfBusinessOrProfessionActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthName = new DateFormatSymbols().getMonths()[month];
                strStartedDate = dayOfMonth + " " + monthName + " " + year;
                tvStartDateOfBOPVal.setText(strStartedDate);
                //CommonMethods.setValueAgainstKey(this,CommonStrings.BUSINESS_OR_PROFESSION_START_DATE,dayOfMonth + " " + monthName + " " + year);
                CommonStrings.cusEmpDetailsModel.setbOPStartDate(dayOfMonth + " " + monthName + " " + year);
                Intent intent = new Intent(StartDateOfBusinessOrProfessionActivity.this, LastYearSalesOrTurnOver.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvStartDateOfBOPLbl.getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, CommonStrings.cusEmpDetailsModel.getbOPStartDate());
                startActivity(intent);

            }
        }, cYear, cMonth, cDay);

        startDateOfBOP.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        startDateOfBOP.show();
    }

}
