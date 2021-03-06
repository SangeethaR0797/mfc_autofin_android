package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.LastYearProfitActivity;
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
    Button btnNext;

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

        if (CommonStrings.cusEmpDetails.getEmploymentType() != null && !CommonStrings.cusEmpDetails.getEmploymentType().isEmpty()) {
            strEmpType = CommonStrings.cusEmpDetails.getEmploymentType();
        }

        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.cusEmpDetails.getBusinessStartDate() != null && !CommonStrings.cusEmpDetails.getBusinessStartDate().isEmpty()) {
                strStartedDate = CommonStrings.cusEmpDetails.getBusinessStartDate();
            }
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
        btnNext = findViewById(R.id.btnNext);
        if (strEmpType.equals(getResources().getString(R.string.lbl_business_owner))) {
            tvStartDateOfBOPLbl.setText(getResources().getString(R.string.lbl_starting_date_of_business));
        } else {
            tvStartDateOfBOPLbl.setText(getResources().getString(R.string.lbl_starting_date_of_profession));
        }
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        if (!strStartedDate.isEmpty()) {
            tvStartDateOfBOPVal.setText(strStartedDate);
        }
        tvStartDateOfBOPLbl.setOnClickListener(this);

        if(!CommonStrings.IS_OLD_LEAD)
        {
            btnNext.setVisibility(View.VISIBLE);
            if(btnNext.getVisibility()==View.VISIBLE)
            {
                btnNext.setOnClickListener(this);
            }
        }
        else
        {
            btnNext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvStartDateOfBOPLbl) {
            showDatePickerDialog();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!strStartedDate.isEmpty())
            {
                moveToNextPage();
            }
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
                if (!CommonStrings.IS_OLD_LEAD) {
                    moveToNextPage();
                }
            }
        }, cYear, cMonth, cDay);

        startDateOfBOP.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        startDateOfBOP.show();
    }

    private void moveToNextPage() {
        CommonStrings.cusEmpDetails.setBusinessStartDate(strStartedDate);
        Intent intent = new Intent(StartDateOfBusinessOrProfessionActivity.this, LastYearProfitActivity.class);
        intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvStartDateOfBOPLbl.getText().toString());
        intent.putExtra(CommonStrings.PREVIOUS_VALUE, strStartedDate);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
