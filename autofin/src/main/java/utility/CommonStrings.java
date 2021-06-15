package utility;

import com.mfc.autofin.framework.Activity.ResidentialActivity.ResidentialCity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import model.add_lead_details.LoanDetails;
import model.addtional_fields.AdditionFields;
import model.basic_details.BasicDetails;
import model.basic_details.EmploymentDetails;
import model.basic_details.PersonalDetailsData;
import model.basic_details.ReferenceDetails;
import model.basic_details.ResidentialDetails;
import model.custom_model.CusEmpDetailsModel;
import model.custom_model.CustomBasicDetailsModel;
import model.kyc_model.Doc;
import model.residential_models.CityData;
import model.vehicle_details.vehicle_category.VehicleDetails;
import model.vehicle_details.vehicle_category.stock_details.StockResponseData;

public class CommonStrings {

    public static final String TOKEN_URL_END = "token/generate-token";
    public static final String RES_TYPE_URL_END = "resident-type";
    public static final String EMP_TYPE_URL_END = "employee-type";
    public static final String ORG_NAME_LIST_URL = "employer/employer-masters";
    public static final String BANK_NAME_URL = "bank/get-banks";
    public static final String GENDER_URL = "genders";
    public static final String EDUCATION_QUALIFICATION_URL = "educational-qualifications";
    public static final String UPLOAD_KYC_DOC_URL = "kyc/upload-customer-kyc";
    public static final String SELECT_RECOMMENDED_BANK_URL = "select-recommended-bank";
    public static final String INTERESTED_BANK_OFFER_URL = "get-selected-recommended-bank";
    public static final String RECOMMENDED_BANK_URL = "get-recommended-bank";
    public static final String ADD_BASIC_DETAILS_URL = "v2/add-basic-details";
    public static final String EDIT_LEAD_URL = "v2/edit-lead";
    public static final String CUSTOMER_DETAILS_URL = "v2/customer-details";
    public static final String CUSTOMER_DETAILS_END_URL = "customer-details";
    public static final String INDUSTRY_TYPE_URL = "industry-type";
    public static final String MAHINDRA_FILTER_URL = "employer/filter-by-name/mahind";
    public static final String GET_SALUTATION_URL = "salutations";
    public static final String PROFESSION_URL = "professions";
    public static final String PROFESSION_FILTER_URL = "professions/deve";
    public static final String RES_CITY_URL = "pincode/city/";
    public static final String GET_ADDITIONAL_FIELDS = "additionalfields/additional-fields";
    public static final String SUBMIT_ADDITIONAL_FIELDS_URL = "submit-additional-data";

