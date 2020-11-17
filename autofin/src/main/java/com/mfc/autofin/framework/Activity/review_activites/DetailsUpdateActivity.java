package com.mfc.autofin.framework.Activity.review_activites;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.R;

public class DetailsUpdateActivity extends AppCompatActivity
{
    TextView tvCommonAppBarTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details_update);
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        tvCommonAppBarTitle.setText("UPDATING...");
    }
}
