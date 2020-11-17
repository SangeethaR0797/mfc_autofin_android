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

import static retrofit_config.RetroBase.retrofitInterface;

public class EducationActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = EducationActivity.class.getSimpleName();
    private String strGender = "";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvEducationVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private List<String> educationQualificationList;

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
        retrofitInterface.getFromWeb(CommonStrings.EDUCATION_QUALIFICATION_URL).enqueue(this);

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
            if(educationQualificationList!=null)
            {
                BSDListItemFragment bSDListItemFragment = new BSDListItemFragment(this, tvEducationVal,educationQualificationList);
                bSDListItemFragment.show(getSupportFragmentManager(),"EducationList");
            }
            else
            {retrofitInterface.getFromWeb(CommonStrings.EDUCATION_QUALIFICATION_URL).enqueue(this);}
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

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
