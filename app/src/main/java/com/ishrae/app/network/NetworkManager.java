package com.ishrae.app.network;

import android.content.Context;

import com.ishrae.app.model.SharedPref;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Nss Solutions on 3/21/2017.
 */

public class NetworkManager {

    private static OkHttpClient client;
    private static MediaType JSON;
    private static okhttp3.Callback callback;
    private static Context context;

    private static RequestBody body;
    private static Request request;
    private static MultipartBody.Builder requestBody;
    public static final String MEDIA_TYPE_JPG = "image/jpg";

    public static void init(Context context1, okhttp3.Callback callback1) {
        client = new OkHttpClient();
        getRequestObj();
        JSON = MediaType.parse("application/json; charset=utf-8");
        if (callback1 != null) {
            callback = callback1;
        }
        context = context1;
    }

    public static void requestForAPI(Context mContext, okhttp3.Callback callback1, String requestType, String url, String json, boolean isShowLoader) {
        init(context, callback1);
        if (isShowLoader) {
            Util.progressDialog(mContext, "loading");
        }

        if (json.length() > 0) {
            body = RequestBody.create(JSON, json);
        }

        if (requestType.equals(Constants.VAL_POST)) {
            request = new Request.Builder()
                    .header(Constants.FLD_CONTENT_TYPE, Constants.VAL_CONTENT_TYPE)
                    .header(Constants.FLD_CIPHER, Constants.VAL_CIPHER)
                    .header(Constants.FLD_BUILD_VERSION, Constants.VAL_BUILD_VERSION)
                    .header(Constants.FLD_DEVICE_TYPE, Constants.VAL_ANDROID)
                    .url(url)
                    .post(body)
                    .build();

        } else if (requestType.equals(Constants.VAL_GET)) {
            request = new Request.Builder()
                    .header(Constants.FLD_CONTENT_TYPE, Constants.VAL_CONTENT_TYPE)
                    .header(Constants.FLD_CIPHER, Constants.VAL_CIPHER)
                    .header(Constants.FLD_BUILD_VERSION, Constants.VAL_BUILD_VERSION)
                    .header(Constants.FLD_DEVICE_TYPE, Constants.VAL_ANDROID)
                    .url(url)
                    .get()
                    .build();
        }

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.newCall(request).enqueue(callback);
    }


    /**
     * Common function for make request to server as post
     *
     * @param credential
     * @param url
     */
    public static void uploadFileToServer(Context mContext, okhttp3.Callback callback1, String credential, String url, boolean isShowLoader) {
        if (isShowLoader) {
            Util.progressDialog(mContext, "loading");
        }
        MultipartBody multipartBody = requestBody.build();

//        if (credential != null && !credential.isEmpty()) {
//            request = new okhttp3.Request.Builder().header("Authorization", credential)
//                    .url(url)
//                    .post(multipartBody)
//                    .build();
//        } else {
        request = new Request.Builder()
                .header(Constants.FLD_CONTENT_TYPE, Constants.VAL_CONTENT_TYPE)
                .header(Constants.FLD_CIPHER, Constants.VAL_CIPHER)
                .header(Constants.FLD_BUILD_VERSION, Constants.VAL_BUILD_VERSION)
                .header(Constants.FLD_DEVICE_TYPE, Constants.VAL_ANDROID)
                .url(url)
                .post(multipartBody)
                .build();
//        }
//

//        OkHttpClient httpClient=new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }

    /**
     * This is the common function for add file to request to server
     *
     * @param key
     * @param filPath
     * @return
     */

    public static MultipartBody.Builder addFileForUploading(String key, String filPath, String mediaType, String type) {
        File f = new File(filPath);
        if (mediaType == null) {
            mediaType = MEDIA_TYPE_JPG;
        }
        if (f.exists()) {
            // mediaType=ApiConnection.MEDIA_TYPE_UNKNOWN;
            requestBody.addFormDataPart(key, f.getName(), RequestBody.create(MediaType.parse(mediaType), f));
            addDataToRequest(Constants.FLD_TYPE, type);
            addDataToRequest(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
        }
        return requestBody;
    }

    private static MultipartBody.Builder getRequestObj() {
        requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        return requestBody;
    }

    /**
     * add key value data for request to Server
     *
     * @param key
     * @param value
     * @return
     */
    public static MultipartBody.Builder addDataToRequest(String key, String value) {
        requestBody.addFormDataPart(key, value);
        return requestBody;
    }
}
