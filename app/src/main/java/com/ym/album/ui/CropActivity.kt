package com.ym.album.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.FileProvider
import com.ym.album.R
import com.ym.album.utils.CommonUtil
import com.ym.album.utils.PhotoUtils
import java.io.File

import com.ym.album.utils.CommonUtil.hasSdcard
import java.lang.Exception
import java.util.*

class CropActivity : BaseActivity(), View.OnClickListener {
    companion object{
        private const val TAG = "MainActivity"
        private const val CODE_GALLERY_REQUEST = 0xa0
        private const val CODE_CAMERA_REQUEST = 0xa1
        private const val CODE_RESULT_REQUEST = 0xa2

        private const val FILE_PROVIDER = "com.ym.album.fileprovider"
    }

    private var fileUri: File =
        File(Environment.getExternalStorageDirectory().path + "/photo.jpg")
    private var fileCropUri: File =
        File(Environment.getExternalStorageDirectory().path + "/crop_photo.jpg")
    private var photoImage:String?=null
    private var cropImage:String?=null
    private var takePath:String? = null
    private var imageUri: Uri? = null
    private var cropImageUri: Uri? = null

    private var photo: ImageView? = null
    private var btnTakePhoto:Button?=null
    private var btnTakeGallery:Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()

        btnTakePhoto = findViewById(R.id.btn_takePhoto)
        btnTakeGallery = findViewById(R.id.btn_takeGallery)
        photo = findViewById(R.id.photo)

        btnTakePhoto?.setOnClickListener(this)
        btnTakeGallery?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_takePhoto ->{
                requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                    object :RequestPermissionCallBack{
                        override fun granted() {
                            if (hasSdcard()){
//                                photoImage = TimeUtil.dateToStr(Date(),"yyyy_MMdd_hhmmss")+".jpg"
//                                imageUri = getImageUri(photoImage!!)
                                Log.d(TAG,"fileUri $fileUri")

                                imageUri = Uri.fromFile(fileUri)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                                    try {
                                        imageUri = FileProvider.getUriForFile(this@CropActivity,
                                            FILE_PROVIDER,fileUri)
                                    }catch (e:Exception){
                                        Log.e(TAG,"FileProvider error ",e)
                                    }
                                    PhotoUtils.takePicture(this@CropActivity,imageUri,
                                        CODE_CAMERA_REQUEST
                                    )
                                }else{
                                    Toast.makeText(this@CropActivity,"Device didn't have SD Card!",Toast.LENGTH_SHORT)
                                    Log.e(TAG,"Device didn't have SD Card!")
                                }
                            }
                        }

                        override fun denied() {
                            Toast.makeText(this@CropActivity,
                                "Part of the permissions failed to obtain, and normal functions were affected ",Toast.LENGTH_SHORT)
                            Log.e(TAG,"Permission granted fail!")
                        }

                    })
            }
            R.id.btn_takeGallery ->{
                requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    object :RequestPermissionCallBack{
                        override fun granted() {
                            PhotoUtils.openPic(this@CropActivity, CODE_GALLERY_REQUEST);
                        }

                        override fun denied() {
                            Toast.makeText(this@CropActivity,
                                "Part of the permissions failed to obtain, and normal functions were affected",Toast.LENGTH_SHORT)
                        }

                    })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val outputX = 480
        val outputY = 480
        if (resultCode == RESULT_OK){
            when(requestCode){
                CODE_CAMERA_REQUEST ->{
                    cropImageUri = Uri.fromFile(fileCropUri);
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, outputX, outputY, CODE_RESULT_REQUEST);
                }
                CODE_GALLERY_REQUEST ->{
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri)
                        var newUri = Uri.parse(PhotoUtils.getPath(this, data?.data))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            newUri = FileProvider.getUriForFile(this, FILE_PROVIDER, File(newUri.path))
                        }
                        PhotoUtils.cropImageUri(
                            this,
                            newUri,
                            cropImageUri,
                            1,
                            1,
                            outputX,
                            outputY,
                            CODE_RESULT_REQUEST
                        )
                    } else {
                        Toast.makeText(this@CropActivity, "Device didn't have SD Card", Toast.LENGTH_SHORT).show()
                    }
                }
                CODE_RESULT_REQUEST ->{
                    val bitmap = PhotoUtils.getBitmapFromUri(this,imageUri)
                    if (bitmap != null){
                        photo?.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }

    private fun getImageUri(name:String): Uri? {
        val file:File = CommonUtil.createFile(this,name)
        if (file != null){
            takePath = file.absolutePath
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                return FileProvider.getUriForFile(this, FILE_PROVIDER,file)
            }else{
                return Uri.fromFile(file)
            }
        }
        return Uri.EMPTY
    }
}