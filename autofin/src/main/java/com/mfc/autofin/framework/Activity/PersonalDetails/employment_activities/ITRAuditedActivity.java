package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class ITRAuditedActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvIsITRAuditedLbl;
    private Button btnItrAuditedYes, btnItrAuditedNo, btnNext;
    private String strPreviousLbl, strPreviousVal, strJoiningDate = "", strIsITRAudited = "";
    private Intent intent;
    private boolean itrAudited = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itr_audited);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.cusEmpDetails.getIsLastestItraudited()) {
                strIsITRAudited = "Yes";
            } else {
                strIsITRAudited = "No";
            }
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvIsITRAuditedLbl = findViewById(R.id.tvIsITRAuditedLbl);
        btnNext = findViewById(R.id.btnNext);
        btnItrAuditedYes = findViewById(R.id.btnItrAuditedYes);
        btnItrAuditedNo = findViewById(R.id.btnItrAuditedNo);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        if (!strIsITRAudited.isEmpty()) {
            if (strIsITRAudited.equalsIgnoreCase("Yes")) {
                CommonMethods.highLightSelectedButton(this, btnItrAuditedYes);
                CommonMethods.deHighLightButton(this, btnItrAuditedNo);
            } else {
                CommonMethods.highLightSelectedButton(this, btnItrAuditedNo);
                CommonMethods.deHighLightButton(this, btnItrAuditedYes);
            }
        }
        btnItrAuditedYes.setOnClickListener(this);
        btnItrAuditedNo.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnItrAuditedYes) {
            CommonMethods.highLightSelectedButton(this,btnItrAuditedYes);
            CommonMethods.deHighLightButton(this,btnItrAuditedNo);
            strIsITRAudited="Yes";
            itrAudited = true;
        } else if (v.getId() == R.id.btnItrAuditedNo) {
            CommonMethods.highLightSelectedButton(this,btnItrAuditedNo);
            CommonMethods.deHighLightButton(this,btnItrAuditedYes);
            strIsITRAudited="No";
            itrAudited=false;
        } else if (v.getId() == R.id.btnNext) {
            if(!strIsITRAudited.isEmpty())
            moveToNextPage();
        }
    }

    private void moveToNextPage() {
        CommonStrings.cusEmpDetails.setIsLastestItraudited(itrAudited);
        Intent intent = new Intent(this, IndustryTypeActivity.class);
        intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvIsITRAuditedLbl.getText().toString());
        intent.putExtra(CommonStrings.PREVIOUS_VALUE, btnItrAuditedYes.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
