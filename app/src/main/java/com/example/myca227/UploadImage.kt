package com.example.myca227

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class UploadImage : AppCompatActivity() {
    lateinit var chooseImageBtn: Button
    lateinit var uploadImageBtn: Button
    lateinit var imageView: ImageView
    var fileUri: Uri?=null
    lateinit var getImage:ActivityResultLauncher<String>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)
        chooseImageBtn = findViewById(R.id.btnReq)
        uploadImageBtn = findViewById(R.id.btnUpload)
        imageView = findViewById(R.id.idIVImage)
        getImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                if(it != null){
                    fileUri = it
                }
                imageView.setImageURI(it)
            }
        )
        chooseImageBtn.setOnClickListener {
            getImage.launch("image/*")
        }
        uploadImageBtn.setOnClickListener {
            uploadImage()
        }
    }
    fun uploadImage(){
        if(fileUri != null){
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.setMessage("Uploading your image...")
            progressDialog.show()

            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(UUID.randomUUID().toString())
            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Image Upload..",Toast.LENGTH_LONG).show()
            }.addOnFailureListener{
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Fail to Upload Image.."+it.message,Toast.LENGTH_LONG).show()
            }
        }
    }
}