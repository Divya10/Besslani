/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import com.jangletech.mie.R;

import java.io.File;

import static com.jangletech.mie.util.Common.throwCrashFirebase;

/**
 * Created by Deepak on 27/12/2019.
 */
public class PathUtil {
    /*
     * Gets the file path of the given Uri.
     */
    @SuppressLint("NewApi")
    public static String getPath(Context context, Uri uri) {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:/")) {
                    return id.replace("raw:/", "file:///");
                }
                else if (id.startsWith("msf:")) {
                    return id.replace("msf:/", "file:///");
                }
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                }
                else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{split[1]};
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            }
            catch (Exception e) {
                throwCrashFirebase(e);
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static String getDirectory(Context context) {
        File directory = context.getExternalFilesDir(context.getResources().getString(R.string.app_name));
        if (directory != null) {
            String path = directory.getAbsolutePath();
            if (path.contains("/Android/")) {
                path = path.substring(0, path.indexOf("/Android/"));
                directory = new File(path, context.getResources().getString(R.string.app_name));
                if (!directory.exists()) {
                    if (directory.mkdir()) {
                        return directory.getAbsolutePath();
                    }
                }
                else {
                    return directory.getAbsolutePath();
                }
            }
        }
        //Log.e("ActSwarm", "error in creation of directory");
        return directory.getAbsolutePath();
    }

}