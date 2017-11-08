package com.ishrae.app.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.ishrae.app.R;
import com.ishrae.app.utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Vishal on 28/07/2016.
 */

public class SharedPref {

    private static SharedPreferences mPref;
    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static void setLogin(Context context, boolean login) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putBoolean(Constants.FLD_IS_LOGIN, login).commit();
    }

    public static boolean isLogin(Context context) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        boolean isLogin = mPref.getBoolean(Constants.FLD_IS_LOGIN, false);
        return isLogin;
    }

    public static int getMemberId(Context context) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        int memberId = mPref.getInt(Constants.FLD_MEMBER_ID, 0);
        return memberId;
    }

    public static void setMemberId(Context context, int memberId) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putInt(Constants.FLD_MEMBER_ID, memberId).commit();
    }

    public static String getUserModelJSON(Context context) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        String userModelJson = mPref.getString(Constants.FLD_USER_MODEL_JSON, "");
        return userModelJson;
    }

    public static void setUserModelJSON(Context context, String userModelJson) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putString(Constants.FLD_USER_MODEL_JSON, userModelJson).commit();
    }

    public static void setLoginUserToken(Context context, String token) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putString(Constants.FLD_USER_TOKEN, token).commit();
    }

    public static String getLoginUserToken() {
        mPref = (SharedPreferences) mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        String userToken = mPref.getString(Constants.FLD_USER_TOKEN, "");
        return userToken;
    }

    public static void setScreenW(Context context, int screenW) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putInt(Constants.FLD_SCREEN_WIDTH, screenW).commit();
    }

    public static int getScreenW(Context context) {

        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        return mPref.getInt(Constants.FLD_SCREEN_WIDTH, 0);
    }

    public static void setDeviceToken(Context context, String deviceToken) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putString(Constants.FLD_DEVICE_TOKEN, deviceToken).commit();
    }

    public static String getDeviceToken() {
        mPref = (SharedPreferences) mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        String deviceToken = mPref.getString(Constants.FLD_DEVICE_TOKEN, "");
        return deviceToken;
    }


    public static void setDontShowMeAgain(Context context, boolean dontShowMeAgain) {
        mPref = (SharedPreferences) context.getSharedPreferences(context.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        mPref.edit().putBoolean(Constants.FLD_DONT_SHOW_ME_AGAIN, dontShowMeAgain).commit();
    }

    public static boolean getDontShowMeAgain() {
        mPref = (SharedPreferences) mContext.getSharedPreferences(mContext.getResources().getString(R.string.app_name), Context.MODE_PRIVATE);
        return  mPref.getBoolean(Constants.FLD_DONT_SHOW_ME_AGAIN, false);

    }
}
