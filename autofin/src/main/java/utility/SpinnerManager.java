package utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Surya on 11/06/2018.(Class reused from MFC Business created by Surya Sir)
 * * Provides a global spinner API for a single activity
 * Use this when there are multiple network calls that are being performed in parallel
 * and we don't want to dismiss the spinner till all the calls are complete.
 * <p>
 * The class keeps track of how many show() and hide() requests happen for a particular context
 * and only dismisses the spinner once the difference between count(show()) and count(hide())
 * reaches zero
 */

public class SpinnerManager {
    private static Map<Context, Integer> map = Collections.synchronizedMap(new HashMap<Context, Integer>());
    private static Map<Context, ProgressDialog> spinnerMap = Collections.synchronizedMap(new HashMap<Context, ProgressDialog>());

    public static void showSpinner(Context ctx) {
        try {
            if (!map.containsKey(ctx)) {
                map.put(ctx, 1);
                spinnerMap.put(ctx, createProgressDialog(ctx));
            } else {
                int value = map.get(ctx);
                map.put(ctx, value + 1);
            }

           /* if (SplashActivity.progress && spinnerMap!=null) {
                spinnerMap.get(ctx).show();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSpinner(Context ctx) {
        try {
            if (!map.containsKey(ctx)) {
                return;
            }

            if (AutoFinDashBoardActivity.progress &&spinnerMap!=null) {
            Integer spinnerCount = map.get(ctx);
            if (spinnerCount == 1) {

                spinnerMap.get(ctx).dismiss();
                map.remove(ctx);
                spinnerMap.remove(ctx);
            } else if (spinnerCount > 1) {
                map.put(ctx, spinnerCount - 1);
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext, R.style.AppTheme_loading);
        try {

            if (AutoFinDashBoardActivity.progress) {
                dialog.show();
            }
        } catch (WindowManager.BadTokenException e) {

        }

        // dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_progress_dialog);

        // dialog.setMessage(Message);
        return dialog;
    }

}
