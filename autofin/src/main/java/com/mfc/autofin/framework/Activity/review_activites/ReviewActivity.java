package com.mfc.autofin.framework.Activity.review_activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCommonAppBarTitle;
    private CheckBox cbAgreeTAndC;
    private Button btnReviewNext;
    private boolean isTAndCAgreed;
    private String strTAndC="<font color='#0061e6'><u>terms and conditions</u></font>",strPrivacyPolicy="<font color='#0061e6'><u>privacy policy</u></font>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        initView();
    }

    private void initView()
    {
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        cbAgreeTAndC=findViewById(R.id.cbAgreeTAndC);
        btnReviewNext=findViewById(R.id.btnReviewNext);
        tvCommonAppBarTitle.setText(getResources().getString(R.string.str_review));
        cbAgreeTAndC.setText("I Agree to the "+ Html.fromHtml(strTAndC)+" and "+Html.fromHtml(strPrivacyPolicy));
        cbAgreeTAndC.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.cbAgreeTAndC)
        {
            if(cbAgreeTAndC.isChecked())
            {
                isTAndCAgreed=true;
            }
            else
            {
                isTAndCAgreed=false;
            }
        }
        if(v.getId()==R.id.btnReviewNext)
        {
            if(isTAndCAgreed)
            {
                startActivity(new Intent(this,ReviewActivity.class));
            }
        }
    }
}