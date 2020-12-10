package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import utility.Global;

import static retrofit_config.RetroBase.retrofitInterface;

public class ResidenceTypeActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {

    private static final String TAG = ResidenceTypeActivity.class.getSimpleName();
    TextView tvGivenLbl, tvGivenPreviousVal, tvGivenValEdit, tvResidenceTypeLbl;
    ImageView iv_residential_details_backBtn;
    String strCResidence = "",strResType="";
    LinearLayout llRadioGroup;
    List<ResidenceType> residenceTypeList;
    RadioGroup customRG;
    Button btnNext;
    RadioButton rbOwnedBySelfOrSpouse, rbOwnedByParent, rbRentedWithFamily, rbRentedWithFriends, rbRentedStayingAlone, rbPayingGuest, rbHostel, rbCompanyProvided;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_type);
        if (CommonStrings.customResDetails.getMoveInResidenceYear().length() > 0) {
            strCResidence = CommonStrings.customResDetails.getMoveInResidenceYear();
        } else {
            strCResidence = "";
        }
        retrofitInterface.getFromWeb(Global.customerAPI_Master_URL+CommonStrings.RES_TYPE_URL_END).enqueue(this);
        Log.i(TAG, "onCreate: "+CommonMethods.getStringValueFromKey(this,CommonStrings.CUSTOMER_ID));
        if(CommonStrings.IS_OLD_LEAD)
        {
            if(CommonStrings.customResDetails.getResidenceType()!=null && !CommonStrings.customResDetails.getResidenceType().isEmpty())
            {
                strResType= String.valueOf(CommonStrings.customResDetails.getResidenceType());
            }
        }
        initView();
    }

    private void initView() {
        tvGivenLbl = findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal = findViewById(R.id.tvGivenPreviousVal);
        tvResidenceTypeLbl = findViewById(R.id.tvResidenceTypeLbl);
        tvGivenValEdit = findViewById(R.id.tvGivenValEdit);
        customRG = findViewById(R.id.customRG);
        llRadioGroup = findViewById(R.id.llRadioGroup);
        btnNext=findViewById(R.id.btnNext);
        customRG.setOnClickListener(this);
        iv_residential_details_backBtn = findViewById(R.id.iv_residential_details_backBtn);
        rbOwnedBySelfOrSpouse = findViewById(R.id.rbOwnedBySelfOrSpouse);
        rbOwnedByParent = findViewById(R.id.rbOwnedByParent);
        rbRentedWithFamily = findViewById(R.id.rbRentedWithFamily);
        rbRentedWithFriends = findViewById(R.id.rbRentedWithFriends);
        rbRentedStayingAlone = findViewById(R.id.rbRentedStayingAlone);
        rbPayingGuest = findViewById(R.id.rbPayingGuest);
        rbHostel = findViewById(R.id.rbHostel);
        rbCompanyProvided = findViewById(R.id.rbCompanyProvided);
        tvGivenLbl.setText(getResources().getString(R.string.lbl_when_moved_to_current_residence));
        tvGivenPreviousVal.setText(strCResidence);
        iv_residential_details_backBtn.setVisibility(View.INVISIBLE);
        tvGivenValEdit.setOnClickListener(this);
        if(!strResType.isEmpty())
        {
            if(strResType.equalsIgnoreCase(rbOwnedBySelfOrSpouse.getText().toString()))
            {
                rbOwnedBySelfOrSpouse.setChecked(true);
                rbOwnedByParent.setChecked(false);
                rbRentedWithFamily.setChecked(false);
                rbRentedWithFriends.setChecked(false);
                rbRentedStayingAlone.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbHostel.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbOwnedByParent.getText().toString()))
            {
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(true);
                rbRentedWithFamily.setChecked(false);
                rbRentedWithFriends.setChecked(false);
                rbRentedStayingAlone.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbHostel.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbRentedWithFamily.getText().toString()))
            {
                rbRentedWithFamily.setChecked(true);
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(false);
                rbRentedWithFriends.setChecked(false);
                rbRentedStayingAlone.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbHostel.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbRentedWithFriends.getText().toString()))
            {
                rbRentedWithFriends.setChecked(true);
                rbRentedWithFamily.setChecked(false);
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(false);
                rbRentedStayingAlone.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbHostel.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbRentedStayingAlone.getText().toString()))
            {
                rbRentedStayingAlone.setChecked(true);
                rbRentedWithFriends.setChecked(false);
                rbRentedWithFamily.setChecked(false);
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbHostel.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbPayingGuest.getText().toString()))
            {
                rbPayingGuest.setChecked(true);
                rbRentedStayingAlone.setChecked(true);
                rbRentedWithFriends.setChecked(false);
                rbRentedWithFamily.setChecked(false);
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(false);
                rbHostel.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbHostel.getText().toString()))
            {
                rbHostel.setChecked(true);
                rbRentedStayingAlone.setChecked(false);
                rbRentedWithFriends.setChecked(false);
                rbRentedWithFamily.setChecked(false);
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbCompanyProvided.setChecked(false);

            }
            else if(strResType.equalsIgnoreCase(rbCompanyProvided.getText().toString()))
            {
                rbCompanyProvided.setChecked(true);
                rbRentedStayingAlone.setChecked(false);
                rbRentedWithFriends.setChecked(false);
                rbRentedWithFamily.setChecked(false);
                rbOwnedBySelfOrSpouse.setChecked(false);
                rbOwnedByParent.setChecked(false);
                rbPayingGuest.setChecked(false);
                rbHostel.setChecked(false);

            }
        }
        rbOwnedBySelfOrSpouse.setOnClickListener(this);
        rbOwnedByParent.setOnClickListener(this);
        rbRentedWithFamily.setOnClickListener(this);
        rbRentedWithFriends.setOnClickListener(this);
        rbRentedStayingAlone.setOnClickListener(this);
        rbPayingGuest.setOnClickListener(this);
        rbHostel.setOnClickListener(this);
        rbCompanyProvided.setOnClickListener(this);

        if(CommonStrings.IS_OLD_LEAD)
        {
            btnNext.setVisibility(View.VISIBLE);
            btnNext.setOnClickListener(this);
        }
        else
        {
            btnNext.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        ResidenceTypeRes residenceTypeRes = new Gson().fromJson(strRes, ResidenceTypeRes.class);
        try {
                if (residenceTypeRes.getStatus() && residenceTypeRes.getData() != null) {
                residenceTypeList = residenceTypeRes.getData().getTypes();
            } else {
                CommonMethods.showToast(this, "No type available!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            finish();
        }

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.rbOwnedBySelfOrSpouse) {
            strResType=rbOwnedBySelfOrSpouse.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbOwnedByParent) {
            strResType=rbOwnedByParent.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbRentedWithFamily) {
            strResType=rbRentedWithFamily.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbRentedWithFriends) {
            strResType=rbRentedWithFriends.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbRentedStayingAlone) {
            strResType=rbRentedStayingAlone.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbPayingGuest) {
            strResType=rbPayingGuest.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbHostel) {
            strResType=rbHostel.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        } else if (v.getId() == R.id.rbCompanyProvided) {
            strResType=rbCompanyProvided.getText().toString();
            if(!CommonStrings.IS_OLD_LEAD)
            {
                moveToNextPage();
            }
        }else if(v.getId()==R.id.btnNext)
        { if(!strResType.isEmpty())
            {
                moveToNextPage();
            }
        }
    }

    private void moveToNextPage()
    {
        CommonStrings.customResDetails.setResidenceType(strResType);
        startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
    }

    @Override
    public void onBackPressed() {

    }

}