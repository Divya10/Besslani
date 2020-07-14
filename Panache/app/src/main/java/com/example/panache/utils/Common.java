/*
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 * //
 * //            © Copyright 2019 JangleTech Systems Private Limited, Thane, India
 * //
 * /////////////////////////////////////////////////////////////////////////////////////////////////
 */

package com.example.panache.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.CalendarContract;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.maps.model.LatLng;
import com.jangletech.mie.R;
import com.jangletech.mie.enums.CustomMediaType;

import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.ocpsoft.prettytime.PrettyTime;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Common {
    private static Dialog dialog;
    private static androidx.appcompat.app.AlertDialog alertDialog;

    public static String encode(long a) {
        String aa = String.valueOf(a);
        String aaa = "";
        while (aa.length() < 10) aa = "0" + aa;
        aa = new StringBuilder(aa).reverse().toString();
        for (int i = 0; i < 10; i += 2)
            aaa += (char) Integer.parseInt(aa.substring(i, i + 2));
        return aaa;
    }

    public static long decode(String a) {
        String aa = "";
        for (int i = 0; i < 5; i++)
            aa += (int) a.charAt(i);
        aa = new StringBuilder(aa).reverse().toString();
        return Long.valueOf(aa);
    }

    public static String awayDistance(String meters) {
        if (meters == null || meters.isEmpty()) {
            return "0m";
        }
        int a = (int) Double.parseDouble(meters);
        if (a <= 0) return "0";
        final String[] units = new String[]{"m", "Km", "Mm"};
        int digitGroups = (int) (Math.log10(a) / Math.log10(1000));
        return new DecimalFormat("#,###").format(a / Math.pow(1000, digitGroups)) + " " + units[digitGroups];
    }

    public static String awayDistance(Double meters) {
        if (meters <= 10) return "10m";
        final String[] units = new String[]{"m", "Km", "Mm"};
        int digitGroups = (int) (Math.log10(meters) / Math.log10(1000));
        return new DecimalFormat("#,##0.#").format(meters / Math.pow(1000, digitGroups)) + " " + units[digitGroups];
    }

    public static String prettyCount(Double count) {
        if (count == null) return "0";
        if (count < 1000) return "" + count.intValue();
        int exp = (int) (Math.log(count) / Math.log(1000));
        return new DecimalFormat("#,##0.#")
                .format(count / Math.pow(1000, exp))
                + " " + "kMBTQ".charAt(exp - 1);
    }

    public static String prettyTime(String date) {
        if (date == null || date.isEmpty()) {
            return "";
        }
        try {
            return new PrettyTime().format(getUTCToCurrentDate(date));
        } catch (Exception e) {
            throwCrashFirebase(e);
            return date;
        }
    }

    public static File createImageFile(Activity activity) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }


    /**
     * Get AGE of user
     *
     * @param dobString date of birth in " dd/MM/yyyy hh:mm aa"
     * @return age from now in dd Age this format
     */
    public static String getAge(String dobString) {
        if (dobString == null || dobString.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat format = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            dob.setTime(format.parse(dobString));
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
            return age + "Yrs";
        } catch (ParseException e) {
            throwCrashFirebase(e);
            return "";
        }
    }


    public static String formatDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
        try {
            Date formattedDate = format.parse(dateString);
            format = getDateFormat(formattedDate);
            if (formattedDate == null) return dateString;
            return format.format(formattedDate);
        } catch (ParseException e) {
            throwCrashFirebase(e);
            return dateString;
        }
    }

    public static SimpleDateFormat getDateFormat(Date formattedDate) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        now.setTime(formattedDate);
        if (now.get(Calendar.YEAR) == year) {
            return new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT_WITHOUT_YEAR, Locale.ENGLISH);
        }
        return new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT_WITH_YEAR, Locale.ENGLISH);
    }

    public static SimpleDateFormat getDateTimeFormat(Date formattedDate) {
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        now.setTime(formattedDate);
        if (now.get(Calendar.YEAR) == year) {
            return new SimpleDateFormat(Constants.DISPLAY_DATETIME_FORMAT_WITHOUT_YEAR, Locale.ENGLISH);
        }
        return new SimpleDateFormat(Constants.DISPLAY_DATETIME_FORMAT_WITH_YEAR, Locale.ENGLISH);
    }

    public static String formatDateStringSend(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
        try {
            Date formattedDate = format.parse(dateString);
            format = new SimpleDateFormat(Constants.DB_FORMAT_SEND, Locale.ENGLISH);
            if (formattedDate == null) return dateString;
            return format.format(formattedDate);
        } catch (ParseException e) {
            throwCrashFirebase(e);
            return dateString;
        }
    }

    //Used only in edit profile for dob hence dob then their should be year
    public static String formatStringSend(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DISPLAY_DATE_FORMAT_WITH_YEAR, Locale.ENGLISH);
        try {
            Date formattedDate = format.parse(dateString);
            format = new SimpleDateFormat(Constants.DB_FORMAT_SEND, Locale.ENGLISH);
            if (formattedDate == null) return dateString;
            return format.format(formattedDate);
        } catch (ParseException e) {
            throwCrashFirebase(e);
            return dateString;
        }
    }

    public static InputFilter[] getFilter(String blockChars) {
        return new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (source != null && blockChars.contains("" + source.charAt(i))) {
                    return source.subSequence(start, i);
                }
            }
            return null;
        }, new InputFilter.LengthFilter(255)};
    }

    public static InputFilter[] getEmojiFilter(String blockChars) {
        return new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            String source1 = StringEscapeUtils.escapeJava(source.toString());
            for (int i = start; i < end; i++) {
                if (source != null && blockChars.contains("" + source1.charAt(i))) {
                    return source.subSequence(start, i);
                }
            }
            return null;
        }};
    }

    public static String formatAgeDateString(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
        try {
            Date formattedDate = format.parse(dateString);
            format = getDateFormat(formattedDate);
            if (formattedDate == null) return dateString;
            return format.format(formattedDate);
        } catch (ParseException e) {
            throwCrashFirebase(e);
            return dateString;
        }
    }

    public static void showDialog(Context a) {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
            dialog = new Dialog(a);
            dialog.getWindow().setBackgroundDrawableResource(R.color.colorTransparent);
            dialog.setContentView(R.layout.element_loading);
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            throwCrashFirebase(e);
        }
    }

    public static void dismissDialog() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            throwCrashFirebase(e);
        }
    }

    public static void throwCrashFirebase(Exception e) {
        e.printStackTrace();
        System.out.println("#>>> prettyLog: " + e.getClass().getCanonicalName() + ": " + e.getMessage());
        Crashlytics.log(Log.DEBUG, e.getClass().getCanonicalName(), e.getMessage());
        Crashlytics.logException(new Throwable(e.toString()));
    }

    public static void throwCrashFirebase(String e) {
        System.out.println("#>>> prettyLog: " + e);
        Crashlytics.log(Log.DEBUG, "API response Error: ", e);
        Crashlytics.logException(new Throwable(e));
    }

    public static Boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true;
                    }
                }
            } else {
                try {
                    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                    if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                        //Log.i("update_status", "Network is available : true");
                        return true;
                    }
                } catch (Exception e) {
                    throwCrashFirebase(e);
                }
            }
        }
        //Log.i("update_status", "Network is available : FALSE ");
        return false;
    }


    public static void alertDialogBuild(@NotNull Context context, @NotNull String title, @NotNull String massage, String postiveBtnText, String negetiveBtnText, @NotNull AlertClickHandler clickHandler) {
        alertDialogBuild(context, title, massage, postiveBtnText, negetiveBtnText, clickHandler, false);
    }

    public static void alertDialogBuild(@NotNull Context context, @NotNull String title, @NotNull String massage, String postiveBtnText, String negetiveBtnText, @NotNull AlertClickHandler clickHandler, @NotNull Boolean isCancellable) {
        try {
            if (alertDialog != null && alertDialog.isShowing()) {
                alertDialog.dismiss();
            }
        } catch (Exception e) {
            throwCrashFirebase(e);
        }
        androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.AlertDialogTheme);
        alertDialogBuilder.setMessage(massage);
        if (postiveBtnText != null && !postiveBtnText.isEmpty()) {
            alertDialogBuilder.setPositiveButton(postiveBtnText, (dialog, which) -> {
                dialog.dismiss();
                clickHandler.positiveClickAction();
            });
        }
        if (negetiveBtnText != null && !negetiveBtnText.isEmpty()) {
            alertDialogBuilder.setNegativeButton(negetiveBtnText, (dialog, which) -> {
                dialog.dismiss();
                //Log.d("sd", "alertDialogBuild: ");
                clickHandler.negativeClickAction(false);
            });
        }
        alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //Log.d("sd", "alertDialogBuild: ");
                clickHandler.negativeClickAction(true);
            }
        });
        alertDialogBuilder.setCancelable(isCancellable);
        alertDialogBuilder.setTitle(Html.fromHtml("<font color='" +
                String.format("#%06X", (0xFFFFFF & context.getResources().getColor(R.color.colorPrimary))) + "'>" + title + "</font>"));
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static boolean isImage(Uri media, Context context) {
        ContentResolver cR = context.getContentResolver();
        String content = cR.getType(media);
        if (content != null) {
            //Log.d("#>content got ", content);
            if (content.contains("image")) {
                //Log.d("#>detected ", "is image");
                return true;
            }
        } else {
            File file = new File(media.getPath());
            String[] okFileExtensions = new String[]{"jpg", "png", "gif", "jpeg"};
            //Log.d("#>file ext got ", file.getName().toLowerCase());
            for (String extension : okFileExtensions) {
                if (file.getName().toLowerCase().endsWith(extension)) {
                    //Log.d("#>detected ", "is image");
                    return true;
                }
            }
        }
        return false;
    }

    public static void dumpScrapDir(Context c) {
        deleteRecursive(new File(getDir(c).getAbsolutePath() + "/.temp/"));
    }

    private static void deleteRecursive(File fileOrDirectory) {
        try {
            if (fileOrDirectory.isDirectory())
                for (File child : fileOrDirectory.listFiles())
                    deleteRecursive(child);

            fileOrDirectory.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File newScrapFile(String extension, Context c) {
        File tempDir = getDir(c);
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        try {
            return File.createTempFile("file", "." + extension, tempDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File getDir(Context c) {
        File directory = c.getExternalFilesDir(c.getResources().getString(R.string.app_name));
        if (directory != null) {
            String path = directory.getAbsolutePath();
            if (path.contains("/Android/")) {
                path = path.substring(0, path.indexOf("/Android/"));
                directory = new File(path, c.getResources().getString(R.string.app_name));
                if (!directory.exists()) {
                    if (directory.mkdir()) {
                        return directory;
                    }
                } else {
                    return directory;
                }
            }
        }
        //Log.e("ActSwarm", "error in creation of directory");
        return c.getExternalFilesDir(c.getResources().getString(R.string.app_name));
    }

    public static MultipartBody.Part getMultipartDataPart(String value, CustomMediaType type) {
        return MultipartBody.Part.createFormData(
                "test_name",
                "test",
                toRequestBody(value, type)
        );
    }

    public static String createLocalGif(Uri contentUri, Context context) {
        String baseDir = getDir(context).getAbsolutePath();
        String fileName = contentUri.getEncodedPath().substring(contentUri.getEncodedPath().lastIndexOf('/') + 1);
        File sharingGifFile = new File(baseDir, fileName);
        if (sharingGifFile.exists()) {
            sharingGifFile.delete();
        }
        InputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            if (!sharingGifFile.exists()) {
                sharingGifFile.createNewFile();
            }
            bis = context.getContentResolver().openInputStream(contentUri);
            bos = new BufferedOutputStream(new FileOutputStream(sharingGifFile, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sharingGifFile.getAbsolutePath();
    }

    public static RequestBody toRequestBody(String value, CustomMediaType type) {
        switch (type) {
            case IMAGE:
            case VIDEO:
            case FILE:
                return RequestBody.create(
                        MediaType.parse(type.value),
                        new File(value)
                );
            case TEXT:
                String value1 = value == null ? "" : value.trim();
                return RequestBody.create(MediaType.parse(type.value), value1);
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
    }

    public static String getCurrentToUTCFormatDate(Date time) {
        SimpleDateFormat outputFmt = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
        outputFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
        System.out.println("currentUTC: " + outputFmt.format(time));
        return outputFmt.format(time);
    }

    public static String getUTCToCurrentFormatDateTime(String dbDate) {
        try {
            Calendar current = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dbDate);
            SimpleDateFormat curruntDF = getDateTimeFormat(date);
            curruntDF.setTimeZone(TimeZone.getDefault());
            current.setTime(date);
            return curruntDF.format(current.getTime());
        } catch (Exception e) {
            throwCrashFirebase(e);
            return "";
        }
    }

    public static String getLocalFormatDateTime(String dbDate, boolean isDateTime) {
        try {
            Calendar current = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
            Date date = df.parse(dbDate);
            SimpleDateFormat curruntDF = isDateTime ? getDateTimeFormat(date) : getDateFormat(date);
            curruntDF.setTimeZone(TimeZone.getDefault());
            current.setTime(date);
            return curruntDF.format(current.getTime());
        } catch (Exception e) {
            throwCrashFirebase(e);
            return "";
        }
    }

    public static Date getUTCToCurrentDateTime(String dbDate) {
        try {
            Calendar current = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dbDate);
            SimpleDateFormat curruntDF = getDateTimeFormat(date);
            curruntDF.setTimeZone(TimeZone.getDefault());
            current.setTime(date);
            return current.getTime();
        } catch (Exception e) {
            throwCrashFirebase(e);
            return Calendar.getInstance().getTime();
        }
    }

    public static String getUTCToCurrentFormatDatetime(String dbDate) {
        try {
            Calendar current = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dbDate);
            SimpleDateFormat curruntDF = getDateTimeFormat(date);
            curruntDF.setTimeZone(TimeZone.getDefault());
            current.setTime(date != null ? date : Calendar.getInstance().getTime());
            return curruntDF.format(current.getTime());

        } catch (Exception e) {
            throwCrashFirebase(e);
            return "";
        }

    }

    public static Date getUTCToCurrentDate(String dbDate) {
        try {
            Calendar current = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat(Constants.DB_FORMAT, Locale.ENGLISH);
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = df.parse(dbDate);
            SimpleDateFormat curruntDF = getDateFormat(date);
            curruntDF.setTimeZone(TimeZone.getDefault());
            current.setTime(date);
            return current.getTime();
        } catch (Exception e) {
            throwCrashFirebase(e);
            return Calendar.getInstance().getTime();
        }
    }

    public static LatLng getLatLngFromString(String u_latest_lat, String u_latest_long) {
        LatLng latLng = null;
        try {
            double lat = Double.parseDouble(u_latest_lat);
            double lng = Double.parseDouble(u_latest_long);
            latLng = new LatLng(lat, lng);
        } catch (Exception e) {
            throwCrashFirebase(e);
            latLng = new LatLng(28.0000, 72.00000);
        }
        return latLng;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public static void setCalenderEvent(Context context, String s_id, long startTime, long endTime, String des, String title) {
        try {


        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, des);
        values.put(CalendarContract.Events.ORIGINAL_ID, Long.parseLong(s_id));

        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());

        // Default calendar
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        values.put(CalendarContract.Events.HAS_ALARM, 1);

        // Insert event to calendar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                Toast.makeText(context, "No Permission to write event in calender", Toast.LENGTH_LONG).show();
                return;
            }
        }
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        // get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Toast.makeText(context, "Calender Created with ID:" + eventID, Toast.LENGTH_LONG).show();

        ContentResolver reminder_cr = context.getContentResolver();
        ContentValues reminder_values = new ContentValues();

        reminder_values.put(CalendarContract.Reminders.EVENT_ID, eventID);
        reminder_values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        reminder_values.put(CalendarContract.Reminders.MINUTES, 15);
        Uri reminder_uri = reminder_cr.insert(CalendarContract.Reminders.CONTENT_URI, reminder_values);}catch (Exception e){
            throwCrashFirebase(e.toString());
        }
    }

    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Events._ID,
            CalendarContract.Events.TITLE,
            CalendarContract.Events.DESCRIPTION,
            CalendarContract.Events.ORIGINAL_ID
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_TITLE = 1;
    private static final int PROJECTION_DESCRIPTION = 2;
    private static final int PROJECTION_ORIGINAL_ID = 3;

    public static void deleteEvent(Context context, String s_id){
        try {
        long eventID=getEventID(context,s_id);
        if(eventID !=0){
            ContentResolver cr = context.getContentResolver();
            Uri deleteUri = null;
            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            int rows = cr.delete(deleteUri, null, null);
            //Log.i("event", "Rows deleted: " + rows);
            cr.delete(ContentUris.withAppendedId(CalendarContract.Reminders.CONTENT_URI, eventID),null,null);

        }}catch (Exception e){
            throwCrashFirebase(e.toString());
        }
    }

    public static void updateEvent(Context context, String s_id, long startTime, long endTime, String des, String title){
       try{
        long eventID=getEventID(context,s_id);
        if(eventID !=0){
            ContentResolver cr = context.getContentResolver();
            ContentValues values = new ContentValues();
            Uri updateUri = null;
            // The new title for the event
            values.put(CalendarContract.Events.DTSTART, startTime);
            values.put(CalendarContract.Events.DTEND, endTime);
            values.put(CalendarContract.Events.TITLE, title);
            values.put(CalendarContract.Events.DESCRIPTION, des);
            values.put(CalendarContract.Events.ORIGINAL_ID, Long.parseLong(s_id));

            updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
            int rows = cr.update(updateUri, values, null, null);
        }else{
            setCalenderEvent(context,s_id,startTime,endTime,des,title);
        }}catch (Exception e){
           throwCrashFirebase(e.toString());
       }
    }

    public static long getEventID(Context context, String s_id) {
        try {
        Cursor cur = null;
        ContentResolver cr = context.getContentResolver();
        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = "("
                + CalendarContract.Events.ORIGINAL_ID + " = ?)";
        String[] selectionArgs = new String[]{s_id};
        // Submit the query and get a Cursor object back.
        // Insert event to calendar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                Toast.makeText(context, "No Permission to write event in calender", Toast.LENGTH_LONG).show();
                return 0;
            }
        }
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        long calID = 0;
        while (cur.moveToNext()) {
            String displayTitle = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values̥
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayTitle = cur.getString(PROJECTION_TITLE);
            accountName = cur.getString(PROJECTION_DESCRIPTION);
            ownerName = cur.getString(PROJECTION_ORIGINAL_ID);


            //Log.d("getting event id", "updateCalenderEvent: " + calID + " " + displayTitle + " " + accountName + " " + ownerName);


        }
        return calID;
        }catch (Exception e){
            throwCrashFirebase(e.toString());
        }
        return 0;
    }

    public interface AlertClickHandler {
        void positiveClickAction();

        void negativeClickAction(boolean forceClose);
    }
}
