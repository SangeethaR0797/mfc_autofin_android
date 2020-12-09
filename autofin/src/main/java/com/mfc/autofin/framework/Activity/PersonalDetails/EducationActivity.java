package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

public class EducationActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = EducationActivity.class.getSimpleName();
    private String strGender = "",strEducation="";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvEducationVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private List<String> educationQualificationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details);
        try{
            if (!CommonStrings.customPersonalDetails.getGender().equals("")) {
                strGender = CommonStrings.customPersonalDetails.getGender();
            } else {
                strGender = "";
            }

        }catch(Exception exception)
        {
            exception.printStackTrace();
        }
        retrofitInterface.getFromWeb(Global.customerAPI_Master_URL+CommonStrings.EDUCATION_QUALIFICATION_URL).enqueue(this);
        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customPersonalDetails.getEducation()!=null && !CommonStrings.customPersonalDetails.getEducation().isEmpty())
            {
                strEducation= CommonStrings.customPersonalDetails.getEducation();
            }
        }

        initView();
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
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        if(!strEducation.isEmpty())
        {
            tvEducationVal.setText(strEducation);
        }
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strRes);
        try
        {
            DataListResponse eduQualificationListRes=new Gson().fromJson(strRes,DataListResponse.class);
            if(eduQualificationListRes.getStatus() && eduQualificationListRes!=null)
            {
                if(eduQualificationListRes.getData()!=null)
                {
                    educationQualificationList=eduQualificationListRes.getData();
                }
                else
                {
                    eduQualificationListRes.getMessage();
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
        if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.tvEducationVal)
        {
            if(educationQualificationList!=null)
            {
                BSDListItemFragment bSDListItemFragment = new BSDListItemFragment(this, tvEducationVal,educationQualificationList,"SELECT EDUCATION");
                bSDListItemFragment.show(getSupportFragmentManager(),"EducationList");
            }
            else
            {retrofitInterface.getFromWeb(Global.customerAPI_Master_URL+CommonStrings.EDUCATION_QUALIFICATION_URL).enqueue(this);}
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvEducationVal.getText().toString().equals(""))
            {
                CommonStrings.customPersonalDetails.setEducation(tvEducationVal.getText().toString());
                startActivity(new Intent(this,MonthlyIncome.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select your Education");
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
