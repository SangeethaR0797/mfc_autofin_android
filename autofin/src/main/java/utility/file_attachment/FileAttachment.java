package utility.file_attachment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.List;

public class FileAttachment {
    private static int RESULT_LOAD = 1;
    public File file;
    public Uri fileUri;

    public void chooseImage(final Activity activity, final int requestCode) {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) != null) {
            file = new File(activity.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            fileUri = Uri.fromFile(file);
            fileUri = FileProvider.getUriForFile(activity, activity.getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.putExtra("fileUri", fileUri);
            activity.startActivityForResult(intent, requestCode);
            activity.overridePendingTransition(0, 0);
            cameraRequirement(intent, fileUri, activity);
        }
    }

    public void chooseFile(final Activity activity, final int requestCode) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(gallery, requestCode);
    }


    void cameraRequirement(Intent intent, Uri fileUri, Activity activity) {
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

}
