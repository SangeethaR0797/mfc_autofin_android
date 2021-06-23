package com.mfc.autofin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.bank_offer_activities.AdditionalFieldsActivity;

import kyc.DocumentUploadActivity;
import utility.AutoFinConstants;
import v2.view.HostActivity;

public class MainActivity extends AppCompatActivity {

    private Button mLaunchAutofin;
    private TextView tvBuildDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLaunchAutofin = findViewById(R.id.launchAutofin);
        tvBuildDate = findViewById(R.id.tvBuildDate);
        tvBuildDate.setText("Build on " + BuildConfig.BUILD_TIMESTAMP.toString());
        mLaunchAutofin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent autodashboard = new Intent(MainActivity.this, DocumentUploadActivity.class);
                // Intent autodashboard = new Intent(MainActivity.this, AutoFinDashBoardActivity.class);
                // Intent autodashboard = new Intent(MainActivity.this, AdditionalFieldsActivity.class);

                Intent autodashboard = new Intent(MainActivity.this, HostActivity.class);
                autodashboard.putExtra(AutoFinConstants.APP_NAME, "MFCBusiness");
                autodashboard.putExtra(AutoFinConstants.DEALER_ID, "242");
                autodashboard.putExtra(AutoFinConstants.USER_TYPE, "Dealer");
                startActivity(autodashboard);
            }
        });

    }
}