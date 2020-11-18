package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

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

public class EmployerNameActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = EmployerNameActivity.class.getSimpleName() ;
    private String strEmployerName = "";
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvSelectedEmployerVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private List<String> employerList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer_name);
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvSelectedEmployerVal=findViewById(R.id.tvSelectedEmployerVal);
        btnNext=findViewById(R.id.btnNext);
        tvSelectedEmployerVal.setOnClickListener(this);
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

            SpinnerManager.showSpinner(EmployerNameActivity.this);
            retrofitInterface.getFromWeb(CommonStrings.BANK_NAME_URL).enqueue(this);
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvSelectedEmployerVal.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.EMPLOYER_NAME,tvSelectedEmployerVal.getText().toString());
                startActivity(new Intent(this, IndustryTypeActivity.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select Employer Name");
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
            BankNamesRes employerNameRes=new Gson().fromJson(strRes,BankNamesRes.class);
            if(employerNameRes.getStatus() && employerNameRes!=null)
            {
                if(employerNameRes.getData()!=null)
                {
                    employerList=employerNameRes.getData();
                    new CustomSearchDialog(EmployerNameActivity.this,employerList,tvSelectedEmployerVal).show();
                }
                else
                {
                    employerNameRes.getMessage();
                }
            }
            else
            {
                CommonMethods.showToast(this,"No employer names found,Please try again!");
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
