package com.developer.hare.tworaveler.Util.File;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.developer.hare.tworaveler.Net.Net;
import com.developer.hare.tworaveler.Util.LogManager;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.kakao.auth.StringSet.file;

/**
 * Created by Hare on 2017-08-06.
 */

public class FileManager {
    private static final FileManager ourInstance = new FileManager();
    private SharedPreferences sharedPreferences;
    private static Activity activity;
    public static final String KEY_SHAREDPREFERENCES = "pref", KEY_SESSION = "session";

    public static FileManager getInstance() {
        return ourInstance;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences(KEY_SHAREDPREFERENCES, MODE_PRIVATE);
    }

    public Bitmap encodeFileToBitmap(File file) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (Exception e) {
            LogManager.log(getClass(), "encodeFileToBitmap(File file)", e);
        }
        return bitmap;
    }

    public byte[] encodeBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        } catch (Exception e) {
            LogManager.log(getClass(), "encodeBitmapToFile(Bitmap bitmap)", e);
        }
        return bytes.toByteArray();
    }

    public void doFileUpload(File file, String method) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
//        String existingFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mypic.png";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        final int maxBufferSize = 1 * 1024 * 1024;
        String responseFromServer = "";
        String urlString = Net.getU();
        try {
            //------------------ CLIENT REQUEST
            FileInputStream fileInputStream = new FileInputStream(file);
            // open a URL connection to the Servlet
            URL url = new URL(urlString + method);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + file.getName() + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("Debug", "File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        } catch (IOException ioe) {
            Log.e("Debug", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream(conn.getInputStream());
            String str;
            while ((str = inStream.readLine()) != null) {
                Log.e("Debug", "Server Response " + str);
            }
            inStream.close();
        } catch (IOException ioex) {
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
    }

    // 값 불러오기
    public SharedPreferences getPreference() {
        return sharedPreferences;
    }


    // 값 저장하기
    public void savePreferences(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof String) {
            editor.putBoolean(key, (boolean) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set<String>) value);
        }
        editor.commit();
    }
    // 값(Key Data) 삭제하기

    public void removePreferences(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    public void removeAllPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    private byte[] getFileFromURL(String path) {
        byte[] buf = null;
        try {
            URL url = new URL(path);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            OutputStream outStream = new FileOutputStream(file);
            // 읽어들일 버퍼크기를 메모리에 생성
            buf = new byte[1024];
            int len = 0;
            // 끝까지 읽어들이면서 File 객체에 내용들을 쓴다
            while ((len = inputStream.read(buf)) > 0) {
                outStream.write(buf, 0, len);
            }
            // Stream 객체를 모두 닫는다.
            outStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buf;
    }

    public void copyAssetAll(String srcPath) {
        AssetManager assetMgr = activity.getAssets();
        String assets[] = null;
        try {
            assets = assetMgr.list(srcPath);
            if (assets.length == 0) {
                copyFile(srcPath);
            } else {
                String destPath = activity.getFilesDir().getAbsolutePath() + File.separator + srcPath;

                File dir = new File(destPath);
                if (!dir.exists())
                    dir.mkdir();
                for (String element : assets) {
                    copyAssetAll(srcPath + File.separator + element);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void copyFile(String srcFile) {
        AssetManager assetMgr = activity.getAssets();

        InputStream is = null;
        OutputStream os = null;
        try {
            String destFile = activity.getFilesDir().getAbsolutePath() + File.separator + srcFile;

            is = assetMgr.open(srcFile);
            os = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            is.close();
            os.flush();
            os.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return activity.getFilesDir().getAbsolutePath() + File.separator;
    }

}
