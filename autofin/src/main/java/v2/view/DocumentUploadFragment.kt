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
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
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
import kyc.ImageUploadCompleted
import kyc.ImageUploadTask
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit_config.RetroBase.retrofitInterface
import utility.CommonStrings
import v2.model.request.KYCDocumentUploadDataRequest
import v2.model.request.KYCUploadDocs
import v2.model.request.KYCUploadDocumentData
import v2.model.response.CustomerDetailsResponse
import v2.model.response.UploadKYCResponse
import v2.model.response.master.Docs
import v2.model.response.master.KYCDocumentData
import v2.utility.RealPathUtil
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
    lateinit var customerDetailsResponse: CustomerDetailsResponse
    lateinit var ivBackToRedDetails: ImageView

    private var currentImageName = ""
    private var currentImageKey = ""
    private lateinit var currentTextView: TextView
    lateinit var fragmentContext: View
    private var commonList = ArrayList<v2.model.response.master.GroupedDoc>()
    lateinit var kycDocumentData: KYCDocumentData
    private var documentHashMap = HashMap<String, KYCUploadDocs>()
    private var listOfUploadImageURL = ArrayList<String>()
    private var caseId = ""
    private var totalListSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val safeArgs = DocumentUploadFragmentArgs.fromBundle(it)
            customerId = safeArgs.CustomerId
            kycDocumentData = safeArgs.KYCDocuments.data
            caseId = safeArgs.caseID
            customerDetailsResponse = safeArgs.customerData

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
        ivBackToRedDetails = view?.findViewById(R.id.ivBackFromDocumentUpload)!!

        linearLayoutImageUpload = view?.findViewById(R.id.linearLayoutImageUpload)!!
        buttonUploadDocument = view.findViewById(R.id.buttonUploadDocument)!!

        if(commonList.isEmpty())
        {
            if (kycDocumentData.nonGroupedDoc.isNotEmpty())
                addNonGroupedDocDataToCommonList()

            if (kycDocumentData.groupedDoc.isNotEmpty())
                addGroupedDocDataToCommonList()

        }

        ivBackToRedDetails.setOnClickListener(View.OnClickListener {
            activity?.onBackPressed()
        })

        buttonUploadDocument.setOnClickListener(View.OnClickListener {
            if (kycDocumentData.groupedDoc.isNotEmpty()) {
                if (isGroupedDocFilled()) {
                    showProgressDialog(requireActivity())
                    retrofitInterface.getFromWeb(getUploadKYCRequest(), CommonStrings.UPLOAD_KYC_DOC_URL_V2).enqueue(this)
                } else {
                    showToast("Attach anyone document for each Mandatory each document group")
                }
            } else {
                navigateToBankOfferStatus(customerId, customerDetailsResponse, "DocUpload")
            }
        })
        generateDocumentTileUI()
    }


    private fun getUploadKYCRequest(): KYCDocumentUploadDataRequest {
        val docList: ArrayList<KYCUploadDocs> = ArrayList<KYCUploadDocs>(documentHashMap.values)
        val kycUploadDocumentData = KYCUploadDocumentData(customerId.toInt(), caseId, docList)
        val kycDocumentUploadDataRequest = KYCDocumentUploadDataRequest(CommonStrings.DEALER_ID, CommonStrings.USER_TYPE, kycUploadDocumentData)
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
            totalListSize += kycDocumentData.groupedDoc[index].docs.size
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
        var tile1APIKey = ""
        var tile1ImageName = ""

        if (!isTile1IsGrouped) {
            textViewTitle.text = tileData1.docs[0].displayLabel
            textViewImageDescription.text = tileData1.docs[0].description
            tile1APIKey = tileData1.docs[0].apiKey
            tile1ImageName = textViewTitle.text.toString().trim()
        } else {
            setMandatoryTitle(textViewTitle, tileData1.groupName)
            textViewImageDescription.text = tileData1.description
        }

        if(documentHashMap.keys.contains(tile1APIKey))
            setFileAttachedText(textViewAttachmentStatus)

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
                        attachDocument(IMAGE_GALLERY_CODE)
                    }
                })
            } else {
                currentImageKey = tileData1.docs[0].apiKey
                currentImageName = textViewTitle.text.toString().trim()
                currentTextView = textViewAttachmentStatus
                attachDocument(IMAGE_GALLERY_CODE)
            }

        })

        imageViewCamera.setOnClickListener(View.OnClickListener {
            if (isTile1IsGrouped) {
                showImageSelectionDialog(tileData1.groupName, tileData1.docs, object : DocumentSelectionCallBack {
                    override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                        tile1APIKey = apiKey
                        tile1ImageName = displayLabel.trim()
                        currentImageKey = tile1APIKey
                        currentImageName = tile1ImageName
                        currentTextView = textViewAttachmentStatus
                        attachDocument(IMAGE_CAPTURE_CODE)
                    }
                })
            } else {
                currentImageKey = tileData1.docs[0].apiKey
                currentImageName = textViewTitle.text.toString().trim()
                currentTextView = textViewAttachmentStatus
                attachDocument(IMAGE_CAPTURE_CODE)
            }


        })

        if (tileData1 != tileData2) {

            val isTile2IsGrouped = tileData2.groupName.isNotEmpty()
            var tile2APIKey = ""
            var tile2ImageName = ""

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
                tile2APIKey = tileData2.docs[0].apiKey
                tile2ImageName = textViewTitle2.text.toString().trim()
            } else {
                setMandatoryTitle(textViewTitle2, tileData2.groupName)
                textViewImageDescription2.text = tileData2.description
            }

            if(documentHashMap.keys.contains(tile2APIKey))
                setFileAttachedText(textViewAttachmentStatus2)

            imageViewGallery2.setOnClickListener(View.OnClickListener {
                if (isTile2IsGrouped) {
                    showImageSelectionDialog(tileData2.groupName, tileData2.docs, object : DocumentSelectionCallBack {
                        override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                            tile2APIKey = apiKey
                            tile2ImageName = displayLabel.trim()
                            currentImageKey = tile2APIKey
                            currentImageName = tile2ImageName
                            currentTextView = textViewAttachmentStatus2
                            attachDocument(IMAGE_GALLERY_CODE)

                        }
                    })
                } else {
                    currentImageKey = tileData2.docs[0].apiKey
                    currentImageName = textViewTitle2.text.toString().trim()
                    currentTextView = textViewAttachmentStatus2
                    attachDocument(IMAGE_GALLERY_CODE)

                }

            })
            imageViewCamera2.setOnClickListener(View.OnClickListener {
                if (isTile2IsGrouped) {
                    showImageSelectionDialog(tileData2.groupName, tileData2.docs, object : DocumentSelectionCallBack {
                        override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                            tile2APIKey = apiKey
                            tile2ImageName = displayLabel.trim()
                            currentImageKey = tile2APIKey
                            currentImageName = tile2ImageName
                            currentTextView = textViewAttachmentStatus2
                            attachDocument(IMAGE_CAPTURE_CODE)

                        }
                    })
                } else {
                    currentImageKey = tile2APIKey
                    currentImageName = tile2ImageName
                    currentTextView = textViewAttachmentStatus2
                    attachDocument(IMAGE_CAPTURE_CODE)
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
                dialog.dismiss()

            } else {
                showToast("Select any one Document Type")
            }
        })

        dialog.show()
    }

    private fun attachDocument(code: Int) {

        if (checkPermissions(requireActivity())) {

            if (code == IMAGE_CAPTURE_CODE) {
                openCamera()
            } else if (code == IMAGE_GALLERY_CODE) {
                openGallery()
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
        //gallery.type = "";
        gallery.type="image/*|application/pdf/*"
        val mimetypes = arrayOf("image/*", "application/*")

        gallery.action = Intent.ACTION_GET_CONTENT
        gallery.addCategory(Intent.CATEGORY_OPENABLE)
        gallery.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)

        requireActivity().startActivityForResult(getFileChooserIntent(), IMAGE_GALLERY_CODE)
    }

    private fun getFileChooserIntent(): Intent? {
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
            if (mimeTypes.size > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        return intent
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_GALLERY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    fileUri = data?.data
                    var picturePath = RealPathUtil().getRealPath(requireContext(), fileUri!!)
                   /* val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor: Cursor? = fileUri?.let { requireActivity().contentResolver.query(it, filePathColumn, null, null, null) }
                    cursor?.moveToFirst()
                    val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
                    val picturePath = columnIndex?.let { cursor?.getString(it) }
                    cursor?.close()*/
                    file = File(picturePath)
                   // compressImage(file?.path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                ImageUploadTask(activity, file?.path, CommonStrings.DEALER_ID + "/" + customerId, currentImageName, currentImageKey, requestCode, this).execute()
            }
        } else if (requestCode == IMAGE_CAPTURE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (file?.length() != 0L) {
                    try {
                        compressImage(file?.path)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                    ImageUploadTask(activity, file?.absolutePath, CommonStrings.DEALER_ID + "/" + customerId, currentImageName, currentImageKey, requestCode, this).execute()
                }
            }
        }
    }

    override fun onImageUploadCompleted(key: String, imageurl: String?, statuscode: Int) {
        if (key.isNotEmpty() && imageurl?.isNotEmpty() == true) {
            val document = KYCUploadDocs(key, imageurl.toString())
            documentHashMap[key] = document
            Log.i("TAG", "onImageUploadCompleted: " + documentHashMap[key]?.Key)
            setFileAttachedText(currentTextView)
            currentImageName = ""
            currentImageKey = ""
        } else {
            showToast("Please attach again")
        }

    }

    private fun setFileAttachedText(textView: TextView) {
        textView.text = "File attached"
        textView.setTextColor(resources.getColor(R.color.v2_green))
        textView.compoundDrawablePadding = 10
        val img = resources.getDrawable(R.drawable.ic_green_tick)
        img.setBounds(0, 0, 30, 30)
        textView.setCompoundDrawables(img, null, null, null)
    }

    override fun onResponse(call: Call<Any>, response: Response<Any>) {
        hideProgressDialog()
        val strRes = Gson().toJson(response.body())
        val uploadKYCDocumentResponse = Gson().fromJson(strRes, UploadKYCResponse::class.java)
        if (uploadKYCDocumentResponse?.status == true) {
            listOfUploadImageURL = uploadKYCDocumentResponse.data as ArrayList<String>
            navigateToBankOfferStatus(customerId, customerDetailsResponse, "DocUpload")
        } else {
            if (uploadKYCDocumentResponse?.message != null)
                showToast(uploadKYCDocumentResponse.message)
        }
    }

    override fun onFailure(call: Call<Any>, t: Throwable) {
        hideProgressDialog()
        t.printStackTrace()
    }

    private fun setMandatoryTitle(textView: TextView, titleText: String)
    {
        val text = "$titleText "
        val colored = getString(R.string.lbl_asterick)
        val builder = SpannableStringBuilder()

        builder.append(text)
        val start = builder.length
        builder.append(colored)
        val end = builder.length

        builder.setSpan(ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        textView.text = builder
    }

    private fun isGroupedDocFilled(): Boolean {
        var docFilled=false
        var docCount=0
        var docMap=HashMap<String, String>()

        if(documentHashMap.size>=kycDocumentData.groupedDoc.size)
        {
            for(index in kycDocumentData.groupedDoc.indices)
            {
                val docList= kycDocumentData.groupedDoc[index].docs
                for (docIndex in docList.indices)
                {

                    if(documentHashMap.containsKey(docList[docIndex].apiKey))
                    {
                        docMap[kycDocumentData.groupedDoc[index].groupName] = docList[docIndex].apiKey
                    }
                }

            }
        }
        return docMap.size==kycDocumentData.groupedDoc.size
    }

    fun getRealPathFromURI(context: Context, contentUri: Uri?): String? {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(contentUri!!, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } finally {
            cursor?.close()
        }
    }

}