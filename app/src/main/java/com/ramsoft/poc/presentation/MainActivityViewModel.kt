package com.ramsoft.poc.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ramsoft.poc.repository.DogRepository
import com.ramsoft.poc.response.RandomDogResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivityViewModel(private val dogRepository: DogRepository) : ViewModel() {

    var dogImageResponse = MutableLiveData<RandomDogResponse>()

    init {
        getRandomDogImage()
    }

    fun getRandomDogImage() {
        val response = dogRepository.getDogImage()
        response.enqueue(object : Callback<RandomDogResponse> {
            override fun onResponse(
                call: Call<RandomDogResponse>, response: Response<RandomDogResponse>
            ) {
                dogImageResponse.postValue(response.body())
            }

            override fun onFailure(call: Call<RandomDogResponse>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun saveImage(image: Bitmap, context: Context): String? {
        var savedImagePath: String? = null
        val imageFileName = "RANDOM_" + "DOG" + ".jpg"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .toString() + "/DOG-API"
        )
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            galleryAddPic(savedImagePath, context)
        }
        return savedImagePath
    }

    private fun galleryAddPic(imagePath: String, context: Context) {
        imagePath.let { path ->
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(path)
            val contentUri: Uri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            context.sendBroadcast(mediaScanIntent)
        }
    }

}