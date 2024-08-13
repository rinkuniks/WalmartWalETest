package com.example.walmartwaletest

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.walmartwaletest.Utils.PreferenceHelper
import com.example.walmartwaletest.Utils.PreferenceHelper.description
import com.example.walmartwaletest.Utils.PreferenceHelper.lastUpdatedDate
import com.example.walmartwaletest.Utils.PreferenceHelper.title
import com.example.walmartwaletest.Utils.SHARED_PREF
import com.example.walmartwaletest.Utils.Utility
import com.example.walmartwaletest.databinding.ActivityMainBinding
import com.example.walmartwaletest.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()
    var mImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel

        if (Utility().internetCheck(this)) {
            viewModel.getAPODResponse()
        } else {
            fetchDetailsFromFile()
        }
        observeResponse()
    }

    private fun fetchDetailsFromFile() {
        Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
        val pref = PreferenceHelper.preference(this, SHARED_PREF)
        binding.tvTitle.text = pref.title
        binding.description.text = pref.description
    }

    private fun observeResponse() {
        viewModel.response.observe(this) {
            binding.tvTitle.text = it.title
            Glide.with(this).load(it.url).into(binding.apodImageView)
            binding.description.text = it.explanation
            saveImageToFile(it.url)
            saveDetails(it.title, it.url, it.explanation, it.date)
        }
    }

    private fun saveImageToFile(url: String) {
        val myExecutor = Executors.newSingleThreadExecutor()
        val myHandler = Handler(Looper.getMainLooper())
        myExecutor.execute{
            mImage = mLoad(url)
            myHandler.post{
                if(mImage!=null){
                    mSaveMediaToStorage(mImage)
                }
            }
        }
    }

    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    private fun saveDetails(title: String, url: String, explanation: String, date: String) {
        val pref = PreferenceHelper.preference(this, SHARED_PREF)
        pref.title = title
        pref.description = explanation
        pref.lastUpdatedDate = date
    }

    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val folderPath = Environment.DIRECTORY_PICTURES + "/APOD"
        val folder = File(folderPath)
        if (folder.exists()) {
            folder.deleteRecursively()
        }else{
            folder.mkdirs()
        }
        val filename = File(folderPath, "apod.jpg")
        if (!filename.absoluteFile.exists()) {
            filename.deleteRecursively()
        }

        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename.name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, folderPath)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(folderPath)
            val image = File(imagesDir, filename.name)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }

}