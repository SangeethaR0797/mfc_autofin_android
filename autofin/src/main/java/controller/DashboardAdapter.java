package controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.CustomerData;
import utility.CustomFonts;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private String TAG = DashboardAdapter.class.getSimpleName();
    private Activity activity;
    List<CustomerData> customerDataList;

    public DashboardAdapter(Activity activity, List<CustomerData> customerDataList) {
        this.activity = activity;
        this.customerDataList = customerDataList;
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
            holder.tvLeadComplete.setVisibility(View.VISIBLE);
        } else {
            holder.tvLeadComplete.setVisibility(View.GONE);
        }

        if (holder.tvLeadComplete.getVisibility() == View.VISIBLE) {
            holder.tvLeadComplete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(activity, "Yet to implement", Toast.LENGTH_LONG).show();
                }
            });
        }


        holder.btnCallCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:" + holder.tvCMobileNum.getText().toString()));
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},1);
                }
                else
                {
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

        TextView tvCaseIDVal,tvStatus,tvDate,tvKYCStatus,tvCName,tvCMobileNum,tvCEmailId,tvLeadComplete;
        Button btnCallCustomer;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCaseIDVal = itemView.findViewById(R.id.tvCaseIDVal);
            tvStatus= itemView.findViewById(R.id.tvStatus);
            tvDate= itemView.findViewById(R.id.tvDate);
            tvKYCStatus= itemView.findViewById(R.id.tvKYCStatus);
            tvCName= itemView.findViewById(R.id.tvCName);
            tvCMobileNum= itemView.findViewById(R.id.tvCMobileNum);
            tvCEmailId= itemView.findViewById(R.id.tvCEmailId);
            tvLeadComplete= itemView.findViewById(R.id.tvLeadComplete);
            btnCallCustomer=itemView.findViewById(R.id.btnCallCustomer);
            tvCMobileNum.setTypeface(CustomFonts.getRobotoRegularTF(activity));
            tvCEmailId.setTypeface(CustomFonts.getRobotoRegularTF(activity));
            tvLeadComplete.setTypeface(CustomFonts.getRobotoRegularTF(activity));
        }
    }

}
