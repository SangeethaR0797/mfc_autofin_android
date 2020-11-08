package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.R;

import java.util.List;


import model.residential_models.ResidenceType;
import model.residential_models.ResidenceTypeRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global_URLs;

import static retrofit_config.RetroBase.retrofit;
import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.IBB_VEH_DETAILS_END_POINT;

public class ResidenceTypeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = ResidenceTypeActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvResidenceTypeLbl;
    ImageView iv_residential_details_backBtn;
    String strCResidence = "";
    LinearLayout llRadioGroup;
    List<ResidenceType> residenceTypeList;
    ViewGroup customRG;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_type);
        if (CommonMethods.getStringValueFromKey(this, CommonStrings.MOVED_TO_CRESIDENCE).length() > 0) {
            strCResidence = CommonMethods.getStringValueFromKey(this, CommonStrings.MOVED_TO_CRESIDENCE);
        } else {
            strCResidence = "";
        }
        retrofitInterface.getFromWeb(CommonStrings.RES_TYPE_URL).enqueue(this);
        initView();

    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvResidenceTypeLbl = findViewById(R.id.tvResidenceTypeLbl);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        customRG = findViewById(R.id.customRG);
        llRadioGroup = findViewById(R.id.llRadioGroup);
        customRG.setOnClickListener(this);
        iv_residential_details_backBtn = findViewById(R.id.iv_residential_details_backBtn);
        tvGivenPreviousVal.setText(strCResidence);
        iv_residential_details_backBtn.setOnClickListener(this);
        tvGivenValEdit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        ResidenceTypeRes residenceTypeRes = new Gson().fromJson(strRes, ResidenceTypeRes.class);
        try {
            if (residenceTypeRes.getStatus() && residenceTypeRes.getData() != null) {
                residenceTypeList = residenceTypeRes.getData().getTypes();
                attachToAdapter(residenceTypeList);
            } else {
                CommonMethods.showToast(this, "No type available!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            finish();
        }

    }

    private void attachToAdapter(List<ResidenceType> residenceTypeList) {

        for (int i = 0; i < residenceTypeList.size(); i++) {
            radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null);
            radioButton.setId(i);
            radioButton.setText(residenceTypeList.get(i).getDisplayLabel());
            customRG.addView(radioButton);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
                }
            });
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}