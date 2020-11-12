package controller;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.R;

import java.util.List;

import model.bank_models.SelectBankData;

public class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.ViewHolder> {


    private String TAG = SelectBankAdapter.class.getSimpleName();
    private Activity activity;
    private List<SelectBankData> bankDetailsList;

    public SelectBankAdapter(Activity activity, List<SelectBankData> bankDetailsList) {
        this.activity = activity;
        this.bankDetailsList = bankDetailsList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_select_bank_row_item, parent, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (bankDetailsList.get(position).getBankStatus() != null)
        {
            if(bankDetailsList.get(position).getBankStatus().equalsIgnoreCase("Recommended"))
            {
                holder.tvBankStatus.setText(bankDetailsList.get(position).getBankStatus());
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.light_green));
            }
            else
            {
                holder.tvBankStatus.setText(bankDetailsList.get(position).getBankStatus());
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.grey_color));
            }


        }
        else
            Log.i(TAG, "onBindViewHolder: BankStatus is null");

        if (bankDetailsList.get(position).getLoanAmount() != null)
            holder.tvLoanAmountVal.setText(bankDetailsList.get(position).getLoanAmount());
        else
            Log.i(TAG, "onBindViewHolder: Loan Amount is null");

        if (bankDetailsList.get(position).getRateOfInterest() != null)
            holder.tvROIVal.setText(bankDetailsList.get(position).getRateOfInterest());
        else
            Log.i(TAG, "onBindViewHolder: ROI is null");

        if (bankDetailsList.get(position).getEmiAmount() != null)
            holder.tvEMIVal.setText(bankDetailsList.get(position).getEmiAmount());
        else
            Log.i(TAG, "onBindViewHolder: EMI AMount is null");

        if (bankDetailsList.get(position).getTenure() != null)
            holder.tvTenureVal.setText(bankDetailsList.get(position).getTenure());
        else
            Log.i(TAG, "onBindViewHolder: Tenure is null");


        holder.tvApplyNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Yet to implement", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return bankDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBankStatus, tvLoanAmountVal, tvROIVal, tvEMIVal, tvTenureVal, tvApplyNow;

        public ViewHolder(View itemView) {
            super(itemView);

            tvBankStatus = itemView.findViewById(R.id.tvBankStatus);
            tvLoanAmountVal = itemView.findViewById(R.id.tvLoanAmountVal);
            tvROIVal = itemView.findViewById(R.id.tvROIVal);
            tvEMIVal = itemView.findViewById(R.id.tvEMIVal);
            tvTenureVal = itemView.findViewById(R.id.tvTenureVal);
            tvApplyNow = itemView.findViewById(R.id.tvApplyNow);

        }

    }
}
