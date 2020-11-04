package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

public class ResidenceTypeActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvGivenLbl,tvGivenPreviousVal,tvResidenceTypeLbl;
    ImageView iv_residential_details_backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residence_type);
        initView();

    }

    private void initView()
    {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvResidenceTypeLbl=findViewById(R.id.tvResidenceTypeLbl);
        iv_residential_details_backBtn=findViewById(R.id.iv_residential_details_backBtn);
    }

    @Override
    public void onClick(View v) {

    }
}