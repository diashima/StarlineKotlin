package kz.tengrilab.starline.ui.main

import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import kz.tengrilab.starline.ApiClient
import kz.tengrilab.starline.ApiInterface
import kz.tengrilab.starline.BuildConfig
import kz.tengrilab.starline.R
import kz.tengrilab.starline.crop.ImageCrop
import kz.tengrilab.starline.databinding.FragmentDetectionBinding
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class DetectionFragment : Fragment() {

    private var mImageFileLocation = ""
    private val CAMERA_PIC_REQUEST = 1111
    private lateinit var postPath: String
    private lateinit var fileUri: Uri
    lateinit var currentPhotoPath: String

    private var _binding: FragmentDetectionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        captureImage()
    }

    private fun captureImage() {
        val callCameraIntent = Intent()
        callCameraIntent.action = MediaStore.ACTION_IMAGE_CAPTURE

        val photoFile: File = createImageFile()

        Log.d("Test", photoFile.toString())

        val outputUri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            photoFile
        )

        callCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
        callCameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION and Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(callCameraIntent, CAMERA_PIC_REQUEST)
    }

    private fun createImageFile() : File {
        val timestamp = SimpleDateFormat("yyyy_MM_dd_HHmmSS", Locale.getDefault()).format(Date())
        val imageFileName = "IMAGE_$timestamp"

        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/" + "frfrfr")

        //val storageDirectory = getAppSpecificAlbumStorageDir(requireContext(), "${R.string.app_name}")

        if (!storageDirectory.exists()){
            storageDirectory.mkdir()
            Log.d("Test", "Creating directory")
        } else {
            Log.d("Test", "Directory exists!!!")
        }

        val image = File(storageDirectory, "$imageFileName.jpg")
        mImageFileLocation = image.absolutePath

        return image
    }

    private fun sendImage(path: String) {
        Log.d("Test", "sendImage started")
        val file = File(path)
        val bitmap = BitmapFactory.decodeFile(path)
        //binding.imageView.setImageBitmap(bitmap)
        val retrofit = ApiClient.getTengrilabClient()
        val clientInterface = retrofit.create(ApiInterface::class.java)

        val requestBody = RequestBody.create(MediaType.parse("*/*"), file)

        val body = MultipartBody.Part.createFormData("file", file.name, requestBody)
        val personId = MultipartBody.Part.createFormData("person_id", "1")
        val threshold = MultipartBody.Part.createFormData("threshold", "60")

        val call = clientInterface.uploadImage(body, personId, threshold)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val answer = response.code()
                Log.d("Test", answer.toString())
                if (response.body() != null) {
                    if (response.code() == 200) {
                        Log.d("Test", response.body().toString())
                        Log.d("Test", "response 200")
                    }
                } else {
                    Log.d("Test", response.code().toString())
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Test", "Not Sent")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_PIC_REQUEST) {
            if (Build.VERSION.SDK_INT > 25) {
                val timeStamp = SimpleDateFormat("yyyy-MM-dd HH:mm:SS", Locale.getDefault()).format(
                    Date()
                )
                postPath = mImageFileLocation
                val file = File(postPath)
                if (file.exists()) {
                    val sentPath = ImageCrop.detectFaces(postPath, requireContext())
                    Log.d("Test", "URI returned: $sentPath")
                    Log.d("Test", "captureImage")
                } else {
                    DetectionFragmentDirections.moveBack().apply {
                        findNavController().navigate(this)
                    }
                }
            } else {
                postPath = fileUri.path!!
                val file = File(postPath)
                if (file.exists()) {
                    val sentPath = ImageCrop.detectFaces(postPath, requireContext())
                    Log.d("Test", "captureImage2")
                } else {
                    DetectionFragmentDirections.moveBack().apply {
                        findNavController().navigate(this)
                    }
                }
            }
        }
    }
}