package controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.LeadDetailsActivity;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.CustomerData;
import utility.CommonStrings;
import utility.CustomFonts;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private String TAG = DashboardAdapter.class.getSimpleName();
    private Activity activity;
    ArrayList<CustomerData> customerDataList = new ArrayList<>();
    ArrayList<CustomerData> customerDataListRes = new ArrayList<>();


    public DashboardAdapter(Activity activity, List<CustomerData> customerDataList) {
        this.activity = activity;
        this.customerDataList.addAll(customerDataList);
        this.customerDataListRes.addAll(customerDataList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_lead_list, parent, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (customerDataList.get(position).getCaseId() != null)
            holder.tvCaseIDVal.setText(customerDataList.get(position).getCaseId());
        else
            Log.i(TAG, "onBindViewHolder: CaseId is null");

        if (customerDataList.get(position).getMainStatus() != null)
            holder.tvStatus.setText(customerDataList.get(position).getMainStatus());
        else
            Log.i(TAG, "onBindViewHolder: Status is null");

        if (customerDataList.get(position).getCreationDate() != null)
            holder.tvDate.setText(customerDataList.get(position).getCreationDate());
        else
            Log.i(TAG, "onBindViewHolder: Creation Date is null");

        if (customerDataList.get(position).getStatus() != null)
            holder.tvKYCStatus.setText(customerDataList.get(position).getStatus());
        else
            Log.i(TAG, "onBindViewHolder: KYC Status is null");

        if (customerDataList.get(position).getCustomerName() != null)
            holder.tvCName.setText(customerDataList.get(position).getCustomerName());
        else
            Log.i(TAG, "onBindViewHolder: Customer Name is null");

        if (customerDataList.get(position).getMobile() != null)
            holder.tvCMobileNum.setText(customerDataList.get(position).getMobile());

        else
            Log.i(TAG, "onBindViewHolder: Customer Mobile Number is null");

        if (customerDataList.get(position).getEmail() != null)
            holder.tvCEmailId.setText(customerDataList.get(position).getEmail());
        else
            Log.i(TAG, "onBindViewHolder: Customer Email is null");

        if (holder.tvStatus.getText().equals("open") || holder.tvStatus.getText().equals("Open")) {
            holder.tvViewDetails.setVisibility(View.VISIBLE);
        } else {
            holder.tvViewDetails.setVisibility(View.GONE);
        }

        if (holder.tvViewDetails.getVisibility() == View.VISIBLE) {
            holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "Yet to implement", Toast.LENGTH_LONG).show();
                }
            });
        }

        holder.tvViewDetails.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, LeadDetailsActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(CommonStrings.CUSTOMER_ID,customerDataList.get(position).getCustomerId());
                bundle.putString(CommonStrings.CASE_ID,holder.tvCaseIDVal.getText().toString());
                bundle.putString(CommonStrings.CNAME,holder.tvCName.getText().toString());
                bundle.putString(CommonStrings.CEMAIL,holder.tvCEmailId.getText().toString());
                bundle.putString(CommonStrings.CMOBILE_NUM,holder.tvCMobileNum.getText().toString());
                bundle.putString(CommonStrings.LEAD_CREATION_DATE,holder.tvDate.getText().toString());
                bundle.putString(CommonStrings.LEAD_STATUS,holder.tvStatus.getText().toString());
                bundle.putString(CommonStrings.KYC_STATUS,holder.tvKYCStatus.getText().toString());
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
        holder.btnCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.tvCMobileNum.getText().toString()));
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    activity.startActivity(callIntent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return customerDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCaseIDVal, tvStatus, tvDate, tvKYCStatus, tvCName, tvCMobileNum, tvCEmailId, tvViewDetails;
        Button btnCallCustomer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCaseIDVal = itemView.findViewById(R.id.tvCaseIDVal);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvKYCStatus = itemView.findViewById(R.id.tvKYCStatus);
            tvCName = itemView.findViewById(R.id.tvCName);
            tvCMobileNum = itemView.findViewById(R.id.tvCMobileNum);
            tvCEmailId = itemView.findViewById(R.id.tvCEmailId);
            tvViewDetails = itemView.findViewById(R.id.tvViewDetails);
            btnCallCustomer = itemView.findViewById(R.id.btnCallCustomer);
            tvCMobileNum.setTypeface(CustomFonts.getRobotoRegularTF(activity));
            tvCEmailId.setTypeface(CustomFonts.getRobotoRegularTF(activity));
            tvViewDetails.setTypeface(CustomFonts.getRobotoRegularTF(activity));
        }
    }

    public void filter(String charText) {

        charText = charText.toLowerCase(Locale.getDefault());
        customerDataList.clear();
        if (charText.length() == 0) {
            customerDataList.addAll(customerDataListRes);
        } else {
            for (CustomerData it : customerDataListRes) {
                if (it.getCustomerName() != null) {

                    if (it.getCustomerName().toLowerCase(Locale.getDefault()).contains(charText)) {
                        customerDataList.add(it);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

}
