package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.personal_details_models.BankNamesRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.CustomSearchDialog;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class SavingsBankAccountActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {
    private static final String TAG = BankNamesActivity.class.getSimpleName();
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvSavingsBankNameLbl, tvSelectSavingsBank;
    private ImageView iv_app_savings_bank_search, iv_savings_hdfc, iv_savings_icici, iv_savings_axis, iv_savings_sbi;
    private List<String> bankNameList;
    private Button btnNext;
    private String strPreviousLbl = "", strPreviousVal = "";
    private LinearLayout llSelectSavingsBank;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_savings_bank);
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
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        tvSavingsBankNameLbl = findViewById(R.id.tvSavingsBankNameLbl);
        tvSelectSavingsBank = findViewById(R.id.tvSelectSavingsBank);
        llSelectSavingsBank = findViewById(R.id.llSelectSavingsBank);
        iv_app_savings_bank_search = findViewById(R.id.iv_app_bank_search);
        iv_savings_hdfc = findViewById(R.id.iv_savings_hdfc);
        iv_savings_icici = findViewById(R.id.iv_savings_icici);
        iv_savings_axis = findViewById(R.id.iv_savings_axis);
        iv_savings_sbi = findViewById(R.id.iv_savings_sbi);
        btnNext = findViewById(R.id.btnNext);
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
        llSelectSavingsBank.setOnClickListener(this);
        iv_savings_hdfc.setOnClickListener(this);
        iv_savings_sbi.setOnClickListener(this);
        iv_savings_icici.setOnClickListener(this);
        iv_savings_axis.setOnClickListener(this);
        btnNext.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llSelectSavingsBank) {
            if (CommonMethods.isInternetWorking(this)) {
                SpinnerManager.showSpinner(this);
                retrofitInterface.getFromWeb(CommonStrings.BANK_NAME_URL).enqueue(this);
            } else {
                CommonMethods.showToast(this, "Please check your Internet Connection");
            }

        } else if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if(v.getId()==R.id.iv_savings_hdfc)
        {
            tvSelectSavingsBank.setText("HDFC");
        }
        else if(v.getId()==R.id.iv_savings_icici)
        {
            tvSelectSavingsBank.setText("ICICI");
        }
        else if(v.getId()==R.id.iv_savings_axis)
        {
            tvSelectSavingsBank.setText("AXIS");
        } else if(v.getId()==R.id.iv_savings_sbi)
        {
            tvSelectSavingsBank.setText("SBI");
        }
        else if (v.getId() == R.id.btnNext) {
            if (!tvSelectSavingsBank.getText().toString().equals("")) {
                CommonStrings.customPersonalDetails.setSavingsAccount(tvSelectSavingsBank.getText().toString());
                if (CommonStrings.cusEmpDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_salaried))) {
                    Intent intent = new Intent(this, YearOfExperienceActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvSavingsBankNameLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, tvSelectSavingsBank.getText().toString());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, PanCardNumberActivity.class);
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE_LBL, tvSavingsBankNameLbl.getText().toString());
                    intent.putExtra(CommonStrings.PREVIOUS_VALUE, tvSelectSavingsBank.getText().toString());
                    startActivity(intent);
                }
            } else {
                CommonMethods.showToast(this, "Please select any bank");
            }
        }

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        try {
            BankNamesRes bankNamesRes = new Gson().fromJson(strRes, BankNamesRes.class);
            if (bankNamesRes.getStatus() && bankNamesRes != null) {
                if (bankNamesRes.getData() != null) {
                    SpinnerManager.hideSpinner(this);
                    bankNameList = bankNamesRes.getData();
                    new CustomSearchDialog(SavingsBankAccountActivity.this, bankNameList, tvSelectSavingsBank, "SELECT BANK NAME", tvSelectSavingsBank.getText().toString()).show();
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

    @Override
    public void onBackPressed() {

    }
}
