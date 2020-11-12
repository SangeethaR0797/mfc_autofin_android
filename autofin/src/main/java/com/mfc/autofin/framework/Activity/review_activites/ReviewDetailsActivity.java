package com.mfc.autofin.framework.Activity.review_activites;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mfc.autofin.framework.Activity.BasicDetailsActivities.BasicDetailsActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.UserDOBActivity;
import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidentialCity;
import com.mfc.autofin.framework.Activity.VehicleDetailsActivities.VehicleCategory;
import com.mfc.autofin.framework.R;

import java.util.ArrayList;
import java.util.List;
import controller.ReviewAdapter;
import model.custom_model.ReviewData;
import utility.CommonMethods;
import utility.CommonStrings;

import static utility.CommonStrings.*;

public class ReviewDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ReviewDetailsActivity.class.getSimpleName();
    private TextView tvVehDetailsTitle,tvEditVehDetails,tvCommonAppBarTitle,tvBasicDetails,tvEditBasicDetails,tvResidentialDetails,tvEditResidentialDetails,tvPersonalDetails,tvEditPersonalDetails;
    private RecyclerView rvVehDetails,rvBasicDetails,rvResidentialDetails,rvPersonalDetails;
    private FloatingActionButton fab_close_review_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        initView();
    }

    private void initView() {
        tvVehDetailsTitle = findViewById(R.id.tvVehDetailsTitle);
        tvVehDetailsTitle.setOnClickListener(this);
        rvVehDetails = findViewById(R.id.rvVehDetails);
        tvBasicDetails=findViewById(R.id.tvBasicDetails);
        tvEditBasicDetails=findViewById(R.id.tvEditBasicDetails);
        tvEditVehDetails=findViewById(R.id.tvEditVehDetails);
        tvEditPersonalDetails=findViewById(R.id.tvEditPersonalDetails);
        tvEditResidentialDetails=findViewById(R.id.tvEditResidentialDetails);
        rvResidentialDetails=findViewById(R.id.rvResidentialDetails);
        tvEditResidentialDetails=findViewById(R.id.tvEditResidentialDetails);
        tvResidentialDetails=findViewById(R.id.tvResidentialDetails);
        tvCommonAppBarTitle=findViewById(R.id.tvCommonAppBarTitle);
        tvPersonalDetails=findViewById(R.id.tvPersonalDetails);
        tvEditPersonalDetails=findViewById(R.id.tvEditPersonalDetails);
        rvPersonalDetails=findViewById(R.id.rvPersonalDetails);
        fab_close_review_details=findViewById(R.id.fab_close_review_details);
        rvBasicDetails=findViewById(R.id.rvBasicDetails);
        tvEditVehDetails.setOnClickListener(this);
        tvBasicDetails.setOnClickListener(this);
        tvEditBasicDetails.setOnClickListener(this);
        tvResidentialDetails.setOnClickListener(this);
        tvEditResidentialDetails.setOnClickListener(this);
        tvPersonalDetails.setOnClickListener(this);
        tvEditPersonalDetails.setOnClickListener(this);
        fab_close_review_details.setOnClickListener(this);
        tvCommonAppBarTitle.setText("REVIEW");
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tvVehDetailsTitle) {
            fab_close_review_details.setVisibility(View.VISIBLE);
            rvVehDetails.setVisibility(View.VISIBLE);
            tvEditVehDetails.setVisibility(View.VISIBLE);
            if (customVehDetails.getVehCategory().equals(getResources().getString(R.string.new_car))) {
                displayNewCarVehicleDetails();
            } else if (customVehDetails.getVehCategory().equals(getResources().getString(R.string.old_car))) {
                displayOldCarVehicleDetails();
            }
        }
        else if(v.getId()==R.id.fab_close_review_details)
        {
            if(rvVehDetails.getVisibility()==View.VISIBLE)
            {
                rvVehDetails.setVisibility(View.GONE);
                tvEditVehDetails.setVisibility(View.GONE);
                fab_close_review_details.setVisibility(View.GONE);

            }
            else if(rvBasicDetails.getVisibility()==View.VISIBLE)
            {
                rvBasicDetails.setVisibility(View.GONE);
                tvEditBasicDetails.setVisibility(View.GONE);
                fab_close_review_details.setVisibility(View.GONE);
            }
            else if(rvResidentialDetails.getVisibility()==View.VISIBLE)
            {
                rvResidentialDetails.setVisibility(View.GONE);
                tvEditResidentialDetails.setVisibility(View.GONE);
                fab_close_review_details.setVisibility(View.GONE);
            }
            else if(rvPersonalDetails.getVisibility()==View.VISIBLE)
            {
                rvPersonalDetails.setVisibility(View.GONE);
                tvEditPersonalDetails.setVisibility(View.GONE);
                fab_close_review_details.setVisibility(View.GONE);
            }

        }
        else if (v.getId() == R.id.tvBasicDetails) {
            fab_close_review_details.setVisibility(View.VISIBLE);
            rvBasicDetails.setVisibility(View.VISIBLE);
            tvEditBasicDetails.setVisibility(View.VISIBLE);
                displayBasicDetails();

        }
        else if (v.getId() == R.id.tvResidentialDetails) {
            fab_close_review_details.setVisibility(View.VISIBLE);
            rvResidentialDetails.setVisibility(View.VISIBLE);
            tvEditResidentialDetails.setVisibility(View.VISIBLE);
            displayResidentialDetails();
        }
        else if (v.getId() == R.id.tvPersonalDetails) {
            fab_close_review_details.setVisibility(View.VISIBLE);
            rvPersonalDetails.setVisibility(View.VISIBLE);
            tvEditPersonalDetails.setVisibility(View.VISIBLE);
            displayPersonalDetails();

        }
        else if(v.getId()==R.id.tvEditVehDetails)
        {
            startActivity(new Intent(this, VehicleCategory.class));
        }
        else if(v.getId()==R.id.tvEditBasicDetails)
        {
            startActivity(new Intent(this, BasicDetailsActivity.class));
        }
        else if(v.getId()==R.id.tvEditResidentialDetails)
        {
            startActivity(new Intent(this, ResidentialCity.class));
        }
        else if(v.getId()==R.id.tvEditPersonalDetails)
        {
            startActivity(new Intent(this, UserDOBActivity.class));
        }
    }

    private void displayPersonalDetails()
    {
        try {
            ReviewAdapter reviewAdapter=new ReviewAdapter(this,getPersonalDetails(),getResources().getString(R.string.title_personal_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvPersonalDetails.setLayoutManager(layoutManager);
            rvPersonalDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> getPersonalDetails()
    {
        ArrayList<ReviewData> personalDetails=new ArrayList<>();
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_dob), CommonMethods.getStringValueFromKey(this, USER_DOB)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_gender),CommonMethods.getStringValueFromKey(this, GENDER)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_education), CommonMethods.getStringValueFromKey(this, EDUCATION)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_monthly_income), CommonMethods.getStringValueFromKey(this, MONTHLY_INCOME)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_total_emi), CommonMethods.getStringValueFromKey(this, MONTHLY_EMI)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_loan_required), CommonMethods.getStringValueFromKey(this, LOAN_REQUIRED)));
        personalDetails.add(new ReviewData("PANCARD NO.", CommonMethods.getStringValueFromKey(this, PAN_CARD_NUMBER)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_employment_type), CommonMethods.getStringValueFromKey(this, EMP_TYPE_VAL)));
        personalDetails.add(new ReviewData("BANK NAME", CommonMethods.getStringValueFromKey(this, BANK_NAME)));
        personalDetails.add(new ReviewData("NUMBER OF EXISTING LOAN", CommonMethods.getStringValueFromKey(this, NO_OF_EXISTING_LOAN)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_working_organization_name), CommonMethods.getStringValueFromKey(this, CURRENT_ORG_NAME)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_joining_date_of_your_current_org), CommonMethods.getStringValueFromKey(this, CURRENT_ORG_JOINING_DATE)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_year_of_experience), CommonMethods.getStringValueFromKey(this, YEARS_OF_EXPERIENCE)));
        personalDetails.add(new ReviewData(getResources().getString(R.string.lbl_sal_mode), CommonMethods.getStringValueFromKey(this, SALARY_MODE)));
        return personalDetails;
    }

    private void displayResidentialDetails()
    {
        try {
            ReviewAdapter reviewAdapter=new ReviewAdapter(this,getResidentialDetails(),getResources().getString(R.string.title_residential_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvResidentialDetails.setLayoutManager(layoutManager);
            rvResidentialDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> getResidentialDetails()
    {
        ArrayList<ReviewData> residentialDetails=new ArrayList<>();
        residentialDetails.add(new ReviewData("PINCODE", customCityData.getPincode()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_state), customCityData.getState()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.city_lbl), customCityData.getCity()));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.address_one_lbl), CommonMethods.getStringValueFromKey(this, ADDRESS1)));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.address_two_lbl), CommonMethods.getStringValueFromKey(this, ADDRESS2)));
        residentialDetails.add(new ReviewData(getResources().getString(R.string.lbl_landmark), CommonMethods.getStringValueFromKey(this, LANDMARK)));

        return residentialDetails;
    }

    private void displayBasicDetails()
    {
        try {
            ReviewAdapter reviewAdapter=new ReviewAdapter(this,getBasicDetails(),getResources().getString(R.string.title_basic_details));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvBasicDetails.setLayoutManager(layoutManager);
            rvBasicDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private List<ReviewData> getBasicDetails()
    {
        ArrayList<ReviewData> basicDetails=new ArrayList<>();
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_name), customBasicDetails.getFullName()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_email), customBasicDetails.getEmail()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.lbl_phone_no), customBasicDetails.getCustomerMobile()));
        basicDetails.add(new ReviewData(getResources().getString(R.string.otp_lbl), customBasicDetails.getOtp()));
        return basicDetails;
    }

    private void displayOldCarVehicleDetails()
    {
        try {
            ReviewAdapter reviewAdapter=new ReviewAdapter(this,getOldCarDataList(),getResources().getString(R.string.vehicle_details_title));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private List<ReviewData> getOldCarDataList()
    {
        ArrayList<ReviewData> reviewDataList=new ArrayList<>();
        String vehHaveRegNo="",vehRegNo="";
        if(CommonMethods.getStringValueFromKey(this, CAR_HAVE_REG_NO).equalsIgnoreCase("Yes"))
        {
            vehHaveRegNo=CommonMethods.getStringValueFromKey(this, CAR_HAVE_REG_NO);
            if(!customVehDetails.getVehRegNum().equals(""))
            {
                vehRegNo=customVehDetails.getVehRegNum();
            }
            else
            {
                vehRegNo="No data available";
            }
        }
        else
        {
            vehHaveRegNo=CommonMethods.getStringValueFromKey(this, CAR_HAVE_REG_NO);
        }

        reviewDataList.add(new ReviewData(CommonStrings.VEH_CATEGORY_TITLE, customVehDetails.getVehCategory()));
        Log.i(TAG, "getOldCarDataList: "+customVehDetails.getVehCategory());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_reg_num_qn),vehHaveRegNo));
        Log.i(TAG, "getOldCarDataList: "+vehHaveRegNo);
        reviewDataList.add(new ReviewData(CommonStrings.VEH_REG_NO_TITLE, vehRegNo));
        Log.i(TAG, "getOldCarDataList: "+ vehRegNo);
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), "2000"));

        // reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), customVehDetails.getRegistrationYear()));
        Log.i(TAG, "getOldCarDataList: "+ customVehDetails.getRegistrationYear());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), customVehDetails.getMake()));
        Log.i(TAG, "getOldCarDataList: "+ customVehDetails.getMake());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), customVehDetails.getModel()));
        Log.i(TAG, "getOldCarDataList: "+ customVehDetails.getModel());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), customVehDetails.getVariant()));
        Log.i(TAG, "getOldCarDataList: "+ customVehDetails.getVariant());
        reviewDataList.add(new ReviewData(getString(R.string.lbl_veh_ownership), String.valueOf(customVehDetails.getOwnership())));
        Log.i(TAG, "getOldCarDataList: "+ customVehDetails.getOwnership());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.vehicle_have_loan_qn), String.valueOf(customVehDetails.getDoesCarHaveLoan())));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insurance_on_vehicle), String.valueOf(customVehDetails.getInsurance())));
        reviewDataList.add(new ReviewData("INSURANCE AMOUNT", customVehDetails.getInsuranceAmount()));
        reviewDataList.add(new ReviewData("INSURANCE VALIDITY", customVehDetails.getInsuranceValidity()));
        reviewDataList.add(new ReviewData("INSURANCE TYPE", customVehDetails.getInsuranceType()));
        return reviewDataList;
    }

    private void displayNewCarVehicleDetails()
    {
        try {
            ReviewAdapter reviewAdapter=new ReviewAdapter(this,getNewCarDataList(),getResources().getString(R.string.vehicle_details_title));
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            rvVehDetails.setLayoutManager(layoutManager);
            rvVehDetails.setAdapter(reviewAdapter);
        } catch (NullPointerException nullPointerExc) {
            nullPointerExc.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private List<ReviewData> getNewCarDataList()
    {
        ArrayList<ReviewData> reviewDataList=new ArrayList<>();
        reviewDataList.add(new ReviewData(CommonStrings.VEH_CATEGORY_TITLE, customVehDetails.getVehCategory()));
        Log.i(TAG, "getNewCarDataList: "+customVehDetails.getVehCategory());
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_reg_year), customVehDetails.getRegistrationYear()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_make), customVehDetails.getMake()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_model), customVehDetails.getModel()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_veh_variant), customVehDetails.getVariant()));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_road_price), CommonMethods.getStringValueFromKey(this, ROAD_PRICE)));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_vehicle_purchase_amount),CommonMethods.getStringValueFromKey(this, VEH_PURCHASE_AMOUNT)));
        reviewDataList.add(new ReviewData(getResources().getString(R.string.lbl_insured_amount),CommonMethods.getStringValueFromKey(this, VEH_INSURED_AMOUNT) ));
        return reviewDataList;

    }
}
