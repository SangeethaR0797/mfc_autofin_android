package com.mfc.autofin.framework.Activity.ResidentialActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

public class MovedToCityMYActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvGivenLbl,tvGivenPreviousVal,tvMovedToCity;
    ImageView iv_residential_details_backBtn;
    LinearLayout llMonthAndYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moved_to_city_m_y);
        initView();
    }

    private void initView() {
        tvGivenLbl=findViewById(R.id.tvGivenLbl);
        tvGivenPreviousVal=findViewById(R.id.tvGivenPreviousVal);
        tvMovedToCity=findViewById(R.id.tvMovedToCity);
        llMonthAndYear=findViewById(R.id.llMonthAndYear);
        iv_residential_details_backBtn=findViewById(R.id.iv_residential_details_backBtn);
    }

    @Override
    public void onClick(View v) {

    }
}