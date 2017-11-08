package com.ishrae.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.adapter.RenewalListAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.RenewalTmp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationGray;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class RenewalAct extends BaseAppCompactActivity implements Callback, OnLoadMoreListener, View.OnClickListener {

    private Toolbar toolbar;

    private TextView activityTitle;
    private TextView txtEmptyRNewal;
    private TextView txtUpdatInvoice;

    private RecyclerView rvRNewal;

    private LinearLayout llRNContainer;
    private LinearLayout llRnewal;

    ArrayList<RenewalTmp.RenewalListBean> itemsList;
    private RenewalListAdapter mAdapter;
    private NetworkResponse resp;
    private int fromWhere;
    private int pageNumber = 1;
    RenewalTmp renewalTmp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.renewal_main);
        initialize();
        customTextView();
    }


    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        txtEmptyRNewal = (TextView) findViewById(R.id.txtEmptyRNewal);
        txtUpdatInvoice = (TextView) findViewById(R.id.txtUpdatInvoice);

        rvRNewal = (RecyclerView) findViewById(R.id.rvRNewal);

        llRNContainer = (LinearLayout) findViewById(R.id.llRNContainer);
        llRnewal = (LinearLayout) findViewById(R.id.llRnewal);
        llRnewal.setOnClickListener(this);
        llRNContainer.setVisibility(View.GONE);
        rvRNewal.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        rvRNewal.setLayoutManager(new LinearLayoutManager(this));
        rvRNewal.addItemDecoration(new DividerItemDecorationGray(this));
        itemsList = new ArrayList<>();
        pageNumber = 1;
        getRenewalList(true);
        manageProfileVisibility();


    }


    public void manageProfileVisibility() {

        if (Util.getUserRole(this).equalsIgnoreCase(Constants.ROLL_MEMBER)) {
            txtUpdatInvoice.setVisibility(View.VISIBLE);
        }

    }

    public void getRenewalList(boolean showIndicator) {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.createRenewalListCmd("" + pageNumber);
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.GET_RENEWAL_LIST_URL, params.toString(), showIndicator);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Renewal));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(this, e);
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtEmptyRNewal.setVisibility(View.VISIBLE);
                rvRNewal.setVisibility(View.GONE);
                removeLoader();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {

            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);

                if (responseData.length() > 0) {
                    final String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;

                    if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                        renewalTmp = (RenewalTmp) Util.getJsonToClassObject(resp.respStr, RenewalTmp.class);
                        this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (fromWhere == 0) {

                                    removeLoader();
                                    itemsList.addAll(renewalTmp.RenewalList);
                                    setAdapter();
//
                                }
                            }
                        });
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAdapter() {

        if (itemsList != null && itemsList.size() > 0) {
            txtEmptyRNewal.setVisibility(View.GONE);
            llRNContainer.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new RenewalListAdapter(this, itemsList, rvRNewal, this);
                rvRNewal.setAdapter(mAdapter);
            } else {
                mAdapter.notifyItemInserted(itemsList.size());
            }
            if (Util.isDateBeforeSixMonth(itemsList.get(itemsList.size() - 1).RenewalDate))
                llRnewal.setVisibility(View.VISIBLE);
        } else {
            llRNContainer.setVisibility(View.GONE);
            rvRNewal.setVisibility(View.GONE);
            llRnewal.setVisibility(View.GONE);
        }
    }


    @Override
    public void onLoadMore() {
        if (renewalTmp != null && renewalTmp.TotalItems > itemsList.size()) {
            itemsList.add(null);

            mAdapter.notifyItemInserted(itemsList.size() - 1);

            pageNumber += 1;

            getRenewalList(false);
        }

    }

    private void customTextView() {
        String mainText = getResources().getString(R.string.To_download_the) + " " + AppUrls.UPDATE_PROFILE_URL;
        txtUpdatInvoice.setText(mainText);
        SpannableString ss = new SpannableString(txtUpdatInvoice.getText().toString());
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                String url = AppUrls.UPDATE_PROFILE_HTTP_URL;
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
        ss.setSpan(clickableSpan, getResources().getString(R.string.To_download_the).length() + 1, mainText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), getResources().getString(R.string.To_download_the).length() + 1, mainText.length(), 0);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.toolbar_bg)), getResources().getString(R.string.To_download_the).length() + 1, mainText.length(), 0);

        txtUpdatInvoice.setText(ss);
        txtUpdatInvoice.setMovementMethod(LinkMovementMethod.getInstance());
        txtUpdatInvoice.setHighlightColor(Color.TRANSPARENT);
    }

    public void removeLoader() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null && mAdapter.isLoaded()) {

                    itemsList.remove(itemsList.size() - 1);

                    mAdapter.notifyItemRemoved(itemsList.size());

                    mAdapter.setLoaded();

                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        int vId = view.getId();
        if (vId == R.id.llRnewal) {
            Util.setCommonFormAct(RenewalAct.this, Constants.FLD_ACTION_RENEWAL, getResources().getString(R.string.Renewal), null);
        }
    }
}
