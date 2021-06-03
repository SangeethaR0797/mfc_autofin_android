package v2.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.fragment_o_t_p_bottom_sheet_list_dialog.*
import kotlinx.android.synthetic.main.soft_offer_action_bar.*
import kotlinx.android.synthetic.main.v2_custom_document_parent_layout.*
import kyc.ImageUploadCompleted
import kyc.ImageUploadTask
import model.kyc_model.Doc
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit_config.RetroBase.retrofitInterface
import utility.CommonStrings
import v2.model.request.KYCDocumentUploadDataRequest
import v2.model.request.KYCUploadDocs
import v2.model.request.KYCUploadDocumentData
import v2.model.response.FieldDetails
import v2.model.response.UploadKYCResponse
import v2.model.response.master.APIDropDownResponse
import v2.model.response.master.Docs
import v2.model.response.master.KYCDocumentData
import v2.view.adapter.KYCDocumentListAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.DocumentSelectionCallBack
import v2.view.callBackInterface.itemClickCallBack
import java.io.*


class DocumentUploadFragment : BaseFragment(), ImageUploadCompleted, Callback<Any> {

    var file: File? = null
    var fileUri: Uri? = null
    private val RESULT_LOAD = 1
    private val IMAGE_CAPTURE_CODE = 101
    private val IMAGE_GALLERY_CODE = 102

    private lateinit var buttonUploadDocument: Button
    lateinit var linearLayoutImageUpload: LinearLayout

