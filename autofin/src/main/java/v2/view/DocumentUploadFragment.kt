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
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.fragment_o_t_p_bottom_sheet_list_dialog.*
import kotlinx.android.synthetic.main.soft_offer_action_bar.*
import kotlinx.android.synthetic.main.v2_custom_document_parent_layout.*
import kyc.ImageUploadCompleted
import kyc.ImageUploadTask
import utility.CommonStrings
import v2.model.request.KYCUploadDocs
import v2.model.response.master.Docs
import v2.model.response.master.KYCDocumentData
import v2.model.response.master.KYCDocumentResponse
import v2.view.adapter.KYCDocumentListAdapter
import v2.view.base.BaseFragment
import v2.view.callBackInterface.DocumentSelectionCallBack
import java.io.*
import v2.model.response.master.GroupedDoc as kycDocumentData


class DocumentUploadFragment : BaseFragment(), ImageUploadCompleted {


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

        if(kycDocumentData.groupedDoc.isNotEmpty())
            addGroupedDocDataToCommonList()

        generateDocumentTileUI()
    }


    private fun addNonGroupedDocDataToCommonList() {
            for (index in kycDocumentData.nonGroupedDoc.indices) {
                commonList.add(v2.model.response.master.GroupedDoc("", "", listOf(kycDocumentData.nonGroupedDoc[index])))
            }
    }
    private fun addGroupedDocDataToCommonList() {

        for (index in kycDocumentData.groupedDoc.indices)
        {
            commonList.add(kycDocumentData.groupedDoc[index])
        }
    }

    private fun generateDocumentTileUI() {
        for (index in commonList.indices) {
            if (index > 0 && index % 2 != 0) {
                setImageCaptureTile(commonList[index - 1], commonList[index])
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

        val isTile1IsGrouped=tileData1.groupName.isNotEmpty()
        val isTile2IsGrouped=tileData2.groupName.isNotEmpty()
        var tile1APIKey=""
        var tile2APIKey=""
        var tile1ImageName=""
        var tile2ImageName=""

        if(!isTile1IsGrouped)
        {
            textViewTitle.text = tileData1.docs[0].displayLabel
            textViewImageDescription.text = tileData1.docs[0].description
            tile1APIKey=tileData1.docs[0].apiKey
            tile1ImageName=textViewTitle.text.toString().trim()
        }
        else
        {
            textViewTitle.text = tileData1.groupName
            textViewImageDescription.text = tileData1.description
        }

        imageViewGallery.setOnClickListener(View.OnClickListener {
            currentTextView = textViewAttachmentStatus
            if(isTile1IsGrouped)
            {
                showImageSelectionDialog(tileData1.groupName,tileData1.docs,object: DocumentSelectionCallBack {
                    override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                        tile1APIKey=apiKey
                        tile1ImageName=displayLabel.trim()
                    }
                })
            }

            currentImageKey=tile1APIKey
            currentImageName=tile1ImageName
            currentTextView=textViewAttachmentStatus
            attachDocument("GALLERY")
        })
        imageViewCamera.setOnClickListener(View.OnClickListener {
            View.OnClickListener {
                if(isTile1IsGrouped)
                {
                    showImageSelectionDialog(tileData1.groupName,tileData1.docs,object: DocumentSelectionCallBack {
                        override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                            tile1APIKey=apiKey
                            tile1ImageName=displayLabel.trim()
                        }
                    })
                }

                currentImageKey=tile1APIKey
                currentImageName=tile1ImageName
                currentTextView=textViewAttachmentStatus
                attachDocument("CAMERA")
            }
        })

        val columnView2: View = LayoutInflater.from(fragmentContext.context).inflate(R.layout.v2_image_upload_tile_layout, columnLayout2, false)
        val textViewTitle2: TextView = columnView2.findViewById(R.id.textViewImageTitle)
        val textViewImageDescription2: TextView = columnView2.findViewById(R.id.textViewImageDescription)
        val imageViewGallery2: ImageView = columnView2.findViewById(R.id.imageViewGallery)
        val imageViewCamera2: ImageView = columnView2.findViewById(R.id.imageViewCamera)
        val textViewAttachmentStatus2: TextView = columnView2.findViewById(R.id.textViewAttachmentStatus)
        currentTextView = textViewAttachmentStatus2
        if(!isTile2IsGrouped)
        {
            textViewTitle2.text  = tileData2.docs[0].displayLabel
            textViewImageDescription2.text = tileData2.docs[0].description
            tile1APIKey=tileData2.docs[0].apiKey
            tile1ImageName=textViewTitle2.text.toString().trim()
        }
        else
        {
            textViewTitle2.text = tileData1.groupName
            textViewImageDescription2.text = tileData1.description
        }

        imageViewGallery2.setOnClickListener(View.OnClickListener {

            attachDocument("GALLERY")
        })
        imageViewCamera2.setOnClickListener(View.OnClickListener {
            View.OnClickListener {
                if(isTile2IsGrouped)
                {
                    showImageSelectionDialog(tileData1.groupName,tileData1.docs,object: DocumentSelectionCallBack {
                        override fun onSelectedDoc(apiKey: String, displayLabel: String) {
                            tile2APIKey=apiKey
                            tile2ImageName=displayLabel.trim()
                        }
                    })
                }

                currentImageKey=tile2APIKey
                currentImageName=tile2ImageName
                currentTextView=textViewAttachmentStatus
                attachDocument("CAMERA")
            }
        })

        columnLayout1.addView(columnView1)
        columnLayout2.addView(columnView2)

        linearLayoutImageUpload.addView(rowView)


        /*columnLayout1.addView(getColumnLayout(columnLayout1, tileData1))

        columnLayout2.addView(getColumnLayout(columnLayout2, tileData2))
*/
    }

    private fun showImageSelectionDialog(groupName:String,docs: List<Docs>, documentSelectionCallBack: DocumentSelectionCallBack) {

        val dialog=Dialog(fragmentContext.context)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.v2_document_upload_dialog)

        val textViewDocumentDialogTitle:TextView=dialog.findViewById(R.id.textViewDocumentDialogTitle)
        val imageViewCloseDocumentDialog:ImageView=dialog.findViewById(R.id.imageViewCloseDocumentDialog)
        val listViewDocument:ListView=dialog.findViewById(R.id.listViewDocument)

        textViewDocumentDialogTitle.text=groupName

        val documentListAdapter= KYCDocumentListAdapter(fragmentContext.context,docs)

    }

    private fun getColumnLayout(linearLayout: LinearLayout, data: Docs): View? {
        val columnView: View = LayoutInflater.from(fragmentContext.context).inflate(R.layout.v2_image_upload_tile_layout, linearLayout, false)
        val textViewTitle: TextView = columnView.findViewById(R.id.textViewImageTitle)
        val textViewImageDescription: TextView = columnView.findViewById(R.id.textViewImageDescription)
        val imageViewGallery: ImageView = columnView.findViewById(R.id.imageViewGallery)
        val imageViewCamera: ImageView = columnView.findViewById(R.id.imageViewCamera)
        val textViewAttachmentStatus: TextView = columnView.findViewById(R.id.textViewAttachmentStatus)
        currentTextView = textViewAttachmentStatus
        textViewTitle.text = data.displayLabel
        textViewImageDescription.text = data.description
        currentImageKey = data.apiKey
        currentImageName = textViewTitle.text.toString()

        imageViewGallery.setOnClickListener(View.OnClickListener {
            attachDocument("GALLERY")
        })

        imageViewCamera.setOnClickListener(View.OnClickListener {
            View.OnClickListener {
                attachDocument("CAMERA")
            }
        })

        linearLayout.addView(columnView)

        return columnView

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
        val document = KYCUploadDocs(key, imageurl.toString())
        documentHashMap[key] = document
        currentTextView.text = "File attached"
        currentImageName = ""
        currentImageKey = ""
        showToast("$key Image Uploaded successfully")
    }


}