package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.PersonalDetails.CurrentOrganizationActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class ITRAuditedActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl,tvGivenPreviousVal, tvGivenValEdit,tvIsITRAuditedLbl;
    private Button btnItrAuditedYes,btnItrAuditedNo;
    private String strJoiningDate = "", strIsITRAudited = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itr_audited);
        initView();
    }

    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvIsITRAuditedLbl=findViewById(R.id.tvIsITRAuditedLbl);
        btnItrAuditedYes=findViewById(R.id.btnItrAuditedYes);
        btnItrAuditedNo=findViewById(R.id.btnItrAuditedNo);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        btnItrAuditedYes.setOnClickListener(this);
        btnItrAuditedNo.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            finish();
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnItrAuditedYes)
        {
            CommonMethods.setValueAgainstKey(this, CommonStrings.ITR_AUDITERD_OR_NOT, btnItrAuditedYes.getText().toString());
            startActivity(new Intent(this, CurrentOrganizationActivity.class));
        }
        else if(v.getId()==R.id.btnItrAuditedNo)
        {
            CommonMethods.setValueAgainstKey(this, CommonStrings.INCOME_AFTER_TAX,  btnItrAuditedNo.getText().toString());
            startActivity(new Intent(this,CurrentOrganizationActivity.class));
        }
    }
}
