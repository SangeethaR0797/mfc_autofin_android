package kyc;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.mfc.autofin.framework.Activity.AutoFinDashBoardActivity;
import com.mfc.autofin.framework.Activity.PersonalDetails.InterestedBankOfferActivity;
import com.mfc.autofin.framework.Activity.bank_offer_activities.InterestedBankOfferDetailsActivity;
import com.mfc.autofin.framework.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import model.CustomerDetailsRes;
import model.kyc_model.Doc;
import model.kyc_model.DocumentData;
import model.kyc_model.KYCDocUploadResponse;
import model.kyc_model.UploadDocRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utility.AutoFinConstants;
import utility.CommonMethods;
import utility.CommonStrings;
import utility.Global;
import utility.SpinnerManager;

import static retrofit_config.RetroBase.retrofitInterface;

public class DocumentUploadActivity extends AppCompatActivity implements View.OnClickListener, ImageUploadCompleted, Callback<Object> {

    private static final String TAG = DocumentUploadActivity.class.getSimpleName();
    private LinearLayout llPhotoIDProof, llPanCard, llAadharCard, llVoterIdCard, llPassport, llResidenceProof, llRentAgreement, llElectricityBill, llResAadharCard, llBankDocs, llBankStatement, llSalarySlip, llForm16, llITR, llAdditionalDoc;
    private TextView tvSkipPhotoIdProof, tvPanCardURL, tvAadharCardURL, tvVoterIdURL, tvPassportURL, tvSkipResidenceProof, tvRentAgreementURL, tvElectricityBillURL, tvResAadharURL, tvBankStatementURL, tvSalarySlipURL, tvForm16URL, tvITRURL, tvAdditionalDocURL,tvAdditionalLbl,tvBankStatementLbl;
    private CheckBox cbSkipBankDocs, cbUploadDocsAgreeTAndC;
    private CaptureImage captureImage;
    private Button btnUpdateDoc;

    private String panCardURL = "", aadhaarCardURL = "", voterIdURL = "", passportURL = "",
            rentalAgreementURL = "", ebBillURL = "", resAadhaarURL = "",
            bankStmtURL = "", salSlip = "", form16URL = "", itrURL = "",additionalDocURL="";

    private File file;
    private Uri fileUri;
    private List<Doc> documentList = new ArrayList<>();
    private List<Doc> photoIDProofList = new ArrayList<>();
    private List<Doc> residenceProofList = new ArrayList<>();
    private List<Doc> bankStmtList = new ArrayList<>();

