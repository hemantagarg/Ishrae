package com.ishrae.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.adapter.PollOfResultRowAdapterMain;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.PollOfDayResultModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 24-04-2017.
 */

public class CommonFormAct extends BaseAppCompactActivity implements Callback{

    private Toolbar toolbar;
    private TextView activityTitle;

    private NetworkResponse resp;

    private String formAction;
    private String headerTitle;
    private Bundle bundle;
    private WebView webViewForm;
    private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_form);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        webViewForm = (WebView) findViewById(R.id.webViewForm);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        bundle=getIntent().getExtras();
        if(bundle!=null&&bundle.containsKey(Constants.FLD_ACTIONNAME)) {
            formAction=bundle.getString(Constants.FLD_ACTIONNAME);
            headerTitle=bundle.getString(Constants.FLD_HEADER_TITLE);
            getFormUrl(true);
        }

            if(bundle!=null&&bundle.containsKey(Constants.FLD_URL)) {
                url=bundle.getString(Constants.FLD_URL);
                headerTitle=bundle.getString(Constants.FLD_HEADER_TITLE);
                startWebView();
            }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(TextUtils.isEmpty(headerTitle))
            headerTitle=getResources().getString(R.string.app_name);
        activityTitle.setText(headerTitle);
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
        super.onBackPressed();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(this, e);
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {

        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                url = jsonObject.getString(Constants.FLD_RESPONSE_DATA);

                if (url.length() > 0) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startWebView();
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startWebView() {
        WebSettings settings = webViewForm.getSettings();
        settings.setJavaScriptEnabled(true);
        webViewForm.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

       Util.progressDialog(CommonFormAct.this,"");

        webViewForm.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
              webViewForm.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Util.dismissProgressDialog();
                    }
                }, 3000);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(CommonFormAct.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        webViewForm.loadUrl(url);
    }

    private void getFormUrl(boolean showProIndicator) {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.getFormCmd(formAction);
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.GET_ACTION_AUTH_BY_MEMBER_URL, params.toString(), showProIndicator);
        }
    }


}