    private var currentImageName = ""
    private var currentImageKey = ""
    private lateinit var currentTextView: TextView
    lateinit var fragmentContext: View
    private var commonList = ArrayList<v2.model.response.master.GroupedDoc>()
    lateinit var kycDocumentData: KYCDocumentData
    private var documentHashMap = HashMap<String, KYCUploadDocs>()
    private var listOfUploadImageURL=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = DocumentUploadFragmentArgs.fromBundle(it)
            customerId = safeArgs.CustomerId
            kycDocumentData = safeArgs.KYCDocuments.data

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_fragment_document_upload, container, false)
        fragmentContext = view
        initViews(view)
        //caseID = "0242210316000103"
        //customerId = "448"

        return view
    }

    private fun initViews(view: View?) {
        linearLayoutImageUpload = view?.findViewById(R.id.linearLayoutImageUpload)!!
        buttonUploadDocument = view.findViewById(R.id.buttonUploadDocument)!!

        if (kycDocumentData.nonGroupedDoc.isNotEmpty())
            addNonGroupedDocDataToCommonList()

        if (kycDocumentData.groupedDoc.isNotEmpty())
            addGroupedDocDataToCommonList()

        buttonUploadDocument.setOnClickListener(View.OnClickListener {
            if (commonList.size == documentHashMap.size || documentHashMap.size>0) {
                retrofitInterface.getFromWeb(getUploadKYCRequest(), "https://15.207.148.230:3003/api/kyc/upload-customer-kyc").enqueue(this)
            } else {
                showToast("Attach all the documents")
            }
        })
        generateDocumentTileUI()
    }

    private fun getUploadKYCRequest(): KYCDocumentUploadDataRequest {
        val docList: ArrayList<KYCUploadDocs> = ArrayList<KYCUploadDocs>(documentHashMap.values)
        val kycUploadDocumentData= KYCUploadDocumentData(customerId.toInt(),"0242210415000012",docList)
        val kycDocumentUploadDataRequest=KYCDocumentUploadDataRequest(CommonStrings.DEALER_ID,CommonStrings.USER_TYPE,kycUploadDocumentData)
        return kycDocumentUploadDataRequest
    }


    private fun addNonGroupedDocDataToCommonList() {
        for (index in kycDocumentData.nonGroupedDoc.indices) {
            commonList.add(v2.model.response.master.GroupedDoc("", "", listOf(kycDocumentData.nonGroupedDoc[index])))
        }
    }

    private fun addGroupedDocDataToCommonList() {

        for (index in kycDocumentData.groupedDoc.indices) {
            commonList.add(kycDocumentData.groupedDoc[index])
        }
    }

    private fun generateDocumentTileUI() {
        for (index in commonList.indices) {
            if (index > 0 && index % 2 != 0) {
                setImageCaptureTile(commonList[index - 1], commonList[index])
            } else if (index == commonList.size - 1 && index % 2 == 0) {
                setImageCaptureTile(commonList[index], commonList[index])

            }
        }
    }

    private fun setImageCaptureTile(tileData1: v2.model.response.master.GroupedDoc, tileData2: v2.model.response.master.GroupedDoc) {

        val rowView: View = LayoutInflater.from(fragmentContext.context).inflate(R.layout.v2_custom_document_parent_layout, linearLayoutImageUpload, false)
        val columnLayout1: LinearLayout = rowView.findViewById(R.id.linearLayoutImageUploadTile1)
        val columnLayout2: LinearLayout = rowView.findViewById(R.id.linearLayoutImageUploadTile2)
        val columnView1: View = LayoutInflater.from(fragmentContext.context).inflate(R.layout.v2_image_upload_tile_layout, columnLayout1, false)
        val textViewTitle: TextView = columnView1.findViewById(R.id.textViewImageTitle)
        val textViewImageDescription: TextView = columnView1.findViewById(R.id.textViewImageDescription)
        val imageViewGallery: ImageView = columnView1.findViewById(R.id.imageViewGallery)
        val imageViewCamera: ImageView = columnView1.findViewById(R.id.imageViewCamera)
        val textViewAttachmentStatus: TextView = columnView1.findViewById(R.id.textViewAttachmentStatus)

        val isTile1IsGrouped = tileData1.groupName.isNotEmpty()
        val isTile2IsGrouped = tileData2.groupName.isNotEmpty()
        var tile1APIKey = ""
        var tile2APIKey = ""
        var tile1ImageName = ""
        var tile2ImageName = ""

        if (!isTile1IsGrouped) {
            textViewTitle.text = tileData1.docs[0].displayLabel
            textViewImageDescription.text = tileData1.docs[0].description
            tile1APIKey = tileData1.docs[0].apiKey
            tile1ImageName = textViewTitle.text.toString().trim()
        } else {
            textViewTitle.text = tileData1.groupName
            textViewImageDescription.text = tileData1.description
        }

        imageViewGallery.setOnClickListener(View.OnClickListener {
            currentTextView = textViewAttachmentStatus
            if (isTile1IsGrouped) {
                showImageSelectionDialog(tileData1.groupName, tileData1.docs, object : DocumentSelectionCallBack {
                    override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                        tile1APIKey = apiKey
                        tile1ImageName = displayLabel.trim()
                        currentImageKey = tile1APIKey
                        currentImageName = tile1ImageName
                        currentTextView = textViewAttachmentStatus
                        attachDocument("GALLERY")

                    }
                })
            } else {
                currentImageKey = tile1APIKey
                currentImageName = tile1ImageName
                currentTextView = textViewAttachmentStatus
                attachDocument("GALLERY")
            }

        })
        imageViewCamera.setOnClickListener(View.OnClickListener {
            View.OnClickListener {
                if (isTile1IsGrouped) {
                    showImageSelectionDialog(tileData1.groupName, tileData1.docs, object : DocumentSelectionCallBack {
                        override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                            tile1APIKey = apiKey
                            tile1ImageName = displayLabel.trim()
                            currentImageKey = tile1APIKey
                            currentImageName = tile1ImageName
                            currentTextView = textViewAttachmentStatus
                            attachDocument("CAMERA")
                        }
                    })
                } else {
                    currentImageKey = tile1APIKey
                    currentImageName = tile1ImageName
                    currentTextView = textViewAttachmentStatus
                    attachDocument("CAMERA")
                }

            }
        })

        if (tileData1 != tileData2) {

            val columnView2: View = LayoutInflater.from(fragmentContext.context).inflate(R.layout.v2_image_upload_tile_layout, columnLayout2, false)
            val textViewTitle2: TextView = columnView2.findViewById(R.id.textViewImageTitle)
            val textViewImageDescription2: TextView = columnView2.findViewById(R.id.textViewImageDescription)
            val imageViewGallery2: ImageView = columnView2.findViewById(R.id.imageViewGallery)
            val imageViewCamera2: ImageView = columnView2.findViewById(R.id.imageViewCamera)
            val textViewAttachmentStatus2: TextView = columnView2.findViewById(R.id.textViewAttachmentStatus)
            currentTextView = textViewAttachmentStatus2
            if (!isTile2IsGrouped) {
                textViewTitle2.text = tileData2.docs[0].displayLabel
                textViewImageDescription2.text = tileData2.docs[0].description
                tile1APIKey = tileData2.docs[0].apiKey
                tile1ImageName = textViewTitle2.text.toString().trim()
            } else {
                textViewTitle2.text = tileData1.groupName
                textViewImageDescription2.text = tileData1.description
            }

            imageViewGallery2.setOnClickListener(View.OnClickListener {
                if (isTile2IsGrouped) {
                    showImageSelectionDialog(tileData2.groupName, tileData2.docs, object : DocumentSelectionCallBack {
                        override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                            tile2APIKey = apiKey
                            tile2ImageName = displayLabel.trim()
                            currentImageKey = tile2APIKey
                            currentImageName = tile2ImageName
                            currentTextView = textViewAttachmentStatus
                            attachDocument("GALLERY")

                        }
                    })
                } else {
                    currentImageKey = tile2APIKey
                    currentImageName = tile2ImageName
                    currentTextView = textViewAttachmentStatus

                    attachDocument("GALLERY")

                }

            })
            imageViewCamera2.setOnClickListener(View.OnClickListener {
                View.OnClickListener {
                    if (isTile2IsGrouped) {
                        showImageSelectionDialog(tileData2.groupName, tileData2.docs, object : DocumentSelectionCallBack {
                            override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                                tile2APIKey = apiKey
                                tile2ImageName = displayLabel.trim()
                                currentImageKey = tile2APIKey
                                currentImageName = tile2ImageName
                                currentTextView = textViewAttachmentStatus
                                attachDocument("CAMERA")
                            }
                        })
                    } else {
                        currentImageKey = tile2APIKey
                        currentImageName = tile2ImageName
                        currentTextView = textViewAttachmentStatus
                        attachDocument("CAMERA")
                    }


                }
            })

            columnLayout1.addView(columnView1)
            columnLayout2.addView(columnView2)
        } else {
            columnLayout1.addView(columnView1)
        }


        linearLayoutImageUpload.addView(rowView)


        /*columnLayout1.addView(getColumnLayout(columnLayout1, tileData1))

        columnLayout2.addView(getColumnLayout(columnLayout2, tileData2))
*/
    }

    private fun showImageSelectionDialog(groupName: String, docs: List<Docs>, documentSelectionCallBack: DocumentSelectionCallBack) {

        val dialog = Dialog(fragmentContext.context, R.style.FullScreenDialogTheme)
        dialog.setCancelable(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.v2_document_upload_dialog)

        val textViewDocumentDialogTitle: TextView = dialog.findViewById(R.id.textViewDocumentDialogTitle)
        val imageViewCloseDocumentDialog: ImageView = dialog.findViewById(R.id.imageViewCloseDocumentDialog)
        val listViewDocument: ListView = dialog.findViewById(R.id.listViewDocument)
        val buttonDocumentUpload: Button = dialog.findViewById(R.id.buttonDocumentUpload)

        textViewDocumentDialogTitle.text = groupName
        var selectedImage = Docs("", "", "")
        listViewDocument.choiceMode = ListView.CHOICE_MODE_SINGLE;
        listViewDocument.adapter = KYCDocumentListAdapter(fragmentContext.context, docs, object : itemClickCallBack {
            override fun itemClick(item: Any?, position: Int) {
                selectedImage = item as Docs
            }
        })

        imageViewCloseDocumentDialog.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })

        buttonDocumentUpload.setOnClickListener(View.OnClickListener {
            if (selectedImage.apiKey.isNotEmpty()) {
                documentSelectionCallBack.onSelectedDoc(selectedImage.apiKey, selectedImage.displayLabel.trim())
                showToast(selectedImage.apiKey)
                dialog.dismiss()

            } else {
                showToast("Select any one Document Type")
            }
        })

        dialog.show()
    }

    private fun attachDocument(captureType: String) {

        if (checkPermissions(activity)) {
            when (captureType) {
                "CAMERA" -> {
                    openCamera()
                }
                "GALLERY" -> {
                    openGallery()
                }
            }

        } else {
            callPermissions()
        }

    }


    private fun checkPermissions(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }


    private fun callPermissions() {
        Log.d("Camera Permission Check", "Comes into Permission Check method")
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    private fun cameraRequirement(intent: Intent?, fileUri: Uri?, activity: Activity) {
        val resInfoList = activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            activity.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            file = File(requireActivity().externalCacheDir,
                    System.currentTimeMillis().toString() + ".jpg")
            fileUri = Uri.fromFile(file)
            fileUri = FileProvider.getUriForFile(requireActivity(), requireActivity().applicationContext.packageName + ".provider", file!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)

            requireActivity().startActivityForResult(intent, IMAGE_CAPTURE_CODE)
            requireActivity().overridePendingTransition(0, 0)
            cameraRequirement(intent, fileUri, requireActivity())
        }

    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        requireActivity().startActivityForResult(gallery, IMAGE_GALLERY_CODE)
    }


    private fun compressImage(path: String?) {
        try {
            val `in`: InputStream = FileInputStream(path)
            val bm2 = BitmapFactory.decodeStream(`in`)
            val stream: OutputStream = FileOutputStream(path)
            bm2.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            stream.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onImageUploadCompleted(key: String, imageurl: String?, statuscode: Int) {
        if(key.isNotEmpty() && imageurl?.isNotEmpty() == true)
        {
            val document = KYCUploadDocs(key, imageurl.toString())
            documentHashMap[key] = document
            currentTextView.text = "File attached"
            showToast("$key Image Uploaded successfully")
            currentImageName = ""
            currentImageKey = ""
        }
        else
        {
            val document = KYCUploadDocs("EmptyKey", imageurl.toString())
            documentHashMap[key] = document
            currentTextView.text = "File attached"
            showToast("$key Image Uploaded successfully")
            currentImageName = ""
            currentImageKey = ""

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    fileUri = data?.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = fileUri?.let { requireActivity().contentResolver.query(it, filePathColumn, null, null, null) }
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    val picturePath = cursor.getString(columnIndex)
                    cursor.close()
                    file = File(picturePath)
                    compressImage(file!!.path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                ImageUploadTask(activity, file?.path, CommonStrings.DEALER_ID + "/" + customerId, currentImageName, currentImageKey, requestCode, this).execute()

            }
        } else if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (file!!.length() != 0L) {
                    try {
                        compressImage(file!!.path)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ImageUploadTask(activity, file?.absolutePath, CommonStrings.DEALER_ID + "/" + customerId, currentImageName, currentImageKey, requestCode, this).execute()
                }

            }
        }
    }

    override fun onResponse(call: Call<Any>, response: Response<Any>)
    {
        val strRes = Gson().toJson(response.body())
        val uploadKYCDocumentResponse = Gson().fromJson(strRes, UploadKYCResponse::class.java)
        if(uploadKYCDocumentResponse.status)
        {
            listOfUploadImageURL=uploadKYCDocumentResponse.data as ArrayList<String>
            showToast(uploadKYCDocumentResponse.message)
        }
        else
        {
            if(uploadKYCDocumentResponse.message!=null)
                showToast(uploadKYCDocumentResponse.message)
        }
    }

    override fun onFailure(call: Call<Any>, t: Throwable) {
        t.printStackTrace()
    }

}