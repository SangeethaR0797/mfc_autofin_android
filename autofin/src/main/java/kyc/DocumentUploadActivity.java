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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private LinearLayout llPhotoIDProof, llPanCard, llAadharCard, llVoterIdCard, llPassport, llResidenceProof, llRentAgreement, llElectricityBill, llResAadharCard, llBankDocs, llBankStatement, llSalarySlip, llForm16, llITR;
    private TextView tvSkipPhotoIdProof, tvPanCardURL, tvAadharCardURL, tvVoterIdURL, tvPassportURL, tvSkipResidenceProof, tvRentAgreementURL, tvElectricityBillURL, tvResAadharURL, tvBankStatementURL, tvSalarySlipURL, tvForm16URL, tvITRURL;
    private CheckBox cbSkipBankDocs, cbUploadDocsAgreeTAndC;
    private CaptureImage captureImage;
    private Button btnUpdateDoc;

    private File file;
    private Uri fileUri;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_upload);
        initViews();
        Callpermissions(DocumentUploadActivity.this);

    }

    private void initViews() {
        captureImage = new CaptureImage();
        llPanCard = findViewById(R.id.llPanCard);
        llPhotoIDProof = findViewById(R.id.llPhotoIDProof);
        llAadharCard = findViewById(R.id.llAadharCard);
        llVoterIdCard = findViewById(R.id.llVoterIdCard);
        llPassport = findViewById(R.id.llPassport);
        llResidenceProof = findViewById(R.id.llResidenceProof);
        llRentAgreement = findViewById(R.id.llRentAgreement);
        llElectricityBill = findViewById(R.id.llElectricityBill);
        llResAadharCard = findViewById(R.id.llResAadharCard);
        llBankDocs = findViewById(R.id.llBankDocs);
        llBankStatement = findViewById(R.id.llBankStatement);
        llSalarySlip = findViewById(R.id.llSalarySlip);
        llForm16 = findViewById(R.id.llForm16);
        llITR = findViewById(R.id.llITR);
        tvSkipPhotoIdProof= findViewById(R.id.tvSkipPhotoIdProof);
        tvPanCardURL= findViewById(R.id.tvPanCardURL);
        tvAadharCardURL= findViewById(R.id.tvAadharCardURL);
        tvVoterIdURL= findViewById(R.id.tvVoterIdURL);
        tvPassportURL= findViewById(R.id.tvPassportURL);
        tvSkipResidenceProof= findViewById(R.id.tvSkipResidenceProof);
        tvRentAgreementURL= findViewById(R.id.tvRentAgreementURL);
        tvElectricityBillURL= findViewById(R.id.tvElectricityBillURL);
        tvResAadharURL= findViewById(R.id.tvResAadharURL);
        tvBankStatementURL= findViewById(R.id.tvBankStatementURL);
        tvSalarySlipURL= findViewById(R.id.tvSalarySlipURL);
        tvForm16URL= findViewById(R.id.tvForm16URL);
        tvITRURL= findViewById(R.id.tvITRURL);
        cbSkipBankDocs= findViewById(R.id.cbSkipBankDocs);
        cbUploadDocsAgreeTAndC= findViewById(R.id.cbUploadDocsAgreeTAndC);
        btnUpdateDoc= findViewById(R.id.btnUpdateDoc);
        llPanCard.setOnClickListener(DocumentUploadActivity.this);
        llAadharCard.setOnClickListener(this);
        llVoterIdCard.setOnClickListener(this);
        llPassport.setOnClickListener(this);
        llResidenceProof.setOnClickListener(this);
        llRentAgreement.setOnClickListener(this);
        llElectricityBill.setOnClickListener(this);
        llResAadharCard.setOnClickListener(this);
        llBankDocs.setOnClickListener(this);
        llBankStatement.setOnClickListener(this);
        llSalarySlip.setOnClickListener(this);
        llForm16.setOnClickListener(this);
        llITR.setOnClickListener(this);
        cbSkipBankDocs.setOnClickListener(this);
        cbUploadDocsAgreeTAndC.setOnClickListener(this);
        btnUpdateDoc.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llPanCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.PANCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llAadharCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.AADHARCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llVoterIdCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.VOTERIDCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llPassport) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.PASSPORT);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llRentAgreement) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.RENTAL_AGREEMENT);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llElectricityBill) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.ELECTRICITY_BILL);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llResAadharCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.RES_AADHAR_CARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llBankStatement) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.BANK_STATEMENT);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llSalarySlip) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.SALARY_SLIP);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llForm16) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.FORM_16);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if (v.getId() == R.id.llITR) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.ITR);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        }
        else if(v.getId()==R.id.cbSkipBankDocs)
        {
            if(cbSkipBankDocs.isChecked())
            {}
        }
        else if(v.getId()==R.id.cbUploadDocsAgreeTAndC)
        {
            if(cbUploadDocsAgreeTAndC.isChecked())
            {}
        }
        else if(v.getId()==R.id.btnUpdateDoc)
        {}




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

                    new ImageUploadTask(DocumentUploadActivity.this, file.getPath(), "123456", "PAN", requestCode, this).execute();


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
                        new ImageUploadTask(DocumentUploadActivity.this, captureImage.file.getPath(), "123456", "PAN", requestCode, this).execute();


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
        Log.e("Image received", "Image received " + Imageurl);

    }

    private void setFileOnTextView(String fileName,TextView textView)
    {
        textView.setText(fileName);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_attachment, 0, R.drawable.ic_refresh, 0);
    }
}


