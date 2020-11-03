package com.mfc.autofin.framework.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehPostInspectionActivity;
import com.mfc.autofin.framework.R;

import java.io.File;

import utility.file_attachment.FileAttachment;

public class ValuationReportBSD extends BottomSheetDialogFragment implements View.OnClickListener {

    private LinearLayout llBrowseReport, llOpenCamera;
    TextView tvSkipNowReport;
    Button btnReportAttachmentNext;
    Activity activity;

    public ValuationReportBSD(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_valuation_report_b_s_d_list_dialog, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        llBrowseReport = view.findViewById(R.id.llBrowseReport);
        llOpenCamera = view.findViewById(R.id.llOpenCamera);
        tvSkipNowReport = view.findViewById(R.id.tvSkipNowReport);
        btnReportAttachmentNext = view.findViewById(R.id.btnReportAttachmentNext);
        llBrowseReport.setOnClickListener(this);
        llOpenCamera.setOnClickListener(this);
        tvSkipNowReport.setOnClickListener(this);
        btnReportAttachmentNext.setOnClickListener(this);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llBrowseReport) {
            // Toast.makeText(activity.getApplication().getApplicationContext(),"Hi",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()); // a directory
            intent.setDataAndType(uri, "New ");
            activity.startActivity(Intent.createChooser(intent, "Open folder"));
        } else if (v.getId() == R.id.btnReportAttachmentNext) {
            Intent intent = new Intent(activity, VehPostInspectionActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.llOpenCamera) {
            FileAttachment fileAttachment = new FileAttachment();
            fileAttachment.chooseImage(activity, 1);
        }
    }
}