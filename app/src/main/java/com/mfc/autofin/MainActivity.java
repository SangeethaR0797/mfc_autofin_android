package com.mfc.autofin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.bank_offer_activities.AdditionalFieldsActivity;

import kyc.DocumentUploadActivity;
import utility.AutoFinConstants;
import v2.view.HostActivity;

public class MainActivity extends AppCompatActivity {

    private Button mLaunchAutofin;
    private TextView tvBuildDate;
    String[] dealerCode = {"Dealer >> 242", "Dealer >> 2857"};
    private Spinner spDealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLaunchAutofin = findViewById(R.id.launchAutofin);
        tvBuildDate = findViewById(R.id.tvBuildDate);
        spDealer = findViewById(R.id.spDealer);
        tvBuildDate.setText("Build on " + BuildConfig.BUILD_TIMESTAMP.toString());

        // ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dealerCode);
        ArrayAdapter ad = new ArrayAdapter(this, R.layout.item, R.id.tvItem, dealerCode);
        spDealer.setAdapter(ad);

        mLaunchAutofin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent autodashboard = new Intent(MainActivity.this, DocumentUploadActivity.class);
                // Intent autodashboard = new Intent(MainActivity.this, AutoFinDashBoardActivity.class);
                // Intent autodashboard = new Intent(MainActivity.this, AdditionalFieldsActivity.class);
                String[] value = spDealer.getSelectedItem().toString().split(">>");
                Intent autodashboard = new Intent(MainActivity.this, HostActivity.class);
                autodashboard.putExtra(AutoFinConstants.APP_NAME, "MFCBusiness");
                // autodashboard.putExtra(AutoFinConstants.DEALER_ID, "242");
                autodashboard.putExtra(AutoFinConstants.DEALER_ID, value[1].toString().trim());
                autodashboard.putExtra(AutoFinConstants.USER_TYPE, "Dealer");
                startActivity(autodashboard);
            }
        });

    }
}