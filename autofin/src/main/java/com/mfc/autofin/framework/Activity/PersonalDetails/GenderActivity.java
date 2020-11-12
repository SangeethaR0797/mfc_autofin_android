package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.review_activites.GenderBSDFragment;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class GenderActivity extends AppCompatActivity implements View.OnClickListener {

    private String strUserDOB = "";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvGenderVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private String[] genderArr={"Male","Female","Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_personal_details);
        try{
            if (!CommonMethods.getStringValueFromKey(this, CommonStrings.USER_DOB).equals("")) {
                strUserDOB = CommonMethods.getStringValueFromKey(this, CommonStrings.USER_DOB);
            } else {
                strUserDOB = "";
            }
            initView();
        }catch(Exception exception)
        {
            exception.printStackTrace();
        }

    }

    private void initView() {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvGenderVal = findViewById(R.id.tvGenderVal);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_dob));
        tvGivenPreviousVal.setText(strUserDOB);
        btnNext=findViewById(R.id.btnNext);
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        btnNext.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        tvGenderVal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
       else if (v.getId() == R.id.tvGenderVal) {
            GenderBSDFragment genderBSDFragment = new GenderBSDFragment(this, tvGenderVal,genderArr);
            genderBSDFragment.show(getSupportFragmentManager(),"GenderFragment");
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvGenderVal.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.GENDER,tvGenderVal.getText().toString());
            startActivity(new Intent(this,EducationActivity.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select Gender");
            }
        }
    }
}
