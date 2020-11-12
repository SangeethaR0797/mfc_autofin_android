package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class GenderActivity extends AppCompatActivity implements View.OnClickListener {
    private String strUserDOB="";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit;
    private Spinner genderSpinner;
    private String[] genderArr={"Male","Female","Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender);
        if(CommonMethods.getStringValueFromKey(this,CommonStrings.USER_DOB).length()>0)
        {
            strUserDOB= CommonMethods.getStringValueFromKey(this,CommonStrings.USER_DOB);
        }
        else
        {
            strUserDOB="";
        }
        initView();
    }

    private void initView()
    {
        genderSpinner=findViewById(R.id.genderSpinner);
        genderSpinner.setOnClickListener(this);
        //Setting the ArrayAdapter data on the Spinner



    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.genderSpinner)
        {
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,genderArr);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(aa);
        }
    }
}
