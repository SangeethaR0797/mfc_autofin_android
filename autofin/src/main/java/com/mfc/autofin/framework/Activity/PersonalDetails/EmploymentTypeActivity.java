package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import utility.Global;

import static retrofit_config.RetroBase.retrofitInterface;

public class EmploymentTypeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = EmploymentTypeActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvEmpTypeLbl;
    LinearLayout llEmpTypeRadioGroup;
    ImageView iv_personal_details_backBtn;
    ViewGroup customEmpTypeRG;
    RadioButton rbSalaried, rbBusinessOwner, rbSelfEmployedProfessional, rbIndependentWorker, rbStudent, rbRetired, rbHomeMaker;
    List<EmpTypeList> empTypeList;
    private Intent intent;
    private String strPreviousLbl = "", strPreviousVal = "", empType = "";
    Button btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employment_type);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.cusEmpDetails.getEmploymentType() != null && !CommonStrings.cusEmpDetails.getEmploymentType().isEmpty()) {
                empType = CommonStrings.cusEmpDetails.getEmploymentType();
            }
        }

        retrofitInterface.getFromWeb(Global.customerAPI_Master_URL + CommonStrings.EMP_TYPE_URL_END).enqueue(this);
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        customEmpTypeRG = findViewById(R.id.customEmpTypeRG);
        tvGivenLbl.setText(strPreviousLbl);
        llEmpTypeRadioGroup = findViewById(R.id.llEmpTypeRadioGroup);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvEmpTypeLbl = findViewById(R.id.tvEmpTypeLbl);
        rbSalaried = findViewById(R.id.rbSalaried);
        rbBusinessOwner = findViewById(R.id.rbBusinessOwner);
        rbSelfEmployedProfessional = findViewById(R.id.rbSelfEmployedProfessional);
        rbIndependentWorker = findViewById(R.id.rbIndependentWorker);
        rbStudent = findViewById(R.id.rbStudent);
        rbRetired = findViewById(R.id.rbRetired);
        rbHomeMaker = findViewById(R.id.rbHomeMaker);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        btnNext=findViewById(R.id.btnNext);
        tvGivenValEdit.setOnClickListener(this);
        if (empType.equalsIgnoreCase(rbSalaried.getText().toString())) {
            rbSalaried.setChecked(true);
            rbBusinessOwner.setChecked(false);
            rbSelfEmployedProfessional.setChecked(false);
            rbIndependentWorker.setChecked(false);
            rbRetired.setChecked(false);
        } else if (empType.equalsIgnoreCase(rbBusinessOwner.getText().toString())) {
            rbSalaried.setChecked(false);
            rbBusinessOwner.setChecked(true);
            rbSelfEmployedProfessional.setChecked(false);
            rbIndependentWorker.setChecked(false);
            rbRetired.setChecked(false);
        } else if (empType.equalsIgnoreCase(rbSelfEmployedProfessional.getText().toString())) {
            rbSalaried.setChecked(false);
            rbBusinessOwner.setChecked(false);
            rbSelfEmployedProfessional.setChecked(true);
            rbIndependentWorker.setChecked(false);
            rbRetired.setChecked(false);
        } else if (empType.equalsIgnoreCase(rbIndependentWorker.getText().toString())) {
            rbSalaried.setChecked(false);
            rbBusinessOwner.setChecked(false);
            rbSelfEmployedProfessional.setChecked(false);
            rbIndependentWorker.setChecked(true);
            rbRetired.setChecked(false);
        } else if (empType.equalsIgnoreCase(rbRetired.getText().toString())) {
            rbSalaried.setChecked(false);
            rbBusinessOwner.setChecked(false);
            rbSelfEmployedProfessional.setChecked(false);
            rbIndependentWorker.setChecked(false);
            rbRetired.setChecked(true);
        }
        rbSalaried.setOnClickListener(this);
        rbBusinessOwner.setOnClickListener(this);
        rbSelfEmployedProfessional.setOnClickListener(this);
        rbIndependentWorker.setOnClickListener(this);
        rbStudent.setVisibility(View.GONE);
        rbRetired.setOnClickListener(this);
        rbHomeMaker.setVisibility(View.GONE);
        if (CommonStrings.IS_OLD_LEAD) {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        } else {
            btnNext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {

        /*if (v.getId() == R.id.iv_residential_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else*/
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.rbSalaried) {
            empType = rbSalaried.getText().toString();
            if (!CommonStrings.IS_OLD_LEAD) {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbBusinessOwner) {
            empType = rbBusinessOwner.getText().toString();
            if (!CommonStrings.IS_OLD_LEAD) {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbSelfEmployedProfessional) {
            empType = rbSelfEmployedProfessional.getText().toString();
            if (!CommonStrings.IS_OLD_LEAD) {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbIndependentWorker) {
            empType = rbIndependentWorker.getText().toString();
            if (!CommonStrings.IS_OLD_LEAD) {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbRetired) {
            empType = rbRetired.getText().toString();
            if (!CommonStrings.IS_OLD_LEAD) {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.btnNext) {
            if(!empType.isEmpty())
            moveToNextPage();
        }
    }

    private void moveToNextPage() {
        if (empType.equalsIgnoreCase(rbSalaried.getText().toString())) {
            CommonStrings.cusEmpDetails.setEmploymentType(empType);
            Intent intent = new Intent(this, BankNamesActivity.class);
            intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvEmpTypeLbl.getText().toString());
            intent.putExtra(CommonStrings.PREVIOUS_VALUE, empType);
            startActivity(intent);
        } else if (empType.equalsIgnoreCase(rbBusinessOwner.getText().toString())) {
            CommonStrings.cusEmpDetails.setEmploymentType(empType);
            Intent intent = new Intent(this, EmploymentRole.class);
            intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvEmpTypeLbl.getText().toString());
            intent.putExtra(CommonStrings.PREVIOUS_VALUE, empType);
            startActivity(intent);
        }else if(empType.equalsIgnoreCase(rbSelfEmployedProfessional.getText().toString()))
        {
            CommonStrings.cusEmpDetails.setEmploymentType(empType);
            Intent intent = new Intent(this, ProfessionActivity.class);
            intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvEmpTypeLbl.getText().toString());
            intent.putExtra(CommonStrings.PREVIOUS_VALUE, empType);
            startActivity(intent);
        }
        else if(empType.equalsIgnoreCase(rbIndependentWorker.getText().toString()))
        {
            CommonStrings.cusEmpDetails.setEmploymentType(empType);
            Intent intent = new Intent(this, SavingsBankAccountActivity.class);
            intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvEmpTypeLbl.getText().toString());
            intent.putExtra(CommonStrings.PREVIOUS_VALUE, empType);
            startActivity(intent);

        }
        else if(empType.equalsIgnoreCase(rbRetired.getText().toString()))
        {
            CommonStrings.cusEmpDetails.setEmploymentType(empType);
            Intent intent = new Intent(this, SavingsBankAccountActivity.class);
            intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvEmpTypeLbl.getText().toString());
            intent.putExtra(CommonStrings.PREVIOUS_VALUE, empType);
            startActivity(intent);
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

    @Override
    public void onBackPressed() {

    }
}