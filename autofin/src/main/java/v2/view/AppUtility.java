package v2.view;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppUtility {
    private static AppUtility appUtility;
    private static Context mContext;

    private AppUtility(Context context) {
        mContext = context;
    }


    public static String DAY = "DAY";
    public static String HOURS = "HOURS";
    public static String MINUTE = "MINUTE";
    public static String SECOND = "SECOND";
    public static int MAX_VALIDE_MINUTE_FOR_RECORD = 10;

    public static AppUtility getInstance() {
        if (appUtility == null) {
            appUtility = new AppUtility(mContext);
        }
        return appUtility;

    }

    public static String getCurrentDateTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return formatter.format(date);
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {


            if (activity.getCurrentFocus() != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        activity.getCurrentFocus().getWindowToken(), 0);
            } else {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        } catch (Exception e) {

        }
    }

    public static void hideSoftKeyboard(Activity activity, View view) {
        try {
            if (view != null) {
                InputMethodManager inputMethodManager =
                        (InputMethodManager) activity.getSystemService(
                                Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(
                        view.getWindowToken(), 0);
            } else {
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        } catch (Exception e) {

        }
    }


    public static String getFormatedMobileNumber(String mobileNumber) {
        mobileNumber = mobileNumber.replace(" ", "");
        mobileNumber = mobileNumber.substring(0, 5) + " " + mobileNumber.substring(5, mobileNumber.length());
        return mobileNumber;

    }


    public static String getFormatedMobileNumberWithCountryCode(String mobileNumber) {
        return "+91 " + getFormatedMobileNumber(mobileNumber);
    }

    private static String TimeStampConverter(final String inputFormat,
                                             String inputTimeStamp, final String outputFormat)
            throws ParseException {
        return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(
                inputFormat).parse(inputTimeStamp));
    }

    public static String getUnFormatedMobileNumber(String mobileNumber) {
        mobileNumber = mobileNumber.replace(" ", "");
        mobileNumber = mobileNumber.replace("+91", "");
        return mobileNumber;

    }

    public static String getFormatedPrice(Object price) {
        return getFormatedPrice(String.valueOf(price));
    }

    public static String getFormatedPrice(String price) {
        return "₹ " + price;
    }

    public static String getFormatedAmmountWithoutRupeeIcon(Object amt) {
        return getFormatedAmmount(String.valueOf(amt)).replace("₹ ", "");
    }

    public static String getFormatedAmmountWithoutRupeeIcon(String amt) {
        return getFormatedAmmount(String.valueOf(amt)).replace("₹ ", "");
    }

    public static String getFormatedAmmount(Object amt) {
        return getFormatedAmmount(String.valueOf(amt));
    }

    public static String getUnFormatedAmmount(String amt) {
        if (amt == null) {
            return "";
        } else {
            amt = amt.replace(",", "");
            amt = amt.replace("₹", "");
            amt = amt.replace(" ", "");
        }
        return amt;
    }

    public static String getFormatedAmmount(String amt) {
        try {
            if (amt == null) {
                return "";
            } else {
                amt = amt.replace(",", "");
                amt = amt.replace("₹", "");
                amt = amt.replace(" ", "");
                String[] amt_parts = amt.split("\\.");
//                if (amt_parts[0].equals("0"))
//                    return amt_parts[0] + "." + amt_parts[1];
                if (amt_parts.length > 1) {
                    amt = amt_parts[0] + "." + amt_parts[1];
                } else {
                    amt = amt_parts[0];
                }
                double amount = Double.parseDouble(amt);
                DecimalFormat formatter = new DecimalFormat("#,##0.00");
                amt = formatter.format(amount);
                if (".0".equalsIgnoreCase(amt)) {
                    return "0";
                }

//                amt = amt.replace(".0", "");
                amt = "₹ " + amt;
                return amt;
            }
        } catch (Exception e) {
            return "";
        }

    }


    public static double getDoubleWith2DecimalValue(double value) {
        return Double.parseDouble(new DecimalFormat("###0.00").format(value));

    }


    public static String capitalizeWord(String capString) {
        if (TextUtils.isEmpty(capString)) {
            return "";
        }
        StringBuffer capBuffer = new StringBuffer();
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(capString);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();
    }


    public static String convertGstDate(String gstDate) {
        String inputFormat = "dd/MM/yyyy";
        String outputFormat = "yyyy-MM-dd";
        String date = "";
        try {
            date = TimeStampConverter(inputFormat, gstDate,
                    outputFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getFormatedAmount(String amount) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(Double.parseDouble(amount));
    }

    /*public static double getNumberFromFormattedString(String formattedStr) {
        String str = formattedStr.replace("₹", "").trim();
        return Double.parseDouble(str)
    }*/

    public static String getFormattedDistance(double distance) {
        DecimalFormat df2 = new DecimalFormat("#.##");
        String formattedDistance = df2.format(distance);
        return formattedDistance;
    }


    public static HashMap<String, Long> getDateTimeDiffrence(String date1, String date2) {
        HashMap<String, Long> dateDiff = new HashMap<>();
        String dateStart = date1;
        String dateStop = date2;

        //HH converts hour in 24 hours format (0-23), day calculation
        //  SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            dateDiff.put(DAY, diffDays);
            dateDiff.put(HOURS, diffHours);
            dateDiff.put(MINUTE, diffMinutes);
            dateDiff.put(SECOND, diffSeconds);

            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateDiff;
    }
}


