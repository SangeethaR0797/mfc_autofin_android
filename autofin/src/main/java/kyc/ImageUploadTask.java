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

    private Context mContext;

    private ImageUploadCompleted mImageUploadCompleted;

    public ImageUploadTask(Context mContext, String imagepath, String caseId, String imageName, int statuscode, ImageUploadCompleted mImageUploadCompleted) {
        this.imagePath = imagepath;
        this.statuscode = statuscode;
        this.caseId = caseId;
        this.imageName = imageName;
        this.mImageUploadCompleted = mImageUploadCompleted;
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        SpinnerManager.createProgressDialog(mContext);

    }

    @Override
    protected String doInBackground(Void... voids) {
        imageURL = AutofinMediaManager.getImageUrl(imagePath, caseId, imageName);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SpinnerManager.hideSpinner(mContext);
        if (imageURL !=  null && mImageUploadCompleted != null){
            mImageUploadCompleted.onImageUploadCompleted(imageURL,statuscode);
        }
    }
}