    public static final String CUSTOMER_DETAILS_URL_END = "customer-listing";
    public static final String IBB_ACCESS_TOKEN_URL_END = "get_access_token";
    public static final String VEH_OWNER_STRING = "veh_no_of_owners";
    public static final String VEH_INSURANCE_TYPE = "veh_insurance_type";
    public static final String STOCK_DETAILS_URL_END = "MMV/stock-details";
    public static final String VEH_REG_NO = "veh_registration_number";
    public static final String VEH_MFG_YEAR = "veh_mfg_year";
    public static final String VEH_MFG_MAKE = "veh_mfg_make";
    public static final String VEH_MFG_MODEL = "veh_mfg_model";
    public static final String VEH_VARIANT = "veh_variant";
    public static final String VEH_INSURANCE_VALIDITY = "veh_insurance_validity_dt";
    public static final String DEALER_ID_VAL = "dealer_id_val";
    public static final String USER_TYPE_VAL = "user_type_val";
    public static final String APP_NAME_VAL = "app_name_val";
    public static final String OTP_URL_END = "generate-otp";
    public static final String VALIDATE_OTP_URL_END = "validate-otp";
    public static final String ADD_LEAD_URL_END = "add-lead";
    public static final String UPDATE_LEAD_BASIC_DETAILS_URL_END = "update-basic-details";
    public static final String ADD_EMPLOYMENT_URL_END = "add-employment-details";
    public static final String ADD_RESIDENT_URL_END = "add-resident-details";
    public static final String MOVED_TO_CCITY = "moved_to_CurrentCity";
    public static final String MOVED_TO_CRESIDENCE = "moved_to_current_residence";
    public static final String USER_DOB = "user_dob";
    public static final String MONTHLY_INCOME = "monthly_income";
    public static final String MONTHLY_EMI = "monthly_emi";
    public static final String LOAN_REQUIRED = "loan_amount_required";
    public static final String PAN_CARD_NUMBER = "pan_card_number_value";
    public static final String BANK_NAME = "bank_name";
    public static final String NO_OF_EXISTING_LOAN = "number_of_existing_loan";
    public static final String CURRENT_ORG_NAME = "working_organization_name";
    public static final String CURRENT_ORG_JOINING_DATE = "joining_date_of_current_organization";
    public static final String YEARS_OF_EXPERIENCE = "years_of_experience";
    public static final String SALARY_MODE = "salary_mode";
    public static final String RESIDENCE_TYPE = "residence_type";
    public static final String EMP_TYPE_VAL = "employment_type";
    public static final String LIKELY_PURCHASE_DATE = "likely_purchase_date";
    public static final String ROAD_PRICE = "road_price";
    public static final String VEH_PURCHASE_AMOUNT = "vehicle_purchase_amount";
    public static final String VEH_INSURED_AMOUNT = "vehicle_insured_amount";
    public static final String GENDER = "personal_details_gender";
    public static final String EDUCATION = "personal_details_education_qualification";
    public static final String VEH_CATEGORY_TITLE = "VEHICLE CATEGORY";
    public static final String VEH_REG_NO_TITLE = "VEHICLE REGISTRATION NUMBER";
    public static final String CAR_HAVE_REG_NO = "vehicle_have_reg_no_or_not";
    public static final String ADDRESS1 = "address_line_1";
    public static final String ADDRESS2 = "address_line_2";
    public static final String LANDMARK = "landmark";
    public static final String PANCARD_LBL = "PANCARD NUMBER";
    public static final String CNAME = "customer_name";
    public static final String CEMAIL = "customer_email";
    public static final String CMOBILE_NUM = "customer_mobile_number";
    public static final String LEAD_CREATION_DATE = "lead_creation_date";
    public static final String LEAD_STATUS = "lead_status";
    public static final String KYC_STATUS = "kyc_status";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String EMPLOYMENT_ROLE_VAL = "employment_role";
    public static final String PROFESSION_VAL = "profession_role";
    public static final String BUSINESS_OR_PROFESSION_START_DATE = "business_or_profession_started_date";
    public static final String LAST_YEAR_TURN_OVER = "last_year_turn_over_or_sales";
    public static final String INCOME_AFTER_TAX = "income_after_tax";
    public static final String ITR_AUDITERD_OR_NOT = "was_last_year_itr_audited_or_not";
    public static final String EMPLOYER_NAME = "employer_name";
    public static final String CASE_ID = "case_id";
    public static final String INDUSTRY_TYPE = "industry_type";
    public static final String PREVIOUS_VALUE_LBL = "previous_value_lbl";
    public static final String PREVIOUS_VALUE = "previous_screen_value";
    public static final String LAST_YEAR_PROFIT = "last_year_profit";
    public static final String PROCESS_WITH_BANK_URL_END = "process-customer-with-bank";
    public static final String CUSTOMER_ADDITIONAL_FIELDS = "customer-additional-data";
    public static final String KMS_DRIVEN = "Masters/kms-driven";
    public static final String EMPLOYEEMENT_END_POINT = "Masters/employee-type";
    public static final String RESIDENT_TYPE_END_POINT = "Masters/resident-type";
    public static final String RESIDENT_YEARS_END_POINT = "Masters/resident-year";
    public static final String BANK_LIST_END_POINT = "Bank/get-banks";
    public static final String COMMON_BANK_LIST_END_POINT = "Bank/common-banks";
    public static final String SALUTATION_END_POINT = "Masters/salutations";
    public static final String VALIDATE_LEAD = "validate-lead";
    public static final String RESET_JOURNEY = "reset-customer-journey";
    public static final String CITY_SEARCH_VIA_TEXT_END_POINT = "Pincode/city-by-name/";
    public static final String TERMS_AND_CONDITION_URL="https://www.stagemfc.com/autofin-TermsAndCondition";

