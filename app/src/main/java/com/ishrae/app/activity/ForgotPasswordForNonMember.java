package com.ishrae.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
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
 * Created by Nss Solutions on 21-03-2017.
 */

public class ForgotPasswordForNonMember extends BaseAppCompactActivity implements View.OnClickListener, okhttp3.Callback {

    private EditText eTextMobile;

    private Button btnSubmit;

    private Context mContext;


    private LinearLayout llFPMain;

    private String typeValue;

    private NetworkResponse resp;

    private AlertDialog alertDialog = null;

    private String memberMob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_non_member);
        mContext = ForgotPasswordForNonMember.this;
        initialize();
    }

    private void initialize() {
        eTextMobile = (EditText) findViewById(R.id.eTextMobile);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);



        llFPMain = (LinearLayout) findViewById(R.id.llFPMain);

        btnSubmit.setOnClickListener(this);




        eTextMobile.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        Util.hideSoftKeyboard(mContext, eTextMobile);
                        forgotPasswordProcess();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            forgotPasswordProcess();
        }
    }

    private void forgotPasswordProcess() {
        memberMob = eTextMobile.getText().toString().trim();
        if (memberMob.length() == 0) {
            Util.showDefaultAlert(mContext, getResources().getString(R.string.error_mobile_number_blank), null);
        }
        else
        {
            forgotPass();
        }
    }

    private void forgotPass() {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.createForgotPasswordCmd(memberMob, Constants.Type_T3);
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.FORGOTPASSWORD_URL, params.toString(), true);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        handleResponse(response);
    }


    private void handleResponse(Response response) {
        final JSONObject jsonObject = Util.getObjectFromResponse(response);

            if (jsonObject != null ) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = null;
                        try {
                            message = jsonObject.getString(Constants.FLD_RESPONSE_MESSAGE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if(jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                                alertDialog = Util.showDefaultAlertWithOkClick(ForgotPasswordForNonMember.this, message, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                        finish();
                                    }
                                });
                            }
                            else
                            {
                                Util.showDefaultAlertWithOkClick(ForgotPasswordForNonMember.this, message,null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(this, e);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
