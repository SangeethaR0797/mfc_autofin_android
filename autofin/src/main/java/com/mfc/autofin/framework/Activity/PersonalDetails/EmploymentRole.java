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
import com.mfc.autofin.framework.Activity.review_activites.BSDListItemFragment;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;

import static retrofit_config.RetroBase.retrofitInterface;

public class EmploymentRole extends AppCompatActivity implements Callback<Object>, View.OnClickListener {
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvEmploymentRoleVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    String strEmpType="";
    ArrayList<String> employmentRoleList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_role);
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
        employmentRoleList=new ArrayList<>();
        employmentRoleList.add("Proprietor");
        employmentRoleList.add("Partner Applying as an Individual");
        employmentRoleList.add("Partnership Firm");
        employmentRoleList.add("Private Limited Company");
        initView();
    }

    private void initView() {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvEmploymentRoleVal=findViewById(R.id.tvEmploymentRoleVal);
        btnNext=findViewById(R.id.btnNext);
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_employment_type));
        tvGivenPreviousVal.setText(strEmpType);
        tvGivenValEdit.setOnClickListener(this);
        tvEmploymentRoleVal.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        iv_personal_details_backBtn.setOnClickListener(this);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
        else if (v.getId() == R.id.tvEmploymentRoleVal) {
            if(employmentRoleList!=null && employmentRoleList.size()>0)
            {
                BSDListItemFragment BSDListItemFragment = new BSDListItemFragment(this, tvEmploymentRoleVal,employmentRoleList);
                BSDListItemFragment.show(getSupportFragmentManager(),"EmploymentRole List");
            }
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvEmploymentRoleVal.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.EMPLOYMENT_ROLE_VAL,tvEmploymentRoleVal.getText().toString());
                startActivity(new Intent(this,EducationActivity.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select Employment Role");
            }
        }
    }
}
