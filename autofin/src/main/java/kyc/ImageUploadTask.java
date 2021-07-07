package kyc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.util.Timer;
import java.util.TimerTask;

import aws.AutofinMediaManager;
import utility.SpinnerManager;

public class ImageUploadTask extends AsyncTask<Void, Void, String> {


    private final String imagePath;
    private final int statusCode;
    private final String caseId;
    private final String imageName;

    private String imageURL;
    private final String key;

    private final Context mContext;

    private final ImageUploadCompleted mImageUploadCompleted;

    public ImageUploadTask(Context mContext, String imagepath, String caseId, String imageName, String key, int statuscode, ImageUploadCompleted mImageUploadCompleted) {
        this.imagePath = imagepath;
        statusCode = statuscode;
        this.key = key;
        this.caseId = caseId;
        this.imageName = imageName;
        this.mImageUploadCompleted = mImageUploadCompleted;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        SpinnerManager.showSpinner(mContext);
    }

    @Override
    protected String doInBackground(Void... params) {
        imageURL = AutofinMediaManager.getImageUrl(imagePath, caseId, imageName);
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        SpinnerManager.hideSpinner(mContext);
        if (imageURL != null && mImageUploadCompleted != null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mImageUploadCompleted.onImageUploadCompleted(key, imageURL, statusCode);

          /*  new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, 3000);*/

        }
    }
}
