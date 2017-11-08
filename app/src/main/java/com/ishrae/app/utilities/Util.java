package com.ishrae.app.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ishrae.app.Manifest;
import com.ishrae.app.R;
import com.ishrae.app.activity.BaseAppCompactActivity;
import com.ishrae.app.activity.CIQEventsDetailAct;
import com.ishrae.app.activity.CIQEventsListAct;
import com.ishrae.app.activity.CommonFormAct;
import com.ishrae.app.activity.DashboardActivity;
import com.ishrae.app.activity.LoginActivity;
import com.ishrae.app.adapter.RollSelectorAdapter;
import com.ishrae.app.model.SelectorModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.tempModel.UserDetailsTemp;
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationGray;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.Response;


/**
 * this class having all the common function over all in the application.
 */
public class Util {

    private static Dialog apiCallingProgressDialog = null;
    private static AlertDialog retryCustomAlert;
    private static Dialog alert;
    private static JSONObject jsonObject1 = null;
    private static  Dialog dialogSelectRoll;
    private static RecyclerView rvRollSelector;
    private static UserDetailsTemp  userDetailsTemp;

    private static int requestPermissionForPhoto = 0;

    private static JSONObject loginResponse;
    public static void progressDialog(Context mContext, String title) {
        if (apiCallingProgressDialog == null) {

            apiCallingProgressDialog = new Dialog(mContext, android.R.style.Theme_Translucent);
            apiCallingProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            apiCallingProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            apiCallingProgressDialog.setContentView(R.layout.progress_alert);

//            TextView txtProgressMsg = (TextView) apiCallingProgressDialog.findViewById(R.id.txtProgressMsg);
//            txtProgressMsg.setText(title);

            ProgressBar progressBar = (ProgressBar) apiCallingProgressDialog.findViewById(R.id.progressBar);
            progressBar.getIndeterminateDrawable().setColorFilter(mContext.getResources().getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);

            apiCallingProgressDialog.setCancelable(false);
            apiCallingProgressDialog.show();
        }
    }
    public static void dismissProgressDialog() {
        if (apiCallingProgressDialog != null) {
            apiCallingProgressDialog.dismiss();
            apiCallingProgressDialog = null;
        }
    }

    public static void showDefaultAlert(final Context mContext, String msg, final Class<?> cls) {
        new android.support.v7.app.AlertDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (cls != null) {
                            startActivityWithFinish(mContext, cls);
                        }
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    public static android.support.v7.app.AlertDialog showDefaultAlertWithOkClick(final Context mContext, String msg, DialogInterface.OnClickListener onClickListener) {
        if (onClickListener == null)
            onClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            };
        return new android.support.v7.app.AlertDialog.Builder(mContext)
                .setTitle(mContext.getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, onClickListener)
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    public static AlertDialog retryAlertDialog(Context mContext, String title, String msg, String firstButton, String SecondButton, View.OnClickListener secondButtonClick) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        dialogBuilder.setCancelable(false);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.retry_alert, null);
        dialogBuilder.setView(dialogView);

        TextView txtRAMsg = (TextView) dialogView.findViewById(R.id.txtRAMsg);
        TextView txtRAFirst = (TextView) dialogView.findViewById(R.id.txtRAFirst);
        TextView txtRASecond = (TextView) dialogView.findViewById(R.id.txtRASecond);
        View deviderView = (View) dialogView.findViewById(R.id.deviderView);

        txtRAMsg.setText(msg);

        if (firstButton.length() == 0) {
            txtRAFirst.setVisibility(View.GONE);
            deviderView.setVisibility(View.GONE);
        } else {
            txtRAFirst.setVisibility(View.VISIBLE);
            txtRAFirst.setText(firstButton);
        }

        if (SecondButton.length() == 0) {
            txtRASecond.setVisibility(View.GONE);
            deviderView.setVisibility(View.GONE);
        } else {
            txtRASecond.setVisibility(View.VISIBLE);
            txtRASecond.setText(SecondButton);
        }

        txtRAFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retryCustomAlert.dismiss();
            }
        });

        txtRASecond.setOnClickListener(secondButtonClick);

        retryCustomAlert = dialogBuilder.create();
        retryCustomAlert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        retryCustomAlert.show();
        return retryCustomAlert;
    }

    public static void dismissRetryAlert() {
        if (retryCustomAlert != null) {
            retryCustomAlert.dismiss();
        }
    }

    public static void infoAlert(Context mContext, String msg, String buttonFirst, String buttonSecond, View.OnClickListener firstButtonClick, boolean isRating) {
        alert = new Dialog(mContext);
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        alert.setContentView(R.layout.alert);

        TextView txtMsg = (TextView) alert.findViewById(R.id.txtMsg);
        Button btnFirst = (Button) alert.findViewById(R.id.btnFirst);
        Button btnSecond = (Button) alert.findViewById(R.id.btnSecond);

        txtMsg.setText(msg);
        if (buttonFirst.length() > 0) {
            btnFirst.setText(buttonFirst);
            btnFirst.setVisibility(View.VISIBLE);
        } else {
            btnFirst.setText("");
            btnFirst.setVisibility(View.GONE);
        }

        if (buttonSecond.length() > 0) {
            btnSecond.setText(buttonSecond);
            btnSecond.setVisibility(View.VISIBLE);
        } else {
            btnSecond.setText("");
            btnSecond.setVisibility(View.GONE);
        }

        btnFirst.setOnClickListener(firstButtonClick);

        btnSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        alert.setCancelable(false);
        alert.show();
    }

    public static void dismissAlert() {
        if (alert != null) {
            alert.dismiss();
        }
    }

    //will check whether device is connected to any internet connection or not.
    public static boolean isDeviceOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isDeviceOnline(Context context, boolean isShowToast) {
        boolean isDeviceOnline = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            isDeviceOnline = cm.getActiveNetworkInfo().isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!isDeviceOnline && isShowToast) {
            Util.showDefaultAlert(context, context.getResources().getString(R.string.msg_internet), null);
        }
        return isDeviceOnline;
    }

    //parse the response coming from server using gson library.
    public static Object getJsonToClassObject(String jsonStr, Class<?> classs) {
        try {
            Gson gson = new GsonBuilder().create();
            return (Object) gson.fromJson(jsonStr, classs);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //to show a center toast.
    public static void showCenterToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    //hide soft key board
    public static void hideSoftKeyboard(Context ctx, EditText view) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    //to start any activity.
    public static void startActivity(Context context, Class<?> class1) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setClass(context, class1);
        ((Activity) context).startActivity(intent);
    }

    //to start any activity.
    public static void startActivityWithFinish(Context context, Class<?> class1) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(context, class1);
        ((Activity) context).startActivity(intent);
        ((Activity) context).finish();
    }


    public static String getMACAddress(Context context) {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static void disableSpecialChar(EditText editText) {
        final String blockCharacterSet = "<>";
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source != null && blockCharacterSet.contains(("" + source))) {
                    return "";
                }
                return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    public static String getMessageFromJObj(JSONObject jsonObject) {
        String message = "";
        try {
            message = jsonObject.getString(Constants.FLD_RESPONSE_MESSAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static void logout(Activity mContext) {
        SharedPref.setLogin(mContext, false);
        SharedPref.setUserModelJSON(mContext, "");
        SharedPref.setMemberId(mContext, 0);
        SharedPref.setDontShowMeAgain(mContext, false);
        Util.startActivityWithFinish(mContext, LoginActivity.class);
    }

    public static boolean isDateBeforeSixMonth(String renewalDate) {
        boolean show=false;
        if (!TextUtils.isEmpty(renewalDate)) {
            try {

                SimpleDateFormat df = new SimpleDateFormat( Constants.SERVER_DATE_FORMAT_COMING);
                long time = df.parse(renewalDate).getTime();
                Date date = new Date();
                Date dateCurrent = new Date();
                date.setTime(time);
                Calendar c = Calendar.getInstance();
                c.set(date.getYear(), date.getMonth(), date.getDay());
                c.add(Calendar.MONTH, -5);
                Calendar cCurrent = Calendar.getInstance();
                cCurrent.set(dateCurrent.getYear(), dateCurrent.getMonth()+1, dateCurrent.getDay());

               if(cCurrent.getTimeInMillis()>=c.getTimeInMillis())
                   show=true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return show;
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({GRANTED, DENIED, BLOCKED})
    public @interface PermissionStatus {
    }

    public static final int GRANTED = 0;
    public static final int DENIED = 1;
    public static final int BLOCKED = 2;

    @PermissionStatus
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    public static int getDeviceWidth(Context mContext) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.widthPixels;
    }

    public static int getDeviceHeight(Context mContext) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }

    public static boolean isAppInstalled(Context mContext, String packageName) {
        PackageManager pm = mContext.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return pm.getApplicationInfo(packageName, 0).enabled;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static JSONObject getObjectFromResponse(Response response) {
         jsonObject1 = null;
        try {
            String strResponse = response.body().string();
            if (strResponse != null && !strResponse.isEmpty()) {
                jsonObject1 = new JSONObject(strResponse);
                 String responseData=jsonObject1.getString(Constants.FLD_RESPONSE_DATA);
                 String responseObj=jsonObject1.getString(Constants.FLD_RESPONSE_OBJECT);
                  if(jsonObject1!=null&&(jsonObject1.getInt(Constants.FLD_RESPONSE_CODE)==Constants.CODE_LOGIN_FROM_OTHER_DEVICE||jsonObject1.getInt(Constants.FLD_RESPONSE_CODE)==Constants.CODE_TOKEN_EXPIRE))
                  {
                      final JSONObject jobj=jsonObject1;
                      BaseAppCompactActivity.baseAppCompactActivity.runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              try {
                                  showDefaultAlertWithOkClick(BaseAppCompactActivity.baseAppCompactActivity, jobj.getString(Constants.FLD_RESPONSE_MESSAGE), new DialogInterface.OnClickListener() {
                                      @Override
                                      public void onClick(DialogInterface dialogInterface, int i) {
                                          logout(BaseAppCompactActivity.baseAppCompactActivity);
                                      }
                                  });
                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                      });
                      jsonObject1=null;
                  }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject1;
    }

    public static String decodeToken(String token) {
        String response = "";
        String[] segments = token.split("\\.");
        if (segments.length > 1) {
            String base64String = segments[1];
            int requiredLength = (int) (4 * Math.ceil(base64String.length() / 4.0));
            int nbrPaddings = requiredLength - base64String.length();

            if (nbrPaddings > 0) {
                base64String = base64String + "====".substring(0, nbrPaddings);
            }
            base64String = base64String.replace("-", "+");
            base64String = base64String.replace("_", "/");

            try {
                byte[] data123 = Base64.decode(base64String, Base64.DEFAULT);
                response = new String(data123, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response;
    }

    public static void manageFailure(final Activity activity, final IOException e) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Util.dismissProgressDialog();
                Util.showDefaultAlert(activity, "server not working. please try later.", null);
            }
        });

    }

    public static void manageFailure(final Activity activity, final IOException e, final View v) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Util.dismissProgressDialog();
                if (v != null)
                    v.setEnabled(true);
                Util.showDefaultAlert(activity, e.getMessage(), null);
            }
        });
    }

    public static String convertDateToFormat(Date date, String newFormat) {
        String convertedDate = null;
        try {
            long time = date.getTime();
            date.setTime(time);
            SimpleDateFormat df = new SimpleDateFormat(newFormat);
            convertedDate = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertedDate.trim();
    }

    public static Gson getGsonBuilder(String comingDateFormat) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        if (comingDateFormat != null)
            gsonBuilder.setDateFormat(comingDateFormat);
        Gson gson = gsonBuilder.create();
        return gson;
    }

    public static String convertDateTimeFormat(String utcTime, String comingDateFormate, String format) {
        String convertedDate = "";
        if (!TextUtils.isEmpty(utcTime)) {
            try {
                if (comingDateFormate == null) {
                    comingDateFormate = Constants.SERVER_DATE_FORMAT_FOR_SENDING;
                }
                SimpleDateFormat df = new SimpleDateFormat(comingDateFormate);
                long time = df.parse(utcTime).getTime();
                Date date = new Date();
                date.setTime(time);
                convertedDate = df.format(date);
                if (format != null && !format.isEmpty()) {
                    convertedDate = new SimpleDateFormat(format).format(date);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return convertedDate;
    }

    public static void replaceFrg(AppCompatActivity ctx, Fragment frag, boolean addToBackStack, int resId) {
        FragmentManager fm = ctx.getSupportFragmentManager();
        int addedFrgCount = fm.getBackStackEntryCount();
        FragmentTransaction ft = fm
                .beginTransaction();
        if (resId == Constants.DEFAULT_ID) {
            resId = R.id.fmContainer;
        }
        ft.replace(resId, frag, frag.getClass().getName());
        if (addToBackStack)
            ft.addToBackStack(frag.getClass().getName());
        ft.commit();
    }


    public static void openUrlOnBrowser(Context ctx, String url) {
        if (!TextUtils.isEmpty(url)) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            ctx.startActivity(browserIntent);
        }
    }


    public static void setDeviceSreenH(Activity act) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        act.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int width = displaymetrics.widthPixels;
        SharedPref.setScreenW(act, width);

    }


    public static boolean isValidName(Activity act, String fName) {
        if (fName.trim().length() <= 0) {
            showDefaultAlert(act, act.getResources().getString(R.string.error_user_name_blank), null);
        }
//       else if (fName.trim().length() < Constants.MINIMUMLENGTHOFNAME) {
//            showDefaultAlert(act, act.getString(R.string.name_error_msg),null);
//        }
        else if (checkStringsContainsOnlySpecialChar(fName)) {
            showDefaultAlert(act, act.getString(R.string.only_special_characters_not_allowed), null);
        } else if (isNumeric(fName)) {
            showDefaultAlert(act, act.getString(R.string.only_numbers_not_allowed), null);
        } else {
            return true;
        }
        return false;
    }

    public static boolean isValidEmail(Activity act, String email) {
        boolean b = true;
        if (TextUtils.isEmpty(email)) {
            b = false;
            showDefaultAlert(act, act.getResources().getString(R.string.error_email_blank), null);
        } else {
            b = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
            if (!b) {
                showDefaultAlert(act, act.getString(R.string.enter_valid_email), null);
            }
        }
        return b;
    }

    public static boolean isValidMobileNumber(Activity act, String phone) {
        boolean b = true;
        if (TextUtils.isEmpty(phone)) {
            showDefaultAlert(act, act.getResources().getString(R.string.error_mobile_number_blank), null);
            b = false;
        }
        return b;
    }

    public static boolean isValidPassword(Activity act, String pass) {
        boolean b = true;
        if (TextUtils.isEmpty(pass)) {
            showDefaultAlert(act, act.getResources().getString(R.string.error_password_blank), null);
            b = false;
        }
        return b;
    }

    public static boolean checkStringsContainsOnlySpecialChar(String inputString) {
        boolean found = false;
        try {
            String splChrs = "/^[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]*$/";
            found = inputString.matches("[" + splChrs + "]+");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return found;
    }

    public static boolean isNumeric(String str) {
        try {
            long d = Long.parseLong(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public static String getUserRole(Activity activity) {
        String userRole = "";
        UserDetailsTemp userDetailsTemp;
        userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(SharedPref.getUserModelJSON(activity), UserDetailsTemp.class);
        if (userDetailsTemp != null && userDetailsTemp.Roles != null && userDetailsTemp.Roles.size() > 0) {
            userRole = userDetailsTemp.Roles.get(0);
        }
        return userRole;
    }

    public static Dialog rollSelectionDialog(final Activity activity, UserDetailsTemp userDetailsTemp1, JSONObject jsonObject) {
        userDetailsTemp=userDetailsTemp1;
        if (activity != null) {
            loginResponse=jsonObject;
            final List<String> rolls=userDetailsTemp.Roles;
            try {
                 dialogSelectRoll = new Dialog(activity,
                        android.R.style.Theme_Translucent_NoTitleBar);
                dialogSelectRoll.setContentView(R.layout.roll_selector_dialog);
                rvRollSelector= (RecyclerView) dialogSelectRoll.findViewById(R.id.rvRollSelector);
                ArrayList<SelectorModel> list=new ArrayList<>();
                for (int i = 0; i < rolls.size(); i++) {
                    SelectorModel selectorModel=new SelectorModel();
                    selectorModel.name=rolls.get(i);
                    list.add(selectorModel);
                }

                rvRollSelector.setLayoutManager(new LinearLayoutManager(activity));
                rvRollSelector.addItemDecoration(new DividerItemDecorationGray(activity));
                rvRollSelector.addOnItemTouchListener(new RecyclerItemClickListener(activity, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        JSONArray jsonArray=new JSONArray();
                        jsonArray.put(rolls.get(position));
                        try {
                            loginResponse.put(userDetailsTemp.KEY_ROLLS,jsonArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SharedPref.setLogin(activity, true);
                        SharedPref.setUserModelJSON(activity,loginResponse.toString());

                        setToken(activity);
                        Util.startActivityWithFinish(activity, DashboardActivity.class);
                    }
                }));

                RollSelectorAdapter rollSelectorAdapter =new RollSelectorAdapter(activity,list);
                rvRollSelector.setAdapter(rollSelectorAdapter);
                dialogSelectRoll.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dialogSelectRoll;
        } else {
            return null;
        }
    }
    private static void setToken(Activity activity) {
        try {
            JSONObject jsonObject = new JSONObject(SharedPref.getUserModelJSON(activity));
            JSONObject loginUserToken = jsonObject.getJSONObject(Constants.FLD_LOGIN_tOKAN);
            String token = loginUserToken.getString("token_type") + loginUserToken.getString("access_token");
            SharedPref.setLoginUserToken(activity, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void loadImageFromUrl(Context mContext, ImageView imgV, String imageUrl, int placeHolder)
    {
        Glide.with(mContext)
                .load(imageUrl)
                .placeholder(placeHolder)
                .priority(Priority.IMMEDIATE)
                .error(placeHolder)
                .fallback(placeHolder)
                .into(imgV);
    }


    public static void sendMail(Context ctx,String emailAddress,String subject,String text)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("plain/text");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ emailAddress });
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        i.putExtra(android.content.Intent.EXTRA_TEXT, text);
      ctx.  startActivity(Intent.createChooser(i, "Send email"));
    }
public static void makeCall(Activity activity,String phoneNumber)
{

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

//    if (ActivityCompat.checkSelfPermission(activity,
//            Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//        return;
//    }
            activity.startActivity(callIntent);




}

    public static void setCommonFormAct(Activity activity,String actionName,String headerTitle,String url) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(actionName!=null)
        intent.putExtra(Constants.FLD_ACTIONNAME,actionName);
        intent.putExtra(Constants.FLD_HEADER_TITLE,headerTitle);
        if(url!=null)
        intent.putExtra(Constants.FLD_URL,url);
        intent.setClass(activity, CommonFormAct.class);
        activity. startActivity(intent);
    }

    public static boolean checkAndRequestPermissions(Activity mContext) {
        int cameraPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA);
        int writeExternalPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readExternalPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        int recordAudioPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.RECORD_AUDIO);
        int phoneCallPermission = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
        }

        if (writeExternalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (readExternalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (recordAudioPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECORD_AUDIO);
        }

        if (phoneCallPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CALL_PHONE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            mContext.requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestPermissionForPhoto);
            return false;
        }
        return true;
    }
}
