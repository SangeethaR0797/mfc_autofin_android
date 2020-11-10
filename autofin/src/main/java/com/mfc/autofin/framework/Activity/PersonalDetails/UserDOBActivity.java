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
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidenceTypeActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.InsuranceTypeActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehInsuranceValidityActivity;
import com.mfc.autofin.framework.R;

import java.text.SimpleDateFormat;

import model.residential_models.ResidenceType;
import utility.CommonMethods;
import utility.CommonStrings;

public class UserDOBActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {

    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvDOBLbl;
    LinearLayout llDOBCalendarView;
    ImageView iv_personal_details_backBtn;
    CalendarView cvUserDOB;
    String strResidenceType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_d_o_b);
        if(!CommonMethods.getStringValueFromKey(this,CommonStrings.RESIDENCE_TYPE).isEmpty())
        {
            strResidenceType=CommonMethods.getStringValueFromKey(this,CommonStrings.RESIDENCE_TYPE);
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
        llDOBCalendarView = findViewById(R.id.llDOBCalendarView);
        cvUserDOB = findViewById(R.id.cvUserDOB);
        cvUserDOB.setMinDate(System.currentTimeMillis() - 1000);
        cvUserDOB.setOnDateChangeListener(this);
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvDOBLbl.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(UserDOBActivity.this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.tvDOBLbl) {
            llDOBCalendarView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String monthName = new SimpleDateFormat("MMMM").format(view.getDate());
        String dob = dayOfMonth + "/" + monthName + "/" + year;
        tvDOBLbl.setText(dob);
        CommonMethods.setValueAgainstKey(this, CommonStrings.USER_DOB, dayOfMonth + " " + monthName + " " + year);
        Intent intent = new Intent(UserDOBActivity.this, MonthlyIncome.class);
        startActivity(intent);
    }
}