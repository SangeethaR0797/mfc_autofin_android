package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class LeadDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvLeadDetailsAppBarTitle, tvLeadCaseId, tvLeadDetailsDate, tvLeadDetailsStatus, tvKYCStatus, tvLeadDetailsCName, tvLeadDetailsCMobileNum, tvLeadDetailsCEmailId;
    Button btnLeadDetailsCallCustomer, btnUpdateLeadDetails, btnRemoveLeads;
    String strCName = "", strCaseId = "", strEmailId = "", strMobileNo = "", strLeadStatus = "", strKYCStatus = "", strLeadCreationDate = "";
    ImageView iv_lead_details_backBtn;
    Intent intent;
    LinearLayout llUpdateLeadDetails;
    RadioGroup rgLeadStatus;
    RadioButton rbKYCUpdated,rbPendingAtBank,rbDisbursement;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_details);
        if (getIntent() != null) {
            intent = getIntent();
            Bundle leadData = intent.getExtras();
            strCaseId = leadData.getString(CommonStrings.CASE_ID);
            strCName = leadData.getString(CommonStrings.CNAME);
            strEmailId = leadData.getString(CommonStrings.CEMAIL);
            strMobileNo = leadData.getString(CommonStrings.CMOBILE_NUM);
            strLeadCreationDate = leadData.getString(CommonStrings.LEAD_CREATION_DATE);
            strLeadStatus = leadData.getString(CommonStrings.LEAD_STATUS);
            strKYCStatus = leadData.getString(CommonStrings.KYC_STATUS);
        } else {
            CommonMethods.showToast(this, "No Data found");
        }
        getIntent();
        initView();
    }

    private void initView() {
        tvLeadDetailsAppBarTitle = findViewById(R.id.tvLeadDetailsAppBarTitle);
        tvLeadCaseId = findViewById(R.id.tvLeadCaseId);
        tvLeadDetailsDate = findViewById(R.id.tvLeadDetailsDate);
        tvLeadDetailsStatus = findViewById(R.id.tvLeadDetailsStatus);
        tvKYCStatus = findViewById(R.id.tvKYCStatus);
        tvLeadDetailsCName = findViewById(R.id.tvLeadDetailsCName);
        tvLeadDetailsCMobileNum = findViewById(R.id.tvLeadDetailsCMobileNum);
        tvLeadDetailsCEmailId = findViewById(R.id.tvLeadDetailsCEmailId);
        btnLeadDetailsCallCustomer = findViewById(R.id.btnLeadDetailsCallCustomer);
        btnUpdateLeadDetails = findViewById(R.id.btnUpdateLeadDetails);
        btnRemoveLeads = findViewById(R.id.btnRemoveLeads);
        iv_lead_details_backBtn = findViewById(R.id.iv_lead_details_backBtn);
        llUpdateLeadDetails=findViewById(R.id.llUpdateLeadDetails);
        rgLeadStatus=findViewById(R.id.rgLeadStatus);
        rbKYCUpdated=findViewById(R.id.rbKYCUpdated);
        rbPendingAtBank=findViewById(R.id.rbPendingAtBank);
        rbDisbursement=findViewById(R.id.rbDisbursement);
        iv_lead_details_backBtn.setOnClickListener(this);
        btnLeadDetailsCallCustomer.setOnClickListener(this);
        btnUpdateLeadDetails.setOnClickListener(this);
        btnRemoveLeads.setOnClickListener(this);
        rbKYCUpdated.setOnClickListener(this);
        rbPendingAtBank.setOnClickListener(this);
        rbDisbursement.setOnClickListener(this);
        if (strCaseId != null && !strCaseId.equals("")) {
            tvLeadDetailsAppBarTitle.setText(strCaseId);
            tvLeadCaseId.setText(strCaseId);
        }
        if (strCName != null && !strCName.equals("")) {
            tvLeadDetailsCName.setText(strCName);
        }
        if (strEmailId != null && !strEmailId.equals("")) {
            tvLeadDetailsCEmailId.setText(strEmailId);
        }
        if (strMobileNo != null && !strMobileNo.equals("")) {
            tvLeadDetailsCMobileNum.setText(strMobileNo);
        }
        if (strLeadStatus != null && !strLeadStatus.equals("")) {
            tvLeadDetailsStatus.setText(strLeadStatus);
        }
        if (strLeadCreationDate != null && !strLeadCreationDate.equals("")) {
            tvLeadDetailsDate.setText(strLeadCreationDate);
        }
        if (strKYCStatus != null && !strKYCStatus.equals("")) {
            tvKYCStatus.setText(strKYCStatus);
        }


    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_lead_details_backBtn) {
            finish();
        } else if (v.getId() == R.id.btnLeadDetailsCallCustomer) {
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + tvLeadDetailsCMobileNum.getText().toString()));
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                startActivity(callIntent);
            }
        }
        else if(v.getId()==R.id.rbKYCUpdated)
        {
            CommonMethods.showToast(this,"You've checked "+rbKYCUpdated.getText().toString());
        }
        else if(v.getId()==R.id.rbPendingAtBank)
        {
            CommonMethods.showToast(this,"You've checked "+rbPendingAtBank.getText().toString());
        }
        else if(v.getId()==R.id.rbDisbursement)
        {
            CommonMethods.showToast(this,"You've checked "+rbDisbursement.getText().toString());
        }else if (v.getId() == R.id.btnUpdateLeadDetails) {
            llUpdateLeadDetails.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.btnRemoveLeads) {

        }


    }
}
