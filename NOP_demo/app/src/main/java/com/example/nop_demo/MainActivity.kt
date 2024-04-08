package com.example.nop_demo

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File


//@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private val requesttofile = 1
    private var urlForDownload: String? = null
    private var filename: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uploadbtn = findViewById<Button>(R.id.uploadBtn)
        uploadbtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            startActivityForResult(intent, requesttofile)
            Toast.makeText(this, "Select a Specific Document", Toast.LENGTH_SHORT).show()

        }
        val downloadBtn = findViewById<Button>(R.id.downloadBtn)
        downloadBtn.setOnClickListener {
            DownloadFile()
        }


    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.escuelajs.co/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service = retrofit.create(ApiService::class.java)

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requesttofile && resultCode == Activity.RESULT_OK) {
            val fileUrl = data?.data
            //val filepath = filename?.path?.let { File(it).absolutePath }
            val filepath = File(fileUrl?.path!!).absolutePath
            //val filepath=fileUrl?.path
            //val file = File(fileUri?.path!!)
            //val file = File("document/image:13")
            val requestFile =
                RequestBody.create(MediaType.parse("*/*"), "Choose All types of files")
            val body = MultipartBody.Part.createFormData("file", filepath, requestFile)

            val call = service.uploadFile(body)
            call.enqueue(object : Callback<datamodel> {
                override fun onResponse(call: Call<datamodel>, response: Response<datamodel>) {
                    if (response.isSuccessful) {
                        val data = response.body()
                        Log.d("Upload Status", "File is successfully uploaded. Response")
                        Log.d("Original Name", data?.originalname!!)
                        Log.d("Filename", data.filename)
                        Log.d("Location", data.location)

                        Log.d(
                            "Uploaded File Path",
                            "File is successfully uploaded to the remote server ${filepath}"
                        )
                        urlForDownload = data.location
                        filename = data.filename


                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "File is unable to uploading to server ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<datamodel>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "File is uploading Failed : ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        }
    }

    private fun DownloadFile() {
        Log.d(
            "Download Status",
            "Downloading file: urlForDownload=$urlForDownload, filename=$filename"
        )

        val call = service.downloadFile()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val request =
                        DownloadManager.Request(Uri.parse("$urlForDownload"))
                            .setTitle("Downloading")
                            .setDescription("File is Downloaded succesfully")
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                "$filename"
                            )


                    val downloadManager =
                        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)

                    Log.d("Download Status", "File is Downloaded successfully ")
                } else {

                    Log.d("Download Status", "Failed to download the file ")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Download Status", "Download failed: ${t.message}")
            }
        })
    }


}