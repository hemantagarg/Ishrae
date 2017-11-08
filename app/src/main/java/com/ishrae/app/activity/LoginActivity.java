package com.ishrae.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.model.UserPersonalDetails;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.notification.FireBaseInstanceIDService;
import com.ishrae.app.tempModel.UserDetailsTemp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 19-03-2017.
 */

public class LoginActivity extends BaseAppCompactActivity implements View.OnClickListener, okhttp3.Callback {

    private TextView txtForgotPassword;
    private TextView txtRegister;
    private TextView txtSkip;

    private EditText eTextMemberId;
    private EditText eTextPassword;

    private Button btnLogin;

    private Context mContext;

    private String memberId;
    private String password;

    private NetworkResponse resp;

    private UserPersonalDetails UserDetails;
    UserDetailsTemp  userDetailsTemp;
    private static JSONObject loginResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mContext = LoginActivity.this;
        FireBaseInstanceIDService.getDeviceToken(this);
        initialize();
    }

    private void initialize() {
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtSkip = (TextView) findViewById(R.id.txtSkip);

        eTextMemberId = (EditText) findViewById(R.id.eTextMemberId);
        eTextPassword = (EditText) findViewById(R.id.eTextPassword);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        txtForgotPassword.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        txtSkip.setOnClickListener(this);

        customTextView();

        eTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        Util.hideSoftKeyboard(mContext, eTextPassword);
                        loginProcess();
                        break;
                }
                return true;
            }
        });


      /*  For Member*/
        eTextMemberId.setText("11280");
        eTextPassword.setText("abcd1234");

         /*  For Student*/
//        eTextMemberId.setText("S16011902");
//        eTextPassword.setText("lc00v5wqehd");

//        eTextMemberId.setText("19387");
//        eTextPassword.setText("akash1234");

        /* For Chapter*/
//        eTextMemberId.setText("ishraejaipur");
//        eTextPassword.setText("jaipur100");

      /*  For Admin*/
//        eTextMemberId.setText("admin");
//        eTextPassword.setText("jharsh16");
    /*  For CIQ*/
//        eTextMemberId.setText("ishraekolkata");
//       eTextPassword.setText("kolkata");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.txtForgotPassword) {
            Util.startActivity(mContext, ForgotPassword.class);

        } else if (view.getId() == R.id.btnLogin) {
            if (Util.isDeviceOnline(mContext)) {
                loginProcess();
            } else {
                Util.showDefaultAlert(mContext, getResources().getString(R.string.msg_internet), null);
            }
        } else if (view.getId() == R.id.txtSkip) {
            Util.startActivity(mContext, SkipActivity.class);
        }
    }

    private void loginProcess() {
        memberId = eTextMemberId.getText().toString().trim();
        password = eTextPassword.getText().toString().trim();
        if (memberId.length() == 0) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_member_id_blank), null);
        } else if (password.length() == 0) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_password_blank), null);
        } else {
            JSONObject params = CmdFactory.createLoginCmd(memberId, password);
            NetworkManager.requestForAPI(mContext, this, Constants.VAL_POST, AppUrls.LOGIN_URL, params.toString(), true);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;
                      userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), UserDetailsTemp.class);
                      userDetailsTemp.RolesTmp=userDetailsTemp.Roles;
                    loginResponse=resp.getJsonObject();
                    JSONArray jsonArrayTmp=new JSONArray();
                    if(userDetailsTemp.Roles!=null&&userDetailsTemp.Roles.size()>1)
                   {

                       for (int i = 0; i < userDetailsTemp.Roles.size(); i++) {
                           jsonArrayTmp.put(userDetailsTemp.Roles.get(i));
                           if(userDetailsTemp.Roles.get(i).equalsIgnoreCase(Constants.ROLL_MEMBER))
                           {
                               JSONArray jsonArray=new JSONArray();
                               jsonArray.put(userDetailsTemp.Roles.get(i));
                               try {
                                   loginResponse.put(userDetailsTemp.KEY_ROLLS,jsonArray);
                                   loginResponse.put(userDetailsTemp.KEY_ROLESTMP,jsonArrayTmp);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                               SharedPref.setLogin(LoginActivity.this, true);
                               SharedPref.setUserModelJSON(LoginActivity.this,loginResponse.toString());

                               setToken();
                               Util.startActivityWithFinish(LoginActivity.this, DashboardActivity.class);
                           }

                           else if(userDetailsTemp.Roles.get(i).equalsIgnoreCase(Constants.ROLL_CHAPTER))
                           {
                               JSONArray jsonArray=new JSONArray();
                               jsonArray.put(userDetailsTemp.Roles.get(i));
                               try {
                                   loginResponse.put(userDetailsTemp.KEY_ROLLS,jsonArray);
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                               SharedPref.setLogin(LoginActivity.this, true);
                               SharedPref.setUserModelJSON(LoginActivity.this,loginResponse.toString());

                               setToken();
                               Util.startActivityWithFinish(LoginActivity.this, DashboardActivity.class);
                           }

                       }
//                       runOnUiThread(new Runnable() {
//                           @Override
//                           public void run() {
//                               Util.rollSelectionDialog(LoginActivity.this,userDetailsTemp,resp.getJsonObject());
//                           }
//                       });

                   }
                   else {

                        for (int i = 0; i < userDetailsTemp.Roles.size(); i++) {
                            jsonArrayTmp.put(userDetailsTemp.Roles.get(i));
                        }
                        try {
                            loginResponse.put(userDetailsTemp.KEY_ROLESTMP,jsonArrayTmp);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       SharedPref.setLogin(mContext, true);
                       SharedPref.setUserModelJSON(mContext, resp.getJsonObject().toString());
                       setToken();
                       Util.startActivityWithFinish(mContext, DashboardActivity.class);
                   }
                }
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(jsonObject!=null)
                            Util.showDefaultAlert(mContext, jsonObject.getString(Constants.FLD_RESPONSE_MESSAGE).toString(), null);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setToken() {
        try {
            JSONObject jsonObject = new JSONObject(SharedPref.getUserModelJSON(mContext));
            JSONObject loginUserToken = jsonObject.getJSONObject(Constants.FLD_LOGIN_tOKAN);
            String token = loginUserToken.getString("token_type") + loginUserToken.getString("access_token");
            SharedPref.setLoginUserToken(mContext, token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(LoginActivity.this, e);
    }

    private void customTextView() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.register_text));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = AppUrls.REGISTRATION_URL;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 26, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 26, 37, 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 26, 37, 0);

        txtRegister.setText(ss);
        txtRegister.setMovementMethod(LinkMovementMethod.getInstance());
        txtRegister.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
