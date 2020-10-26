package controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mfc.autofin.framework.R;
import java.util.List;
import model.CustomerData;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder>
{

    TextView tvCaseIDVal,tvStatus,tvDate,tvKYCStatus,tvCName,tvCMobileNum,tvCEmailId,tvLeadComplete;
    Button btnCallCustomer;
    private String TAG=DashboardAdapter.class.getSimpleName();
    private Activity activity;
    List<CustomerData> customerDataList;
    public DashboardAdapter(Activity activity, List<CustomerData> customerDataList) {
        this.activity=activity;
        this.customerDataList=customerDataList;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        if(customerDataList.get(position).getCaseId()!=null)
        tvCaseIDVal.setText(customerDataList.get(position).getCaseId());
        else
            Log.i(TAG, "onBindViewHolder: CaseId is null");

        if(customerDataList.get(position).getStatus()!=null)
            tvStatus.setText(customerDataList.get(position).getStatus());
        else
            Log.i(TAG, "onBindViewHolder: Status is null");

        if(customerDataList.get(position).getCreationDate()!=null)
            tvDate.setText(customerDataList.get(position).getCreationDate());
        else
            Log.i(TAG, "onBindViewHolder: Creation Date is null");

        if(customerDataList.get(position).getBankStatus()!=null)
            tvKYCStatus.setText(customerDataList.get(position).getBankStatus());
        else
            Log.i(TAG, "onBindViewHolder: KYC Status is null");

        if(customerDataList.get(position).getCustomerName()!=null)
            tvCName.setText(customerDataList.get(position).getCustomerName());
        else
            Log.i(TAG, "onBindViewHolder: Customer Name is null");

        if(customerDataList.get(position).getMobile()!=null)
            tvCMobileNum.setText(customerDataList.get(position).getMobile());
        else
            Log.i(TAG, "onBindViewHolder: Customer Mobile Number is null");

        if(customerDataList.get(position).getEmail()!=null)
            tvCEmailId.setText(customerDataList.get(position).getEmail());
        else
            Log.i(TAG, "onBindViewHolder: Customer Email is null");

        if(!tvStatus.equals("open") && !tvStatus.equals("Open"))
        {
            tvLeadComplete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return customerDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


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
        }

    }

}
