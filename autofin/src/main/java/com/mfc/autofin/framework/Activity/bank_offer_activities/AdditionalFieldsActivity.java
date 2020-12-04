package com.mfc.autofin.framework.Activity.bank_offer_activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.mfc.autofin.framework.R;
import java.util.ArrayList;
import java.util.List;
import controller.AdditionalFieldAdapter;
import model.add_lead_details.AddLeadRequest;
import model.addtional_fields.AdditionFields;
import model.addtional_fields.AdditionalFieldData;
import model.addtional_fields.AdditionalFieldResponse;
import model.addtional_fields.BankName;
import model.addtional_fields.GetAdditionFieldsReq;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class AdditionalFieldsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {
    private static final String TAG = AdditionalFieldsActivity.class.getSimpleName();
    private ImageView iv_common_bar_backBtn;
    private TextView tvCommonAppBarTitle,tvReferenceLbl;
    private LinearLayout llRefContent;
    private RecyclerView rvAdditionalFields;
    private List<AdditionFields> additionFieldsList=new ArrayList<AdditionFields>();
    private Button btnNext;
    private String mUserId="",mUserType="",mAppName="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_fields);
        SpinnerManager.showSpinner(this);
        retrofitInterface.getFromWeb(getAdditionalFieldReq(),CommonStrings.GET_ADDITIONAL_FIELDS).enqueue(this);
        Bundle data = getIntent().getExtras();
        if (data != null) {
            mUserId = data.getString(AutoFinConstants.DEALER_ID);
            mUserType = data.getString(AutoFinConstants.USER_TYPE);
            mAppName = data.getString(AutoFinConstants.APP_NAME);

            // Added by Sangeetha
            CommonMethods.setValueAgainstKey(this, CommonStrings.DEALER_ID_VAL, mUserId);
            CommonMethods.setValueAgainstKey(this, CommonStrings.USER_TYPE_VAL, mUserType);
            CommonMethods.setValueAgainstKey(this, CommonStrings.APP_NAME_VAL, mAppName);
        }
        initView();
    }

    private GetAdditionFieldsReq getAdditionalFieldReq()
    {
        GetAdditionFieldsReq additionFieldsReq=new GetAdditionFieldsReq();
        additionFieldsReq.setUserId(mUserId);
        additionFieldsReq.setUserType(mUserType);
        BankName bankName=new BankName();
        bankName.setBankName("HDFC Bank");
        additionFieldsReq.setData(bankName);
        return additionFieldsReq;
    }

    private void initView()
    {
        iv_common_bar_backBtn=findViewById(R.id.iv_common_bar_backBtn);
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        tvReferenceLbl=findViewById(R.id.tvReferenceLbl);
        llRefContent=findViewById(R.id.llRefContent);
        rvAdditionalFields=findViewById(R.id.rvAdditionalFields);
        iv_common_bar_backBtn.setVisibility(View.INVISIBLE);
        tvCommonAppBarTitle.setText("REFERENCE");
        tvReferenceLbl.setText(getResources().getString(R.string.lbl_reference_name));
        btnNext=findViewById(R.id.btnNext);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_common_bar_backBtn)
        {

        }
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes=new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: "+strRes);
        try
        {
            AdditionalFieldResponse additionalFieldResponse=new Gson().fromJson(strRes,AdditionalFieldResponse.class);
            if(additionalFieldResponse.getStatus() && additionalFieldResponse!=null)
            {
                if(additionalFieldResponse.getData()!=null)
                {
                    additionFieldsList.addAll(additionalFieldResponse.getData());
                    attachToAdapter();
                }
                else
                {
                    additionalFieldResponse.getMessage();
                }
            }
            else
            {
                CommonMethods.showToast(this,"No data found,Please try again!");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void attachToAdapter()
    {
        AdditionalFieldAdapter additionalFieldAdapter = new AdditionalFieldAdapter(AdditionalFieldsActivity.this, additionFieldsList,btnNext);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvAdditionalFields.setLayoutManager(layoutManager);
        rvAdditionalFields.setAdapter(additionalFieldAdapter);
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
