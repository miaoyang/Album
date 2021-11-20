package com.ym.album.utils;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

public class PhotoUtils {
    private static final String TAG = PhotoUtils.class.getSimpleName();
    private static final String ACTION_CROP = "com.android.camera.action.CROP";

    /**
     * take photo
     * @param activity
     * @param imageUri image uri store path
     * @param requestCode
     */
    public static void takePicture(Activity activity, Uri imageUri,int requestCode){
        Intent intentCamera = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intentCamera.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        activity.startActivityForResult(intentCamera,requestCode);
    }

    /**
     * open Album
     * @param activity
     * @param requestCode
     */
    public static void openPic(Activity activity,int requestCode){
        Intent openPicIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openPicIntent.setType("iamge/*");
        activity.startActivityForResult(openPicIntent,requestCode);
    }

    /**
     * crop image
     * @param activity
     * @param orgUri uri of original image
     * @param desUri
     * @param aspectX Ratio in x direction
     * @param aspectY
     * @param width outputX
     * @param height outputY
     * @param requestCode
     */
    public static void cropImageUri(Activity activity,Uri orgUri,Uri desUri,int aspectX,int aspectY,
                                    int width,int height,int requestCode){
        Intent cropIntent = new Intent(ACTION_CROP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){ // 24
            cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        cropIntent.setDataAndType(orgUri,"image/*");
        cropIntent.putExtra("crop","true");
        cropIntent.putExtra("aspectX",aspectX);
        cropIntent.putExtra("aspectY",aspectY);
        cropIntent.putExtra("outputX",width);
        cropIntent.putExtra("outputY",height);
        cropIntent.putExtra("scale",true);
        // save the crop images in desUri
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT,desUri);
        cropIntent.putExtra("return-data",false);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        cropIntent.putExtra("noFaceDetection",true);
        activity.startActivityForResult(cropIntent,requestCode);
    }

    /**
     * read image from uri
     * @param context
     * @param imageUri
     * @return
     */
    public static Bitmap getBitmapFromUri(final Context context,Uri imageUri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imageUri);
            return bitmap;
        }catch (Exception e){
            Log.e(TAG,"getBitmapFromUri ",e);
            return null;
        }
    }

    /**
     * Get data from uri
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    private static String getDataColumn(final Context context,Uri uri,
                                       String selection,String[] selectionArgs){
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
            if (cursor != null && cursor.moveToFirst()){
                final int colum_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(colum_index);
            }
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Get String object from uri
     * @param context
     * @param uri
     * @return
     */
    public static String getPath(final Context context,final Uri uri){
        final boolean isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        String pathHead = "file:///";
        if (isKitkat && DocumentsContract.isDocumentUri(context,uri)){
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return pathHead + Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)){
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(id));
                return pathHead + getDataColumn(context,contentUri,null,null);
            } else if (isMediaDocument(uri)){
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return pathHead + getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())){
            return pathHead + getDataColumn(context,uri,null,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            return pathHead + uri.getPath();
        }
        return null;
    }


    private static boolean isExternalStorageDocument(Uri uri){
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
