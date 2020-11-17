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
    private TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit,tvSelectedBankName;
    private ImageView iv_app_bank_search;
    private List<String> bankNameList;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private Button btnNext;
    private String strEmployeeType="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_names);
        if(!CommonMethods.getStringValueFromKey(this,CommonStrings.EMP_TYPE_VAL).equals(""))
        {
            strEmployeeType=CommonMethods.getStringValueFromKey(this,CommonStrings.EMP_TYPE_VAL);
        }
        initView();

    }

    private void initView()
    {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvGivenValEdit=findViewById(R.id.tvGivenValEdit);
        tvGivenLbl.setText(getResources().getString(R.string.employment_type));
        tvGivenPreviousVal.setText(strEmployeeType);
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
            SpinnerManager.showSpinner(BankNamesActivity.this);
            retrofitInterface.getFromWeb(CommonStrings.BANK_NAME_URL).enqueue(this);
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