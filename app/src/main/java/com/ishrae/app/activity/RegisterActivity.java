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
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 19-03-2017.
 */

public class RegisterActivity extends BaseAppCompactActivity implements View.OnClickListener, okhttp3.Callback {

    private EditText eTextUSerNameR;
    private EditText eTextMNumberR;
    private EditText eTextEmailR;
    private EditText eTextPasswordR;

    private Button btnRegister;

    private TextView txtLogin;

    private Context mContext;

    private String name;
    private String mobileNumber;
    private String email;
    private String password;

    private String countryId = "1";
    private String stateId = "42";
    private String cityId = "432";
    private String address = "Jaipur";
    private String location = "Jaipur";
    private String latitude = "25.25";
    private String logitude = "76.76";


    private NetworkResponse resp;

    private UserPersonalDetails UserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        mContext = RegisterActivity.this;
        initialize();
    }

    private void initialize() {

        txtLogin = (TextView) findViewById(R.id.txtLogin);

        eTextUSerNameR = (EditText) findViewById(R.id.eTextUSerNameR);
        eTextMNumberR = (EditText) findViewById(R.id.eTextMNumberR);
        eTextEmailR = (EditText) findViewById(R.id.eTextEmailR);
        eTextPasswordR = (EditText) findViewById(R.id.eTextPasswordR);

        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(this);


        eTextPasswordR.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        Util.hideSoftKeyboard(mContext, eTextPasswordR);
                        registerProcess();
                        break;
                }
                return true;
            }
        });
        customTextView();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.txtForgotPassword) {

            Util.startActivity(mContext, ForgotPassword.class);

        } else if (view.getId() == R.id.btnRegister) {

            if (Util.isDeviceOnline(mContext, true)) {
                registerProcess();
            }

        }
    }

    private void registerProcess() {

        name = eTextUSerNameR.getText().toString().trim();
        mobileNumber = eTextMNumberR.getText().toString().trim();
        email = eTextEmailR.getText().toString().trim();
        password = eTextPasswordR.getText().toString().trim();

        if (Util.isValidName(this, name) && Util.isValidMobileNumber(this, mobileNumber) && Util.isValidEmail(this, email) && Util.isValidPassword(this, password)) {
            JSONObject params = CmdFactory.createRegisterCmd(name, mobileNumber, email, password, countryId, stateId, cityId, address, location, latitude, logitude);
            NetworkManager.requestForAPI(mContext, this, Constants.VAL_POST, AppUrls.REGISTER_NON_MEMBER_URL, params.toString(), true);
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
                    SharedPref.setLogin(mContext, true);
                    SharedPref.setUserModelJSON(mContext, resp.getJsonObject().toString());
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
        Util.manageFailure(RegisterActivity.this, e);
    }

    private void customTextView() {
String loginText=getResources().getString(R.string.Already_have_account);
        SpannableString ss = new SpannableString(loginText);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Util.startActivity(RegisterActivity.this, LoginNonMemberActivity.class);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        ss.setSpan(clickableSpan, 22, loginText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 22, loginText.length(), 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 22, loginText.length(), 0);

        txtLogin.setText(ss);
        txtLogin.setMovementMethod(LinkMovementMethod.getInstance());
        txtLogin.setHighlightColor(Color.TRANSPARENT);
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
