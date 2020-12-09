package utility;

import android.app.Activity;
import android.app.Application;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.add_lead_details.LoanDetails;
import model.basic_details.BasicDetails;
import model.basic_details.EmploymentDetails;
import model.basic_details.PersonalDetailsData;
import model.basic_details.ResidentialDetails;
import model.custom_model.CusEmpDetailsModel;
import model.custom_model.CustomBasicDetailsModel;
import model.residential_models.CityData;
import model.vehicle_details.vehicle_category.VehicleDetails;

public class CommonMethods {
    private static final String TAG = CommonMethods.class.getSimpleName();
    public static Activity appContext;

    public static boolean isInternetWorking(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
        }
        return false;
    }

    public static void setValueAgainstKey(Activity activity, String Key,
                                          String value) {

        try {
            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(activity);
            SharedPreferences.Editor editor = preference.edit();
            editor.putString(Key, value);

            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String getStringValueFromKey(Activity activity, String key) {

        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(activity.getApplicationContext());

        return preference.getString(key, "");

    }

    public static void deHighLightButton(Activity activity, Button button) {
        button.setBackground(activity.getResources().getDrawable(R.drawable.grey_box_1dp));
    }

    public static void highLightSelectedButton(Activity activity, Button button) {
        button.setBackground(activity.getResources().getDrawable(R.drawable.navy_blue_outline));
    }

    public static String getIVDateInFormat(String string) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd MMMM yyyy");

        Date myDate = null;
        try {
            myDate = dateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd");
        String finalDate = timeFormat.format(myDate);
        return finalDate;
    }

    public static void showToast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static void clearData() {
        CommonStrings.customVehDetails = new VehicleDetails();
        CommonStrings.customBasicDetails = new BasicDetails();
        CommonStrings.customResDetails = new ResidentialDetails();
        CommonStrings.cusEmpDetails = new EmploymentDetails();
        CommonStrings.customLoanDetails = new LoanDetails();
        CommonStrings.customPersonalDetails=new PersonalDetailsData();
    }

    public static void redirectToDashboard(Activity activity) {
        Intent intent = new Intent(activity, AutoFinDashBoardActivity.class);
        intent.putExtra(AutoFinConstants.APP_NAME, "MFCBusiness");
        intent.putExtra(AutoFinConstants.DEALER_ID, CommonMethods.getStringValueFromKey(activity, CommonStrings.DEALER_ID_VAL));
        intent.putExtra(AutoFinConstants.USER_TYPE, CommonMethods.getStringValueFromKey(activity, CommonStrings.USER_TYPE_VAL));
        activity.startActivity(intent);
    }

    public static String removeDecimal(double givenVal) {
        double actualVal = givenVal;
        NumberFormat nf = DecimalFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        String str = nf.format(actualVal);
        return str;
    }

    public static String getFormattedDouble(double givenVal) {
        double actualVal = givenVal;
        int decimalPlaces = 2;
        BigDecimal bd = new BigDecimal(actualVal);
        bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
        actualVal = bd.doubleValue();
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        System.out.println(format.format(new BigDecimal(actualVal)));
        return String.valueOf(actualVal);
    }

    public static String getFormattedAmount(double givenVal) {

        String val = String.format("%.0f", givenVal);
        Log.i(TAG, "getFormattedAmount: "+val);
        double cVal=Double.parseDouble(val);
        DecimalFormat formatter = new DecimalFormat("#,##,###");
        String yourFormattedString = formatter.format(givenVal);

        return yourFormattedString;
    }

    public static String getToken()
    {
        SharedPreferences preference = PreferenceManager
                .getDefaultSharedPreferences(appContext.getApplicationContext());

        return preference.getString("token", "");

    }
}
