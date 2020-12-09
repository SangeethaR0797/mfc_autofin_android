package utility;

public class Global
{
    public static CommonURLs commonURLs=new CommonURLs("Stage");
    public static String baseURL = commonURLs.baseURL;
    public static String ibb_base_url = commonURLs.ibb_base_url;
    public static String stock_details_base_url = commonURLs.stock_details_base_url;
    public static String customerDetails_BaseURL = commonURLs.customerDetails_BaseURL;
    public static String customerAPI_BaseURL = commonURLs.customerAPI_BaseURL;
    public static String customerAPI_Master_URL = customerDetails_BaseURL+"masters/";
    public static String customer_bank_baseURL=commonURLs.customer_bank_baseURL;
    public static String document_upload_baseURL=commonURLs.document_upload_baseURL;
}
