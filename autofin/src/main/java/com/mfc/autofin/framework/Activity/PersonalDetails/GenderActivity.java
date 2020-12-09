package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.review_activites.BSDListItemFragment;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.personal_details_models.DataListResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;

import static retrofit_config.RetroBase.retrofitInterface;

public class GenderActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG =GenderActivity.class.getSimpleName() ;
    private String strUserDOB = "",strGender="";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvGenderVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private List<String> genderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_personal_details);
        try{
            if (!CommonStrings.customPersonalDetails.getBirthDate().equals("")) {
                strUserDOB = CommonStrings.customPersonalDetails.getBirthDate();
            } else {
                strUserDOB = "";
            }
        }catch(Exception exception)
        {
            exception.printStackTrace();
        }
        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customPersonalDetails.getGender()!=null && !CommonStrings.customPersonalDetails.getGender().isEmpty())
            {
                strGender= CommonStrings.customPersonalDetails.getGender();
            }
        }
        retrofitInterface.getFromWeb(Global.customerAPI_Master_URL+CommonStrings.GENDER_URL).enqueue(this);
        initView();
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
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        if(!strGender.isEmpty())
        {
            tvGenderVal.setText(strGender);
        }
        btnNext.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        tvGenderVal.setOnClickListener(this);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strRes);
        try
        {
            DataListResponse genderListRes=new Gson().fromJson(strRes,DataListResponse.class);
            if(genderListRes.getStatus() && genderListRes!=null)
            {
                if(genderListRes.getData()!=null)
                {
                    genderList=genderListRes.getData();
                }
                else
                {
                    genderListRes.getMessage();
                }
            }
            else
            {
                CommonMethods.showToast(this,"No data found,Please try again!");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGenderVal) {
            if(genderList!=null && genderList.size()>0)
            {
                BSDListItemFragment BSDListItemFragment = new BSDListItemFragment(this, tvGenderVal,genderList,"SELECT GENDER");
                BSDListItemFragment.show(getSupportFragmentManager(),"GenderFragment");
            }
            else
            {
                retrofitInterface.getFromWeb(Global.customerAPI_Master_URL+CommonStrings.GENDER_URL).enqueue(this);
            }
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvGenderVal.getText().toString().equals(""))
            {
                CommonStrings.customPersonalDetails.setGender(tvGenderVal.getText().toString());
                startActivity(new Intent(this,EducationActivity.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select Gender");
            }
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
    @Override
    public void onBackPressed() {

    }
}
