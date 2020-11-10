package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidenceTypeActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.personal_details_models.EmpTypeList;
import model.personal_details_models.EmploymentTypeRes;
import model.residential_models.ResidenceType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;

import static retrofit_config.RetroBase.retrofitInterface;

public class EmploymentTypeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = EmploymentTypeActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit;
    LinearLayout llEmpTypeRadioGroup;
    ImageView iv_personal_details_backBtn;
    ViewGroup customEmpTypeRG;
    RadioButton radioButton;
    List<EmpTypeList> empTypeList;
    String strLoanRequired="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_type);
        if(!CommonMethods.getStringValueFromKey(this,CommonStrings.LOAN_REQUIRED).isEmpty())
        {
            strLoanRequired=CommonMethods.getStringValueFromKey(this,CommonStrings.LOAN_REQUIRED);
        }
        retrofitInterface.getFromWeb(CommonStrings.EMP_TYPE_URL).enqueue(this);
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        customEmpTypeRG=findViewById(R.id.customEmpTypeRG);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_loan_required).toUpperCase());
        llEmpTypeRadioGroup=findViewById(R.id.llEmpTypeRadioGroup);
        tvGivenPreviousVal.setText(strLoanRequired);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        customEmpTypeRG.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, empTypeList.get(radioButton.getId()).getValue());
            startActivity(new Intent(EmploymentTypeActivity.this, BankNamesActivity.class));
         if (v.getId() == R.id.iv_residential_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        if (strRes != null) {
            try {
                EmploymentTypeRes employmentTypeRes = new Gson().fromJson(strRes, EmploymentTypeRes.class);
                if (employmentTypeRes.getStatus()) {
                    empTypeList = employmentTypeRes.getData().getTypes();
                    setRadioButtonInRG(empTypeList);
                } else {
                    CommonMethods.showToast(this, "No Employment data available");
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    private void setRadioButtonInRG(List<EmpTypeList> empTypeList) {

        for (int i = 0; i < empTypeList.size(); i++) {
            String displayVal="";
            radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null);
            radioButton.setId(i);
            displayVal=empTypeList.get(i).getDisplayLabel();
            radioButton.setText(getSeparatedDisplayVal(displayVal));
            radioButton.setOnClickListener(this);
            customEmpTypeRG.addView(radioButton);
        }
    }

    private String getSeparatedDisplayVal(String strDisplayVal) {
        String strResult = "";
        if (strDisplayVal.contains("\\(")) {
            String[] strArr = strDisplayVal.split("\\(");
            strResult=strArr[0]+"//\n"+ Html.fromHtml("<font color='#9babc4'>strArr[1]</font>");

        } else
        {
            strResult=strDisplayVal;
        }
        return strResult;
    }

}