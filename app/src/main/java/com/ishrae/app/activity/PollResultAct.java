package com.ishrae.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationGray;
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

public class PollResultAct extends BaseAppCompactActivity implements Callback, View.OnClickListener, OnLoadMoreListener {

    private Toolbar toolbar;
    private TextView activityTitle;
    private TextView txtEmptyPollResult;



    private RecyclerView rvPollOfDayResult;

    private NetworkResponse resp;

    private int fromWhere;
    private boolean isRefreshed;
    private int pageNumber = 1;

    private ArrayList<PollOfDayResultModel> pendingPostEventList;
    private PollOfResultRowAdapterMain pendingPostEventAdapter;
//    CI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poll_of_day_result);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        txtEmptyPollResult = (TextView) findViewById(R.id.txtEmptyPollResult);

        rvPollOfDayResult = (RecyclerView) findViewById(R.id.rvPollOfDayResult);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        rvPollOfDayResult.setLayoutManager(new LinearLayoutManager(this));

        rvPollOfDayResult.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        }));
        getOpenionPollList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Poll_Result));
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
        if (isRefreshed)
            setResult(Constants.REFRESH);
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
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);

                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<PollOfDayResultModel>>() {
                                }.getType();
                                pendingPostEventList = gson.fromJson(resp.respStr.toString(), type);
                                setPollStatusAdapter();
                            }
                        }
                    });
                }
            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPollStatusAdapter();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPollStatusAdapter() {
        if(pendingPostEventList!=null&&pendingPostEventList.size()>0)
        {
            txtEmptyPollResult.setVisibility(View.GONE);
            rvPollOfDayResult.setVisibility(View.VISIBLE);
            pendingPostEventAdapter =new PollOfResultRowAdapterMain(PollResultAct.this,pendingPostEventList,rvPollOfDayResult,PollResultAct.this,PollResultAct.this);
            rvPollOfDayResult.setAdapter(pendingPostEventAdapter);
        }
        else {
            rvPollOfDayResult.setVisibility(View.GONE);
            txtEmptyPollResult.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if(vId==R.id.btnPostEventDetail)
        {
            Intent intent = new Intent(PollResultAct.this, CIQEventsDetailAct.class);
            startActivityForResult(intent, Constants.REFRESH);
        }
    }

    private void getOpenionPollList(boolean showProIndicator) {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.getOpenionPollStatusCmd(""+pageNumber);
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.GET_OPINION_POLL_STATUS_LIST_URL, params.toString(), showProIndicator);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constants.REFRESH) {
            getOpenionPollList(true);
        }

    }

    @Override
    public void onLoadMore() {
//        if (forumModelTemp != null && forumModelTemp.TotalItems > pendingPostEventList.size()) {
//            if (pendingPostEventAdapter != null) {
//
//                pendingPostEventList.add(null);
//
//                pendingPostEventAdapter.notifyItemInserted(pendingPostEventList.size() - 1);
//
//                pageNumber += 1;
//
//                getOpenionPollList(false);
//            }
//        }
    }
}
