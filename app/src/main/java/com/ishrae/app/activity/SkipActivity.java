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

public class SkipActivity extends BaseAppCompactActivity implements View.OnClickListener {

    private Button btnLoginS;
    private Button btnRegS;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.skip_layout);
        mContext = SkipActivity.this;
        initialize();

    }

    private void initialize() {

        btnLoginS = (Button) findViewById(R.id.btnLoginS);
        btnRegS = (Button) findViewById(R.id.btnRegS);

        btnLoginS.setOnClickListener(this);
        btnRegS.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnRegS) {

            Util.startActivity(mContext, RegisterActivity.class);

        } else if (view.getId() == R.id.btnLoginS) {

            Util.startActivity(mContext, LoginNonMemberActivity.class);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
