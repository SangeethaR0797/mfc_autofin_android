package com.mfc.autofin.framework.Activity.review_activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.autofin.framework.R;

import model.custom_model.ReviewData;

public class ReviewActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCommonAppBarTitle,tvReviewNav;
    private CheckBox cbAgreeTAndC;
    private Button btnReviewNext;
    private boolean isTAndCAgreed=false;
    private ImageView iv_common_bar_backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        try{
            initView();
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }

    }

    private void initView()
    {
        try{
            tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
            iv_common_bar_backBtn=findViewById(R.id.iv_common_bar_backBtn);
            tvReviewNav=findViewById(R.id.tvReviewNav);
            cbAgreeTAndC=findViewById(R.id.cbAgreeTAndC);
            btnReviewNext=findViewById(R.id.btnReviewNext);
            tvCommonAppBarTitle.setText(getResources().getString(R.string.str_review));
            iv_common_bar_backBtn.setOnClickListener(this);
            tvReviewNav.setOnClickListener(this);
            btnReviewNext.setOnClickListener(this);
            cbAgreeTAndC.setOnClickListener(this);
        }catch(Exception exception)
        {
            exception.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.iv_common_bar_backBtn)
        {
            finish();
        }
        else if(v.getId()==R.id.cbAgreeTAndC)
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
        else if(v.getId()==R.id.tvReviewNav)
        {
            try{
                Intent intent=new Intent(ReviewActivity.this,ReviewDetailsActivity.class);
                startActivity(intent);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else if(v.getId()==R.id.btnReviewNext)
        {
            if(isTAndCAgreed)
            {
                try{
                    startActivity(new Intent(this,DetailsUpdateActivity.class));
                }catch (Exception exception){exception.printStackTrace();}
            }
        }

    }
}