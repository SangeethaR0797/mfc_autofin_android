package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomFonts;

public class YearOfExperienceActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvYearOfExpLbl, tvYears, tvErrorMessage;
    private EditText etNOOfYears;
    private View belowETYearsOE;
    private Button btnNext;
    private String strJoiningDate = "", strYearsOfExperience = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_of_experience);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.CURRENT_ORG_JOINING_DATE).isEmpty()) {
            strJoiningDate = CommonMethods.getStringValueFromKey(this, CommonStrings.CURRENT_ORG_JOINING_DATE);
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvYearOfExpLbl = findViewById(R.id.tvYearOfExpLbl);
        tvYears = findViewById(R.id.tvYears);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
        etNOOfYears = findViewById(R.id.etNOOfYears);
        belowETYearsOE = findViewById(R.id.belowETYearsOE);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_joining_date_of_your_current_org));
        tvGivenPreviousVal.setText(strJoiningDate);
        tvYears.setTypeface(CustomFonts.getRobotoRegularTF(this));
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        etNOOfYears.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            if (v.getId() == R.id.iv_personal_details_backBtn) {
                startActivity(new Intent(this, AutoFinDashBoardActivity.class));
            } else if (v.getId() == R.id.tvGivenValEdit) {
                finish();
            } else if (v.getId() == R.id.btnNext) {
                if (!etNOOfYears.getText().toString().isEmpty()) {
                    strYearsOfExperience = etNOOfYears.getText().toString();
                    CommonMethods.setValueAgainstKey(this, CommonStrings.YEARS_OF_EXPERIENCE, strYearsOfExperience);
                    startActivity(new Intent(this, SalaryModeActivity.class));
                } else {
                    belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.error_red));
                    tvErrorMessage.setVisibility(View.VISIBLE);
                    tvErrorMessage.setText(getResources().getString(R.string.please_enter_year_of_experience));
                }

            } else if (v.getId() == R.id.etNOOfYears) {
                belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));
                if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                    tvErrorMessage.setVisibility(View.GONE);
                }
            }
        }
    }
}


