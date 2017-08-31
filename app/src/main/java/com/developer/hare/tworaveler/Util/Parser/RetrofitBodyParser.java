package com.developer.hare.tworaveler.Util.Parser;

import com.developer.hare.tworaveler.Util.File.FileManager;
import com.developer.hare.tworaveler.Util.Image.ImageManager;
import com.developer.hare.tworaveler.Util.Log_HR;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Hare on 2017-08-23.
 */

public class RetrofitBodyParser {
    private static RetrofitBodyParser retrofitBodyParser = new RetrofitBodyParser();
    private static final int UPLOAD_MAX_SIZE = 640;

    public static RetrofitBodyParser getInstance() {
        return retrofitBodyParser;
    }

    public RequestBody createRequestBody(Object object) {
        RequestBody requestBody = null;
        if (object instanceof String) {
            requestBody = RequestBody.create(MediaType.parse("text/plain"), (String) object);
        } else if (object instanceof Integer) {
            requestBody = RequestBody.create(MediaType.parse("text/plain"), ((int) object) + "");
        }
        return requestBody;
    }

    public MultipartBody.Part createImageMultipartBodyPart(String key, File file) {
        FileManager fileManager = FileManager.getInstance();
        byte[] byteData= fileManager.encodeBitmapToByteArray(ImageManager.getInstance().resizeImage(fileManager.encodeFileToBitmap(file), UPLOAD_MAX_SIZE));

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), byteData);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        return multipartBodyPart;
    }

    public Map<String, RequestBody> parseMapRequestBody(Object obj) {
        final String Key = "GET";
        Map<String, RequestBody> dataMap = new HashMap<>();
        Map<String, Method> methodMap = new HashMap<>();

        Class<? extends Object> objClass = obj.getClass();

        for (Method method : objClass.getMethods()) {
            String methodName = method.getName().toUpperCase();
            if (methodName.contains(Key)) {
                methodMap.put(methodName, method);
            }
        }

        Arrays.asList(objClass.getDeclaredFields()).forEach(field ->
                {
                    String fieldName = field.getName();
                    try {
                        Method method = methodMap.get(Key + fieldName.toUpperCase());
                        if (method != null) {
                            Object value = method.invoke(obj);
                            if (value != null)
                                dataMap.put(fieldName, createRequestBody(value));
                        }
                    } catch (Exception e) {
                        Log_HR.log(getClass(), "parseMapRequestBody(Object obj)", e);
                    }
                }
        );
        return dataMap;
    }
}
