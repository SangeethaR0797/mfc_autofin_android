package com.mfc.autofin.framework.Activity.review_activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidentialCity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleCategory;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;

import controller.ReviewAdapter;
import kyc.DocumentUploadActivity;
import model.add_lead_details.CustomerDetails;
import model.add_lead_details.CustomerDetailsRequest;
import model.add_lead_details.CustomerDetailsResponse;
import model.add_lead_details.CustomerID;
import model.basic_details.BasicDetails;
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
        llEditAndCloseReview = findViewById(R.id.llEditAndCloseReview);
        rvResidentialDetails = findViewById(R.id.rvResidentialDetails);
        tvResidentialDetails = findViewById(R.id.tvResidentialDetails);
        tvCommonAppBarTitle = findViewById(R.id.tvCommonAppBarTitle);
        tvPersonalDetails = findViewById(R.id.tvPersonalDetails);
        rvPersonalDetails = findViewById(R.id.rvPersonalDetails);
        rvBasicDetails = findViewById(R.id.rvBasicDetails);
        btnEditReview.setOnClickListener(this);
        btnCloseReview.setOnClickListener(this);
        tvBasicDetails.setOnClickListener(this);
        tvResidentialDetails.setOnClickListener(this);
        tvPersonalDetails.setOnClickListener(this);
        tvCommonAppBarTitle.setText("REVIEW");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvVehDetailsTitle) {
            llEditAndCloseReview.setVisibility(View.VISIBLE);
            rvVehDetails.setVisibility(View.VISIBLE);
            if (flag) {
                displayReviewVehRes();
            } else {
                if (customVehDetails.getVehCategory().equals(getResources().getString(R.string.new_car))) {
                    displayNewCarVehicleDetails();
                } else if (customVehDetails.getVehCategory().equals(getResources().getString(R.string.old_car))) {
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
            if (flag) {
                displayReviewResidentialRes();
            } else {
                displayResidentialDetails();
            }

        } else if (v.getId() == R.id.tvPersonalDetails) {
            llEditAndCloseReview.setVisibility(View.VISIBLE);
            rvPersonalDetails.setVisibility(View.VISIBLE);
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
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_dob), CommonMethods.getStringValueFromKey(this, USER_DOB)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_gender), CommonMethods.getStringValueFromKey(this, GENDER)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_education), CommonMethods.getStringValueFromKey(this, EDUCATION)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), CommonMethods.getStringValueFromKey(this, MONTHLY_INCOME)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_total_emi), CommonMethods.getStringValueFromKey(this, MONTHLY_EMI)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), CommonMethods.getStringValueFromKey(this, LOAN_REQUIRED)));
        personalDetails.add(new ReviewData("PANCARD NO.", CommonMethods.getStringValueFromKey(this, cusEmpDetailsModel.getPanNum())));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_employment_type), CommonMethods.getStringValueFromKey(this, cusEmpDetailsModel.getEmpType())));
        personalDetails.add(new ReviewData("NUMBER OF EXISTING LOAN", CommonMethods.getStringValueFromKey(this, NO_OF_EXISTING_LOAN)));
        if(cusEmpDetailsModel.getEmpType().equalsIgnoreCase(getResources().getString(R.string.lbl_salaried)))
        {
            personalDetails.add(new ReviewData("BANK NAME", CommonMethods.getStringValueFromKey(this, cusEmpDetailsModel.getEmpSalAcc())));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_working_organization_name), cusEmpDetailsModel.getEmpOrgName()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_joining_date_of_your_current_org), cusEmpDetailsModel.getOrgJoiningDate()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_year_of_experience),cusEmpDetailsModel.getTotalExp()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_sal_mode), cusEmpDetailsModel.getEmpSalMode()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetailsModel.getIndustryType()));

        }
        else  if(cusEmpDetailsModel.getEmpType().equalsIgnoreCase(getResources().getString(R.string.lbl_business_owner)))
        {
            personalDetails.add(new ReviewData("EMPLOYMENT ROLE",cusEmpDetailsModel.getEmpRole()));
            personalDetails.add(new ReviewData("BUSINESS STARTED DATE", cusEmpDetailsModel.getbOPStartDate()));
            personalDetails.add(new ReviewData("LAST YEARS SALES", cusEmpDetailsModel.getLastYearTurnOver()));
            personalDetails.add(new ReviewData("INCOME AFTER TAX",cusEmpDetailsModel.getIncomeAfterTax()));
            personalDetails.add(new ReviewData("LAST YEAR DEPRECIATION", cusEmpDetailsModel.getLastYearDepreciation()));
            personalDetails.add(new ReviewData("WAS LAST YEAR ITR AUDITED", cusEmpDetailsModel.getItrAudited()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetailsModel.getIndustryType()));

        }
        else  if(cusEmpDetailsModel.getEmpType().equalsIgnoreCase(getResources().getString(R.string.lbl_self_employed_professional)))
        {
            personalDetails.add(new ReviewData("EMPLOYMENT ROLE",cusEmpDetailsModel.getEmpRole()));
            personalDetails.add(new ReviewData("PROFESSION",cusEmpDetailsModel.getProfession()));
            personalDetails.add(new ReviewData("BUSINESS STARTED DATE", cusEmpDetailsModel.getbOPStartDate()));
            personalDetails.add(new ReviewData("LAST YEARS SALES", cusEmpDetailsModel.getLastYearTurnOver()));
            personalDetails.add(new ReviewData("INCOME AFTER TAX",cusEmpDetailsModel.getIncomeAfterTax()));
            personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_industry_type), cusEmpDetailsModel.getIndustryType()));

        }
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
        residentialDetails.add(new ReviewData("PINCODE", customCityData.getPincode()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_state), customCityData.getState()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.city_lbl), customCityData.getCity()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.address_one_lbl), CommonMethods.getStringValueFromKey(this, ADDRESS1)));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.address_two_lbl), CommonMethods.getStringValueFromKey(this, ADDRESS2)));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_landmark), CommonMethods.getStringValueFromKey(this, LANDMARK)));

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
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_name), customBasicDetails.getSalutation()+" "+customBasicDetails.getFullName()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_email), customBasicDetails.getEmail()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), customBasicDetails.getCustomerMobile()));
        //basicDetails.add(new ReviewData(getResources().getString(R.string.otp_lbl), customBasicDetails.getOtp()));
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
        String vehHaveRegNo = "", vehRegNo = "", carHaveLoan = "", insuranceAmount = "";
        if (CommonMethods.getStringValueFromKey(this, CAR_HAVE_REG_NO).equalsIgnoreCase("Yes")) {
            vehHaveRegNo = CommonMethods.getStringValueFromKey(this, CAR_HAVE_REG_NO);
            if (!customVehDetails.getVehRegNum().equals("")) {
                vehRegNo = customVehDetails.getVehRegNum();
            } else {
                vehRegNo = "NA";
            }
        } else {
            vehHaveRegNo = CommonMethods.getStringValueFromKey(this, CAR_HAVE_REG_NO);
        }

        if (customVehDetails.getDoesCarHaveLoan()) {
            carHaveLoan = "Yes";
        } else {
            carHaveLoan = "No";
        }

        if (customVehDetails.getInsurance()) {
            insuranceAmount = "Yes";
        } else {
            insuranceAmount = "No";
        }
        reviewDataList.add(new ReviewData(CommonStrings.VEH_CATEGORY_TITLE, customVehDetails.getVehCategory()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_reg_num_qn), vehHaveRegNo));
        reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, vehRegNo));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), "2000"));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), customVehDetails.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), customVehDetails.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), customVehDetails.getVariant()));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), String.valueOf(customVehDetails.getOwnership())));
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
        reviewDataList.add(new ReviewData(CommonStrings.VEH_CATEGORY_TITLE, customVehDetails.getVehCategory()));
        Log.i(TAG, "getNewCarDataList: " + customVehDetails.getVehCategory());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), customVehDetails.getRegistrationYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), customVehDetails.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), customVehDetails.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), customVehDetails.getVariant()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_road_price), CommonMethods.getStringValueFromKey(this, ROAD_PRICE)));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_vehicle_purchase_amount), CommonMethods.getStringValueFromKey(this, VEH_PURCHASE_AMOUNT)));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insured_amount), CommonMethods.getStringValueFromKey(this, VEH_INSURED_AMOUNT)));
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
                    if (customerDetails.getCaseId() != null && !customerDetails.getCaseId().equals("")) {
                        CommonMethods.setValueAgainstKey(this, CommonStrings.CASE_ID, customerDetails.getCaseId());
                        CommonMethods.showToast(this, customerDetails.getCaseId());
                        Log.i(TAG, "onResponse: " + customerDetails.getCaseId());
                        vehDetailsResList = customerDetails.getVehicleDetails();
                        basicDetailsResList = customerDetails.getBasicDetails();
                        residentialDetailsResList = customerDetails.getResidentialDetails();
                        personalDetailsResList = customerDetails.getPersonalDetails();
                    } else {
                        CommonMethods.showToast(this, "No Case-Id found");
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        }
    }

    private List<ReviewData> prepareNewReviewDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_reg_num_qn), String.valueOf(vehDetailsResList.getHaveVehicleNumber())));
        reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, vehDetailsResList.getVehicleNumber()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), vehDetailsResList.getRegistrationYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), vehDetailsResList.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), vehDetailsResList.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), vehDetailsResList.getVariant()));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), String.valueOf(vehDetailsResList.getOwnership())));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_have_loan_qn), String.valueOf(vehDetailsResList.getDoesCarHaveLoan())));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insurance_on_vehicle), String.valueOf(vehDetailsResList.getInsurance())));
        reviewDataList.add(new ReviewData("INSURANCE AMOUNT", vehDetailsResList.getInsuranceAmount()));
        reviewDataList.add(new ReviewData("INSURANCE VALIDITY", vehDetailsResList.getInsuranceValidity()));
        reviewDataList.add(new ReviewData("INSURANCE TYPE", vehDetailsResList.getInsuranceType()));
        return reviewDataList;

    }

    private List<ReviewData> prepareNewResDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData("PINCODE", residentialDetailsResList.getPincode()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.city_lbl), residentialDetailsResList.getCustomerCity()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.address_one_lbl), residentialDetailsResList.getAddressLine1()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.address_two_lbl), residentialDetailsResList.getAddressLine2()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_landmark), residentialDetailsResList.getAddressLine3()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_residence_type), residentialDetailsResList.getResidenceType()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_when_did_you_moved_to_the_city), residentialDetailsResList.getMoveInCityYear()));
        reviewDataList.add(new ReviewData(getString(R.string.lbl_when_moved_to_current_residence), residentialDetailsResList.getMoveInResidenceYear()));

        return reviewDataList;

    }

    private List<ReviewData> preparePersonalDetailsRes() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_dob), personalDetailsResList.getBirthDate()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), personalDetailsResList.getSalaryPerMonth()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_total_emi), personalDetailsResList.getTotalEMIPaid()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.pancard_lbl), personalDetailsResList.getPANNumber()));
        reviewDataList.add(new ReviewData("SAVINGS ACCOUNT", personalDetailsResList.getSavingsAccount()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_gender), personalDetailsResList.getGender()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_education), personalDetailsResList.getEducation()));
        return reviewDataList;

    }

    private List<ReviewData> prepareNewReviewBasicDataList() {
        ArrayList<ReviewData> reviewDataList = new ArrayList<>();
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_name), basicDetailsResList.getFullName()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_email), basicDetailsResList.getEmail()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), basicDetailsResList.getCustomerMobile()));
        return reviewDataList;
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
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, prepareNewResDataList(), getResources().getString(R.string.title_basic_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    private void displayReviewPersonalRes() {
        try {
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, preparePersonalDetailsRes(), getResources().getString(R.string.title_basic_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }
}
