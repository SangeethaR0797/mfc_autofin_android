package aws;

import android.os.StrictMode;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import utility.CommonStrings;


public final class AutofinMediaManager {

    //staging
    public static final String BUCKETNAME = CommonStrings.AWS_S3_BUCKETNAME;
    public static final String BUCKETKEY = CommonStrings.AWS_S3_BUCKETKEY;
    public static final String BUCKETSECRET = CommonStrings.AWS_S3_BUCKETSECRET;

    //Prod
  /*  public static final String BUCKETNAME = "autofin-staging";
    public static final String BUCKETKEY = "AKIAXRVBOK327HDQECM6";
    public static final String BUCKETSECRET = "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";*/

    private static final String TAG = AutofinMediaManager.class.getSimpleName();
    private static final SimpleDateFormat YYYY_MMM_DD_FORMAT = new SimpleDateFormat("yyyy_MMM_dd", Locale.ENGLISH);
    private static final Calendar CALENDAR = Calendar.getInstance();

    public static String getImageUrl(final String filePath,
                                     final String UserID, final String ImgName) {

        String mImageUrl = "";
        try {
            String resourceType = filePath.substring(filePath.lastIndexOf(".") + 1);
            String keydata = ImgName + "." + resourceType;
            ClientConfiguration clientConfiguration = getClientConfiguration();
            final String currentTime = YYYY_MMM_DD_FORMAT.format(CALENDAR.getTime());
            String year = currentTime.split("_")[0];
            String month = currentTime.split("_")[1];
            String day = currentTime.split("_")[2];

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            String path = "";
            //  path = "MFCBusiness" + "/" + year + "/" + month + "/" + day + "/" + UserID + "/" + "original/" + keydata;
           // path = "MFCBusiness" + "/" + UserID + "/" + year + "-" + month + "-" + day + "_" + keydata;
            path =  BUCKETNAME+ "/" + UserID + "/" + year + "-" + month + "-" + day + "_" + keydata;

            /* MFC Business is replaced with Bucket Name(which we get from API) as discussed on the call on 21-09-2021.
            And this change is done by Sangeetha*/

            AWSCredentials awsCredentials = getAWSCredentials(BUCKETKEY, BUCKETSECRET);

            AmazonS3Client s3Client = awsCredentials != null ? new AmazonS3Client(awsCredentials, clientConfiguration) : new AmazonS3Client();
            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));

            Log.i(TAG, " " + Regions.DEFAULT_REGION);

            s3Client.putObject(new PutObjectRequest(BUCKETNAME, path, new File(filePath)).withCannedAcl(CannedAccessControlList.Private));

            mImageUrl = s3Client.getResourceUrl(BUCKETNAME, path);
            Log.i(TAG, "mImageUrl " + mImageUrl);

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());

        }
        return mImageUrl;
    }

    private static AWSCredentials getAWSCredentials(final String awsKey, final String awsSecret) {
        if (awsKey != null && !awsKey.isEmpty() && awsSecret != null && !awsSecret.isEmpty()) {
            return new BasicAWSCredentials(awsKey, awsSecret);
        }
        return null;
    }

    private static ClientConfiguration getClientConfiguration() {
        final ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setConnectionTimeout(300000);
        clientConfiguration.setMaxErrorRetry(10);
        clientConfiguration.setSocketTimeout(300000);
        clientConfiguration.setMaxConnections(500);
        clientConfiguration.setProtocol(Protocol.HTTPS);
        return clientConfiguration;
    }
}
