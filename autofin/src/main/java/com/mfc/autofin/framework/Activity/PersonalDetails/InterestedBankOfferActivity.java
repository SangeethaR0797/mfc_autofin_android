package com.mfc.autofin.framework.Activity.PersonalDetails;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.bank_offer_activities.InterestedBankOfferDetailsActivity;
import com.mfc.autofin.framework.R;

import utility.CommonMethods;
import utility.CommonStrings;

public class InterestedBankOfferActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_common_bar_backBtn;
    TextView tvCommonAppBarTitle, tv_hi_cname, tvInterestedBankOfferCaseId, tv_will_contact_you_lbl, tv_check_list_2_lbl, tv_check_list_3_lbl, tvLbl;
    Button btnUpdate, btnTrackLoan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interested_bank_offer);
        initView();
    }

    private void initView() {
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tv_hi_cname = findViewById(R.id.tv_hi_cname);
        tvInterestedBankOfferCaseId = findViewById(R.id.tvInterestedBankOfferCaseId);
        tv_will_contact_you_lbl = findViewById(R.id.tv_will_contact_you_lbl);
        tv_check_list_2_lbl = findViewById(R.id.tv_check_list_2_lbl);
        tv_check_list_3_lbl = findViewById(R.id.tv_check_list_3_lbl);
        tvLbl = findViewById(R.id.tvLbl);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnTrackLoan = findViewById(R.id.btnTrackLoan);
        tv_hi_cname.setText(CommonStrings.customBasicDetails.getFullName());
        tvInterestedBankOfferCaseId.setText("Your Case Id is " + CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID));
        tvCommonAppBarTitle.setText("INTERESTED BANK OFFER");
        iv_common_bar_backBtn.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnTrackLoan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_common_bar_backBtn) {
            finish();
        } else if (v.getId() == R.id.btnUpdate) {
            CommonMethods.showToast(this, "Yet to implement");
        } else if (v.getId() == R.id.btnTrackLoan) {
            CommonMethods.showToast(this, "Details Updated");
            CommonMethods.redirectToDashboard(this);
        }
    }
}
