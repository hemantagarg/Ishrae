package com.ishrae.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.adapter.PendingPostEventAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.CategoriesModel;
import com.ishrae.app.model.PendingPostEventListModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;
import com.weiwangcn.betterspinner.library.BetterSpinner;

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
 * Created by Nss Solutions on 23-03-2017.
 */

public class CIQEventsListAct extends BaseAppCompactActivity implements Callback, View.OnClickListener, OnLoadMoreListener {

    private Toolbar toolbar;
    private TextView activityTitle;
    private TextView txtNoEvents;

    private Button btnPostEventDetail;

    private BetterSpinner bsAllType;
    private BetterSpinner bsDate;
    private BetterSpinner bsReasons;
    private BetterSpinner bsChapters ;

    private RecyclerView rvPendEvent;

    private NetworkResponse resp;

    private int fromWhere;
    private boolean isRefreshed;
    private int pageNumber = 1;

    private ArrayList<PendingPostEventListModel> pendingPostEventList;
    private PendingPostEventAdapter pendingPostEventAdapter;
//    CI

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ciq_events);
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        btnPostEventDetail = (Button) findViewById(R.id.btnPostEventDetail);
        bsAllType = (BetterSpinner) findViewById(R.id.bsAllType);
        txtNoEvents = (TextView) findViewById(R.id.txtNoEvents);
       bsDate= (BetterSpinner) findViewById(R.id.bsDate);
         bsReasons= (BetterSpinner) findViewById(R.id.bsReasons);
        bsChapters = (BetterSpinner) findViewById(R.id.bsChapters);
        rvPendEvent = (RecyclerView) findViewById(R.id.rvPendEvent);

        btnPostEventDetail.setOnClickListener(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        rvPendEvent.setLayoutManager(new LinearLayoutManager(this));

        rvPendEvent.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (Util.getUserRole(CIQEventsListAct.this).equalsIgnoreCase(Constants.ROLL_CHAPTER)) {
                    Intent in = new Intent(CIQEventsListAct.this, CIQEventsDetailAct.class);
                    in.putExtra(Constants.CIQ_DATA, pendingPostEventList.get(position));
                    startActivity(in);
                }

            }
        }));
        getPendingPostEvents(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Event_List));
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
                                Type type = new TypeToken<List<PendingPostEventListModel>>() {
                                }.getType();
                                pendingPostEventList = gson.fromJson(resp.respStr.toString(), type);
//                                if(pendingPostEventList==null||pendingPostEventList.size()<=0)
//                                {
//                                    pendingPostEventList=new ArrayList<PendingPostEventListModel>();
//                                    for (int i=0;i<10;i++)
//                                    {
//                                        PendingPostEventListModel pendingPostEventListModel=new PendingPostEventListModel();
//                                        pendingPostEventListModel.id=i;
//                                        pendingPostEventListModel.type="type: "+i;
//                                        pendingPostEventListModel.heading="heading: "+i;
//                                        pendingPostEventListModel.venue="venue: "+i;
//                                        pendingPostEventListModel.start="05/Jun/2017";
//                                        pendingPostEventListModel.end="05/Jun/2017";
//                                        pendingPostEventListModel.url="";
//                                        pendingPostEventListModel.available=i;
//                                        pendingPostEventListModel.createdBy="createdBy: "+i;
//                                        pendingPostEventListModel.updatedBy="updatedBy: "+i;
//                                        pendingPostEventListModel.description="description: "+i;
//                                        pendingPostEventList.add(pendingPostEventListModel);
//                                    }
//
//                                }
                                setPendingPostAdapter();

//                                Util.showCenterToast(CIQEventsListAct.this, Util.getMessageFromJObj(jsonObject));
//                                isRefreshed = true;
                            }
                        }
                    });
                }
                else
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtNoEvents.setVisibility(View.VISIBLE);
                            rvPendEvent.setVisibility(View.GONE);
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPendingPostAdapter() {
        pendingPostEventAdapter =new PendingPostEventAdapter(CIQEventsListAct.this,pendingPostEventList,rvPendEvent,CIQEventsListAct.this,CIQEventsListAct.this);
        rvPendEvent.setAdapter(pendingPostEventAdapter);
        if(pendingPostEventList!=null&&pendingPostEventList.size()>0)
        {
            txtNoEvents.setVisibility(View.GONE);
            rvPendEvent.setVisibility(View.VISIBLE);
        }
        else
        {
            txtNoEvents.setVisibility(View.VISIBLE);
            rvPendEvent.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if(vId==R.id.btnPostEventDetail)
        {
            Intent intent = new Intent(CIQEventsListAct.this, CIQEventsDetailAct.class);
            startActivityForResult(intent, Constants.REFRESH);
        }
    }

    private void getPendingPostEvents(boolean showProIndicator) {
        if (Util.isDeviceOnline(this, true)) {
                JSONObject params = CmdFactory.getPendingPostCmd(""+pageNumber);
                NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.GET_PENDING_POST_EVENT_LIST_URL, params.toString(), showProIndicator);
            }
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constants.REFRESH) {
            getPendingPostEvents(true);
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
//                getPendingPostEvents(false);
//            }
//        }
    }
}
