package utility;

import model.custom_model.CustomVehDetails;
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
    public static String VEH_CATEGORY_URL = "https://15.207.148.230:3004/api/masters/vehicle-category";
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
    public static CustomVehDetails customVehDetails =new CustomVehDetails();

}
