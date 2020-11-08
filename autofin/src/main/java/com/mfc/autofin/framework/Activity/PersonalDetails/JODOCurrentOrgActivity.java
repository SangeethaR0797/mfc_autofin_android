package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehInsuranceValidityActivity;
import com.mfc.autofin.framework.R;

import java.text.SimpleDateFormat;

import utility.CommonMethods;
import utility.CommonStrings;

public class JODOCurrentOrgActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {


    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvJODOCurrentOrgLbl,tvCurrentOrgJoiningDate;
    private String strOrgName="",strJoiningDate="";
    CalendarView cvJoiningDateOfCurrentOrg;
    LinearLayout llJODCalendarView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_j_o_d_o_current_org);
        if(!CommonMethods.getStringValueFromKey(this, CommonStrings.CURRENT_ORG_NAME).isEmpty())
        {
            strOrgName=CommonMethods.getStringValueFromKey(this, CommonStrings.CURRENT_ORG_NAME);
        }
            initView();
    }

    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvCurrentOrgJoiningDate=findViewById(R.id.tvCurrentOrgJoiningDate);
        tvJODOCurrentOrgLbl=findViewById(R.id.tvJODOCurrentOrgLbl);
        cvJoiningDateOfCurrentOrg=findViewById(R.id.cvJoiningDateOfCurrentOrg);
        llJODCalendarView=findViewById(R.id.llJODCalendarView);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_working_organization_name));
        tvGivenPreviousVal.setText(strOrgName);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        cvJoiningDateOfCurrentOrg.setMinDate(System.currentTimeMillis() - 1000);
        cvJoiningDateOfCurrentOrg.setOnDateChangeListener(this);
        tvJODOCurrentOrgLbl.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.tvJODOCurrentOrgLbl)
        {
            llJODCalendarView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String monthName = new SimpleDateFormat("MMMM").format(view.getDate());
        strJoiningDate = dayOfMonth + " " + monthName + " " + year;
        tvCurrentOrgJoiningDate.setText(strJoiningDate);
        CommonMethods.setValueAgainstKey(this,CommonStrings.CURRENT_ORG_JOINING_DATE,dayOfMonth + " " + monthName + " " + year);
        startActivity(new Intent(this, YearOfExperienceActivity.class));
    }
}