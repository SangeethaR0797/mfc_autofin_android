package com.mfc.autofin.framework.Activity.review_activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidentialCity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleCategory;
import com.mfc.autofin.framework.R;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

import controller.ReviewAdapter;
import model.add_lead_details.CustomerDetails;
import model.add_lead_details.CustomerDetailsRequest;
import model.add_lead_details.CustomerDetailsResponse;
import model.add_lead_details.CustomerID;
import model.add_lead_details.LoanDetails;
import model.basic_details.BasicDetails;
import model.basic_details.EmploymentDetails;
import model.basic_details.PersonalDetailsData;
import model.basic_details.ResidentialDetails;
import model.custom_model.ReviewData;
import model.vehicle_details.vehicle_category.VehicleDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.*;

public class ReviewDetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {
    private static final String TAG = ReviewDetailsActivity.class.getSimpleName();
    private TextView tvVehDetailsTitle, tvCommonAppBarTitle, tvBasicDetails, tvResidentialDetails, tvPersonalDetails;
    private RecyclerView rvVehDetails, rvBasicDetails, rvResidentialDetails, rvPersonalDetails;
    private LinearLayout llEditAndCloseReview;
    private Button btnRViewBankDetails;
    private Intent intent;
    private int customerId = 0;
    private String userId = "", userType = "", caseId = "";
    private VehicleDetails vehDetailsResList;
    private BasicDetails basicDetailsResList;
    private ResidentialDetails residentialDetailsResList;
    private PersonalDetailsData personalDetailsResList;
    private LoanDetails loanDetailsResList;
    private EmploymentDetails employmentResDetails;
    private ImageView iv_common_bar_backBtn;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        try {
            if (getIntent() != null) {
                intent = getIntent();
                if (intent.getExtras() != null) {
                    Bundle bundle = intent.getExtras();
                    customerId = bundle.getInt(CUSTOMER_ID);
                    caseId = bundle.getString(CASE_ID);
                    if (customerId != 0) {
                        flag = true;
                    } else {
                        flag = false;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            finish();
        }
        initView();
        userId = CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL);
        userType = CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL);

        if (flag) {
            SpinnerManager.showSpinner(this);
            retrofitInterface.getFromWeb(getCustomerDetailsReq(), CUSTOMER_DETAILS_URL).enqueue(this);
        }


    }

    private CustomerDetailsRequest getCustomerDetailsReq() {
        CustomerDetailsRequest customerDetailsReq = new CustomerDetailsRequest();
        customerDetailsReq.setUserId(userId);
        customerDetailsReq.setUserType(userType);
        CustomerID customerID = new CustomerID();
        customerID.setCustomerId(customerId);
        customerDetailsReq.setData(customerID);
        return customerDetailsReq;
    }

    private void initView() {
        tvVehDetailsTitle = findViewById(R.id.tvVehDetailsTitle);
        tvVehDetailsTitle.setOnClickListener(this);
        btnRViewBankDetails = findViewById(R.id.btnRViewBankDetails);
        rvVehDetails = findViewById(R.id.rvVehDetails);
        tvBasicDetails = findViewById(R.id.tvBasicDetails);
        iv_common_bar_backBtn = findViewById(R.id.iv_common_bar_backBtn);
        llEditAndCloseReview = findViewById(R.id.llEditAndCloseReview);
        rvResidentialDetails = findViewById(R.id.rvResidentialDetails);
        tvResidentialDetails = findViewById(R.id.tvResidentialDetails);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tvPersonalDetails = findViewById(R.id.tvReviewPersonalDetails);
        rvPersonalDetails = findViewById(R.id.rvPersonalDetails);
        rvBasicDetails = findViewById(R.id.rvBasicDetails);
        iv_common_bar_backBtn.setOnClickListener(this);
        btnRViewBankDetails.setOnClickListener(this);
        tvBasicDetails.setOnClickListener(this);
        tvResidentialDetails.setOnClickListener(this);
        tvPersonalDetails.setOnClickListener(this);
        tvCommonAppBarTitle.setText("REVIEW");
        if (flag) {

        } else {
            if (customLoanDetails.getLoanCategory().equals(getResources().getString(R.string.new_car))) {
                displayNewCarVehicleDetails();
            } else if (customLoanDetails.getLoanCategory().equals(getResources().getString(R.string.old_car))) {
                displayOldCarVehicleDetails();
            }

            displayBasicDetails();
            displayResidentialDetails();
            displayPersonalDetails();

        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_common_bar_backBtn) {
            finish();
        } else if (v.getId() == R.id.btnRViewBankDetails) {
            CommonMethods.setValueAgainstKey(this, CommonStrings.CUSTOMER_ID, String.valueOf(customerId));
            CommonMethods.setValueAgainstKey(this, CommonStrings.CASE_ID, caseId);
            startActivity(new Intent(this, ViewBankActivity.class));
        }
    }

