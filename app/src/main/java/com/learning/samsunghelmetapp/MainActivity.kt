package com.learning.samsunghelmetapp

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.learning.samsunghelmetapp.utils.Classifier
import com.learning.samsunghelmetapp.utils.Utils
import java.io.FileNotFoundException
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    val NETWORK_FILE = "Helmet.pt"
    val CAMERA_REQUEST_CODE = 1
    val FILE_REQUEST_CODE = 2
    var fileBtn: Button? = null
    var imageBitmap: Bitmap? = null
    var classifier: Classifier? = null
    var resultTextView:TextView? = null
    var copy:Bitmap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        classifier = Classifier(Utils.assetFilePath(this, NETWORK_FILE))
        val captureBtn = findViewById<ImageButton>(R.id.captureBtn)
        captureBtn.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
        }
        val fileBtn = findViewById<ImageButton>(R.id.file)
        resultTextView = findViewById<TextView>(R.id.res)
        fileBtn.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
            galleryIntent.type = "*/*"
            startActivityForResult(galleryIntent, FILE_REQUEST_CODE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            imageBitmap = data?.extras!!["data"] as Bitmap?
        }
        if (requestCode == FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri: Uri? = data?.data // String mimeType = getContentResolver().getType(uri);
            try {
                val inputStream: InputStream? = contentResolver.openInputStream(uri!!)
                imageBitmap = BitmapFactory.decodeStream(inputStream)
                copy= imageBitmap
                val width = imageBitmap!!.width
                val height = imageBitmap!!.height
                var side = 0
                side = if(width>height){
                    width
                }else height
                imageBitmap = Bitmap.createScaledBitmap(imageBitmap!!, side, side, true)
            } catch (e: FileNotFoundException) {
                Toast.makeText(this@MainActivity, "Нет файла", Toast.LENGTH_SHORT).show()
            }
        }
        if (imageBitmap != null) {
            val img = findViewById<ImageView>(R.id.picture)
            img.setImageBitmap(copy)
            val pred = classifier!!.predict(imageBitmap)
            resultTextView!!.text =  pred
        } else {
            resultTextView!!.text = "Что то пошло не так"
        }
    }

}