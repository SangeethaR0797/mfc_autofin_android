package utility;

import model.custom_model.CustomBasicDetailsModel;
import model.custom_model.CustomVehDetails;
import model.residential_models.CityData;
import model.vehicle_details.vehicle_category.stock_details.StockResponseData;

public class CommonStrings {
    public static final String CUSTOMER_DETAILS_URL_END = "customer-listing";
    public static final String IBB_ACCESS_TOKEN_URL_END = "get_access_token";
    public static final String VEH_OWNER_STRING = "veh_no_of_owners";
    public static final String VEH_INSURANCE_TYPE = "veh_insurance_type";
    public static final String STOCK_DETAILS_URL_END = "stock-details";
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
    public static final String ADD_LEAD_URL_END = "v2/add-lead";
    public static final String MOVED_TO_CCITY = "moved_to_CurrentCity";
    public static final String MOVED_TO_CRESIDENCE = "moved_to_current_residence";
    public static final String RES_TYPE_URL = "https://15.207.148.230:3004/api/masters/resident-type";
    public static final String USER_DOB = "user_dob";
    public static final String MONTHLY_INCOME = "monthly_income";
    public static final String MONTHLY_EMI ="monthly_emi";
    public static final String LOAN_REQUIRED ="loan_amount_required" ;
    public static final String PAN_CARD_NUMBER = "pan_card_number_value";
    public static final String BANK_NAME_URL = "https://15.207.148.230:3004/api/bank/get-banks";
    public static final String BANK_NAME ="bank_name" ;
    public static final String NO_OF_EXISTING_LOAN ="number_of_existing_loan" ;
    public static final String CURRENT_ORG_NAME ="working_organization_name" ;
    public static final String CURRENT_ORG_JOINING_DATE ="joining_date_of_current_organization" ;
    public static final String YEARS_OF_EXPERIENCE = "years_of_experience";
    public static String VEH_CATEGORY_URL = "https://15.207.148.230:3004/api/masters/vehicle-category";
    public static String RES_CITY_URL = "https://15.207.148.230:3004/api/pincode/city/";
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
    public static CustomVehDetails customVehDetails = new CustomVehDetails();
    public static CustomBasicDetailsModel customBasicDetails = new CustomBasicDetailsModel();
    public static CityData customCityData = new CityData();

}
