package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.BankNamesActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.JODOCurrentOrgActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.PanCardNumberActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.YearOfExperienceActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.personal_details_models.BankNamesRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomSearchDialog;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class IndustryTypeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = IndustryTypeActivity.class.getSimpleName() ;
    private String strIndustryType = "";
    private LinearLayout llIndustryType;
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvSelectedIndustryType;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private List<String> employerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_type);
        initView();
    }

    private void initView()
    {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvSelectedIndustryType=findViewById(R.id.tvSelectedIndustryType);
        btnNext=findViewById(R.id.btnNext);
        llIndustryType=findViewById(R.id.llIndustryType);
        llIndustryType.setOnClickListener(this);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
        else if (v.getId() == R.id.tvEmploymentRoleVal) {

            SpinnerManager.showSpinner(IndustryTypeActivity.this);
            retrofitInterface.getFromWeb(CommonStrings.BANK_NAME_URL).enqueue(this);
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvSelectedIndustryType.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.INDUSTRY_TYPE,tvSelectedIndustryType.getText().toString());
                if(CommonMethods.getStringValueFromKey(this,CommonStrings.EMP_TYPE_VAL).equals(getResources().getString(R.string.lbl_salaried)))
                {
                    startActivity(new Intent(this, YearOfExperienceActivity.class));
                }
                else if(CommonMethods.getStringValueFromKey(this,CommonStrings.EMP_TYPE_VAL).equals(getResources().getString(R.string.lbl_student)))
                {
                    startActivity(new Intent(this, BankNamesActivity.class));
                }
                else
                {
                    startActivity(new Intent(this, PanCardNumberActivity.class));
                }


            }
            else
            {
                CommonMethods.showToast(this,"Please select industry type");
            }
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strRes);
        try
        {
            BankNamesRes industryNameRes=new Gson().fromJson(strRes,BankNamesRes.class);
            if(industryNameRes.getStatus() && industryNameRes!=null)
            {
                if(industryNameRes.getData()!=null)
                {
                    employerList=industryNameRes.getData();
                    new CustomSearchDialog(IndustryTypeActivity.this,employerList,tvSelectedIndustryType).show();
                }
                else
                {
                    industryNameRes.getMessage();
                }
            }
            else
            {
                CommonMethods.showToast(this,"No Industry type found, Please try again!");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
