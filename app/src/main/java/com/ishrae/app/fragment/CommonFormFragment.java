package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.ishrae.app.R;
import com.ishrae.app.activity.BaseAppCompactActivity;
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
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 24-04-2017.
 */

public class CommonFormFragment extends Fragment implements Callback{


    private TextView activityTitle;

    private NetworkResponse resp;

    private String formAction;
    private String headerTitle;
    private Bundle bundle;
    private WebView webViewForm;
    private String url;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.common_form_frg, container, false);
        initialize();
        return view;
    }



    private void initialize() {

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        webViewForm = (WebView)view. findViewById(R.id.webViewForm);


        bundle=getArguments();
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
           getActivity(). onBackPressed();
        }
        return true;
    }



    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {

        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                url = jsonObject.getString(Constants.FLD_RESPONSE_DATA);

                if (url.length() > 0) {

                    getActivity().runOnUiThread(new Runnable() {
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

       Util.progressDialog(getActivity(),"");

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
                Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        webViewForm.loadUrl(url);
    }

    private void getFormUrl(boolean showProIndicator) {
        if (Util.isDeviceOnline(getActivity(), true)) {
            JSONObject params = CmdFactory.getFormCmd(formAction);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GET_ACTION_AUTH_BY_MEMBER_URL, params.toString(), showProIndicator);
        }
    }


}
