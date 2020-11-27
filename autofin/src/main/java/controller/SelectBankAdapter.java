package controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.bank_offer_activities.AdditionalFieldsActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import kyc.DocumentUploadActivity;
import model.add_lead_details.AddLeadResponse;
import model.bank_models.BankListData;
import model.bank_models.BankListResponse;
import model.bank_models.SelectBankData;
import model.bank_models.SelectRecBankReq;
import model.bank_models.SelectedBankData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import utility.CommonMethods;
import utility.CommonStrings;

import static retrofit_config.RetroBase.retrofitInterface;

public class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.ViewHolder> implements Callback<Object> {


    private String TAG = SelectBankAdapter.class.getSimpleName();
    private Activity activity;
    private List<BankListData> bankDetailsList;

    public SelectBankAdapter(Activity activity, List<BankListData> bankDetailsList) {
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
        if (bankDetailsList.get(position).getBankId() != null)
        {
            if(bankDetailsList.get(position).getBankId()==27)
            {
                holder.tvBankStatus.setText("Recommended");
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.light_green));
            }
            else
            {
                holder.tvBankStatus.setText("Popular");
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.grey_color));
            }


        }
        else
            Log.i(TAG, "onBindViewHolder: BankStatus is null");

        if (bankDetailsList.get(position).getLoanAmount() != null)
            holder.tvLoanAmountVal.setText(bankDetailsList.get(position).getLoanAmount());
        else
            Log.i(TAG, "onBindViewHolder: Loan Amount is null");

        if (bankDetailsList.get(position).getRoi() != null)
            holder.tvROIVal.setText(bankDetailsList.get(position).getRoi());
        else
            Log.i(TAG, "onBindViewHolder: ROI is null");

        if (bankDetailsList.get(position).getEmi() != null)
            holder.tvEMIVal.setText(bankDetailsList.get(position).getEmi());
        else
            Log.i(TAG, "onBindViewHolder: EMI AMount is null");

        if (bankDetailsList.get(position).getTenure() != null)
            holder.tvTenureVal.setText(bankDetailsList.get(position).getTenure());
        else
            Log.i(TAG, "onBindViewHolder: Tenure is null");


        holder.tvApplyNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(bankDetailsList.get(position).getBankName().equalsIgnoreCase("HDFC Bank"))
                {
                    activity.startActivity(new Intent(activity, AdditionalFieldsActivity.class));
                }
                else
                {
                    activity.startActivity(new Intent(activity, DocumentUploadActivity.class));
                }

               // invokeSelectedBankRequest(bankDetailsList.get(position).getBankId().toString());

            }
        });


    }

    private void invokeSelectedBankRequest(String bankId)
    {
        SelectRecBankReq selectRecBankReq=new SelectRecBankReq();
        selectRecBankReq.setUserId(CommonMethods.getStringValueFromKey(activity,CommonStrings.DEALER_ID_VAL));
        selectRecBankReq.setUserType(CommonMethods.getStringValueFromKey(activity,CommonMethods.getStringValueFromKey(activity,CommonStrings.USER_TYPE_VAL)));
        SelectedBankData selectedBankData=new SelectedBankData();
        selectedBankData.setCaseId("0242201118000006");
        selectedBankData.setCustomerId("6416");
        selectedBankData.setRecommendedBankId(bankId);
        selectRecBankReq.setData(selectedBankData);
        retrofitInterface.getFromWeb(selectRecBankReq, CommonStrings.UPLOAD_KYC_DOC_URL).enqueue(this);

    }

    @Override
    public int getItemCount() {
        return bankDetailsList.size();
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response)
    {
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        AddLeadResponse selectedBankRes=new Gson().fromJson(strRes,AddLeadResponse.class);

        try
        {
            if(selectedBankRes!=null && selectedBankRes.getStatus())
            {
                CommonMethods.showToast(activity,selectedBankRes.getMessage());
                activity.startActivity(new Intent(activity,AdditionalFieldsActivity.class));
            }
            else
            {
                CommonMethods.showToast(activity,"Please try again");
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }


    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

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
