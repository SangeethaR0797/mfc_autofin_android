package controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;

import kyc.DocumentUploadActivity;
import model.addtional_fields.AdditionFields;
import model.addtional_fields.AdditionalFieldData;
import model.addtional_fields.CustAdditionalData;
import model.addtional_fields.Field;
import model.addtional_fields.SubmitAdditionalFieldData;
import model.addtional_fields.SubmitAdditionalFldRes;
import model.addtional_fields.SubmitAdditionalFldsReq;
import model.basic_details.ReferenceDetails;
import model.residential_models.CityData;
import model.residential_models.ResidentialPinCodeRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.DEALER_ID_VAL;
import static utility.CommonStrings.RES_CITY_URL;
import static utility.CommonStrings.SUBMIT_ADDITIONAL_FIELDS_URL;
import static utility.CommonStrings.USER_TYPE_VAL;

public class AdditionalFieldAdapter extends RecyclerView.Adapter<AdditionalFieldAdapter.ViewHolder> implements Callback<Object>, AdapterView.OnItemSelectedListener {

    private static final String TAG = AdditionalFieldAdapter.class.getSimpleName();
    Activity activity;
    List<AdditionFields> additionalFields;
    Button btnNext;
    String bankName = "", customerId = "", fName = "", lName = "", mobNum = "", relationship = "", pinCode = "", state = "", city = "", address1 = "", address2 = "";
    boolean isCurrentMandatory = false, isFNameMandatory = false, isLNameMandatory = false, isMNUmMandatory = false, isRelMandatory = false, isPinCodeMandatory = false, isStateMandatory = false, isCityMandatory = false, isWorkAdd1Mandatory = false, isWorkAdd2Mandatory = false;
    String fNameAPIKey = "", lNameAPIKey = "", mobNumAPIKey = "", relationshipAPIKey = "", pinCodeAPIKey = "", stateAPIKey = "", cityAPIKey = "", address1APIKey = "", address2APIKey = "";

    TextView tvState, tvCity;
    EditText etRFirstName, etField, etRLastName, etRPhoneNo, etAFResPinCode, etWorkAddress1, etWorkAddress2;
    List<String> relLabel = new ArrayList<>();
    List<String> relValue = new ArrayList<>();
    List<AdditionalFieldData> spinnerData = new ArrayList<>();
    List<CustAdditionalData> custAdditionalList=new ArrayList<>();
    public AdditionalFieldAdapter(Activity activity, List<AdditionFields> additionalFields, Button btnNext) {
        this.activity = activity;
        this.additionalFields = additionalFields;
        this.btnNext = btnNext;

    }

