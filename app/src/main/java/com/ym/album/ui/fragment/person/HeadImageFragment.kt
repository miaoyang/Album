package com.ym.album.ui.fragment.person

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.ym.album.R
import com.ym.album.app.config.AppConstant
import com.ym.album.app.config.PathConfig
import com.ym.album.base.BaseFragment
import com.ym.album.event.Event
import com.ym.album.utils.WindowUtil
import com.ym.common_util.utils.FileUtil
import com.ym.common_util.utils.LogUtil


@Route(path = PathConfig.Person.PERSON_HEAD_IMAGE)
class HeadImageFragment : BaseFragment() {
    companion object{
        const val TAG = "HeadImageFragment"
        const val FILE_PROVIDER = "android.support.FILE_PROVIDER_PATHS"
        const val AUTHORITY = "com.ym.album.fileprovider"
        const val DOCUMENT_PROVIDER = "com.android.providers.media.documents"
        const val DOCUMENT_DOWNLOAD = "com.android.providers.downloads.documents"
        const val CHOSE_PHOTO = 1104
        const val TAKE_PHOTO = 1127
        // permission
        private val PERMISSION_LIST = arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
        private const val REQUEST_PERMISSION_CODE = 267
    }

    private lateinit var mIvBackup:ImageView
    private lateinit var mIvMore:ImageView
    private lateinit var mIvHeadImage:ImageView

    private lateinit var popView:View
    private lateinit var parentView:View

    private lateinit var popupWind:PopupWindow

    private lateinit var mImageUri:Uri
    private lateinit var mImageUriFromFile: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_head_image, container, false)
        parentView = view
        mIvBackup = view.findViewById(R.id.iv_head_image_backup)
        mIvMore = view.findViewById(R.id.iv_head_image_more)
        mIvHeadImage = view.findViewById(R.id.iv_head_image_personal)

        initView()
        return view
    }



    override fun getLayoutId(): Int {
        return R.layout.fragment_head_image
    }

    override fun initView() {
        mIvBackup.setOnClickListener {
            ARouter.getInstance().build(PathConfig.Person.PERSON_INFO_ACTIVITY).navigation()
            LogUtil.d(TAG,"initView(): path=${PathConfig.Person.PERSON_INFO_ACTIVITY}")
        }
        mIvMore.setOnClickListener {
            popView = LayoutInflater.from(context).inflate(R.layout.person_dialog_layout,null)
            popView.visibility = View.VISIBLE
            popupWind = WindowUtil.createPopupWindow(parentView,popView)
        }
    }

    override fun onMessageEvent(event: Event<*>?) {
        when(event?.code){
            AppConstant.Event.PERSON_DIALOG_TAKE_PHOTO ->{
                takePhoto()
            }
            AppConstant.Event.PERSON_DIALOG_EXIT ->{
                popupWind.dismiss()
            }
            AppConstant.Event.PERSON_DIALOG_OPEN_GALLERY ->{
                openGallery()
            }
        }
        super.onMessageEvent(event)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){
            TAKE_PHOTO ->{
                if (resultCode==RESULT_OK){
                    try {
                        val bitmap:Bitmap = BitmapFactory.decodeStream(context?.contentResolver?.openInputStream(mImageUri))
                        mIvHeadImage.setImageBitmap(bitmap)
                        LogUtil.d(TAG,"onActivityResult(): mIvHeadImage setImageBitmap")
                        addGalleryPicture(mImageUriFromFile)
                    }catch (e:Exception){
                        LogUtil.d(TAG,"onActivityResult(): error ",e)
                    }
                }
            }
            CHOSE_PHOTO ->{
                if (data==null){
                    return
                }
                if (requestCode== RESULT_OK){
                    handleImage(data,requireContext())
                }
            }
        }

    }

    /**
     * 拍照
     */
    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhoto(): Uri? {
        // 弹窗消失
        popupWind.dismiss()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PERMISSION_LIST.forEach {
                if (checkSelfPermission(requireContext(), it)  != PermissionChecker.PERMISSION_GRANTED) {
                    requestPermissions(PERMISSION_LIST, REQUEST_PERMISSION_CODE);
                }
                LogUtil.d(TAG,"takePhoto(): request permission")
            }
        }
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePhotoIntent.resolveActivity(requireContext().packageManager)!=null){
            val imageFile = FileUtil.createImageFile(requireContext())
            mImageUriFromFile = Uri.fromFile(imageFile)
            if (imageFile != null){
                mImageUri = FileProvider.getUriForFile(requireContext(),AUTHORITY,imageFile)
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT,mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, TAKE_PHOTO);//打开相机
            }
        }
        LogUtil.d(TAG,"takePhoto(): success")
        return mImageUri
    }

    /**
     * 发送广播，将拍照的照片添加到相册
     */
    private fun addGalleryPicture(uri:Uri){
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also {
            it.data = uri
            context?.sendBroadcast(it)
        }
        LogUtil.d(TAG,"addGalleryPicture(): $uri")
    }

    /**
     * 打开相册
     */
    private fun openGallery(){
        val openAlbumIntent = Intent(Intent.ACTION_GET_CONTENT)
        openAlbumIntent.type = "image/*"
        startActivityForResult(openAlbumIntent, CHOSE_PHOTO)
        LogUtil.d(TAG,"openGallery(): success")
    }

    /**
     *
     */
    @SuppressLint("Range")
    private fun getImagePathByUri(uri:Uri, selection: String?): String? {
        var path:String? = null
        val cursor: Cursor? = context?.contentResolver?.query(uri,null,selection,null,null)
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                cursor.close()
            }
        }
        return path
    }

    private fun handleImage(intent:Intent,context: Context){
        var imagePath:String? = null
        val uri: Uri? = intent.data
        if (DocumentsContract.isDocumentUri(context,uri)){
            val docId:String = DocumentsContract.getDocumentId(uri)
            if (DOCUMENT_PROVIDER==(uri?.authority)){
                val id:String = docId.split(":")[1]
                val selection = MediaStore.Images.Media._ID + "=" + id
                imagePath = getImagePathByUri(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
            }else if (DOCUMENT_DOWNLOAD == uri?.authority){
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(docId)
                )
                imagePath = getImagePathByUri(contentUri, null)
            }
        }else if ("content".equals(uri?.scheme,true)){
            imagePath = uri?.let { getImagePathByUri(it,null) }
        }else if ("file".equals(uri?.scheme,true)){
            imagePath = uri?.path
        }
        displayImage(imagePath)
    }

    /**
     * 展示图片
     */
    private fun displayImage(imagePath: String?) {
        if (imagePath != null) {
            val bitmap = BitmapFactory.decodeFile(imagePath)
            mIvHeadImage.setImageBitmap(bitmap)
        } else {
            Toast.makeText(context, "failed to get image", Toast.LENGTH_SHORT).show()
        }
    }
}