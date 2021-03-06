package com.ishrae.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.adapter.ForumDetailListAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.ForumModel;
import com.ishrae.app.network.ForumDetailModelTemp;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
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

public class ReplyAct extends BaseAppCompactActivity implements Callback, View.OnClickListener, OnLoadMoreListener {

    private Toolbar toolbar;
    private TextView activityTitle;

    private RecyclerView rvForumDetail;

    private ImageView imgSend;

    private EditText eTxtMessage;

    private NetworkResponse resp;

    private int fromWhere;

    ArrayList<ForumModel> forumModelArrayList;

    ForumDetailListAdapter mAdapter;

    private ForumModel forumModel;
    private int pageNumber = 1;
    private Bundle bundle;
    ForumDetailModelTemp forumDetailModelTemp;
    boolean isRefreshed = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forum_detail);

        initialize();
    }


    private void initialize() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);

        rvForumDetail = (RecyclerView) findViewById(R.id.rvForumDetail);

        imgSend = (ImageView) findViewById(R.id.imgSend);

        eTxtMessage = (EditText) findViewById(R.id.eTxtMessage);

        imgSend.setOnClickListener(this);

        rvForumDetail.setLayoutManager(new LinearLayoutManager(this));

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        forumModelArrayList = new ArrayList<ForumModel>();

        bundle = getIntent().getExtras();

        if (bundle != null) {

            if (bundle.containsKey(Constants.FLD_RESPONSE_DATA)) {

                forumModel = (ForumModel) bundle.getSerializable(Constants.FLD_RESPONSE_DATA);

            }

            if (forumModel.ChildDescriptionDetails != null) {
                forumModelArrayList = forumModel.ChildDescriptionDetails;
            }
        }

        rvForumDetail.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

//                Intent intent = new Intent(getActivity(), EmailDetailAct.class);

//                intent.putExtra(Constants.FLD_RESPONSE_DATA, forumModelArrayList.get(position));

//                startActivity(intent);

            }
        }));

        setForumDetailAdapter();

    }

    @Override
    public void onResume() {
        super.onResume();

        activityTitle.setText(getResources().getString(R.string.Forum_Detail));
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

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (mAdapter != null && mAdapter.isLoaded()) {

                    forumModelArrayList.remove(forumModelArrayList.size() - 1);
                    mAdapter.notifyItemRemoved(forumModelArrayList.size());
                    mAdapter.setLoaded();
                }
            }
        });
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
                            if (mAdapter != null && mAdapter.isLoaded() && forumModelArrayList.get(forumModelArrayList.size() - 1) == null) {

                                forumModelArrayList.remove(forumModelArrayList.size() - 1);
                                mAdapter.notifyItemRemoved(forumModelArrayList.size());
                                mAdapter.setLoaded();

                            }

                            if (resp.respStr != null && resp.respStr.trim().length() > 0) {

                                if (fromWhere == 0) {

                                    forumDetailModelTemp = (ForumDetailModelTemp) Util.getJsonToClassObject(resp.respStr, ForumDetailModelTemp.class);

                                    if (forumDetailModelTemp != null && forumDetailModelTemp.ParentDescriptionDetails != null) {

                                        if (forumDetailModelTemp.ParentDescriptionDetails.size() <= 0) {

                                            setForumDetailAdapter();
                                        }

                                        for (int i = 0; i < forumDetailModelTemp.ParentDescriptionDetails.size(); i++) {

                                            forumModelArrayList.add(forumDetailModelTemp.ParentDescriptionDetails.get(i));
                                            setForumDetailAdapter();
                                        }
                                    }

                                } else if (fromWhere == 1) {
                                    eTxtMessage.setText("");
                                }
                            }


                        }
                    });

                }

            }


        } catch (JSONException e) {

            e.printStackTrace();

        }
    }

    private void setForumDetailAdapter() {

        if (mAdapter == null) {

            forumModelArrayList.add(0, forumModel);
            mAdapter = new ForumDetailListAdapter(this, forumModelArrayList, rvForumDetail, ReplyAct.this, ReplyAct.this, View.GONE);
            mAdapter.setLoaded();
            rvForumDetail.setAdapter(mAdapter);

        } else
            mAdapter.notifyItemInserted(forumModelArrayList.size());


    }

    @Override
    public void onClick(View v) {

        int vId = v.getId();

        if (vId == R.id.llReport) {

            ForumModel forumDetailModel = (ForumModel) v.getTag();
            Util.showCenterToast(this, getResources().getString(R.string.under_development));

        } else if (vId == R.id.llReportCB) {

            Util.showCenterToast(this, getResources().getString(R.string.under_development));

        } else if (vId == R.id.imgReplyFD) {

            ForumModel forumDetailModel = (ForumModel) v.getTag();
            Util.showCenterToast(this, getResources().getString(R.string.under_development));

        } else if (vId == R.id.imgReplyFDCB) {

            ForumModel forumDetailModel = (ForumModel) v.getTag();
            Util.showCenterToast(this, getResources().getString(R.string.under_development));

        } else if (vId == R.id.imgSend) {

            sendDiscussionMessage();

        }

    }

    private void sendDiscussionMessage() {

        if (Util.isDeviceOnline(this, true)) {

            fromWhere = 1;
            String message = eTxtMessage.getText().toString().trim();

            if (!TextUtils.isEmpty(message)) {

                if (!forumModelArrayList.isEmpty()) {

                    isRefreshed = true;
                    JSONObject params = CmdFactory.createDiscussionCmd("" + forumModel.TopicID, "" + forumModelArrayList.get(0).DescriptionId, message);
                    NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.CREATE_DISCUSSION_URL, params.toString(), true);

                }
            } else {

                Util.showCenterToast(this, getResources().getString(R.string.Please_enter_message));
            }
        }
    }


    @Override
    public void onLoadMore() {
//        if (forumDetailModelTemp != null && forumDetailModelTemp.TotalParentCount > forumModelArrayList.size()) {
//
//            forumModelArrayList.add(null);
//
//            mAdapter.notifyItemInserted(forumModelArrayList.size() - 1);
//
//            pageNumber += 1;
//
//            getDiscussionList(false);
//        }
//
    }
}
