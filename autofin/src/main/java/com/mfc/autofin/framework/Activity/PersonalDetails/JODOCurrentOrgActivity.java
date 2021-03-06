package com.mfc.autofin.framework.Activity.PersonalDetails;

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

import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.IndustryTypeActivity;
import com.mfc.autofin.framework.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import model.personal_details_models.IndustryType;
import utility.CommonStrings;

@SuppressLint("NewApi")
public class JODOCurrentOrgActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvJODOCurrentOrgLbl, tvCurrentOrgJoiningDate;
    private String strPreviousLbl = "",strPreviousVal="", strJoiningDate = "";
    LinearLayout llJODCalendarView;
    DatePickerDialog jodoCurrentOrg;
    private Intent intent;
    private Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_o_d_o_current_org);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.cusEmpDetails.getCompanyJoiningDate()!=null && !CommonStrings.cusEmpDetails.getCompanyJoiningDate().isEmpty())
            {
                strJoiningDate=CommonStrings.cusEmpDetails.getCompanyJoiningDate().substring(0,10);
            }
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
        btnNext=findViewById(R.id.btnNext);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        if(!strJoiningDate.isEmpty())
        {
            tvCurrentOrgJoiningDate.setText(strJoiningDate);
        }
        tvGivenValEdit.setOnClickListener(this);
        tvJODOCurrentOrgLbl.setOnClickListener(this);
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
        }

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
        }else if (v.getId() == R.id.btnNext) {
            if(!strJoiningDate.isEmpty())
            {
                moveToNextPage();
            }
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
                if(!CommonStrings.IS_OLD_LEAD)
                {
                    moveToNextPage();
                }
            }
        }, cYear, cMonth, cDay);

        jodoCurrentOrg.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        jodoCurrentOrg.show();
    }
    private void moveToNextPage()
    {
        CommonStrings.cusEmpDetails.setCompanyJoiningDate(strJoiningDate);
        Intent intent = new Intent(JODOCurrentOrgActivity.this, IndustryTypeActivity.class);
        intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvJODOCurrentOrgLbl.getText().toString());
        intent.putExtra(CommonStrings.PREVIOUS_VALUE, strJoiningDate);
        startActivity(intent);
    }

}