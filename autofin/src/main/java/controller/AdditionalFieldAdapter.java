package controller;

import android.app.Activity;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.autofin.framework.R;

import java.util.List;

import model.addtional_fields.AdditionFields;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.RES_CITY_URL;

public class AdditionalFieldAdapter extends RecyclerView.Adapter<AdditionalFieldAdapter.ViewHolder> implements Callback<Object> {

    private static final String TAG = AdditionalFieldAdapter.class.getSimpleName();
    Activity activity;
    List<AdditionFields> additionalFields;
    Button btnNext;

    public AdditionalFieldAdapter(Activity activity, List<AdditionFields> additionalFields, Button btnNext) {
        this.activity = activity;
        this.additionalFields = additionalFields;
        this.btnNext = btnNext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.layout_additional_fields_row_item, parent, false);
        Log.i(TAG, "onCreateViewHolder: ");
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String fieldName = "";
        boolean isCurrentMandatory=false,isFNameMandatory=false,isLNameMandatory=false,isMNUmMandatory=false,isRelMandatory=false,isPinCodeMandatory=false,isStateMandatory=false,isCityMandatory=false,isWorkAdd1Mandatory=false,isWorkAdd2Mandatory=false;
        if (additionalFields.get(position).getFieldName() != null && !additionalFields.get(position).getFieldName().isEmpty()) {
            fieldName = additionalFields.get(position).getFieldName();
            if(additionalFields.get(position).getIsMandatory()) {
                isCurrentMandatory=additionalFields.get(position).getIsMandatory();
                holder.tvTextLbl.setText(fieldName+" "+activity.getResources().getString(R.string.lbl_asterick));
            }
            else
            {
                isCurrentMandatory=additionalFields.get(position).getIsMandatory();
                holder.tvTextLbl.setText(fieldName);
            }
        }
        if (fieldName.equalsIgnoreCase("Reference First Name")) {
            isFNameMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.VISIBLE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        } else if (fieldName.equalsIgnoreCase("Reference Last Name")) {
            isLNameMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.VISIBLE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }else if (fieldName.equalsIgnoreCase("Reference Mobile")) {
            isMNUmMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.VISIBLE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }
        else if (fieldName.equalsIgnoreCase("Relation")) {
            isRelMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.VISIBLE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }
        else if (fieldName.equalsIgnoreCase("Work Pin Code")) {
            isPinCodeMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.VISIBLE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }
        else if (fieldName.equalsIgnoreCase("State")) {
            isStateMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.VISIBLE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }else if (fieldName.equalsIgnoreCase("City")) {
            isCityMandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.VISIBLE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }else if (fieldName.equalsIgnoreCase("Work Address Line 1")) {
            isWorkAdd1Mandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.VISIBLE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.GONE);

        }else if (fieldName.equalsIgnoreCase("Work Address Line 2")) {
            isWorkAdd2Mandatory=isCurrentMandatory;
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.VISIBLE);
            holder.llCommon.setVisibility(View.GONE);

        }
        else
        {
            holder.llRFirstName.setVisibility(View.GONE);
            holder.llRLastName.setVisibility(View.GONE);
            holder.llRPhoneNo.setVisibility(View.GONE);
            holder.llRelationShip.setVisibility(View.GONE);
            holder.llAFPinCode.setVisibility(View.GONE);
            holder.llState.setVisibility(View.GONE);
            holder.llCity.setVisibility(View.GONE);
            holder.llWorkAddress1.setVisibility(View.GONE);
            holder.llWorkAddress2.setVisibility(View.GONE);
            holder.llCommon.setVisibility(View.VISIBLE);
        }

        if(holder.llAFPinCode.getVisibility()==View.VISIBLE)
        {
            holder.etAFResPinCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strPinCode = holder.etAFResPinCode.getText().toString();
                    if (strPinCode != "") {
                        SpinnerManager.showSpinner(activity);
                        retrofitInterface.getFromWeb(RES_CITY_URL + holder.etAFResPinCode.getText().toString()).enqueue(AdditionalFieldAdapter.this);
                    } else if (strPinCode != "") {
                        Toast.makeText(activity, activity.getResources().getString(R.string.please_enter_pincode), Toast.LENGTH_SHORT).show();
                    } else if (strPinCode.length() != 6) {
                        Toast.makeText(activity, activity.getResources().getString(R.string.please_enter_proper_pincode), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalFields.size();
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTextLbl, tvAddFState, tvAddFCity;
        EditText etRFirstName, etField, etRLastName, etRPhoneNo, etAFResPinCode, etWorkAddress1, etWorkAddress2;
        Spinner addFieldSpinner;
        LinearLayout llRFirstName, llRLastName, llRPhoneNo, llRelationShip, llAFPinCode, llState, llCity, llCommon, llWorkAddress1, llWorkAddress2;
        Button btnAFPinCodeCheck;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTextLbl = itemView.findViewById(R.id.tvTextLbl);
            tvAddFState = itemView.findViewById(R.id.tvAddFState);
            tvAddFCity = itemView.findViewById(R.id.tvAddFCity);
            etRFirstName = itemView.findViewById(R.id.etRFirstName);
            etRLastName = itemView.findViewById(R.id.etRLastName);
            etRPhoneNo = itemView.findViewById(R.id.etRPhoneNo);
            etAFResPinCode = itemView.findViewById(R.id.etAFResPinCode);
            etWorkAddress1 = itemView.findViewById(R.id.etWorkAddress1);
            etWorkAddress2 = itemView.findViewById(R.id.etWorkAddress2);
            addFieldSpinner = itemView.findViewById(R.id.addFieldSpinner);
            llRFirstName = itemView.findViewById(R.id.llRFirstName);
            llRLastName = itemView.findViewById(R.id.llRLastName);
            llRPhoneNo = itemView.findViewById(R.id.llRPhoneNo);
            llRelationShip = itemView.findViewById(R.id.llRelationShip);
            llState = itemView.findViewById(R.id.llState);
            llAFPinCode = itemView.findViewById(R.id.llAFPinCode);
            llCity = itemView.findViewById(R.id.llCity);
            llCommon = itemView.findViewById(R.id.llCommon);
            llWorkAddress1 = itemView.findViewById(R.id.llWorkAddress1);
            llWorkAddress2 = itemView.findViewById(R.id.llWorkAddress2);
            btnAFPinCodeCheck = itemView.findViewById(R.id.btnAFPinCodeCheck);
        }
    }
}
