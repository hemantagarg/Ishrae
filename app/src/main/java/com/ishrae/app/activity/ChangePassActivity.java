package com.ishrae.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 30-03-2017.
 */

public class ChangePassActivity extends BaseAppCompactActivity implements View.OnClickListener, Callback {

    private Toolbar toolbar;
    private TextView activityTitle;

    private EditText eTextOldPass;
    private EditText eTextNewPass;
    private EditText eTextConfNewPass;

    private Button btnChangePass;

    private Context mContext;

    private String oldPass;
    private String newPass;
    private String confNewPass;

    private JSONObject loginUserToken;

    private String loginToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        mContext = ChangePassActivity.this;
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);

        eTextOldPass = (EditText) findViewById(R.id.eTextOldPass);
        eTextNewPass = (EditText) findViewById(R.id.eTextNewPass);
        eTextConfNewPass = (EditText) findViewById(R.id.eTextConfNewPass);

        btnChangePass = (Button) findViewById(R.id.btnChangePass);

        btnChangePass.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        activityTitle.setText(getResources().getString(R.string.change_password));

        eTextConfNewPass.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        Util.hideSoftKeyboard(mContext, eTextConfNewPass);
                        btnChangePass();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnChangePass) {
            btnChangePass();
        }
    }

    private void btnChangePass() {
        oldPass = eTextOldPass.getText().toString();
        newPass = eTextNewPass.getText().toString();
        confNewPass = eTextConfNewPass.getText().toString();

        if (oldPass.length() == 0) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_old_pass_blank), null);

        } else if (newPass.length() == 0) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_new_pass_blank), null);

        } else if (confNewPass.length() == 0) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_conf_new_blank), null);

        } else if (!confNewPass.equals(newPass)) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_conf_new_same), null);

        } else {
            changePassword();
        }
    }

    private void changePassword() {
        if (Util.isDeviceOnline(mContext)) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createChangePasswordCmd(oldPass, newPass, loginToken);
            NetworkManager.requestForAPI(mContext, this, Constants.VAL_POST, AppUrls.CHANGE_PASSWORD_URL, params.toString(), true);
        } else {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getLoginUserToken() {
        try {
            JSONObject jsonObject = new JSONObject(SharedPref.getUserModelJSON(mContext));
            loginUserToken = jsonObject.getJSONObject(Constants.FLD_LOGIN_TOKAN);
            loginToken = loginUserToken.getString("token_type") + loginUserToken.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
//                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
//                if (responseData.length() > 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        eTextOldPass.setText("");
                        eTextNewPass.setText("");
                        eTextConfNewPass.setText("");
//                        try {
                            Util.showDefaultAlertWithOkClick(mContext, getResources().getString(R.string.Password_changed_successfully), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Util.logout(ChangePassActivity.this);
                                }
                            });
//                            Util.showCenterToast(ChangePassActivity.this, jsonObject.getString(Constants.FLD_RESPONSE_MESSAGE));
//                            finish();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
//                }
            } else
                Util.showDefaultAlert(mContext, jsonObject.getString(Constants.FLD_MESSAGE), null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.dismissProgressDialog();
        //TODO..
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
