package kyc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.mfc.autofin.framework.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import utility.AutoFinConstants;

public class DocumentUploadActivity extends AppCompatActivity implements View.OnClickListener, ImageUploadCompleted {

    private LinearLayout llpancard;
    private CaptureImage captureImage;

    private File file;
    private Uri fileUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_upload_activity);
        initViews();
        Callpermissions(DocumentUploadActivity.this);

    }

    private void initViews() {
        captureImage = new CaptureImage();
        llpancard = findViewById(R.id.llpancard);
        llpancard.setOnClickListener(DocumentUploadActivity.this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llpancard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.PANCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
               int x = 0;
        if (resultCode == RESULT_OK) {
            if (OptionGalleryCamera.getInstance().getWhichImage().equals("Gallery")) {
                try {
                    fileUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(fileUri,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    file = new File(picturePath);


                    try {

                        CompressImage(file.getPath());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    new ImageUploadTask(DocumentUploadActivity.this,file.getPath(), "123456", "PAN", requestCode, this).execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (OptionGalleryCamera.getInstance().getWhichImage().equals("Camera")) {
                try {

                    if (captureImage.file.length() != 0) {

                        try {

                            CompressImage(captureImage.file.getPath());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ImageUploadTask(DocumentUploadActivity.this,captureImage.file.getPath(), "123456", "PAN", requestCode, this).execute();


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {

        }
    }

    public boolean checkPermissions(Context context) {

        return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public void Callpermissions(Activity activity) {
        Log.d("Camera Permission Check", "Comes into Premission Check method");
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    public void CompressImage(String path) {
        try {
            InputStream in = new FileInputStream(path);
            Bitmap bm2 = BitmapFactory.decodeStream(in);
            OutputStream stream = new FileOutputStream(path);
            bm2.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onImageUploadCompleted(String Imageurl, int statuscode) {

    }
}


