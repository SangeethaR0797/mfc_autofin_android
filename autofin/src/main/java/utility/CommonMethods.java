package utility;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.mfc.autofin.framework.R;

public class CommonMethods {
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

    public static void deHighLightButton(Activity activity,Button button) {
        button.setBackground(activity.getResources().getDrawable(R.drawable.grey_box_1dp));
    }

    public static void highLightSelectedButton(Activity activity,Button button) {
        button.setBackground(activity.getResources().getDrawable(R.drawable.navy_blue_outline));
    }

}
