package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ishrae.app.R;
import com.ishrae.app.adapter.PagerTabAdapter;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Util;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class MyISHAREFrag extends Fragment implements TabLayout.OnTabSelectedListener {

    private View view;

    private TextView activityTitle;

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private String[] tabArray;

    private WebView webView;

    private ProgressBar progressBar;

    private String url = AppUrls.ABOUT_US_URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cmn_tab_layout, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        webView = (WebView) view.findViewById(R.id.webView);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        tabArray = getActivity().getResources().getStringArray(R.array.my_ISHRAE_strings);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[2]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        //Creating our pager adapter
        PagerTabAdapter adapter = new PagerTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setOnTabSelectedListener(MyISHAREFrag.this);
            }
        });

        startWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.my_ishrae));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getPosition() == 0) {
            url = AppUrls.ABOUT_US_URL;
        } else if (tab.getPosition() == 1) {
            url = AppUrls.AFFILIATES_URL;
        } else if (tab.getPosition() == 2) {
            url = AppUrls.AIMS_URL;
        }
        progressBar.setVisibility(View.GONE);
        startWebView();
    }

    private void startWebView() {
        Util.progressDialog(getActivity(), "");
        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String value) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Util.dismissProgressDialog();
                        progressBar.setVisibility(View.GONE);
                    }
                }, 200);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getActivity(), "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        webView.loadUrl(url);
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //TODO..
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //TODO..
    }
}
