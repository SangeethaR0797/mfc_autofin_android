package com.mfc.autofin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;

import kyc.DocumentUploadActivity;
import utility.AutoFinConstants;

public class MainActivity extends AppCompatActivity {

    private Button mLaunchAutofin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLaunchAutofin = findViewById(R.id.launchAutofin);
        mLaunchAutofin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent autodashboard = new Intent(MainActivity.this, AutoFinDashBoardActivity.class);
                     autodashboard.putExtra(AutoFinConstants.APP_NAME,"MFCBusiness");
                     autodashboard.putExtra(AutoFinConstants.DEALER_ID,"242");
                     autodashboard.putExtra(AutoFinConstants.USER_TYPE,"Dealer");
                startActivity(autodashboard);
            }
        });

    }
}