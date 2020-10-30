package com.mfc.autofin.framework.Activity.VehicleDetailsActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utility.CommonMethods;

public class VehRegNumAns extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_vehDetails_backBtn;
    TextView tvVehCategoryQn, tvRegNoLbl;
    EditText etVehRegNo;

    long delay = 2000; // 1 seconds after user stops typing
    long last_text_edit = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veh_reg_num_ans);
        initView();
    }

    private void initView() {
        iv_vehDetails_backBtn = findViewById(R.id.iv_vehDetails_backBtn);
        tvVehCategoryQn = findViewById(R.id.tvVehCategoryQn);
        tvRegNoLbl = findViewById(R.id.tvRegNoLbl);
        etVehRegNo = findViewById(R.id.etVehRegNo);
        iv_vehDetails_backBtn.setOnClickListener(this);
        Runnable input_finish_checker = new Runnable() {
            public void run() {
                if(etVehRegNo.getText().toString().length()>=10 && etVehRegNo.getText().toString().length()<=15)
                {
                    if (System.currentTimeMillis() > (last_text_edit + delay - 1000)) {
                        validateRegNo(etVehRegNo.getText().toString());
                    }
                }
                else
                {
                    if (System.currentTimeMillis() > (last_text_edit + delay - 500)) {
                        Toast.makeText(VehRegNumAns.this,"Please enter valid Vehicle Registration number",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };

        etVehRegNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before,
                                      int count) {
                //You need to remove this to run only once
                handler.removeCallbacks(input_finish_checker);

            }

            @Override
            public void afterTextChanged(final Editable s) {
                //avoid triggering event when text is empty
                if (s.length() > 0) {
                    last_text_edit = System.currentTimeMillis();
                    handler.postDelayed(input_finish_checker, delay);
                } else {

                }
            }
        });


    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_vehDetails_backBtn) {
            Intent intent = new Intent(VehRegNumAns.this, VehRegNumActivity.class);
            startActivity(intent);
        }

    }
    private void validateRegNo(String finalString)
    {
        Pattern p = Pattern.compile("^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$");
        Matcher m = p.matcher(finalString);
        if(m.find())
        {
            Toast.makeText(this,"Vehicle Registration number updated",Toast.LENGTH_SHORT).show();
            CommonMethods.setValueAgainstKey(VehRegNumAns.this,"vehicle_reg_no",finalString);
            Intent intent = new Intent(VehRegNumAns.this, VehRegistrationYear.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Please enter valid Vehicle Registration number",Toast.LENGTH_SHORT).show();
        }
    }
    }