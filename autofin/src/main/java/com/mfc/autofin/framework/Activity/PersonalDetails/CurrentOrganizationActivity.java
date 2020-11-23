package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.IndustryTypeActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities.LastYearDepreciationActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.personal_details_models.BankNamesRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;
import utility.CustomOrgDialog;
import utility.CustomSearchDialog;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class CurrentOrganizationActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {


    private static final String TAG = CurrentOrganizationActivity.class.getSimpleName();
    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvOrganizationLbl, tvGivenValEdit, tvErrorMessage, tvOrgNameLbl;
    private EditText etOrganizationName;
    private Button btnNext;
    private View belowOrgName;
    private String strPreviousLbl = "", strPreviousVal = "", strExistingLoan = "", strOrgName = "";
    private List<String> orgList;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_organization);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvOrgNameLbl = findViewById(R.id.tvOrgName);
        tvOrganizationLbl = findViewById(R.id.tvOrganizationLbl);
        etOrganizationName = findViewById(R.id.etOrganizationName);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        belowOrgName = findViewById(R.id.belowOrgName);
        tvGivenValEdit.setOnClickListener(this);
        tvOrgNameLbl.setTypeface(CustomFonts.getRobotoRegularTF(this));
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        btnNext.setOnClickListener(this);
        etOrganizationName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 3) {
                    SpinnerManager.showSpinner(CurrentOrganizationActivity.this);
                    retrofitInterface.getFromWeb(CommonStrings.ORG_NAME_LIST_URL).enqueue(CurrentOrganizationActivity.this);

                }
            }
        });
        //etOrganizationName.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etOrganizationName.getText().toString().equals("")) {
                strOrgName = etOrganizationName.getText().toString();
                CommonStrings.cusEmpDetailsModel.setEmpOrgName(strOrgName);
                if (CommonMethods.getStringValueFromKey(this, CommonStrings.EMP_TYPE_VAL).equals(getResources().getString(R.string.lbl_salaried))) {
                    Intent intent = new Intent(this, JODOCurrentOrgActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvOrganizationLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, strOrgName);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, IndustryTypeActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvOrganizationLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, strOrgName);
                    startActivity(intent);
                }
            } else {
                belowOrgName.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText("Please enter name of the organization you are working for!");
            }
        } else if (v.getId() == R.id.etOrganizationName) {
            if (etOrganizationName.getText().toString().length() > 3) {
                SpinnerManager.showSpinner(CurrentOrganizationActivity.this);
                retrofitInterface.getFromWeb(CommonStrings.ORG_NAME_LIST_URL).enqueue(CurrentOrganizationActivity.this);
            }

        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        try {
            BankNamesRes bankNamesRes = new Gson().fromJson(strRes, BankNamesRes.class);
            if (bankNamesRes.getStatus() && bankNamesRes != null) {
                if (bankNamesRes.getData() != null) {
                    orgList = bankNamesRes.getData();
                    new CustomOrgDialog(CurrentOrganizationActivity.this, orgList, etOrganizationName).show();
                } else {
                    bankNamesRes.getMessage();
                }
            } else {
                CommonMethods.showToast(this, "No Bank names found,Please try again!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}