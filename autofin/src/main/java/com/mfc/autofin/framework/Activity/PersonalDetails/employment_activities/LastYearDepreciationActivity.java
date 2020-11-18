package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class LastYearDepreciationActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl,tvGivenPreviousVal, tvGivenValEdit,tvErrorMessage;
    private EditText etLastYearDepreciation;
    private View belowETYearsOE;
    private Button btnNext;
    private String strJoiningDate = "", strLastYearDepreciation = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_year_depreciation);
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn=findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvErrorMessage=findViewById(R.id.tvErrorMessage);
        etLastYearDepreciation=findViewById(R.id.etLastYearDepreciation);
        belowETYearsOE=findViewById(R.id.belowETYearsOE);
        btnNext=findViewById(R.id.btnNext);
        tvGivenValEdit.setOnClickListener(this);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!etLastYearDepreciation.getText().toString().isEmpty()) {
                strLastYearDepreciation = etLastYearDepreciation.getText().toString();
                CommonMethods.setValueAgainstKey(this, CommonStrings.INCOME_AFTER_TAX, strLastYearDepreciation);
                startActivity(new Intent(this, ITRAuditedActivity.class));
            } else {
                belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.error_red));
                tvErrorMessage.setVisibility(View.VISIBLE);
                tvErrorMessage.setText(getResources().getString(R.string.please_enter_year_of_experience));
            }

        } else if (v.getId() == R.id.etLastYearDepreciation) {
            belowETYearsOE.setBackgroundColor(getResources().getColor(R.color.very_dark_blue));
            if (tvErrorMessage.getVisibility() == View.VISIBLE) {
                tvErrorMessage.setVisibility(View.GONE);
            }
        }
    }
}
