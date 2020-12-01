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

import static retrofit_config.RetroBase.retrofitInterface;
import static utility.CommonStrings.*;

public class ReviewDetailsActivity extends AppCompatActivity implements View.OnClickListener, Callback<Object> {
    private static final String TAG = ReviewDetailsActivity.class.getSimpleName();
    private TextView tvVehDetailsTitle, tvCommonAppBarTitle, tvBasicDetails, tvResidentialDetails, tvPersonalDetails;
    private RecyclerView rvVehDetails, rvBasicDetails, rvResidentialDetails, rvPersonalDetails;
    private LinearLayout llEditAndCloseReview;
    private Button btnEditReview, btnCloseReview;
    private Intent intent;
    private int customerId = 0;
    private String userId = "", userType = "";
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
        btnEditReview = findViewById(R.id.btnEditReview);
        btnCloseReview = findViewById(R.id.btnCloseReview);
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
        btnEditReview.setOnClickListener(this);
        btnCloseReview.setOnClickListener(this);
        tvBasicDetails.setOnClickListener(this);
        tvResidentialDetails.setOnClickListener(this);
        tvPersonalDetails.setOnClickListener(this);
        tvCommonAppBarTitle.setText("REVIEW");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_common_bar_backBtn) {
            finish();
        } else if (v.getId() == R.id.tvVehDetailsTitle) {
            llEditAndCloseReview.setVisibility(View.VISIBLE);
            rvVehDetails.setVisibility(View.VISIBLE);
            if (flag) {
                displayReviewVehRes();
            } else {
                if (customLoanDetails.getLoanCategory().equals(getResources().getString(R.string.new_car))) {
                    displayNewCarVehicleDetails();
                } else if (customLoanDetails.getLoanCategory().equals(getResources().getString(R.string.old_car))) {
                    displayOldCarVehicleDetails();
                }
            }

        } else if (v.getId() == R.id.btnCloseReview) {
            if (rvVehDetails.getVisibility() == View.VISIBLE) {
                rvVehDetails.setVisibility(View.GONE);
                llEditAndCloseReview.setVisibility(View.GONE);

            } else if (rvBasicDetails.getVisibility() == View.VISIBLE) {
                rvBasicDetails.setVisibility(View.GONE);
                llEditAndCloseReview.setVisibility(View.GONE);
            } else if (rvResidentialDetails.getVisibility() == View.VISIBLE) {
                rvResidentialDetails.setVisibility(View.GONE);
                llEditAndCloseReview.setVisibility(View.GONE);
            } else if (rvPersonalDetails.getVisibility() == View.VISIBLE) {
                rvPersonalDetails.setVisibility(View.GONE);
                llEditAndCloseReview.setVisibility(View.GONE);
            }

        } else if (v.getId() == R.id.tvBasicDetails) {
            llEditAndCloseReview.setVisibility(View.VISIBLE);
            rvBasicDetails.setVisibility(View.VISIBLE);
            btnEditReview.setVisibility(View.GONE);
            if (flag) {
                displayReviewBasicRes();
            } else {
                displayBasicDetails();
            }


        } else if (v.getId() == R.id.tvResidentialDetails) {
            llEditAndCloseReview.setVisibility(View.VISIBLE);
            rvResidentialDetails.setVisibility(View.VISIBLE);
            btnEditReview.setVisibility(View.VISIBLE);
            if (flag) {
                displayReviewResidentialRes();
            } else {
                displayResidentialDetails();
            }

        } else if (v.getId() == R.id.tvReviewPersonalDetails) {
            llEditAndCloseReview.setVisibility(View.VISIBLE);
            rvPersonalDetails.setVisibility(View.VISIBLE);
            btnEditReview.setVisibility(View.VISIBLE);
            if (flag) {
                displayReviewPersonalRes();
            } else {
                displayPersonalDetails();
            }


        } else if (v.getId() == R.id.btnEditReview) {
            if (rvPersonalDetails.getVisibility() == View.VISIBLE)
                startActivity(new Intent(this, VehicleCategory.class));
            else if (rvBasicDetails.getVisibility() == View.VISIBLE)
                CommonMethods.showToast(this, "Sorry! you cannot edit Basic details");
                //startActivity(new Intent(this, BasicDetailsActivity.class));
            else if (rvResidentialDetails.getVisibility() == View.VISIBLE)
                startActivity(new Intent(this, ResidentialCity.class));
            else if (rvPersonalDetails.getVisibility() == View.VISIBLE)
                startActivity(new Intent(this, UserDOBActivity.class));
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
        personalDetails.add(new ReviewData("NUMBER OF EXISTING LOAN", customLoanDetails.getNoOfExistingLoans()));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), customLoanDetails.getRequiredLoanAmount()));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_total_emi), String.valueOf(customPersonalDetails.getTotalEMIPaid())));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_employment_type), String.valueOf(customPersonalDetails.getTotalEMIPaid())));

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
            personalDetails.add(new ReviewData("LAST YEARS PROFIT", CommonMethods.getStringValueFromKey(this, LAST_YEAR_PROFIT)));
            personalDetails.add(new ReviewData("LAST YEARS SALES", cusEmpDetails.getLastYearTurnOver()));
            personalDetails.add(new ReviewData("INCOME AFTER TAX", CommonMethods.getStringValueFromKey(this, INCOME_AFTER_TAX)));
            personalDetails.add(new ReviewData("LAST YEAR DEPRECIATION", cusEmpDetails.getLastYearDepreciation()));
            personalDetails.add(new ReviewData("WAS LAST YEAR ITR AUDITED", String.valueOf(cusEmpDetails.getIsLastestItraudited())));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetails.getIndustryType()));

        } else if (cusEmpDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_self_employed_professional))) {
            personalDetails.add(new ReviewData("EMPLOYMENT ROLE", cusEmpDetails.getEmploymentRole()));
            personalDetails.add(new ReviewData("PROFESSION", cusEmpDetails.getProfession()));
            personalDetails.add(new ReviewData("PROFESSION STARTED DATE", cusEmpDetails.getBusinessStartDate()));
            personalDetails.add(new ReviewData("LAST YEARS PROFIT", CommonMethods.getStringValueFromKey(this, LAST_YEAR_PROFIT)));
            personalDetails.add(new ReviewData("LAST YEARS SALES", cusEmpDetails.getLastYearTurnOver()));
            personalDetails.add(new ReviewData("INCOME AFTER TAX", CommonMethods.getStringValueFromKey(this, INCOME_AFTER_TAX)));
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
        reviewDataList.add(new ReviewData("INSURANCE VALIDITY", customVehDetails.getInsuranceValidity()));
        reviewDataList.add(new ReviewData("INSURANCE TYPE", customVehDetails.getInsuranceType()));
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
        return reviewDataList;
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
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
                        Log.i(TAG, "onResponse: " + customerDetails.getCaseId());
                        vehDetailsResList = customerDetails.getVehicleDetails();
                        basicDetailsResList = customerDetails.getBasicDetails();
                        residentialDetailsResList = customerDetails.getResidentialDetails();
                        personalDetailsResList = customerDetails.getPersonalDetails();
                        employmentResDetails = customerDetails.getEmploymentDetails();
                        loanDetailsResList = customerDetails.getLoanDetails();
                        Log.i(TAG, "onResponse: "+personalDetailsResList.getGender());

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
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, prepareNewReviewDataList(), getResources().getString(R.string.title_basic_details));
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
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_reg_num_qn), vehHaveRegNo));
        reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, vehRegNo));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), vehDetailsResList.getRegistrationYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), vehDetailsResList.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), vehDetailsResList.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), vehDetailsResList.getVariant()));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), String.valueOf(vehDetailsResList.getOwnership())));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_vehicle_purchase_amount), vehDetailsResList.getVehicleSellingPrice()));
        reviewDataList.add(new ReviewData("VEHICLE VALUATION DONE", valuationDone));
        reviewDataList.add(new ReviewData("VEHICLE VALUATION PRICE", vehDetailsResList.getValuationPrice()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_have_loan_qn), carHaveLoan));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insurance_on_vehicle), insuranceAmount));
        reviewDataList.add(new ReviewData("INSURANCE AMOUNT", vehDetailsResList.getInsuranceAmount()));
        reviewDataList.add(new ReviewData("INSURANCE VALIDITY", vehDetailsResList.getInsuranceValidity()));
        reviewDataList.add(new ReviewData("INSURANCE TYPE", vehDetailsResList.getInsuranceType()));
        return reviewDataList;

    }
    private List<ReviewData> prepareNewReviewBasicDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_name), basicDetailsResList.getSalutation() + " " + basicDetailsResList.getFullName()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_email), basicDetailsResList.getEmail()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), basicDetailsResList.getCustomerMobile()));
        return reviewDataList;
    }

    private List<ReviewData> prepareNewResDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData("PINCODE", residentialDetailsResList.getPincode()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.city_lbl), residentialDetailsResList.getCustomerCity()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.address_one_lbl), residentialDetailsResList.getAddressLine1()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.address_two_lbl), residentialDetailsResList.getAddressLine2()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_landmark), residentialDetailsResList.getAddressLine3()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_when_did_you_moved_to_the_city), residentialDetailsResList.getMoveInCityYear()));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_when_moved_to_current_residence), residentialDetailsResList.getMoveInResidenceYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_residence_type), residentialDetailsResList.getResidenceType()));
        return reviewDataList;
    }

    private List<ReviewData> preparePersonalDetailsRes() {
        ArrayList<ReviewData> reviewPersonalDataList = new ArrayList<>();
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_dob), personalDetailsResList.getBirthDate()));
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_gender), personalDetailsResList.getGender()));
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_education), personalDetailsResList.getEducation()));
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), String.valueOf(personalDetailsResList.getSalaryPerMonth())));
        reviewPersonalDataList.add(new ReviewData("NUMBER OF EXISTING LOAN", loanDetailsResList.getNoOfExistingLoans()));
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), loanDetailsResList.getRequiredLoanAmount()));
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_total_emi), String.valueOf(personalDetailsResList.getTotalEMIPaid())));
        reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_employment_type), String.valueOf(personalDetailsResList.getTotalEMIPaid())));
        if (employmentResDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_salaried))) {
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_working_organization_name), employmentResDetails.getCompanyName()));
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_joining_date_of_your_current_org), employmentResDetails.getCompanyJoiningDate()));
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_year_of_experience), employmentResDetails.getTotalWorkExperience()));
            reviewPersonalDataList.add(new ReviewData("SALARY ACCOUNT", employmentResDetails.getSalaryAccount()));
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_sal_mode), employmentResDetails.getSalaryMode()));
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), employmentResDetails.getIndustryType()));
        } else if (employmentResDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_business_owner))) {
            reviewPersonalDataList.add(new ReviewData("EMPLOYMENT ROLE", employmentResDetails.getEmploymentRole()));
            reviewPersonalDataList.add(new ReviewData("BUSINESS STARTED DATE", employmentResDetails.getBusinessStartDate()));
            reviewPersonalDataList.add(new ReviewData("LAST YEARS PROFIT","0"));
            reviewPersonalDataList.add(new ReviewData("LAST YEARS SALES", employmentResDetails.getLastYearTurnOver()));
            reviewPersonalDataList.add(new ReviewData("INCOME AFTER TAX","0"));
            reviewPersonalDataList.add(new ReviewData("LAST YEAR DEPRECIATION", employmentResDetails.getLastYearDepreciation()));
            reviewPersonalDataList.add(new ReviewData("WAS LAST YEAR ITR AUDITED", String.valueOf(employmentResDetails.getIsLastestItraudited())));
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), employmentResDetails.getIndustryType()));

        } else if (employmentResDetails.getEmploymentType().equalsIgnoreCase(getResources().getString(R.string.lbl_self_employed_professional))) {
            reviewPersonalDataList.add(new ReviewData("EMPLOYMENT ROLE", employmentResDetails.getEmploymentRole()));
            reviewPersonalDataList.add(new ReviewData("PROFESSION", employmentResDetails.getProfession()));
            reviewPersonalDataList.add(new ReviewData("PROFESSION STARTED DATE", employmentResDetails.getBusinessStartDate()));
            reviewPersonalDataList.add(new ReviewData("LAST YEARS PROFIT", CommonMethods.getStringValueFromKey(this, LAST_YEAR_PROFIT)));
            reviewPersonalDataList.add(new ReviewData("LAST YEARS SALES", employmentResDetails.getLastYearTurnOver()));
            reviewPersonalDataList.add(new ReviewData("INCOME AFTER TAX", CommonMethods.getStringValueFromKey(this, INCOME_AFTER_TAX)));
            reviewPersonalDataList.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), employmentResDetails.getIndustryType()));

        }
        reviewPersonalDataList.add(new ReviewData("SAVINGS BANK ACCOUNT", personalDetailsResList.getSavingsAccount()));
        reviewPersonalDataList.add(new ReviewData("PANCARD NO.", personalDetailsResList.getPanNumber()));
        reviewPersonalDataList.add(new ReviewData("LIKELY PURCHASE DATE", vehDetailsResList.getLikelyPurchaseDate()));

        return reviewPersonalDataList;

    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