    public AdditionalFieldAdapter(Activity activity, List<AdditionFields> additionalFields, List<CustAdditionalData> custAdditionalList , Button btnNext) {
        this.activity = activity;
        this.additionalFields = additionalFields;
        this.custAdditionalList.addAll(custAdditionalList);
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
            if (additionalFields.get(position).getBankId() != null && !additionalFields.get(position).getBankId().isEmpty()) {
                bankName = additionalFields.get(position).getBankId().toString();
            }

            if (additionalFields.get(position).getFieldName() != null && !additionalFields.get(position).getFieldName().isEmpty()) {
                fieldName = additionalFields.get(position).getFieldName();
                if (additionalFields.get(position).getIsMandatory()) {
                    isCurrentMandatory = additionalFields.get(position).getIsMandatory();
                    setMandatoryLbl(holder.tvTextLbl, fieldName);
                } else {
                    isCurrentMandatory = additionalFields.get(position).getIsMandatory();
                    holder.tvTextLbl.setText(fieldName);
                }
            }
            if (fieldName.equalsIgnoreCase("Reference First Name")) {
                isFNameMandatory = isCurrentMandatory;
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
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.etRFirstName.setText(custAdditionalList.get(position).getValue());
                    }
                }
                else
                {
                    this.etRFirstName = holder.etRFirstName;
                    fNameAPIKey = additionalFields.get(position).getApiKeyName();
                }

            } else if (fieldName.equalsIgnoreCase("Reference Last Name")) {
                isLNameMandatory = isCurrentMandatory;
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
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.etRLastName.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.etRLastName.setText("");
                    }
                }

                this.etRLastName = holder.etRLastName;
                lNameAPIKey = additionalFields.get(position).getApiKeyName();

            } else if (fieldName.equalsIgnoreCase("Reference Mobile")) {
                isMNUmMandatory = isCurrentMandatory;
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
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.etRPhoneNo.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.etRPhoneNo.setText("");
                    }
                }
                this.etRPhoneNo = holder.etRPhoneNo;
                mobNumAPIKey = additionalFields.get(position).getApiKeyName();

            } else if (fieldName.equalsIgnoreCase("Relation")) {
                isRelMandatory = isCurrentMandatory;
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
                relationshipAPIKey = additionalFields.get(position).getApiKeyName();
                if (additionalFields.get(position).getData() != null) {
                    spinnerData.addAll(additionalFields.get(position).getData());
                    addListData();
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, relLabel);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.addFieldSpinner.setAdapter(dataAdapter);
                holder.addFieldSpinner.setOnItemSelectedListener(this);

            } else if (fieldName.equalsIgnoreCase("Work Pin Code")) {
                isPinCodeMandatory = isCurrentMandatory;
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
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.etAFResPinCode.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.etAFResPinCode.setText("");
                    }
                }

                this.etAFResPinCode = holder.etAFResPinCode;
                pinCodeAPIKey = additionalFields.get(position).getApiKeyName();

            } else if (fieldName.equalsIgnoreCase("State")) {
                isStateMandatory = isCurrentMandatory;
                holder.llRFirstName.setVisibility(View.GONE);
                holder.llRLastName.setVisibility(View.GONE);
                holder.llRPhoneNo.setVisibility(View.GONE);
                holder.llRelationShip.setVisibility(View.GONE);
                holder.llAFPinCode.setVisibility(View.GONE);
                holder.llState.setVisibility(View.VISIBLE);
                holder.llCity.setVisibility(View.GONE);
                holder.llWorkAddress1.setVisibility(View.GONE);
                holder.llWorkAddress2.setVisibility(View.GONE);
                tvState = holder.tvAddFState;
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.tvAddFState.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.tvAddFState.setText("");
                    }
                }
                holder.llCommon.setVisibility(View.GONE);
                stateAPIKey = additionalFields.get(position).getApiKeyName();

            } else if (fieldName.equalsIgnoreCase("City")) {
                isCityMandatory = isCurrentMandatory;
                holder.llRFirstName.setVisibility(View.GONE);
                holder.llRLastName.setVisibility(View.GONE);
                holder.llRPhoneNo.setVisibility(View.GONE);
                holder.llRelationShip.setVisibility(View.GONE);
                holder.llAFPinCode.setVisibility(View.GONE);
                holder.llState.setVisibility(View.GONE);
                holder.llCity.setVisibility(View.VISIBLE);
                holder.llWorkAddress1.setVisibility(View.GONE);
                holder.llWorkAddress2.setVisibility(View.GONE);
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.tvAddFCity.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.tvAddFCity.setText("");
                    }
                }
                tvCity = holder.tvAddFCity;
                holder.llCommon.setVisibility(View.GONE);
                cityAPIKey = additionalFields.get(position).getApiKeyName();

            } else if (fieldName.equalsIgnoreCase("Work Address Line 1")) {
                isWorkAdd1Mandatory = isCurrentMandatory;
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
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.etWorkAddress1.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.etWorkAddress1.setText("");
                    }
                }
                this.etWorkAddress1 = holder.etWorkAddress1;
                address1APIKey = additionalFields.get(position).getApiKeyName();

            } else if (fieldName.equalsIgnoreCase("Work Address Line 2")) {
                isWorkAdd2Mandatory = isCurrentMandatory;
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
                if(CommonStrings.IS_OLD_LEAD)
                {
                    if(additionalFields.get(position).getApiKeyName().equalsIgnoreCase(custAdditionalList.get(position).getKey()))
                    {
                        holder.etWorkAddress2.setText(custAdditionalList.get(position).getValue());
                    }
                    else
                    {
                        holder.etWorkAddress2.setText("");
                    }
                }

                this.etWorkAddress2 = holder.etWorkAddress2;
                address2APIKey = additionalFields.get(position).getApiKeyName();

            } else {
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

            if (holder.llAFPinCode.getVisibility() == View.VISIBLE) {
                holder.btnAFPinCodeCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strPinCode = holder.etAFResPinCode.getText().toString();
                        if (strPinCode != "") {
                            SpinnerManager.showSpinner(activity);
                            retrofitInterface.getFromWeb(Global.stock_details_base_url + RES_CITY_URL + holder.etAFResPinCode.getText().toString()).enqueue(AdditionalFieldAdapter.this);
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
                    if (validate()) {
                        retrofitInterface.getFromWeb(getSubmitAddFieldsReq(), Global.customerAPI_BaseURL + CommonStrings.SUBMIT_ADDITIONAL_FIELDS_URL).enqueue(AdditionalFieldAdapter.this);
                    } else {
                        CommonMethods.showToast(activity, "Please fill all mandatory fields");
                    }
                }
            });

    }

    private void addListData() {
        for (int i = 0; i < spinnerData.size(); i++) {
            relLabel.add(spinnerData.get(i).getDisplayLabel());
            relValue.add(spinnerData.get(i).getValue());
        }
    }

    private SubmitAdditionalFldsReq getSubmitAddFieldsReq() {

        CommonStrings.referenceDetails.setReferenceName(fName);
        CommonStrings.referenceDetails.setRelationship(relationship);
        CommonStrings.referenceDetails.setRefMobileNumber(mobNum);
        SubmitAdditionalFldsReq request = new SubmitAdditionalFldsReq();
        request.setUserId(CommonMethods.getStringValueFromKey(activity, DEALER_ID_VAL));
        request.setUserType(CommonMethods.getStringValueFromKey(activity, USER_TYPE_VAL));
        SubmitAdditionalFieldData additionalFieldData = new SubmitAdditionalFieldData();
        additionalFieldData.setBankName(bankName);
        //additionalFieldData.setCustomerId(9469);
        additionalFieldData.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(activity,CommonStrings.CUSTOMER_ID)));
        List<Field> additionalFieldsList = new ArrayList<>();
        additionalFieldsList.add(new Field(fNameAPIKey, fName));
        additionalFieldsList.add(new Field(lNameAPIKey, lName));
        additionalFieldsList.add(new Field(mobNumAPIKey, mobNum));
        additionalFieldsList.add(new Field(relationshipAPIKey, relationship));
        additionalFieldsList.add(new Field(pinCodeAPIKey, pinCode));
        additionalFieldsList.add(new Field(stateAPIKey, state));
        additionalFieldsList.add(new Field(cityAPIKey, city));
        additionalFieldsList.add(new Field(address1APIKey, address1));
        additionalFieldsList.add(new Field(address2APIKey, address2));
        additionalFieldData.setFields(additionalFieldsList);
        request.setData(additionalFieldData);
        return request;
    }

    private void setMandatoryLbl(TextView tvTextLbl, String fieldName) {
        TextView textView = tvTextLbl;
        String fName = fieldName;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(fName);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

    @Override
    public int getItemCount() {
        return additionalFields.size();
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(activity);
        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);

        if (url.contains(RES_CITY_URL)) {
            ResidentialPinCodeRes pinCodeRes = new Gson().fromJson(strRes, ResidentialPinCodeRes.class);
            try {
                if (pinCodeRes != null && pinCodeRes.getStatus()) {
                    if (pinCodeRes.getData() != null) {

                        CityData cityData = pinCodeRes.getData();
                        if (cityData.getCity() != null) {
                            city = cityData.getCity();
                            state = cityData.getState();
                            pinCode = etAFResPinCode.getText().toString();
                            tvState.setText(state);
                            tvCity.setText(city);
                        } else {
                            Toast.makeText(activity, pinCodeRes.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(activity, pinCodeRes.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(activity, pinCodeRes.getMessage(), Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else if (url.contains(SUBMIT_ADDITIONAL_FIELDS_URL)) {
            SubmitAdditionalFldRes submitAdditionalFldRes = new Gson().fromJson(strRes, SubmitAdditionalFldRes.class);
            try {
                if (submitAdditionalFldRes != null && submitAdditionalFldRes.getStatus()) {
                    Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show();
                    activity.startActivity(new Intent(activity, DocumentUploadActivity.class));
                } else {
                    if(submitAdditionalFldRes.getMessage()!=null)
                    {
                        Toast.makeText(activity, submitAdditionalFldRes.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                    else {
                        CommonStrings.referenceDetails=new ReferenceDetails();
                        Toast.makeText(activity, "Please try again", Toast.LENGTH_LONG).show();
                    }
                }
            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(CommonStrings.IS_OLD_LEAD)
        {
         relationship=CommonStrings.referenceDetails.getRelationship();
        }
        else {
            if (relValue.size() > 0)
                relationship = relValue.get(position);
            else
                relationship = item;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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

    private boolean validate() {
        boolean isMandatoryFRFilled = true, isFieldsRValid = false;


        if (!etRFirstName.getText().toString().isEmpty()) {
            fName = etRFirstName.getText().toString();
            if (isFieldsRValid = isValidName(fName)) {
                if (!etRLastName.getText().toString().isEmpty()) {
                    lName = etRLastName.getText().toString();
                    if (isFieldsRValid = isValidName(lName)) {
                        if (!etRPhoneNo.getText().toString().isEmpty()) {
                            mobNum = etRPhoneNo.getText().toString();
                            if (isFieldsRValid = isValidPhoneNum(mobNum)) {
                                if (!etWorkAddress1.getText().toString().isEmpty()) {
                                    address1 = etWorkAddress1.getText().toString();
                                    if (!etWorkAddress2.getText().toString().isEmpty()) {
                                        address2 = etWorkAddress2.getText().toString();
                                    } else {
                                        if (isWorkAdd1Mandatory) {
                                            isMandatoryFRFilled = false;
                                        }
                                    }
                                } else {
                                    if (isWorkAdd1Mandatory) {
                                        isMandatoryFRFilled = false;
                                    }
                                }
                            } else {
                                CommonMethods.showToast(activity, "Please enter Valid Phone Number");
                            }
                        } else {
                            if (isMNUmMandatory) {
                                isMandatoryFRFilled = false;
                            }
                        }
                    } else {
                        CommonMethods.showToast(activity, "Please enter Valid LastName");
                    }

                } else {
                    if (isLNameMandatory) {
                        isMandatoryFRFilled = false;
                    }
                }
            } else {
                CommonMethods.showToast(activity, "Please enter Valid FirstName");
            }
        } else {
            if (isFNameMandatory) {
                isMandatoryFRFilled = false;
            }
        }

        if (isMandatoryFRFilled && isFieldsRValid) {
            return true;
        } else {
            return false;
        }


    }

    private boolean isValidName(String fName) {
        if (fName.matches("^[a-z A-Z ]*$")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isValidPhoneNum(String phoneNum) {
        if (!phoneNum.equals("") && phoneNum.matches("^[6-9]\\d{9}$")) {
            return true;
        } else {
            return false;
        }
    }
}