    public static String TOKEN_VALUE = "";
    public static String IBB_TOKEN_VALUE = "";

    public static String APP_NAME = "";
    public static String DEALER_ID = "";
    public static String USER_TYPE = "";

    public static boolean IS_OLD_LEAD = false;
    public static boolean IS_ADDITIONAL_DETAILS_FILLED = false;
    public static String IBB_PASSWORD = "dHk69ffu7ebP";
    public static String IBB_USERNAME = "mfc@ibb.com";
    public static String IBB_VEH_DETAILS_END_POINT = "MFC";
    public static String IBB_TAG = "app";
    public static String IBB_YEAR = "year";
    public static String IBB_MONTH = "month";
    public static String IBB_MAKE = "make";
    public static String IBB_MODEL = "model";
    public static String IBB_VARIANT = "variant";
    public static StockResponseData stockResData = new StockResponseData();
    public static VehicleDetails customVehDetails = new VehicleDetails();
    public static LoanDetails customLoanDetails = new LoanDetails();
    public static BasicDetails customBasicDetails = new BasicDetails();
    public static ResidentialDetails customResDetails = new ResidentialDetails();
    public static PersonalDetailsData customPersonalDetails = new PersonalDetailsData();
    public static EmploymentDetails cusEmpDetails = new EmploymentDetails();
    public static ReferenceDetails referenceDetails = new ReferenceDetails();
    public static List<AdditionFields> additionFieldsList = new ArrayList<AdditionFields>();
    public static List<Doc> documentList = new ArrayList<>();


    public static final String PREFF_ENCRYPT_TOKEN = "encrypt_token";
    public static final String PREFF_ENCRYPT_IBB_TOKEN = "ibb_encrypt_token";

    public static final String YEAR = "year";
    public static final String MAKE = "make";
    public static final String MODEL = "model";
    public static final String VARIANT = "variant";
    public static final String BANK_DATA_CALL = "bank";

    public static final String VEHICLE_DATA = "vehicleData";

    public static final int RESULT_CODE = 500;
    public static final int CAR_BASIC_DETAIL_ACTIVITY_REQUEST_CODE = 501;
    public static final int MASTER_DETAIL_ACTIVITY_REQUEST_CODE = 502;

    public static final String SELECTED_DATA = "selectedData";
    public static final String SELECTED_DATA_TYPE = "selectedDataType";


    public static final String PRIVACY_AND_POLICY_URL="https://www.stagemfc.com/autofin-TermsAndCondition";
    public static final String URL="url";
    public static final String TITLE="title";

    public static final String LOAN_AMOUNT_URL="default-loan/";

    public static final String UPDATE_ADDRESS_URL="update-address";
    public static final String ADDITIONAL_FIELDS_URL="AdditionalFields/get-additional-fields";
    public static final String KYC_UPLOAD_URL_END_POINT="Masters/kyc-documents/";
    public static final String DASHBOARD_DETAILS_END_POINT="dashboard";
    public static final String GET_RULE_ENGINE_BANKS_END_POINT="Bank/get-rule-engine-banks";
    public static final String BANKTC_END_POINT="terms-and-contition/privacy-policy/";
    public static final String GET_FINAL_OTP_URL_END_POINT="generate-final-submit-otp";
    public static final String VALIDATE_FINAL_OTP="validate-final-submit-otp";
    public static final String UPLOAD_KYC_DOC_URL_V2="https://15.207.148.230:3003/api/kyc/upload-customer-kyc";
    public static final String BANK_BASE_URL="https://15.207.148.230:3004/api/Bank/";
    public static final String NOTICE_BOARD_ACTION_END_POINT="action-notice-board";
}
