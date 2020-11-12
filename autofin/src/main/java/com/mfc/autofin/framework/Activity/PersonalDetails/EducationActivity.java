package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.review_activites.GenderBSDFragment;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class EducationActivity extends AppCompatActivity implements View.OnClickListener {

    private String strGender = "";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvEducationVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private String[] educationArr={"DOCTORATE","MASTER DEGREE","GRADUATION","INTERMEDIATE","DIPLOMA"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details);
        try{
            if (!CommonMethods.getStringValueFromKey(this, CommonStrings.GENDER).equals("")) {
                strGender = CommonMethods.getStringValueFromKey(this, CommonStrings.GENDER);
            } else {
                strGender = "";
            }
            initView();
        }catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void initView()
    {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvEducationVal=findViewById(R.id.tvEducationVal);
        btnNext=findViewById(R.id.btnNext);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_gender));
        tvGivenPreviousVal.setText(strGender);
        tvGivenValEdit.setOnClickListener(this);
        tvEducationVal.setOnClickListener(this);
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        iv_personal_details_backBtn.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.tvEducationVal)
        {
            GenderBSDFragment genderBSDFragment = new GenderBSDFragment(this, tvEducationVal,educationArr);
            genderBSDFragment.show(getSupportFragmentManager(),"GenderFragment");
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvEducationVal.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.EDUCATION,tvEducationVal.getText().toString());
                startActivity(new Intent(this,MonthlyIncome.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select your Education");
            }
        }
    }
}
