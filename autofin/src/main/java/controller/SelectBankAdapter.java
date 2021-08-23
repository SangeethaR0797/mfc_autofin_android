package controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.PersonalDetails.InterestedBankOfferActivity;
import com.mfc.autofin.framework.Activity.bank_offer_activities.AdditionalFieldsActivity;
import com.mfc.autofin.framework.R;

import java.util.List;

import model.add_lead_details.AddLeadResponse;
import model.addtional_fields.AdditionalFieldResponse;
import model.addtional_fields.BankName;
import model.addtional_fields.GetAdditionFieldsReq;
import model.bank_models.BankListData;
import model.bank_models.SelectRecBankReq;
import model.bank_models.SelectedBankData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class SelectBankAdapter extends RecyclerView.Adapter<SelectBankAdapter.ViewHolder> implements Callback<Object> {


    private String TAG = SelectBankAdapter.class.getSimpleName();
    private Activity activity;
    private List<BankListData> bankDetailsList;
    String bankName = "";

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
        if (bankDetailsList.get(position).getBankId() != null) {
            bankName = bankDetailsList.get(position).getBankName();
            if (bankDetailsList.get(position).getBankId() == 27) {
                holder.tvBankStatus.setText("Recommended");
                holder.ivBankName.setBackground(activity.getResources().getDrawable(R.drawable.ic_hdfc));
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.autofin_light_green));
            } else if (bankDetailsList.get(position).getBankName().contains("ICICI")) {
                holder.tvBankStatus.setText("Popular");
                holder.ivBankName.setBackground(activity.getResources().getDrawable(R.drawable.ic_icici));
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.autofin_grey_color));
            } else if (bankDetailsList.get(position).getBankName().contains("AXIS")) {
                holder.tvBankStatus.setText("Popular");
                holder.ivBankName.setBackground(activity.getResources().getDrawable(R.drawable.ic_axis));
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.autofin_grey_color));
            } else if (bankDetailsList.get(position).getBankName().contains("SBI")) {
                holder.tvBankStatus.setText("Popular");
                holder.ivBankName.setBackground(activity.getResources().getDrawable(R.drawable.ic_sbi));
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.autofin_grey_color));
            } else {
                holder.tvBankStatus.setText("Popular");
                holder.ivBankName.setBackground(activity.getResources().getDrawable(0));
                holder.tvBankStatus.setTextColor(activity.getResources().getColor(R.color.autofin_grey_color));
            }


        } else
            Log.i(TAG, "onBindViewHolder: BankStatus is null");

        if (bankDetailsList.get(position).getLoanAmount() != null) {
            String loanAmount = activity.getResources().getString(R.string.rupees_symbol) + " " + CommonMethods.getFormattedAmount(Double.parseDouble(bankDetailsList.get(position).getLoanAmount()));
            holder.tvLoanAmountVal.setText(loanAmount);

        } else
            Log.i(TAG, "onBindViewHolder: Loan Amount is null");

        if (bankDetailsList.get(position).getRoi() != null)
            holder.tvROIVal.setText(CommonMethods.getFormattedDouble(Double.parseDouble(bankDetailsList.get(position).getRoi())));
        else
            Log.i(TAG, "onBindViewHolder: ROI is null");

        if (bankDetailsList.get(position).getEmi() != null) {
            String emiVal = activity.getResources().getString(R.string.rupees_symbol) + " " + CommonMethods.getFormattedDouble(Double.parseDouble(bankDetailsList.get(position).getEmi()));
            holder.tvEMIVal.setText(emiVal);
        } else
            Log.i(TAG, "onBindViewHolder: EMI AMount is null");

        if (bankDetailsList.get(position).getTenure() != null)
            holder.tvTenureVal.setText(bankDetailsList.get(position).getTenure());
        else
            Log.i(TAG, "onBindViewHolder: Tenure is null");


        holder.tvApplyNow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

               /* if(bankDetailsList.get(position).getBankName().equalsIgnoreCase("HDFC Bank"))
                {
                    activity.startActivity(new Intent(activity, AdditionalFieldsActivity.class));
                }
                else
                {
                    activity.startActivity(new Intent(activity, DocumentUploadActivity.class));
                }
*/
                invokeSelectedBankRequest(bankDetailsList.get(position).getBankId().toString());

            }
        });


    }

    private void invokeSelectedBankRequest(String bankId) {
        SelectRecBankReq selectRecBankReq = new SelectRecBankReq();
        selectRecBankReq.setUserId(CommonMethods.getStringValueFromKey(activity, CommonStrings.DEALER_ID_VAL));
        selectRecBankReq.setUserType(CommonMethods.getStringValueFromKey(activity, CommonMethods.getStringValueFromKey(activity, CommonStrings.USER_TYPE_VAL)));
        SelectedBankData selectedBankData = new SelectedBankData();
        selectedBankData.setCaseId(CommonMethods.getStringValueFromKey(activity, CommonStrings.CASE_ID));
        selectedBankData.setCustomerId(CommonMethods.getStringValueFromKey(activity, CommonStrings.CUSTOMER_ID));
        selectedBankData.setRecommendedBankId(bankId);
        selectRecBankReq.setData(selectedBankData);
        SpinnerManager.showSpinner(activity);
        retrofitInterface.getFromWeb(selectRecBankReq, Global.customer_bank_baseURL + CommonStrings.SELECT_RECOMMENDED_BANK_URL).enqueue(this);

    }

    @Override
    public int getItemCount() {
        return bankDetailsList.size();
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(activity);
        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);

        if (url.contains(CommonStrings.SELECT_RECOMMENDED_BANK_URL)) {
            AddLeadResponse selectedBankRes = new Gson().fromJson(strRes, AddLeadResponse.class);

            try {
                if (selectedBankRes != null && selectedBankRes.getStatus()) {
                    CommonMethods.showToast(activity, selectedBankRes.getMessage());
                    CommonMethods.setValueAgainstKey(activity, CommonStrings.BANK_NAME, bankName);
                    SpinnerManager.showSpinner(activity);
                    retrofitInterface.getFromWeb(getAdditionalFieldReq(), Global.customerDetails_BaseURL + CommonStrings.GET_ADDITIONAL_FIELDS).enqueue(this);
                } else {
                    CommonMethods.showToast(activity, "Please try again");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (url.contains(CommonStrings.GET_ADDITIONAL_FIELDS)) {
            try {
                AdditionalFieldResponse additionalFieldResponse = new Gson().fromJson(strRes, AdditionalFieldResponse.class);
                if (additionalFieldResponse.getStatus() && additionalFieldResponse != null) {
                    if (additionalFieldResponse.getData() != null) {
                        Log.i(TAG, "onResponse: " + additionalFieldResponse.getData().toString());
                        CommonStrings.additionFieldsList = additionalFieldResponse.getData();
                        Log.i(TAG, "onResponse: " + CommonStrings.additionFieldsList.toString());
                        activity.startActivity(new Intent(activity, AdditionalFieldsActivity.class));
                    } else {
                        additionalFieldResponse.getMessage();
                    }
                } else if (!additionalFieldResponse.getStatus()) {

                    if (additionalFieldResponse.getMessage().equalsIgnoreCase("Additional Field not found for this Bank ID")) {
                        activity.startActivity(new Intent(activity, InterestedBankOfferActivity.class));
                    } else {
                        CommonMethods.showToast(activity, "No data found");
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }


    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBankStatus, tvLoanAmountVal, tvROIVal, tvEMIVal, tvTenureVal, tvApplyNow;
        ImageView ivBankName;

        public ViewHolder(View itemView) {
            super(itemView);
            ivBankName = itemView.findViewById(R.id.ivBankName);
            tvBankStatus = itemView.findViewById(R.id.tvBankStatus);
            tvLoanAmountVal = itemView.findViewById(R.id.tvLoanAmountVal);
            tvROIVal = itemView.findViewById(R.id.tvROIVal);
            tvEMIVal = itemView.findViewById(R.id.tvEMIVal);
            tvTenureVal = itemView.findViewById(R.id.tvTenureVal);
            tvApplyNow = itemView.findViewById(R.id.tvApplyNow);

        }

    }

    private GetAdditionFieldsReq getAdditionalFieldReq() {
        GetAdditionFieldsReq additionFieldsReq = new GetAdditionFieldsReq();
        additionFieldsReq.setUserId(CommonMethods.getStringValueFromKey(activity, CommonStrings.DEALER_ID_VAL));
        additionFieldsReq.setUserType(CommonMethods.getStringValueFromKey(activity, CommonStrings.USER_TYPE_VAL));
        BankName bankName = new BankName();
        bankName.setBankName(CommonMethods.getStringValueFromKey(activity, CommonStrings.BANK_NAME));
        additionFieldsReq.setData(bankName);
        return additionFieldsReq;
    }
}
