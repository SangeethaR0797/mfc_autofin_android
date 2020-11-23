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
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehInsuranceValidityActivity;
import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import utility.CommonMethods;
import utility.CommonStrings;

@SuppressLint("NewApi")
public class JODOCurrentOrgActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvJODOCurrentOrgLbl, tvCurrentOrgJoiningDate;
    private String strOrgName = "", strJoiningDate = "";
    LinearLayout llJODCalendarView;
    DatePickerDialog jodoCurrentOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_o_d_o_current_org);
        if (!CommonStrings.cusEmpDetailsModel.getEmpOrgName().isEmpty()) {
            strOrgName = CommonStrings.cusEmpDetailsModel.getEmpOrgName();
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvCurrentOrgJoiningDate = findViewById(R.id.tvCurrentOrgJoiningDate);
        tvJODOCurrentOrgLbl = findViewById(R.id.tvJODOCurrentOrgLbl);
        llJODCalendarView = findViewById(R.id.llJODCalendarView);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_working_organization_name));
        tvGivenPreviousVal.setText(strOrgName);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        tvJODOCurrentOrgLbl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else*/
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvJODOCurrentOrgLbl) {
            showDatePickerDialog();
        }

    }

    private void showDatePickerDialog() {
        Date todayDate = Calendar.getInstance().getTime();

        String day = (String) DateFormat.format("dd", todayDate);
        String monthNumber = (String) DateFormat.format("MM", todayDate);
        String year = (String) DateFormat.format("yyyy", todayDate);

        int cMonth = Integer.parseInt(monthNumber), cDay = Integer.parseInt(day), cYear = Integer.parseInt(year);

        jodoCurrentOrg = new DatePickerDialog(JODOCurrentOrgActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthName = new DateFormatSymbols().getMonths()[month];
                strJoiningDate = dayOfMonth + " " + monthName + " " + year;
                tvCurrentOrgJoiningDate.setText(strJoiningDate);
                CommonStrings.cusEmpDetailsModel.setOrgJoiningDate(dayOfMonth + " " + monthName + " " + year);
                Intent intent = new Intent(JODOCurrentOrgActivity.this, YearOfExperienceActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvJODOCurrentOrgLbl.getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, strJoiningDate);
                startActivity(intent);
            }
        }, cYear, cMonth, cDay);

        jodoCurrentOrg.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        jodoCurrentOrg.show();
    }

}