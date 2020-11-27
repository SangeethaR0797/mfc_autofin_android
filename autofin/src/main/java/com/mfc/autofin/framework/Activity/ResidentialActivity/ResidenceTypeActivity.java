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
        /*customRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                for(int i=0; i<rg.getChildCount(); i++) {
                    RadioButton btn = (RadioButton) rg.getChildAt(i);
                    if(btn.getId() == checkedId) {
                        Log.i(TAG, "onCheckedChanged: "+checkedId);
                        String text = btn.getText().toString();
                        CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this,CommonStrings.RESIDENCE_TYPE,text);
                        startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
                        return;
                    }
                }
            }
        });
*/

    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        ResidenceTypeRes residenceTypeRes = new Gson().fromJson(strRes, ResidenceTypeRes.class);
        try {
            if (residenceTypeRes.getStatus() && residenceTypeRes.getData() != null) {

                residenceTypeList = residenceTypeRes.getData().getTypes();
                //attachToAdapter(residenceTypeList);
            } else {
                CommonMethods.showToast(this, "No type available!");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            finish();
        }

    }

   /* private void attachToAdapter(List<ResidenceType> residenceTypeList) {

        for (int i = 0; i < residenceTypeList.size(); i++) {
            radioButton = (RadioButton) getLayoutInflater().inflate(R.layout.radio_button, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                radioButton.setId(View.generateViewId());
                Log.i(TAG, "attachToAdapter: "+radioButton.getId());
            }
            radioButton.setText(residenceTypeList.get(i).getDisplayLabel());
            customRG.addView(radioButton);

        }
    }*/

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onClick(View v) {
        /*int checkedRadioButtonId = customRG.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(checkedRadioButtonId);
        String text = radioButton.getText().toString();

            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this,CommonStrings.RESIDENCE_TYPE,text);
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));

        if (v.getId() == R.id.iv_residential_details_backBtn) {
            startActivity(new Intent(this, AutoFinDashBoardActivity.class));
        } else */
        if (v.getId() == R.id.tvGivenValEdit) {
            finish();
        } else if (v.getId() == R.id.rbOwnedBySelfOrSpouse) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbOwnedBySelfOrSpouse.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbOwnedByParent) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbOwnedByParent.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbRentedWithFamily) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbRentedWithFamily.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbRentedWithFriends) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbRentedWithFriends.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbRentedStayingAlone) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbRentedStayingAlone.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbPayingGuest) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbPayingGuest.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbHostel) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbHostel.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        } else if (v.getId() == R.id.rbCompanyProvided) {
            CommonMethods.setValueAgainstKey(ResidenceTypeActivity.this, CommonStrings.RESIDENCE_TYPE, rbCompanyProvided.getText().toString());
            startActivity(new Intent(ResidenceTypeActivity.this, UserDOBActivity.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}