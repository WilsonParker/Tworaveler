package com.developer.hare.tworaveler.Util.Parser;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Hare on 2017-08-23.
 */

public class RetrofitBodyParser {
    private static RetrofitBodyParser retrofitBodyParser = new RetrofitBodyParser();

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
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData(key, file.getName(), requestBody);
        return multipartBodyPart;
    }
}
