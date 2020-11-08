package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.CarHaveLoanCurrentlyActivity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleOwnerActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class NumOFExistingLoanActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ImageView iv_personal_details_backBtn;
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvNumOfExistingLoanLbl;
    private RadioButton rbLoan01, rbLoan02, rbLoan03, rbLoan04,rbLoan05, rbLoan06, rbNoPendingLoan;
    private String strSelectedBankName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_o_f_existing_loan);
        if (!CommonMethods.getStringValueFromKey(this, CommonStrings.BANK_NAME).isEmpty()) {
            strSelectedBankName = CommonMethods.getStringValueFromKey(this, CommonStrings.BANK_NAME);
        }
        initView();
    }

    private void initView() {
        iv_personal_details_backBtn = findViewById(R.id.iv_personal_details_backBtn);
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvNumOfExistingLoanLbl = findViewById(R.id.tvNumOfExistingLoanLbl);
        rbLoan01=findViewById(R.id.rbLoan01);
        rbLoan02=findViewById(R.id.rbLoan02);
        rbLoan03=findViewById(R.id.rbLoan03);
        rbLoan04=findViewById(R.id.rbLoan04);
        rbLoan05=findViewById(R.id.rbLoan05);
        rbLoan06=findViewById(R.id.rbLoan06);
        rbNoPendingLoan=findViewById(R.id.rbNoPendingLoan);
        tvGivenPreviousVal.setText(strSelectedBankName);
        iv_personal_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);
        rbLoan01.setOnCheckedChangeListener(this);
        rbLoan02.setOnCheckedChangeListener(this);
        rbLoan03.setOnCheckedChangeListener(this);
        rbLoan04.setOnCheckedChangeListener(this);
        rbLoan05.setOnCheckedChangeListener(this);
        rbLoan06.setOnCheckedChangeListener(this);
        rbNoPendingLoan.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.iv_personal_details_backBtn)
        {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton button, boolean isChecked) {
        if(isChecked)
        {
            if(button.getId()==R.id.rbLoan01)
            {
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                moveToNextPage(rbLoan01.getText().toString());

            }
            else if(button.getId()==R.id.rbLoan02)
            {
                rbLoan01.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                moveToNextPage(rbLoan02.getText().toString());

            }
            else if(button.getId()==R.id.rbLoan03)
            {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                moveToNextPage(rbLoan03.getText().toString());

            }
            else if(button.getId()==R.id.rbLoan04)
            {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                moveToNextPage(rbLoan04.getText().toString());

            }
            else if(button.getId()==R.id.rbLoan05)
            {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan06.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                moveToNextPage(rbLoan05.getText().toString());

            }
            else if(button.getId()==R.id.rbLoan06)
            {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbNoPendingLoan.setChecked(false);
                moveToNextPage(rbLoan06.getText().toString());
            }
            else if(button.getId()==R.id.rbNoPendingLoan)
            {
                rbLoan01.setChecked(false);
                rbLoan02.setChecked(false);
                rbLoan03.setChecked(false);
                rbLoan04.setChecked(false);
                rbLoan05.setChecked(false);
                rbLoan06.setChecked(false);
                moveToNextPage(rbNoPendingLoan.getText().toString());

            }
        }
    }

    private void moveToNextPage(String existingLoan) {
        CommonMethods.setValueAgainstKey(this,CommonStrings.NO_OF_EXISTING_LOAN,existingLoan);
        Intent intent = new Intent(this, CurrentOrganizationActivity.class);
        startActivity(intent);
    }
}