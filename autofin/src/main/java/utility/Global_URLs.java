package utility;

public class Global_URLs {
    public static String baseURL = "";
    public static String environment = "";
    public static String IBB_BASE_URL = "";
    public static String STOCK_DETAILS_BASE_URL = "";
    public static String customerDetails_BaseURL = "";
    public static String customerAPI_BaseURL = "";

    public Global_URLs(String environment) {
        this.environment = environment;

        if (this.environment.equalsIgnoreCase("Stage")) {
            customerDetails_BaseURL = "https://15.207.148.230:3004/api/";
            customerAPI_BaseURL = "https://15.207.148.230:3007/api/customer/";
            IBB_BASE_URL = "https://api2.stageibb.com/api/";
            STOCK_DETAILS_BASE_URL = "https://15.207.148.230:3004/api/MMV/";

        } else if (this.environment.equalsIgnoreCase("Production")) {

        }

    }
}
