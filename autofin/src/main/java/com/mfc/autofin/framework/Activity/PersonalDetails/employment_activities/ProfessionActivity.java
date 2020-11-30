package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.CurrentOrganizationActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.EducationActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.review_activites.BSDListItemFragment;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;

import model.personal_details_models.BankNamesRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomOrgDialog;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class ProfessionActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {
    private static final String TAG =ProfessionActivity.class.getSimpleName() ;
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit;
    EditText etProfessionalVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    String strEmpType = "", strPreviousLbl = "", strEmpRole = "";
    ArrayList<String> professionList= new ArrayList<>();;
    private Intent intent;
    private String strOnET="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strEmpRole = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        initView();


    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        etProfessionalVal = findViewById(R.id.etProfessionalVal);
        btnNext = findViewById(R.id.btnNext);
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strEmpRole);
        tvGivenValEdit.setOnClickListener(this);
        etProfessionalVal.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 3) {
                    String temp = ""+s;
                    if (temp.equalsIgnoreCase("dev")) {
                        strOnET=temp;
                        SpinnerManager.showSpinner(ProfessionActivity.this);
                        retrofitInterface.getFromWeb(CommonStrings.PROFESSION_FILTER_URL).enqueue(ProfessionActivity.this);

                    } else {
                        strOnET=temp;
                        SpinnerManager.showSpinner(ProfessionActivity.this);
                        retrofitInterface.getFromWeb(CommonStrings.PROFESSION_URL).enqueue(ProfessionActivity.this);
                    }
                }
            }
        });

        btnNext.setOnClickListener(this);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etProfessionalVal.getText().toString().equals("")) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.PROFESSION_VAL, etProfessionalVal.getText().toString());
                CommonStrings.cusEmpDetails.setProfession(etProfessionalVal.getText().toString());
                Intent intent = new Intent(this, StartDateOfBusinessOrProfessionActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, getResources().getString(R.string.lbl_profession));
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, CommonStrings.cusEmpDetails.getProfession());
                startActivity(intent);
            } else {
                CommonMethods.showToast(this, "Please select Profession");
            }
        }
    }
    @Override
    public void onBackPressed() {

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
                    if(professionList.size()==0) {
                        professionList.addAll(bankNamesRes.getData());
                        new CustomOrgDialog(ProfessionActivity.this, professionList, etProfessionalVal, strOnET).show();
                    }
                else
                    {
                        professionList.clear();
                        professionList.addAll(bankNamesRes.getData());
                        new CustomOrgDialog(ProfessionActivity.this, professionList, etProfessionalVal, strOnET).show();

                    }} else {
                    bankNamesRes.getMessage();
                }
            } else {
                CommonMethods.showToast(this, "No Profession found , Please try again!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
