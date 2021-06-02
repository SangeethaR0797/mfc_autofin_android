package kyc;

import android.content.Context;
import android.os.AsyncTask;

import aws.AutofinMediaManager;
import utility.SpinnerManager;

public class ImageUploadTask extends AsyncTask<Void, Void, String> {


    private String imagePath;
    private int statuscode;
    private String caseId;
    private String imageName;

    private String imageURL;
    private String key;

    private Context mContext;

    private ImageUploadCompleted mImageUploadCompleted;

    public ImageUploadTask(Context mContext, String imagepath, String caseId, String imageName, String key, int statuscode, ImageUploadCompleted mImageUploadCompleted) {
        this.imagePath = imagepath;
        this.statuscode = statuscode;
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
            mImageUploadCompleted.onImageUploadCompleted(key, imageURL, statuscode);
        }
    }
}
