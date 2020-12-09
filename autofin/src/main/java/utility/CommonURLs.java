package utility;

public class CommonURLs {
    public String baseURL = "";
    public String environment = "";
    public String ibb_base_url = "";
    public String stock_details_base_url = "";
    public String customerDetails_BaseURL = "";
    public String customerAPI_BaseURL = "";
    public String customer_bank_baseURL = "";
    public String document_upload_baseURL = "";

    public CommonURLs(String environment) {
        this.environment = environment;

        if (this.environment.equalsIgnoreCase("Stage")) {
            baseURL="https://15.207.148.230:3004/api/";
            customerDetails_BaseURL = "https://15.207.148.230:3004/api/";
            customerAPI_BaseURL = "https://15.207.148.230:3007/api/customer/";
            ibb_base_url = "https://api2.stageibb.com/api/";
            stock_details_base_url = "https://15.207.148.230:3004/api/";
            customer_bank_baseURL = "https://15.207.148.230:3002/api/bank/";
            document_upload_baseURL="https://15.207.148.230:3003/api/";
        } else if (this.environment.equalsIgnoreCase("Production")) {

        }

    }
}
