package v2.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.mfc.autofin.framework.R
import kotlinx.android.synthetic.main.v2_fragment_document_upload.view.*
import kyc.ImageUploadTask
import java.io.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DocumentUploadFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DocumentUploadFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var file: File? = null
    var fileUri: Uri? = null
    private val RESULT_LOAD = 1
    private val IMAGE_CAPTURE_CODE = 101
    private val IMAGE_GALLERY_CODE = 102

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.v2_fragment_document_upload, container, false)



        view.salary_gallery.setOnClickListener {

            if (checkPermissions(activity)) {
                  openGallery()
            } else {
                Callpermissions()
            }
        }

        view.salary_camera.setOnClickListener {
            if (checkPermissions(activity)) {
                openCamera()
            } else {
                Callpermissions()
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DocumentUploadFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DocumentUploadFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_GALLERY_CODE ){
            if(resultCode == Activity.RESULT_OK){
                try {
                    fileUri = data!!.data
                    Log.e("dkasjgdkajsgd","kasjgdkjasgdkjasgdkjg");
                    file = File(fileUri!!.path)
                    try {
                        CompressImage(file!!.path)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }else{

            }
        } else if(requestCode == IMAGE_CAPTURE_CODE ){
            if(resultCode == Activity.RESULT_OK){
                if (file!!.length() != 0L) {
                    try {
                        CompressImage(file!!.getPath())
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
            }

            }
        }
    }

    fun checkPermissions(context: Context?): Boolean {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }


    fun Callpermissions() {
        Log.d("Camera Permission Check", "Comes into Premission Check method")
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    }

    fun cameraRequirement(intent: Intent?, fileUri: Uri?, activity: Activity) {
        val resInfoList = activity.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            activity.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun openCamera(){
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

    fun openGallery(){
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        requireActivity().startActivityForResult(gallery, IMAGE_GALLERY_CODE)
    }


    fun CompressImage(path: String?) {
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

}