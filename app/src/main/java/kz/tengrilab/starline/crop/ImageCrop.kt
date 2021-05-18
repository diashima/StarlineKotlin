package kz.tengrilab.starline.crop

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import kz.tengrilab.starline.ApiClient
import kz.tengrilab.starline.ApiInterface
import kz.tengrilab.starline.R
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

object ImageCrop {
    fun detectFaces(stringPath: String, context: Context) {
        var uri = ""
        val path = Uri.fromFile(File(stringPath))
        val bitmap = getCapturedImage(path, context)
        val image = InputImage.fromFilePath(context, path)


        val options = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.15f)
            .build()

        val detector = FaceDetection.getClient(options)
        var count = 0

        detector.process(image)
            .addOnSuccessListener { faces ->
                Log.d("Test", "size: $faces.size")
                for (face in faces) {
                    count += 1
                    val bounds = face.boundingBox
                    val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                    val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees
                    val faceBitmap = Bitmap
                        .createBitmap(
                            bitmap,
                            face.boundingBox.left,
                            face.boundingBox.top,
                            face.boundingBox.width(),
                            face.boundingBox.height()
                        ).rotate(rotZ)

                    val newBitmap = Bitmap.createScaledBitmap(
                        faceBitmap,
                        112,
                        112,
                        true
                    )
                    uri = saveImage(newBitmap)
                    sendImage(uri)
                    Log.d("Test", "crop created")
                    Log.d("Test", count.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.d("Test", "crop is NOT !!!!!!! created")
            }
    }

    fun getCapturedImage(selectedPhotoUri: Uri, context: Context): Bitmap {
        val source = ImageDecoder.createSource(context.contentResolver, selectedPhotoUri)
        return ImageDecoder.decodeBitmap(source)
    }

    fun Bitmap.rotate(degrees: Float) : Bitmap {
        val matrix = Matrix().apply { postRotate(degrees)}
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    fun saveImage(bitmap: Bitmap) : String {
        val storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/" + "frfrfr")

        val file = File(storageDirectory, "${UUID.randomUUID()}.jpg")
        Log.d("Test", file.toString())
        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the stream
            stream.flush()

            // Close stream
            stream.close()
        } catch (e: IOException){ // Catch the exception
            e.printStackTrace()
        }
        return file.absolutePath
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
        val personId = MultipartBody.Part.createFormData("person_id", "11001")
        val threshold = MultipartBody.Part.createFormData("threshold", "60")

        val call = clientInterface.uploadImage(body, personId, threshold)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val answer = response.code()
                val body = response.body()
                Log.d("Test", "$answer: $body")
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
}