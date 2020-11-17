package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.EducationActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.review_activites.BSDListItemFragment;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;

import utility.CommonMethods;
import utility.CommonStrings;

public class ProfessionActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvProfessionVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    String strEmpType="";
    ArrayList<String> professionList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession);
        try{
            if (!CommonMethods.getStringValueFromKey(this, CommonStrings.EMP_TYPE_VAL).equals("")) {
                strEmpType = CommonMethods.getStringValueFromKey(this, CommonStrings.EMP_TYPE_VAL);
            } else {
                strEmpType = "";
            }
        }catch(Exception exception)
        {
            exception.printStackTrace();
        }
        professionList=new ArrayList<>();
        professionList.add("Proprietor");
        professionList.add("Partner Applying as an Individual");
        professionList.add("Partnership Firm");
        professionList.add("Private Limited Company");
        initView();

    }

    private void initView()
    {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvProfessionVal=findViewById(R.id.tvProfessionVal);
        btnNext=findViewById(R.id.btnNext);
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_employment_type));
        tvGivenPreviousVal.setText(strEmpType);
        tvGivenValEdit.setOnClickListener(this);
        tvProfessionVal.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        iv_personal_details_backBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, UserDOBActivity.class));
        }
        else if (v.getId() == R.id.tvEmploymentRoleVal) {
            if(professionList!=null && professionList.size()>0)
            {
                BSDListItemFragment BSDListItemFragment = new BSDListItemFragment(this, tvProfessionVal,professionList);
                BSDListItemFragment.show(getSupportFragmentManager(),"Profession List");
            }
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvProfessionVal.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.PROFESSION_VAL,tvProfessionVal.getText().toString());
                startActivity(new Intent(this, EducationActivity.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select Profession");
            }
        }
    }
}
