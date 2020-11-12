package com.mfc.autofin.framework.Activity.review_activites;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.R;

import java.util.ArrayList;

import controller.SelectBankAdapter;
import model.bank_models.SelectBankData;

public class ViewBankActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvMatchingBankLbl, tvCommonAppBarTitle;
    private RecyclerView rvSelectBank;
    private ImageView iv_common_bar_backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bank);
        initView();
    }

    private void initView() {
        tvMatchingBankLbl=findViewById(R.id.tvMatchingBankLbl);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        rvSelectBank = findViewById(R.id.rvSelectBank);
        tvCommonAppBarTitle.setText("SELECT BANK");
        iv_common_bar_backBtn.setOnClickListener(this);
        attachToAdapter();
    }

    private void attachToAdapter() {
        ArrayList<SelectBankData> bankDataList = new ArrayList<>();
        bankDataList.add(new SelectBankData("Recommended", "2,00,000", "8.25 - 8.75%", "19,200 - 21,200", "12 Months"));
        bankDataList.add(new SelectBankData("Popular", "1,00,000", "7.25 - 7.75%", "15,200 - 19,200", "9 Months"));
        tvMatchingBankLbl.setText(bankDataList.size()+" "+getResources().getString(R.string.lbl_selected_bank));
        SelectBankAdapter selectBankAdapter = new SelectBankAdapter(ViewBankActivity.this, bankDataList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSelectBank.setLayoutManager(layoutManager);
        rvSelectBank.setAdapter(selectBankAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_common_bar_backBtn) {
            finish();
        }
    }
}
