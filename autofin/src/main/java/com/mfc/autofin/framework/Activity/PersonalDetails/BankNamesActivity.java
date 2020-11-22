package com.mfc.autofin.framework.Activity.PersonalDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class BankNamesActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = BankNamesActivity.class.getSimpleName();
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvSelectedBankName,tvSelectBankNameLbl;
    private ImageView iv_app_bank_search;
    private List<String> bankNameList;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private Button btnNext;
    private String strEmployeeType="",strPreviousLbl="",strPreviousVal="";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_names);
        try {
            intent = getIntent();
            strPreviousLbl = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE_LBL);
            strPreviousVal = intent.getStringExtra(CommonStrings.PREVIOUS_VALUE);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        initView();

    }

    private void initView()
    {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvSelectBankNameLbl=findViewById(R.id.tvSelectBankNameLbl);
        if(CommonMethods.getStringValueFromKey(this,CommonStrings.EMP_TYPE_VAL).equals(getResources().getString(R.string.lbl_salaried)))
        {
            tvSelectBankNameLbl.setText("SELECT SALARY ACCOUNT BANK NAME");
        }
        else
        {
            tvSelectBankNameLbl.setText(getResources().getString(R.string.lbl_select_bank_name));
        }
        tvGivenLbl.setText(strPreviousLbl);
        tvGivenPreviousVal.setText(strPreviousVal);
        tvGivenValEdit.setOnClickListener(this);
        tvSelectedBankName=findViewById(R.id.tvSelectedBankName);
        iv_app_bank_search=findViewById(R.id.iv_app_bank_search);
        btnNext=findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        tvSelectedBankName.setOnClickListener(this);
        iv_app_bank_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.iv_app_bank_search)
        {
            if(CommonMethods.isInternetWorking(this))
            {
                SpinnerManager.showSpinner(this);
                retrofitInterface.getFromWeb(CommonStrings.BANK_NAME_URL).enqueue(this);
            }
            else
            {
                CommonMethods.showToast(this,"Please check your Internet Connection");
            }

        }
        else if(v.getId()==R.id.tvGivenValEdit)
        {
            finish();
        }
        else if(v.getId()==R.id.btnNext)
        {
            if(!tvSelectedBankName.getText().toString().equals(""))
            {
                CommonMethods.setValueAgainstKey(this,CommonStrings.BANK_NAME,tvSelectedBankName.getText().toString());
                startActivity(new Intent(this,NumOFExistingLoanActivity.class));
            }
            else
            {
                CommonMethods.showToast(this,"Please select any bank");
            }
        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strRes);
        try
        {
            BankNamesRes bankNamesRes=new Gson().fromJson(strRes,BankNamesRes.class);
            if(bankNamesRes.getStatus() && bankNamesRes!=null)
            {
                if(bankNamesRes.getData()!=null)
                {
                    SpinnerManager.hideSpinner(this);
                    bankNameList=bankNamesRes.getData();
                    new CustomSearchDialog(BankNamesActivity.this,bankNameList,tvSelectedBankName).show();
                }
                else
                {
                    bankNamesRes.getMessage();
                }
            }
            else
            {
                CommonMethods.showToast(this,"No Bank names found,Please try again!");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

}