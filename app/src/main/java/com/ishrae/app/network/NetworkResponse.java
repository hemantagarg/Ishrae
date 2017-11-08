package com.ishrae.app.network;

import android.text.TextUtils;

import com.ishrae.app.utilities.Constants;

import org.json.JSONObject;


public class NetworkResponse {

    public static boolean isDeviceOnline = false;
    public int netRespCode = -1;
    public String respStr = null;
    private JSONObject jsonObject = null;

    //getting json object of response
    public JSONObject getJsonObject() {
        if (jsonObject == null) {
            toJson();
        }
        return jsonObject;
    }

    private void toJson() {
        try {
            if (respStr != null)
                jsonObject = new JSONObject(respStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NetworkResponse() {
        netRespCode = -1;
        respStr = null;
    }

    //here getting success message
    public boolean isSuccess() {
        if (TextUtils.isEmpty(respStr)) {
            return false;
        }
        try {
            String status = (String) getJsonObject().get(Constants.FLD_RESPONSE);
            return status.equals(Constants.VAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //getting error message
    public String getErrMsg() {
        try {
            return ((String) getJsonObject().get(Constants.FLD_ERROR_MSG));
        } catch (Exception e) {
            e.printStackTrace();
            return Constants.VAL_UNKNOWN;
        }
    }
}
