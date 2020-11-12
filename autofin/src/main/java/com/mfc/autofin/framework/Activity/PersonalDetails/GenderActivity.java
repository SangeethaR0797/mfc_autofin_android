package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.review_activites.GenderBSDFragment;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class GenderActivity extends AppCompatActivity implements View.OnClickListener {

    private String strUserDOB = "";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvGenderVal;

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
        tvGenderVal = findViewById(R.id.tvGenderVal);
        tvGenderVal.setOnClickListener(this);
        //Setting the ArrayAdapter data on the Spinner


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGenderVal) {
            GenderBSDFragment genderBSDFragment = new GenderBSDFragment(this, tvGenderVal);
            genderBSDFragment.show(getSupportFragmentManager(),"GenderFragment");
        }
    }
}