    private void displayPersonalDetails() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, getPersonalDetails(), getResources().getString(R.string.title_personal_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvPersonalDetails.setLayoutManager(layoutManager);
            rvPersonalDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> getPersonalDetails() {
        ArrayList<ReviewData> personalDetails = new ArrayList<>();
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_dob), customPersonalDetails.getBirthDate()));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_gender), customPersonalDetails.getGender()));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_education), customPersonalDetails.getEducation()));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), String.valueOf(customPersonalDetails.getSalaryPerMonth())));
        if(customLoanDetails.getNoOfExistingLoans().contains("."))
        {
            personalDetails.add(new ReviewData("NUMBER OF EXISTING LOAN", customLoanDetails.getNoOfExistingLoans().substring(1,1)));
        }
        else
        {
            personalDetails.add(new ReviewData("NUMBER OF EXISTING LOAN", customLoanDetails.getNoOfExistingLoans()));
        }
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), customLoanDetails.getRequiredLoanAmount()));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_total_emi), String.valueOf(customPersonalDetails.getTotalEMIPaid())));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_employment_type), String.valueOf(cusEmpDetails.getEmploymentType())));

        if (cusEmpDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_salaried))) {
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_working_organization_name), cusEmpDetails.getCompanyName()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_joining_date_of_your_current_org), cusEmpDetails.getCompanyJoiningDate()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_year_of_experience), cusEmpDetails.getTotalWorkExperience()));
            personalDetails.add(new ReviewData("SALARY ACCOUNT", cusEmpDetails.getSalaryAccount()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_sal_mode), cusEmpDetails.getSalaryMode()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetails.getIndustryType()));

        } else if (cusEmpDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_business_owner))) {
            personalDetails.add(new ReviewData("EMPLOYMENT ROLE", cusEmpDetails.getEmploymentRole()));
            personalDetails.add(new ReviewData("BUSINESS STARTED DATE", cusEmpDetails.getBusinessStartDate()));
            personalDetails.add(new ReviewData("LAST YEARS PROFIT", String.valueOf(cusEmpDetails.getLastYearProfit())));
            personalDetails.add(new ReviewData("LAST YEARS SALES", cusEmpDetails.getLastYearTurnOver()));
            personalDetails.add(new ReviewData("INCOME AFTER TAX", cusEmpDetails.getIncomeAfterTax()));
            personalDetails.add(new ReviewData("LAST YEAR DEPRECIATION", cusEmpDetails.getLastYearDepreciation()));
            personalDetails.add(new ReviewData("WAS LAST YEAR ITR AUDITED", String.valueOf(cusEmpDetails.getIsLastestItraudited())));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetails.getIndustryType()));

        } else if (cusEmpDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_self_employed_professional))) {
            personalDetails.add(new ReviewData("EMPLOYMENT ROLE", cusEmpDetails.getEmploymentRole()));
            personalDetails.add(new ReviewData("PROFESSION", cusEmpDetails.getProfession()));
            personalDetails.add(new ReviewData("PROFESSION STARTED DATE", cusEmpDetails.getBusinessStartDate()));
            personalDetails.add(new ReviewData("LAST YEARS PROFIT", String.valueOf(cusEmpDetails.getLastYearProfit())));
            personalDetails.add(new ReviewData("LAST YEARS SALES", cusEmpDetails.getLastYearTurnOver()));
            personalDetails.add(new ReviewData("INCOME AFTER TAX", cusEmpDetails.getIncomeAfterTax()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetails.getIndustryType()));

        }
        personalDetails.add(new ReviewData("SAVINGS BANK ACCOUNT", customPersonalDetails.getSavingsAccount()));
        personalDetails.add(new ReviewData("PANCARD NO.", customPersonalDetails.getPanNumber()));
        personalDetails.add(new ReviewData("LIKELY PURCHASE DATE", customVehDetails.getLikelyPurchaseDate()));
        return personalDetails;
    }

    private void displayResidentialDetails() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, getResidentialDetails(), getResources().getString(R.string.title_residential_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvResidentialDetails.setLayoutManager(layoutManager);
            rvResidentialDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> getResidentialDetails() {
        ArrayList<ReviewData> residentialDetails = new ArrayList<>();
        residentialDetails.add(new ReviewData("PINCODE", customResDetails.getPincode()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_state), customResDetails.getCustomerState()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.city_lbl), customResDetails.getCustomerCity()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.address_one_lbl), customResDetails.getAddressLine1()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.address_two_lbl), customResDetails.getAddressLine2()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_landmark), customResDetails.getAddressLine3()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_when_did_you_moved_to_the_city), customResDetails.getMoveInCityYear()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_when_moved_to_current_residence), customResDetails.getMoveInResidenceYear()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_residence_type), customResDetails.getResidenceType()));
        return residentialDetails;
    }

    private void displayBasicDetails() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, getBasicDetails(), getResources().getString(R.string.title_basic_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvBasicDetails.setLayoutManager(layoutManager);
            rvBasicDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> getBasicDetails() {
        ArrayList<ReviewData> basicDetails = new ArrayList<>();
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_name), customBasicDetails.getSalutation() + " " + customBasicDetails.getFullName()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_email), customBasicDetails.getEmail()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), customBasicDetails.getCustomerMobile()));
        return basicDetails;
    }

    private void displayOldCarVehicleDetails() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, getOldCarDataList(), getResources().getString(R.string.vehicle_details_title));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private List<ReviewData> getOldCarDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        String vehHaveRegNo = "", vehRegNo = "", carHaveLoan = "", insuranceAmount = "", valuationDone = "";
        if (customVehDetails.getHaveVehicleNumber()) {
            vehHaveRegNo = "YES";
            if (!customVehDetails.getVehicleNumber().equals("")) {
                vehRegNo = customVehDetails.getVehicleNumber();
            } else {
                vehRegNo = "NA";
            }
        } else {
            vehHaveRegNo = "NO";
            vehRegNo = "NA";
        }

        if (customVehDetails.getDoesCarHaveLoan()) {
            carHaveLoan = "YES";
        } else {
            carHaveLoan = "NO";
        }

        if (customVehDetails.getInsurance()) {
            insuranceAmount = "YES";
        } else {
            insuranceAmount = "NO";
        }

        if (customVehDetails.getIsValuationDone()) {
            valuationDone = "YES";
        } else {
            valuationDone = "NO";
        }
        reviewDataList.add(new ReviewData(CommonStrings.VEH_CATEGORY_TITLE, customLoanDetails.getLoanCategory()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_reg_num_qn), vehHaveRegNo));
        reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, vehRegNo));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), customVehDetails.getRegistrationYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), customVehDetails.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), customVehDetails.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), customVehDetails.getVariant()));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), String.valueOf(customVehDetails.getOwnership())));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_vehicle_purchase_amount), customVehDetails.getVehicleSellingPrice()));
        reviewDataList.add(new ReviewData("VEHICLE VALUATION DONE", valuationDone));
        reviewDataList.add(new ReviewData("VEHICLE VALUATION PRICE", customVehDetails.getValuationPrice()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_have_loan_qn), carHaveLoan));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insurance_on_vehicle), insuranceAmount));
        reviewDataList.add(new ReviewData("INSURANCE AMOUNT", customVehDetails.getInsuranceAmount()));
        reviewDataList.add(new ReviewData("INSURANCE VALIDITY", CommonMethods.removeDecimal(Double.parseDouble(customVehDetails.getInsuranceValidity()))));
        reviewDataList.add(new ReviewData("INSURANCE TYPE", customVehDetails.getInsuranceType()));
        reviewDataList.add(new ReviewData("LIKELY PURCHASE DATE", customVehDetails.getLikelyPurchaseDate()));
        return reviewDataList;
    }

    private void displayNewCarVehicleDetails() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, getNewCarDataList(), getResources().getString(R.string.vehicle_details_title));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private List<ReviewData> getNewCarDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData(CommonStrings.VEH_CATEGORY_TITLE, customLoanDetails.getLoanCategory()));
        reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, customVehDetails.getVehicleNumber()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), customVehDetails.getRegistrationYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), customVehDetails.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), customVehDetails.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), customVehDetails.getVariant()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_road_price), customVehDetails.getOnRoadPrice()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_vehicle_purchase_amount), customVehDetails.getVehicleSellingPrice()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insured_amount), customVehDetails.getInsuranceAmount()));
        reviewDataList.add(new ReviewData("LIKELY PURCHASE DATE", customVehDetails.getLikelyPurchaseDate()));
        return reviewDataList;
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        CustomerDetailsResponse customerDetailsRes = new Gson().fromJson(strRes, CustomerDetailsResponse.class);
        if (customerDetailsRes != null && customerDetailsRes.getStatus()) {
            try {
                if (customerDetailsRes.getData() != null) {
                    CustomerDetails customerDetails = customerDetailsRes.getData();
                    Log.i(TAG, "onResponse: " + customerDetails);
                    if (customerDetails.getCaseId() != null && !customerDetails.getCaseId().equals("")) {
                        CommonMethods.setValueAgainstKey(this, CommonStrings.CASE_ID, customerDetails.getCaseId());
                        CommonMethods.showToast(this, customerDetails.getCaseId());
                        vehDetailsResList = customerDetails.getVehicleDetails();
                        basicDetailsResList = customerDetails.getBasicDetails();
                        residentialDetailsResList = customerDetails.getResidentialDetails();
                        personalDetailsResList = customerDetails.getPersonalDetails();
                        employmentResDetails = customerDetails.getEmploymentDetails();
                        loanDetailsResList = customerDetails.getLoanDetails();
                        displayReviewVehRes();
                        displayReviewBasicRes();
                        displayReviewResidentialRes();
                        displayReviewPersonalRes();
                    } else {
                        CommonMethods.showToast(this, "No Case-Id found");
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private void displayReviewVehRes() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, prepareNewReviewDataList(), getResources().getString(R.string.vehicle_details_title));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void displayReviewBasicRes() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, prepareNewReviewBasicDataList(), getResources().getString(R.string.title_basic_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvBasicDetails.setLayoutManager(layoutManager);
            rvBasicDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void displayReviewResidentialRes() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, prepareNewResDataList(), getResources().getString(R.string.title_residential_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvResidentialDetails.setLayoutManager(layoutManager);
            rvResidentialDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void displayReviewPersonalRes() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, preparePersonalDetailsRes(), getResources().getString(R.string.title_personal_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvPersonalDetails.setLayoutManager(layoutManager);
            rvPersonalDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> prepareNewReviewDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        String vehHaveRegNo = "", vehRegNo = "", carHaveLoan = "", insuranceAmount = "", valuationDone = "";
        if (vehDetailsResList.getHaveVehicleNumber()) {
            vehHaveRegNo = "YES";
            if (!vehDetailsResList.getVehicleNumber().equals("")) {
                vehRegNo = vehDetailsResList.getVehicleNumber();
            } else {
                vehRegNo = "NA";
            }
        } else {
            vehHaveRegNo = "NO";
            vehRegNo = "NA";
        }

        if (vehDetailsResList.getDoesCarHaveLoan()) {
            carHaveLoan = "YES";
        } else {
            carHaveLoan = "NO";
        }

        if (vehDetailsResList.getInsurance()) {
            insuranceAmount = "YES";
        } else {
            insuranceAmount = "NO";
        }

        if (vehDetailsResList.getIsValuationDone()) {
            valuationDone = "YES";
        } else {
            valuationDone = "NO";
        }
        if (loanDetailsResList.getLoanCategory().equalsIgnoreCase("New Car")) {
            if (vehDetailsResList.getRegistrationYear() != null && !vehDetailsResList.getRegistrationYear().isEmpty()) {
                if (vehDetailsResList.getRegistrationYear().contains("."))
                    reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), vehDetailsResList.getRegistrationYear().substring(0,4)));
                else
                    reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), vehDetailsResList.getRegistrationYear()));

            } else {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), "NA"));

            }
            if(isTheFieldHaveValue(vehDetailsResList.getMake()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), vehDetailsResList.getMake()));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), "NA"));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getModel()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), vehDetailsResList.getModel()));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), "NA"));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getVariant()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), vehDetailsResList.getVariant()));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), vehDetailsResList.getVariant()));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getOnRoadPrice()))
            {
                reviewDataList.add(new ReviewData("INTERESTED VEHICLE AMOUNT", CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getOnRoadPrice()))));
            }else
            {
                reviewDataList.add(new ReviewData("INTERESTED VEHICLE AMOUNT", CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getOnRoadPrice()))));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getVehicleSellingPrice()))
            {
                reviewDataList.add(new ReviewData(getString(R.string.lbl_vehicle_purchase_amount), CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getVehicleSellingPrice()))));
            }else
            {
                reviewDataList.add(new ReviewData(getString(R.string.lbl_vehicle_purchase_amount), CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getVehicleSellingPrice()))));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getInsuranceAmount()))
            {
                reviewDataList.add(new ReviewData("INSURANCE AMOUNT", CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getInsuranceAmount()))));
            }else
            {
                reviewDataList.add(new ReviewData("INSURANCE AMOUNT", CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getInsuranceAmount()))));
            }

        } else {

            reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_reg_num_qn), vehHaveRegNo));
            reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, vehRegNo));

            if (vehDetailsResList.getRegistrationYear() != null && !vehDetailsResList.getRegistrationYear().isEmpty()) {
                if (vehDetailsResList.getRegistrationYear().contains("."))
                    reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), vehDetailsResList.getRegistrationYear().substring(0,4)));
                else
                    reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), vehDetailsResList.getRegistrationYear()));

            } else {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), "NA"));

            }
            if(isTheFieldHaveValue(vehDetailsResList.getMake()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), vehDetailsResList.getMake()));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), "NA"));
            }if(isTheFieldHaveValue(vehDetailsResList.getModel()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), vehDetailsResList.getModel()));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), "NA"));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getVariant()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), vehDetailsResList.getVariant()));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), "NA"));
            }if(isTheFieldHaveValue(String.valueOf(vehDetailsResList.getOwnership())))
            {
                reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), String.valueOf(vehDetailsResList.getOwnership())));
            }else
            {
                reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), "NA"));
            }if(isTheFieldHaveValue(vehDetailsResList.getVehicleSellingPrice()))
            {
                reviewDataList.add(new ReviewData(getString(R.string.lbl_vehicle_purchase_amount), CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getVehicleSellingPrice()))));
            }else
            {
                reviewDataList.add(new ReviewData(getString(R.string.lbl_vehicle_purchase_amount), "NA"));
            }
            if(isTheFieldHaveValue(valuationDone))
            {
                reviewDataList.add(new ReviewData("VEHICLE VALUATION DONE", valuationDone));
            }else
            {
                reviewDataList.add(new ReviewData("VEHICLE VALUATION DONE", "NA"));
            }

            if(isTheFieldHaveValue(vehDetailsResList.getValuationPrice()))
            {
                reviewDataList.add(new ReviewData("VEHICLE VALUATION PRICE", CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getValuationPrice()))));
            }else
            {
                reviewDataList.add(new ReviewData("VEHICLE VALUATION PRICE", "NA"));
            }

            if(isTheFieldHaveValue(carHaveLoan))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_have_loan_qn), carHaveLoan));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_have_loan_qn), "NA"));
            }

            if(isTheFieldHaveValue(insuranceAmount))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insurance_on_vehicle), insuranceAmount));
            }else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insurance_on_vehicle), "NA"));
            }

            if(isTheFieldHaveValue(vehDetailsResList.getInsuranceAmount()))
            {
                reviewDataList.add(new ReviewData("INSURANCE AMOUNT", CommonMethods.removeDecimal(Double.parseDouble(vehDetailsResList.getInsuranceAmount()))));
            }else
            {
                reviewDataList.add(new ReviewData("INSURANCE AMOUNT", "NA"));
            }

            if(isTheFieldHaveValue(vehDetailsResList.getInsuranceValidity()))
            {
                reviewDataList.add(new ReviewData("INSURANCE VALIDITY", String.valueOf(vehDetailsResList.getInsuranceValidity()).substring(0,10)));
            }else
            {
                reviewDataList.add(new ReviewData("INSURANCE VALIDITY", "NA"));
            }
            if(isTheFieldHaveValue(vehDetailsResList.getInsuranceType()))
            {
                reviewDataList.add(new ReviewData("INSURANCE TYPE", vehDetailsResList.getInsuranceType()));
            }else
            {
                reviewDataList.add(new ReviewData("INSURANCE TYPE", "NA"));
            }


        }
        if(isTheFieldHaveValue(vehDetailsResList.getLikelyPurchaseDate()))
        {
            reviewDataList.add(new ReviewData("LIKELY PURCHASE DATE", vehDetailsResList.getLikelyPurchaseDate()));
        }else
        {
            reviewDataList.add(new ReviewData("LIKELY PURCHASE DATE","NA"));
        }

        return reviewDataList;
    }

    private boolean isTheFieldHaveValue(String givenVal)
    {
        if(givenVal !=null && !givenVal.isEmpty())
            return true;
        else
            return false;
    }

    private List<ReviewData> prepareNewReviewBasicDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        Log.i(TAG, "prepareNewReviewBasicDataList: " + basicDetailsResList.getSalutation());
        if(isTheFieldHaveValue(basicDetailsResList.getFullName()))
        {
            if(isTheFieldHaveValue(basicDetailsResList.getSalutation()))
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_name), basicDetailsResList.getSalutation() + " " + basicDetailsResList.getFullName()));
            }
            else
            {
                reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_name), basicDetailsResList.getFullName()));
            }
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_name), "NA"));
        }

        if(isTheFieldHaveValue(basicDetailsResList.getEmail()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_email), basicDetailsResList.getEmail()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_email), "NA"));
        }

        if(isTheFieldHaveValue(basicDetailsResList.getCustomerMobile()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), basicDetailsResList.getCustomerMobile()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), "NA"));
        }

        return reviewDataList;
    }

    private List<ReviewData> prepareNewResDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        if(isTheFieldHaveValue(residentialDetailsResList.getPincode()))
        {
            reviewDataList.add(new ReviewData("PINCODE", residentialDetailsResList.getPincode()));
        }else
        {
            reviewDataList.add(new ReviewData("PINCODE","NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getCustomerCity()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.city_lbl), residentialDetailsResList.getCustomerCity()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.city_lbl), "NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getAddressLine1()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.address_one_lbl), residentialDetailsResList.getAddressLine1()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.address_one_lbl), "NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getAddressLine2()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.address_two_lbl), residentialDetailsResList.getAddressLine2()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.address_two_lbl), "NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getAddressLine3()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_landmark), residentialDetailsResList.getAddressLine3()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_landmark), "NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getMoveInCityYear()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_when_did_you_moved_to_the_city), residentialDetailsResList.getMoveInCityYear()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_when_did_you_moved_to_the_city), "NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getMoveInResidenceYear()))
        {
            reviewDataList.add(new ReviewData(getString(R.string.lbl_when_moved_to_current_residence), residentialDetailsResList.getMoveInResidenceYear()));
        }else
        {
            reviewDataList.add(new ReviewData(getString(R.string.lbl_when_moved_to_current_residence), "NA"));
        }

        if(isTheFieldHaveValue(residentialDetailsResList.getResidenceType()))
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_residence_type), residentialDetailsResList.getResidenceType()));
        }else
        {
            reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_residence_type), "NA"));
        }

        return reviewDataList;
    }

    private List<ReviewData> preparePersonalDetailsRes() {

        Log.i(TAG, "preparePersonalDetailsRes: " + personalDetailsResList.getGender());
        ArrayList<ReviewData> reviewPersonalDataList = new ArrayList<>();
        Log.i(TAG, "preparePersonalDetailsRes: " + personalDetailsResList.getGender());
        if(isTheFieldHaveValue(personalDetailsResList.getBirthDate()))
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_dob), personalDetailsResList.getBirthDate().substring(0, 10)));
        }else
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_dob), "NA"));
        }
        if(isTheFieldHaveValue(personalDetailsResList.getGender()))
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_gender), personalDetailsResList.getGender()));
        }else
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_gender), "NA"));
        }
        if(isTheFieldHaveValue(personalDetailsResList.getEducation()))
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_education), personalDetailsResList.getEducation()));
        }else
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_education), "NA"));
        }
        if(isTheFieldHaveValue(String.valueOf(personalDetailsResList.getSalaryPerMonth())))
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), CommonMethods.removeDecimal(personalDetailsResList.getSalaryPerMonth())));
        }else
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), "NA"));
        }
        if(isTheFieldHaveValue(loanDetailsResList.getNoOfExistingLoans()))
        {
            reviewPersonalDataList.add(new ReviewData("NUMBER OF EXISTING LOAN", CommonMethods.removeDecimal(Double.parseDouble(loanDetailsResList.getNoOfExistingLoans()))));
        }else
        {
            reviewPersonalDataList.add(new ReviewData("NUMBER OF EXISTING LOAN", "NA"));
        }
        if(isTheFieldHaveValue(loanDetailsResList.getRequiredLoanAmount()))
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), CommonMethods.removeDecimal(Double.parseDouble(loanDetailsResList.getRequiredLoanAmount()))));
        }else
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), "NA"));
        }
        if(isTheFieldHaveValue(loanDetailsResList.getRequiredLoanAmount()))
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), CommonMethods.removeDecimal(Double.parseDouble(loanDetailsResList.getRequiredLoanAmount()))));
        }else
        {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), "NA"));
        }

        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_employment_type), employmentResDetails.getEmploymentType()));
        Log.i(TAG, "preparePersonalDetailsRes: " + employmentResDetails.getEmploymentType());
        if (employmentResDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_salaried))) {
            Log.i(TAG, "preparePersonalDetailsRes: " + employmentResDetails.getEmploymentType());
            if(isTheFieldHaveValue(employmentResDetails.getCompanyName()))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_working_organization_name), employmentResDetails.getCompanyName()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_working_organization_name), "NA"));
            }

            if(isTheFieldHaveValue(employmentResDetails.getCompanyJoiningDate()))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_joining_date_of_your_current_org), employmentResDetails.getCompanyJoiningDate().substring(0, 10)));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_joining_date_of_your_current_org), "NA"));
            }

            if(isTheFieldHaveValue(employmentResDetails.getTotalWorkExperience()))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_year_of_experience), employmentResDetails.getTotalWorkExperience()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_year_of_experience), "NA"));
            }

            if(isTheFieldHaveValue(employmentResDetails.getSalaryAccount()))
            {
                reviewPersonalDataList.add(new ReviewData("SALARY ACCOUNT", employmentResDetails.getSalaryAccount()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("SALARY ACCOUNT", "NA"));
            }
            if(isTheFieldHaveValue(employmentResDetails.getSalaryMode()))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_sal_mode), employmentResDetails.getSalaryMode()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_sal_mode), "NA"));
            }

            if(isTheFieldHaveValue(employmentResDetails.getIndustryType()))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), employmentResDetails.getIndustryType()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), "NA"));
            }
        } else if (employmentResDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_business_owner))) {
            Log.i(TAG, "preparePersonalDetailsRes: " + employmentResDetails.getEmploymentType());

            if(isTheFieldHaveValue(employmentResDetails.getEmploymentRole()))
            {
                reviewPersonalDataList.add(new ReviewData("EMPLOYMENT ROLE", employmentResDetails.getEmploymentRole()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("EMPLOYMENT ROLE", "NA"));
            }

            if(isTheFieldHaveValue(employmentResDetails.getBusinessStartDate()))
            {
                reviewPersonalDataList.add(new ReviewData("BUSINESS STARTED DATE", employmentResDetails.getBusinessStartDate()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("BUSINESS STARTED DATE", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getLastYearProfit())))
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEAR PROFIT", CommonMethods.removeDecimal(employmentResDetails.getLastYearProfit())));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEAR PROFIT", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getLastYearTurnOver())))
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEARS SALES", CommonMethods.removeDecimal(Double.parseDouble(employmentResDetails.getLastYearTurnOver()))));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEARS SALES", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getIncomeAfterTax())))
            {
                reviewPersonalDataList.add(new ReviewData("INCOME AFTER TAX", CommonMethods.removeDecimal(Double.parseDouble(employmentResDetails.getIncomeAfterTax()))));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("INCOME AFTER TAX", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getLastYearDepreciation())))
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEAR DEPRECIATION", CommonMethods.removeDecimal(Double.parseDouble(employmentResDetails.getLastYearDepreciation()))));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEAR DEPRECIATION", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getIsLastestItraudited())))
            {
                reviewPersonalDataList.add(new ReviewData("WAS LAST YEAR ITR AUDITED", String.valueOf(employmentResDetails.getIsLastestItraudited())));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("WAS LAST YEAR ITR AUDITED", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getIndustryType())))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), employmentResDetails.getIndustryType()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), "NA"));
            }


        } else if (employmentResDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_self_employed_professional))) {
            Log.i(TAG, "preparePersonalDetailsRes: " + employmentResDetails.getEmploymentType());

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getEmploymentRole())))
            {
                reviewPersonalDataList.add(new ReviewData("EMPLOYMENT ROLE", employmentResDetails.getEmploymentRole()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("EMPLOYMENT ROLE", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getProfession())))
            {
                reviewPersonalDataList.add(new ReviewData("PROFESSION", employmentResDetails.getProfession()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("PROFESSION", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getBusinessStartDate())))
            {
                reviewPersonalDataList.add(new ReviewData("PROFESSION STARTED DATE", employmentResDetails.getBusinessStartDate()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("PROFESSION STARTED DATE", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getLastYearProfit())))
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEAR PROFIT", CommonMethods.removeDecimal(employmentResDetails.getLastYearProfit())));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEAR PROFIT", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getLastYearTurnOver())))
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEARS SALES", CommonMethods.removeDecimal(Double.parseDouble(employmentResDetails.getLastYearTurnOver()))));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("LAST YEARS SALES", "NA"));
            }

            if(isTheFieldHaveValue(String.valueOf(employmentResDetails.getIncomeAfterTax())))
            {
                reviewPersonalDataList.add(new ReviewData("INCOME AFTER TAX", CommonMethods.removeDecimal(Double.parseDouble(employmentResDetails.getIncomeAfterTax()))));
            }else
            {
                reviewPersonalDataList.add(new ReviewData("INCOME AFTER TAX", "NA"));
            }

            if(isTheFieldHaveValue(employmentResDetails.getIndustryType()))
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), employmentResDetails.getIndustryType()));
            }else
            {
                reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type),"NA"));
            }
        }
        if(isTheFieldHaveValue(personalDetailsResList.getSavingsAccount()))
        {
            reviewPersonalDataList.add(new ReviewData("SAVINGS BANK ACCOUNT", personalDetailsResList.getSavingsAccount()));
        }else
        {
            reviewPersonalDataList.add(new ReviewData("SAVINGS BANK ACCOUNT", "NA"));
        }

        if(isTheFieldHaveValue(personalDetailsResList.getPanNumber()))
        {
            reviewPersonalDataList.add(new ReviewData("PANCARD NO.", personalDetailsResList.getPanNumber()));
        }else
        {
            reviewPersonalDataList.add(new ReviewData("PANCARD NO.", "NA"));
        }
        return reviewPersonalDataList;

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
