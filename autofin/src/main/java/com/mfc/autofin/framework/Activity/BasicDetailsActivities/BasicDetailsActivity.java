package com.mfc.autofin.framework.Activity.BasicDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

public class BasicDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        initView();
    }

    private void initView()
    {
    }
}