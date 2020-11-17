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
import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.ProfessionActivity;
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
    RadioButton rbSalaried, rbBusinessOwner, rbSelfEmployedProfessional, rbIndependentWorker, rbStudent, rbRetired, rbHomeMaker;
    List<EmpTypeList> empTypeList;
    String strPanCardNo = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_type);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.PAN_CARD_NUMBER).equals("")) {
            strPanCardNo = CommonMethods.getStringValueFromKey(this, CommonStrings.PAN_CARD_NUMBER);
        }
        retrofitInterface.getFromWeb(CommonStrings.EMP_TYPE_URL).enqueue(this);
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        customEmpTypeRG = findViewById(R.id.customEmpTypeRG);
        tvGivenLbl.setText(CommonStrings.PANCARD_LBL);
        llEmpTypeRadioGroup = findViewById(R.id.llEmpTypeRadioGroup);
        tvGivenPreviousVal.setText(strPanCardNo);
        rbSalaried = findViewById(R.id.rbSalaried);
        rbBusinessOwner = findViewById(R.id.rbBusinessOwner);
        rbSelfEmployedProfessional = findViewById(R.id.rbSelfEmployedProfessional);
        rbIndependentWorker = findViewById(R.id.rbIndependentWorker);
        rbStudent = findViewById(R.id.rbStudent);
        rbRetired = findViewById(R.id.rbRetired);
        rbHomeMaker = findViewById(R.id.rbHomeMaker);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        rbSalaried.setOnClickListener(this);
        rbBusinessOwner.setOnClickListener(this);
        rbSelfEmployedProfessional.setOnClickListener(this);
        rbIndependentWorker.setOnClickListener(this);
        rbStudent.setOnClickListener(this);
        rbRetired.setOnClickListener(this);
        rbHomeMaker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_residential_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.rbSalaried) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbSalaried.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, BankNamesActivity.class));
        } else if (v.getId() == R.id.rbBusinessOwner) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbBusinessOwner.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, EmploymentRole.class));
        } else if (v.getId() == R.id.rbSelfEmployedProfessional) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbSelfEmployedProfessional.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, ProfessionActivity.class));
        } else if (v.getId() == R.id.rbIndependentWorker) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbIndependentWorker.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, BankNamesActivity.class));
        } else if (v.getId() == R.id.rbStudent) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbStudent.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, BankNamesActivity.class));
        } else if (v.getId() == R.id.rbRetired) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbRetired.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, BankNamesActivity.class));
        } else if (v.getId() == R.id.rbHomeMaker) {
            CommonMethods.setValueAgainstKey(EmploymentTypeActivity.this, CommonStrings.EMP_TYPE_VAL, rbHomeMaker.getText().toString());
            startActivity(new Intent(EmploymentTypeActivity.this, BankNamesActivity.class));
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
                    //setRadioButtonInRG(empTypeList);
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

    /* private void setRadioButtonInRG(List<EmpTypeList> empTypeList) {

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
 */
    private String getSeparatedDisplayVal(String strDisplayVal) {
        String strResult = "";
        if (strDisplayVal.contains("\\(")) {
            String[] strArr = strDisplayVal.split("\\(");
            strResult = strArr[0] + "//\n" + Html.fromHtml("<font color='#9babc4'>strArr[1]</font>");

        } else {
            strResult = strDisplayVal;
        }
        return strResult;
    }

}