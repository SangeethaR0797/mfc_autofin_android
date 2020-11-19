package com.mfc.autofin.framework.Activity.review_activites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

import model.basic_details.BasicData;
import model.basic_details.BasicDetailsReq;
import model.basic_details.EmploymentDetails;
import utility.CommonMethods;
import utility.CommonStrings;

import static retrofit_config.RetroBase.retrofitInterface;

public class DetailsUpdateActivity extends AppCompatActivity
{
    TextView tvCommonAppBarTitle,tvUpdateTitle,tvMatchingWithBankLbl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details_update);
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        tvCommonAppBarTitle.setText("UPDATING...");
        //retrofitInterface.getFromWeb(getBasicDetailsRequest(), CommonStrings.RECOMMENDED_BANK_URL).enqueue(this);

        initView();
    }

    private BasicDetailsReq getBasicDetailsRequest()
    {
        BasicDetailsReq basicDetailsReq=new BasicDetailsReq();
        basicDetailsReq.setUserId(CommonMethods.getStringValueFromKey(this,CommonStrings.DEALER_ID_VAL));
        basicDetailsReq.setUserType(CommonMethods.getStringValueFromKey(this,CommonStrings.USER_TYPE_VAL));
       // basicDetailsReq.setData(getBasicData());
        return basicDetailsReq;
    }

    /*private BasicData getBasicData()
    {
        BasicData basicData=new BasicData();
        basicData.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(this,CommonStrings.CUSTOMER_ID)));
       // basicData.setEmploymentDetails(getEmploymentDetails());

    }

    private EmploymentDetails getEmploymentDetails()
    {
        EmploymentDetails employmentDetails=new EmploymentDetails();
       // employmentDetails.set
    }
*/
    private void initView()
    {
        tvUpdateTitle=findViewById(R.id.tvUpdateTitle);
        tvMatchingWithBankLbl=findViewById(R.id.tvMatchingWithBankLbl);
    }
}
