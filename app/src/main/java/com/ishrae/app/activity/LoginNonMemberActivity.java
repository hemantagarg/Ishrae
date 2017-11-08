package com.ishrae.app.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

public class LoginNonMemberActivity extends BaseAppCompactActivity implements View.OnClickListener, okhttp3.Callback {

    private TextView txtForgotPasswordNL;
    private TextView txtRegisterNL;


    private EditText eTextMNumberNL;
    private EditText eTextPasswordNL;

    private Button btnLoginNL;

    private Context mContext;

    private String mobileNumber;
    private String password;

    private NetworkResponse resp;

    private UserPersonalDetails UserDetails;
    private JSONObject loginResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_non_member);
        mContext = LoginNonMemberActivity.this;
        initialize();
    }

    private void initialize() {
        txtForgotPasswordNL = (TextView) findViewById(R.id.txtForgotPasswordNL);
        txtRegisterNL = (TextView) findViewById(R.id.txtRegisterNL);


        eTextMNumberNL = (EditText) findViewById(R.id.eTextMNumberNL);
        eTextPasswordNL = (EditText) findViewById(R.id.eTextPasswordNL);

        btnLoginNL = (Button) findViewById(R.id.btnLoginNL);

        txtForgotPasswordNL.setOnClickListener(this);
        btnLoginNL.setOnClickListener(this);

        customTextView();

        eTextPasswordNL.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        Util.hideSoftKeyboard(mContext, eTextPasswordNL);
                        loginProcess();
                        break;
                }
                return true;
            }
        });
//        eTextMNumberLN.setText("11280");
//        eTextPasswordNL.setText("abcd1234");
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.txtForgotPasswordNL) {

            Util.startActivity(mContext, ForgotPasswordForNonMember.class);

        } else if (view.getId() == R.id.btnLoginNL) {

            if (Util.isDeviceOnline(mContext)) {

                loginProcess();

            } else {

                Util.showDefaultAlert(mContext, getResources().getString(R.string.msg_internet), null);

            }

        } else if (view.getId() == R.id.txtSkip) {
            Util.startActivity(mContext, RegisterActivity.class);
        }
    }

    private void loginProcess() {
        mobileNumber = eTextMNumberNL.getText().toString().trim();
        password = eTextPasswordNL.getText().toString().trim();

        if (Util.isValidMobileNumber(this, mobileNumber) && Util.isValidPassword(this, password)) {
            JSONObject params = CmdFactory.createLoginCmd(mobileNumber, password);
            NetworkManager.requestForAPI(mContext, this, Constants.VAL_POST, AppUrls.NON_MEMBER_LOGIN_URL, params.toString(), true);
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
                    UserDetailsTemp userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), UserDetailsTemp.class);
                    userDetailsTemp.RolesTmp=userDetailsTemp.Roles;

                     loginResponse = resp.getJsonObject();
                    JSONArray jsonArrayTmp=new JSONArray();
                    for (int i = 0; i < userDetailsTemp.Roles.size(); i++) {
                        jsonArrayTmp.put(userDetailsTemp.Roles.get(i));
                    }
                    try {
                        loginResponse.put(userDetailsTemp.KEY_ROLESTMP,jsonArrayTmp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SharedPref.setLogin(mContext, true);
                    SharedPref.setUserModelJSON(mContext, loginResponse.toString());
                    setToken();
                    Util.startActivityWithFinish(mContext, DashboardActivity.class);

                }
            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
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

        Util.manageFailure(LoginNonMemberActivity.this, e);
    }

    private void customTextView() {

        SpannableString ss = new SpannableString(getResources().getString(R.string.register_text_for_non_member));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Util.startActivity(mContext, RegisterActivity.class);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 26, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 26, 34, 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 26, 34, 0);

        txtRegisterNL.setText(ss);
        txtRegisterNL.setMovementMethod(LinkMovementMethod.getInstance());
        txtRegisterNL.setHighlightColor(Color.TRANSPARENT);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
