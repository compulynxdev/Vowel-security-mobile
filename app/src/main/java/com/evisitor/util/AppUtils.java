package com.evisitor.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Hemant Sharma on 23-02-20.
 */

public final class AppUtils {

    private AppUtils() {
        // This utility class is not publicly instantiable
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            AppLogger.w("getAppVersionName", e.toString());
            return "1.0";
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static void setLanguage(Context context, String code) {
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static String getBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    public static Long getBitmapSizeInMB(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] data = bos.toByteArray();
        // Convert the Bytes to KB
        long sizeInKB = data.length / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        AppLogger.e("ImagePickBottomSheetDialog:SizeInKB", "" + sizeInKB);
        return sizeInKB / 1024;
    }

    public static String getFirstLetter(String data) {
        if (data == null || data.isEmpty()) return "";
        return data.substring(0, 1).toUpperCase();
    }

    public static String capitaliseFirstLetter(String data) {
        if (data == null || data.isEmpty()) return "";
        return data.substring(0, 1).toUpperCase() + data.substring(1).toLowerCase();
    }

    public static Bitmap getBitmap(byte[] bitmap) {
        return BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
    }

    public static Bitmap getBitmap(String base64) {
        byte[] data = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static RequestBody createPartFromString(String data) {
        return RequestBody.create(
                MultipartBody.FORM, data);
    }

    public static RequestBody createBody(String bodyContent, String data) {
        return RequestBody.create(MediaType.parse(bodyContent), data);
    }

   /* public static MultipartBody.Part prepareFilePart(Context context, String key, @NonNull Uri uri) {
        File file = FileUtils.getFile(context, uri);
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(Objects.requireNonNull(context.getContentResolver().getType(uri))),
                        file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }*/

    public static MultipartBody.Part prepareFilePart(String key, String fileType, File file) {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(fileType),
                        file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(key, file.getName(), requestFile);
    }

    public static File bitmapToFile(Context context ,Bitmap bitmap, String fileNameToSave) {
        // File name like "image.png"
        //create a file to write bitmap data
        File file = null;
        try {
            //File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = new File(context.getFilesDir() + File.separator + fileNameToSave);

            //path.mkdirs();

            file.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos); // YOU can also save it in JPEG
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
            return file;
        }catch (Exception e){
            e.printStackTrace();
            return file; // it will return null
        }
    }
    public static Bitmap readBitmapFileFromDir(File file_dir){
        Bitmap bitmap = null;
        try{
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmap= BitmapFactory.decodeFile(file_dir.getAbsolutePath(), options);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}
