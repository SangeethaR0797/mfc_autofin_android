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
import com.mfc.autofin.framework.Activity.PersonalDetails.CurrentOrganizationActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.JODOCurrentOrgActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.PanCardNumberActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.SavingsBankAccountActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.YearOfExperienceActivity;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;

import model.personal_details_models.BankNamesRes;
import model.personal_details_models.IndustryType;
import model.personal_details_models.IndustryTypeData;
import model.personal_details_models.IndustryTypeRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomSearchDialog;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class IndustryTypeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = IndustryTypeActivity.class.getSimpleName();
    private String strIndustryType = "", strPreviousLbl = "", strPreviousVal = "";
    private LinearLayout llIndustryType;
    private TextView tvIndustryType, tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvSelectedIndustryType;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    private List<String> industryTypeList = new ArrayList<>();
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_industry_type);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.cusEmpDetails.getIndustryType()!=null && !CommonStrings.cusEmpDetails.getIndustryType().isEmpty())
            {
                strIndustryType=CommonStrings.cusEmpDetails.getIndustryType();
            }
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvIndustryType = findViewById(R.id.tvIndustryType);
        tvSelectedIndustryType = findViewById(R.id.tvSelectedIndustryType);
        btnNext = findViewById(R.id.btnNext);
        llIndustryType = findViewById(R.id.llIndustryType);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        llIndustryType.setOnClickListener(this);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);
        if(!strIndustryType.isEmpty())
        {
            tvSelectedIndustryType.setText(strIndustryType);
        }
        tvSelectedIndustryType.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       /* if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else*/
        if (v.getId() == R.id.llIndustryType) {
            SpinnerManager.showSpinner(IndustryTypeActivity.this);
            retrofitInterface.getFromWeb(Global.customerAPI_Master_URL+CommonStrings.INDUSTRY_TYPE_URL).enqueue(this);
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!tvSelectedIndustryType.getText().toString().equals("")) {
                CommonStrings.cusEmpDetails.setIndustryType(tvSelectedIndustryType.getText().toString());
                Intent intent = new Intent(IndustryTypeActivity.this, SavingsBankAccountActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvIndustryType.getText().toString());
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, tvSelectedIndustryType.getText().toString());
                startActivity(intent);

            } else {
                CommonMethods.showToast(this, "Please select industry type");
            }
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        try {
            IndustryTypeRes industryTypeRes = new Gson().fromJson(strRes, IndustryTypeRes.class);
            if (industryTypeRes.getStatus() && industryTypeRes != null) {
                if (industryTypeRes.getData() != null) {
                    IndustryTypeData industryTypeData = industryTypeRes.getData();
                    List<IndustryType> industryTypes = industryTypeData.getTypes();
                    if (industryTypes != null) {
                        if(industryTypeList.size()==0)
                        {
                        for (int i = 0; i < industryTypes.size(); i++) {
                            industryTypeList.add(industryTypes.get(i).getValue());
                        }
                            new CustomSearchDialog(IndustryTypeActivity.this, industryTypeList, tvSelectedIndustryType,"SELECT INDUSTRY TYPE",tvSelectedIndustryType.getText().toString()).show();
                        }
                        else
                        {
                            industryTypeList.clear();
                            for (int i = 0; i < industryTypes.size(); i++) {
                                industryTypeList.add(industryTypes.get(i).getValue());
                            }
                            new CustomSearchDialog(IndustryTypeActivity.this, industryTypeList, tvSelectedIndustryType,"SELECT INDUSTRY TYPE",tvSelectedIndustryType.getText().toString()).show();
                        }
                    } else {
                        CommonMethods.showToast(this, "No Industry type found, Please try again!");
                    }
                } else {
                    CommonMethods.showToast(this, "No Industry type found, Please try again!");
                }
            } else {
                CommonMethods.showToast(this, "No Industry type found, Please try again!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
    @Override
    public void onBackPressed() {

    }
}