    private String mDealerID, mCaseID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_upload);
        SpinnerManager.hideSpinner(this);
        initViews();

        Callpermissions(DocumentUploadActivity.this);
        mDealerID = CommonMethods.getStringValueFromKey(DocumentUploadActivity.this, CommonStrings.DEALER_ID_VAL);
        mCaseID = CommonMethods.getStringValueFromKey(DocumentUploadActivity.this, CommonStrings.CASE_ID);

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
        llAdditionalDoc = findViewById(R.id.llAdditionalDoc);
        tvSkipPhotoIdProof = findViewById(R.id.tvSkipPhotoIdProof);
        tvPanCardURL = findViewById(R.id.tvPanCardURL);
        tvAadharCardURL = findViewById(R.id.tvAadharCardURL);
        tvVoterIdURL = findViewById(R.id.tvVoterIdURL);
        tvPassportURL = findViewById(R.id.tvPassportURL);
        tvSkipResidenceProof = findViewById(R.id.tvSkipResidenceProof);
        tvRentAgreementURL = findViewById(R.id.tvRentAgreementURL);
        tvElectricityBillURL = findViewById(R.id.tvElectricityBillURL);
        tvResAadharURL = findViewById(R.id.tvResAadharURL);
        tvAdditionalLbl=findViewById(R.id.tvAdditionalLbl);
        tvBankStatementLbl=findViewById(R.id.tvBankStatementLbl);
        tvBankStatementURL = findViewById(R.id.tvBankStatementURL);
        tvSalarySlipURL = findViewById(R.id.tvSalarySlipURL);
        tvForm16URL = findViewById(R.id.tvForm16URL);
        tvITRURL = findViewById(R.id.tvITRURL);
        tvAdditionalDocURL = findViewById(R.id.tvAdditionalDocURL);
        cbSkipBankDocs = findViewById(R.id.cbSkipBankDocs);
        cbUploadDocsAgreeTAndC = findViewById(R.id.cbUploadDocsAgreeTAndC);
        btnUpdateDoc = findViewById(R.id.btnUpdateDoc);
        setMandatoryLbl(tvAdditionalLbl,tvAdditionalLbl.getText().toString());
        setMandatoryLbl(tvBankStatementLbl,tvBankStatementLbl.getText().toString());
        llPanCard.setOnClickListener(DocumentUploadActivity.this);
        tvSkipPhotoIdProof.setOnClickListener(this);
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
        llAdditionalDoc.setOnClickListener(this);
        cbSkipBankDocs.setOnClickListener(this);
        cbUploadDocsAgreeTAndC.setOnClickListener(this);
        btnUpdateDoc.setOnClickListener(this);
        if (CommonStrings.IS_OLD_LEAD) {
            if (CommonStrings.documentList != null && !CommonStrings.documentList.isEmpty()) {
                prefillDocURLs();
                Log.i(TAG, "initViews: "+CommonStrings.documentList.size());
            }
            else
            {
                Log.i(TAG, "initViews: "+CommonStrings.documentList.size());
            }
        }

    }

    private void prefillDocURLs() {
        documentList.addAll(CommonStrings.documentList);
        for (int i = 0; i < documentList.size(); i++) {
            if (documentList.get(i).getKey().equalsIgnoreCase("PanCard")) {
                setFileOnTextView(documentList.get(i).getUrl(), tvPanCardURL);
                photoIDProofList.add(CommonStrings.documentList.get(i));
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("AdhaarCard")) {
                photoIDProofList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvAadharCardURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("VoterId")) {
                photoIDProofList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvVoterIdURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("Passport")) {
                photoIDProofList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvPassportURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            }
            else if (documentList.get(i).getKey().equalsIgnoreCase("RentAgreement")) {
                residenceProofList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvRentAgreementURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("ElectricityBill")) {
                residenceProofList.add(CommonStrings.documentList.get(i));
                residenceProofList.add(CommonStrings.documentList.get(i));
                photoIDProofList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvElectricityBillURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("AdhaarCard")) {
                residenceProofList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvResAadharURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("BankStatement")) {
                bankStmtList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvBankStatementURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("SalarySlip")) {
                bankStmtList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvSalarySlipURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("Form16")) {
                bankStmtList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvForm16URL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("ITRReturn")) {
                bankStmtList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvITRURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            } else if (documentList.get(i).getKey().equalsIgnoreCase("ApplicationForm")) {
                documentList.add(CommonStrings.documentList.get(i));
                setFileOnTextView(documentList.get(i).getUrl(), tvITRURL);
                Log.i(TAG, "prefillDocURLs: "+documentList.get(i).getKey());
            }

        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llPanCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.PANCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llAadharCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.AADHARCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llVoterIdCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.VOTERIDCARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llPassport) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.PASSPORT);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llRentAgreement) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.RENTAL_AGREEMENT);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llElectricityBill) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.ELECTRICITY_BILL);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llResAadharCard) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.RES_AADHAR_CARD);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llBankStatement) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.BANK_STATEMENT);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llSalarySlip) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.SALARY_SLIP);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llForm16) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.FORM_16);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llITR) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.ITR);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.llAdditionalDoc) {
            if (checkPermissions(DocumentUploadActivity.this)) {
                captureImage.chooseImage(DocumentUploadActivity.this, AutoFinConstants.ADDITIONAL_DOCS);
            } else {
                Callpermissions(DocumentUploadActivity.this);
            }
        } else if (v.getId() == R.id.cbSkipBankDocs) {
            if (cbSkipBankDocs.isChecked()) {
                CommonMethods.redirectToDashboard(this);
            }
        } else if (v.getId() == R.id.cbUploadDocsAgreeTAndC) {
            if (cbUploadDocsAgreeTAndC.isChecked()) {

            }
        } else if (v.getId() == R.id.btnUpdateDoc) {
            if (cbUploadDocsAgreeTAndC.isChecked()) {
                if (!photoIDProofList.isEmpty() && !residenceProofList.isEmpty() && isBankDetailsFilled() && !documentList.isEmpty()) {
                    Log.i(TAG, "onClick: " + photoIDProofList.size() + " " + residenceProofList.size() + " " + bankStmtList.size() + " ");
                    SpinnerManager.showSpinner(this);
                    retrofitInterface.getFromWeb(getUploadKYCRequest(), Global.document_upload_baseURL + CommonStrings.UPLOAD_KYC_DOC_URL).enqueue(this);
                } else {
                    Toast.makeText(this, "Please attach mandatory images and at least one document for each section", Toast.LENGTH_LONG).show();
                }

            } else {
                CommonMethods.showToast(this, "Please select image and check T&C");
            }
        }
    }

    private UploadDocRequest getUploadKYCRequest() {
        documentList.addAll(photoIDProofList);
        documentList.addAll(residenceProofList);
        documentList.addAll(bankStmtList);
        UploadDocRequest uploadDocRequest = new UploadDocRequest();
        uploadDocRequest.setUserId(CommonMethods.getStringValueFromKey(this, CommonStrings.DEALER_ID_VAL));
        uploadDocRequest.setUserType(CommonMethods.getStringValueFromKey(this, CommonStrings.USER_TYPE_VAL));
        DocumentData documentData = new DocumentData();
        documentData.setCaseId(CommonMethods.getStringValueFromKey(DocumentUploadActivity.this, CommonStrings.CASE_ID));
        documentData.setCustomerId(Integer.parseInt(CommonMethods.getStringValueFromKey(this, CommonStrings.CUSTOMER_ID)));
        documentData.setDocs(documentList);
        uploadDocRequest.setData(documentData);
        return uploadDocRequest;
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

                    new ImageUploadTask(DocumentUploadActivity.this, file.getPath(), mDealerID + "/" + mCaseID, getImageName(requestCode), "",requestCode, this).execute();


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
                        new ImageUploadTask(DocumentUploadActivity.this, captureImage.file.getPath(), mDealerID + "/" + mCaseID, getImageName(requestCode), "",requestCode, this).execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
    public void onImageUploadCompleted(String imageURL,String key, int statusCode) {
        Log.e("Image received", "Image received " + imageURL + " Status Code " + statusCode);
        if (statusCode == AutoFinConstants.PANCARD) {
            if (imageURL != null) {
                panCardURL = imageURL;
                setFileOnTextView(imageURL, tvPanCardURL);
                Doc doc = new Doc();
                doc.setKey("PanCard");
                doc.setUrl(imageURL);
                photoIDProofList.add(doc);
                Log.i(TAG, "onImageUploadCompleted: " + photoIDProofList.size());
            } else {
                CommonMethods.showToast(this, "Image URL is null");
            }
        } else if (statusCode == AutoFinConstants.AADHARCARD) {
            aadhaarCardURL = imageURL;
            setFileOnTextView(imageURL, tvAadharCardURL);
            Doc doc = new Doc();
            doc.setKey("AdhaarCard");
            doc.setUrl(imageURL);
            photoIDProofList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + photoIDProofList.size());

        } else if (statusCode == AutoFinConstants.VOTERIDCARD) {
            voterIdURL = imageURL;
            setFileOnTextView(imageURL, tvVoterIdURL);
            Doc doc = new Doc();
            doc.setKey("VoterId");
            doc.setUrl(imageURL);
            photoIDProofList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + photoIDProofList.size());

        } else if (statusCode == AutoFinConstants.PASSPORT) {
            passportURL = imageURL;
            setFileOnTextView(imageURL, tvPassportURL);
            Doc doc = new Doc();
            doc.setKey("Passport");
            doc.setUrl(imageURL);
            photoIDProofList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + photoIDProofList.size());

        } else if (statusCode == AutoFinConstants.RENTAL_AGREEMENT) {
            rentalAgreementURL = imageURL;
            setFileOnTextView(imageURL, tvRentAgreementURL);
            Doc doc = new Doc();
            doc.setKey("RentAgreement");
            doc.setUrl(imageURL);
            residenceProofList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + residenceProofList.size());

        } else if (statusCode == AutoFinConstants.ELECTRICITY_BILL) {
            ebBillURL = imageURL;
            setFileOnTextView(imageURL, tvElectricityBillURL);
            Doc doc = new Doc();
            doc.setKey("ElectricityBill");
            doc.setUrl(imageURL);
            residenceProofList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + residenceProofList.size());

        } else if (statusCode == AutoFinConstants.RES_AADHAR_CARD) {
            resAadhaarURL = imageURL;
            setFileOnTextView(imageURL, tvResAadharURL);
            Doc doc = new Doc();
            doc.setKey("AdhaarCard");
            doc.setUrl(imageURL);
            residenceProofList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + residenceProofList.size());

        } else if (statusCode == AutoFinConstants.BANK_STATEMENT) {
            bankStmtURL = imageURL;
            setFileOnTextView(imageURL, tvBankStatementURL);
            Doc doc = new Doc();
            doc.setKey("BankStatement");
            doc.setUrl(imageURL);
            bankStmtList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + bankStmtList.size());

        } else if (statusCode == AutoFinConstants.SALARY_SLIP) {
            salSlip = imageURL;
            setFileOnTextView(imageURL, tvSalarySlipURL);
            Doc doc = new Doc();
            doc.setKey("SalarySlip");
            doc.setUrl(imageURL);
            bankStmtList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + bankStmtList.size());
        } else if (statusCode == AutoFinConstants.FORM_16) {
            form16URL = imageURL;
            setFileOnTextView(imageURL, tvForm16URL);
            Doc doc = new Doc();
            doc.setKey("Form16");
            doc.setUrl(imageURL);
            bankStmtList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + bankStmtList.size());
        } else if (statusCode == AutoFinConstants.ITR) {
            itrURL = imageURL;
            setFileOnTextView(imageURL, tvITRURL);
            Doc doc = new Doc();
            doc.setKey("ITRReturn");
            doc.setUrl(imageURL);
            bankStmtList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + bankStmtList.size());
        } else if (statusCode == AutoFinConstants.ADDITIONAL_DOCS) {
            additionalDocURL = imageURL;
            setFileOnTextView(imageURL, tvAdditionalDocURL);
            Doc doc = new Doc();
            doc.setKey("ApplicationForm");
            doc.setUrl(additionalDocURL);
            documentList.add(doc);
            Log.i(TAG, "onImageUploadCompleted: " + documentList.size());
        }

    }

    private String getImageName(int requestCode) {
        switch (requestCode) {
            case AutoFinConstants.PANCARD:
                return "PanCard";
            case AutoFinConstants.AADHARCARD:
                return "AadharCard";
            case AutoFinConstants.VOTERIDCARD:
                return "VoterIdCard";
            case AutoFinConstants.PASSPORT:
                return "Passport";
            case AutoFinConstants.RENTAL_AGREEMENT:
                return "RentalAgreement";
            case AutoFinConstants.ELECTRICITY_BILL:
                return "ElectricityBill";
            case AutoFinConstants.RES_AADHAR_CARD:
                return "ResAadharCard";
            case AutoFinConstants.BANK_STATEMENT:
                return "BankStatement";
            case AutoFinConstants.SALARY_SLIP:
                return "SalarySlip";
            case AutoFinConstants.FORM_16:
                return "FORM_16";
            case AutoFinConstants.ITR:
                return "ITR";
            case AutoFinConstants.ADDITIONAL_DOCS:
                return "ApplicationForm";

        }
        return "";
    }

    private void setFileOnTextView(String fileName, TextView textView) {
        Log.i(TAG, "setFileOnTextView: ");
        textView.setText(fileName);
        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file_attachment, 0, R.drawable.ic_refresh, 0);
    }

    @Override
    public void onResponse(Call<Object> call, Response<Object> response) {
        SpinnerManager.hideSpinner(this);
        String url = response.raw().request().url().toString();
        Log.i(TAG, "onResponse: URL " + url);

        String strRes = new Gson().toJson(response.body());
        Log.i(TAG, "onResponse: " + strRes);
        KYCDocUploadResponse kycDocUploadResponse = new Gson().fromJson(strRes, KYCDocUploadResponse.class);
        try {
            if (kycDocUploadResponse != null) {
                CommonMethods.showToast(this, kycDocUploadResponse.getMessage());
                startActivity(new Intent(this, InterestedBankOfferActivity.class));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }

    @Override
    public void onFailure(Call<Object> call, Throwable t) {

    }

    private boolean isBankDetailsFilled() {
        if (!tvBankStatementURL.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add_doc_lbl)) &&
                !tvBankStatementURL.getText().toString().equals("") && !bankStmtList.isEmpty() && (!tvAdditionalDocURL.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add_doc_lbl))) && (!tvAdditionalDocURL.getText().toString().isEmpty()))
        {
            return true;
        } else {
            return false;
        }


    }

    private void setMandatoryLbl(TextView tvTextLbl, String fieldName) {
        TextView textView = tvTextLbl;
        String fName = fieldName;
        String colored = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(fName);
        int start = builder.length();
        builder.append(colored);
        int end = builder.length();
        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }
}
/*
(!tvSalarySlipURL.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add_doc_lbl)) &&
        !tvSalarySlipURL.getText().toString().equals("")) && (!tvForm16URL.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add_doc_lbl)) &&
        !tvForm16URL.getText().toString().equals("")) && (!tvITRURL.getText().toString().equalsIgnoreCase(getResources().getString(R.string.add_doc_lbl)) &&
        !tvITRURL.getText().toString().equals(""))*/
