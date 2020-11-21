package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.text.SimpleDateFormat;

import model.bank_models.InterestedBankOfferRes;
import utility.CommonMethods;
import utility.CommonStrings;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class StartDateOfBusinessOrProfessionActivity extends AppCompatActivity implements View.OnClickListener, CalendarView.OnDateChangeListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvStartDateOfBOPLbl,tvStartDateOfBOPVal;
    private String strEmpType="",strPreviousLbl="",strPreviousVal="",strStartedDate="";
    CalendarView cvStartDateOfBOP;
    LinearLayout llStartDateOfBOP,llBusinessCalendarView;
    private Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_date_of_business);

        try
        {
            intent=getIntent();
            strPreviousLbl=intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal=intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        }catch (Exception exception){exception.printStackTrace();}

        initView();
    }

    @SuppressLint("NewApi")
    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvStartDateOfBOPLbl=findViewById(R.id.tvStartDateOfBOPLbl);
        tvStartDateOfBOPVal=findViewById(R.id.tvStartDateOfBOPValue);
        cvStartDateOfBOP=findViewById(R.id.cvStartDateOfBOP);
        llStartDateOfBOP=findViewById(R.id.llStartDateOfBOP);
        llBusinessCalendarView=findViewById(R.id.llBusinessCalendarView);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        cvStartDateOfBOP.setMaxDate(System.currentTimeMillis() - 1000);
        cvStartDateOfBOP.setOnDateChangeListener(this);
        tvStartDateOfBOPLbl.setOnClickListener(this);

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
        else if(v.getId()==R.id.tvStartDateOfBOPLbl)
        {
            llBusinessCalendarView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        String monthName = new SimpleDateFormat("MMMM").format(view.getDate());
        strStartedDate = dayOfMonth + " " + monthName + " " + year;
        tvStartDateOfBOPVal.setText(strStartedDate);
        CommonMethods.setValueAgainstKey(this,CommonStrings.BUSINESS_OR_PROFESSION_START_DATE,dayOfMonth + " " + monthName + " " + year);

        startActivity(new Intent(this, LastYearSalesOrTurnOver.class));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
