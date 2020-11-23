package com.mfc.autofin.framework.Activity.PersonalDetails.employment_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.EducationActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.review_activites.BSDListItemFragment;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;

import utility.CommonMethods;
import utility.CommonStrings;

public class ProfessionActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvProfessionVal;
    private Button btnNext;
    ImageView iv_personal_details_backBtn;
    String strEmpType = "", strPreviousLbl = "", strEmpRole = "";
    ArrayList<String> professionList;
    private Intent intent;

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
        professionList = new ArrayList<>();
        professionList.add("Architect");
        professionList.add("CA");
        professionList.add("Doctor");
        professionList.add("CS");
        professionList.add("Lawyer");
        professionList.add("Other");

        initView();

    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvProfessionVal = findViewById(R.id.tvProfessionVal);
        btnNext = findViewById(R.id.btnNext);
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strEmpRole);
        tvGivenValEdit.setOnClickListener(this);
        tvProfessionVal.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        iv_personal_details_backBtn.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.iv_personal_details_backBtn) {
            startActivity(new Intent(this, UserDOBActivity.class));
        } else*/
        if (v.getId() == R.id.tvProfessionVal) {
            if (professionList != null && professionList.size() > 0) {
                BSDListItemFragment BSDListItemFragment = new BSDListItemFragment(this, tvProfessionVal, professionList,"SELECT PROFESSION");
                BSDListItemFragment.show(getSupportFragmentManager(), "Profession List");
            }
        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.btnNext) {
            if (!tvProfessionVal.getText().toString().equals("")) {
                CommonMethods.setValueAgainstKey(this, CommonStrings.PROFESSION_VAL, tvProfessionVal.getText().toString());
                CommonStrings.cusEmpDetailsModel.setProfession(tvProfessionVal.getText().toString());
                Intent intent = new Intent(this, StartDateOfBusinessOrProfessionActivity.class);
                intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, getResources().getString(R.string.lbl_profession));
                intent.putExtra(CommonStrings.PREVIOUS_VALUE, CommonStrings.cusEmpDetailsModel.getProfession());
                startActivity(intent);
            } else {
                CommonMethods.showToast(this, "Please select Profession");
            }
        }
    }
}
