package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

public class ReferenceActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_common_bar_backBtn;
    TextView tvCommonAppBarTitle,tvRFirstNameLbl,tvLastNameLbl,tvPhoneNoLbl,tvREmailLbl;
    EditText etRFirstName,etRLastName,etRPhoneNo,etREmail;
    Button btnNext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);
        initView();
    }

    private void initView() {
        iv_common_bar_backBtn=findViewById(R.id.iv_common_bar_backBtn);
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        tvRFirstNameLbl=findViewById(R.id.tvRFirstNameLbl);
        tvLastNameLbl=findViewById(R.id.tvLastNameLbl);
        tvPhoneNoLbl=findViewById(R.id.tvPhoneNoLbl);
        tvREmailLbl=findViewById(R.id.tvREmailLbl);
        etRFirstName=findViewById(R.id.etRFirstName);
        etRLastName=findViewById(R.id.etRLastName);
        etRPhoneNo=findViewById(R.id.etRPhoneNo);
        etREmail=findViewById(R.id.etREmail);
        btnNext=findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnNext)
        {

        }
    }
}
