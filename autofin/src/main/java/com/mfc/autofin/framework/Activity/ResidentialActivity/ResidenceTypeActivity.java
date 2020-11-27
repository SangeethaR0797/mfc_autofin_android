package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
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
    RadioGroup customRG;
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
        rbOwnedBySelfOrSpouse.setOnClickListener(this);
        rbOwnedByParent.setOnClickListener(this);
        rbRentedWithFamily.setOnClickListener(this);
        rbRentedWithFriends.setOnClickListener(this);
        rbRentedStayingAlone.setOnClickListener(this);
        rbRentedStayingAlone.setOnClickListener(this);
        rbPayingGuest.setOnClickListener(this);
        rbHostel.setOnClickListener(this);
        rbCompanyProvided.setOnClickListener(this);

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
            CommonStrings.customResDetails.setResidenceType(rbOwnedBySelfOrSpouse.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbOwnedByParent) {
            CommonStrings.customResDetails.setResidenceType(rbOwnedByParent.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbRentedWithFamily) {
            CommonStrings.customResDetails.setResidenceType(rbRentedWithFamily.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbRentedWithFriends) {
            CommonStrings.customResDetails.setResidenceType(rbRentedWithFriends.getText().toString());
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbRentedWithFriends.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbRentedStayingAlone) {
            CommonStrings.customResDetails.setResidenceType(rbRentedStayingAlone.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbPayingGuest) {
            CommonStrings.customResDetails.setResidenceType(rbPayingGuest.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbHostel) {
            CommonStrings.customResDetails.setResidenceType(rbHostel.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbCompanyProvided) {
            CommonStrings.customResDetails.setResidenceType(rbCompanyProvided.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}