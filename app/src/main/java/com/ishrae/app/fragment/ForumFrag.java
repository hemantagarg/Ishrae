package com.ishrae.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.activity.CreateTopicAct;
import com.ishrae.app.activity.ForumDetailAct;
import com.ishrae.app.adapter.ForumListAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.ForumModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.ForumModelTemp;
import com.ishrae.app.tempModel.UserDetailsTemp;
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

public class ForumFrag extends Fragment implements Callback, OnLoadMoreListener, View.OnClickListener {

    private View view;

    private TextView activityTitle;

    private ImageView activityPlus;

    private TextView txtNoDataCate;

    private TextView txtAllDiscuT;

    private TextView txtLatestDisc;

    RecyclerView rvForum;

    ArrayList<ForumModel> list;

    private NetworkResponse resp;
    private int fromWhere;

    private int pageNumber = 1;

    private ForumListAdapter mAdapter;
    ForumModelTemp forumModelTemp;

    private UserDetailsTemp userDetailsTemp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.forum, container, false);
            mAdapter = null;
            initialize();
        }
        return view;
    }

    private void initialize() {
        userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(SharedPref.getUserModelJSON(getActivity()), UserDetailsTemp.class);

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        activityPlus = (ImageView) getActivity().findViewById(R.id.activityPlus);

        txtNoDataCate = (TextView) view.findViewById(R.id.txtNoDataCate);

        txtAllDiscuT = (TextView) view.findViewById(R.id.txtAllDiscuT);

        txtLatestDisc = (TextView) view.findViewById(R.id.txtLatestDisc);

        rvForum = (RecyclerView) view.findViewById(R.id.rvForum);


        activityPlus.setOnClickListener(this);

        txtAllDiscuT.setOnClickListener(this);

        txtLatestDisc.setOnClickListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);


        rvForum.setLayoutManager(gridLayoutManager);

        rvForum.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(getActivity(), ForumDetailAct.class);

                intent.putExtra(Constants.FLD_RESPONSE_DATA, list.get(position));

                getActivity().startActivityForResult(intent, Constants.REFRESH);

            }
        }));

        list = new ArrayList<>();
        pageNumber = 1;
        getTopicList(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constants.REFRESH) {
            if (mAdapter != null) {

                list.clear();

                mAdapter = null;

                pageNumber = 1;

                getTopicList(true);

            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        activityTitle.setText(getResources().getString(R.string.Forum));
        if (userDetailsTemp != null && userDetailsTemp.Roles != null && userDetailsTemp.Roles.size() > 0) {
            if (userDetailsTemp.Roles.get(0).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)||userDetailsTemp.Roles.get(0).equalsIgnoreCase(Constants.ROLL__STUDENT)) {
                activityPlus.setVisibility(View.GONE);
            } else {
                activityPlus.setVisibility(View.VISIBLE);
            }
        } else {
            activityPlus.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStop() {
        activityPlus.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    public void onFailure(Call call, IOException e) {

        Util.manageFailure(getActivity(), e);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                txtNoDataCate.setVisibility(View.VISIBLE);

                rvForum.setVisibility(View.GONE);


                if (mAdapter != null && mAdapter.isLoaded()) {

                    list.remove(list.size() - 1);

                    mAdapter.notifyItemRemoved(list.size());

                    mAdapter.setLoaded();
                }
            }
        });

    }


    public void getTopicList(boolean showIndicator) {

        JSONObject params = CmdFactory.getTopicList("" + pageNumber);

        NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GET_TOPIC_LIST_URL, params.toString(), showIndicator);
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

                    if (resp.respStr != null && resp.respStr.trim().length() > 0) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (fromWhere == 0) {

                                    if (mAdapter != null && mAdapter.isLoaded()) {

                                        list.remove(list.size() - 1);

                                        mAdapter.notifyItemRemoved(list.size());

                                        mAdapter.setLoaded();
                                    }
                                    forumModelTemp = (ForumModelTemp) Util.getJsonToClassObject(resp.respStr, ForumModelTemp.class);
                                    if (forumModelTemp != null && forumModelTemp.DiscussionTopicList != null) {
                                        list.addAll(forumModelTemp.DiscussionTopicList);
                                        setAdapter();
                                    }
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

        if (list != null && list.size() > 0) {

            txtNoDataCate.setVisibility(View.GONE);

            rvForum.setVisibility(View.VISIBLE);

            if (mAdapter == null) {

                mAdapter = new ForumListAdapter(getActivity(), list, rvForum, this);

                rvForum.setAdapter(mAdapter);

            } else {
                mAdapter.notifyItemInserted(list.size());
            }
        } else {

            txtNoDataCate.setVisibility(View.VISIBLE);

            rvForum.setVisibility(View.GONE);
        }
    }


    @Override
    public void onLoadMore() {
        if (forumModelTemp != null && forumModelTemp.TotalItems > list.size()) {
            if (mAdapter != null) {

                list.add(null);

                mAdapter.notifyItemInserted(list.size() - 1);

                pageNumber += 1;

                getTopicList(false);
            }
        }
    }

    private void unselectAllFields() {

        boolean b = false;

        txtLatestDisc.setSelected(b);

        txtAllDiscuT.setSelected(b);

        txtLatestDisc.setEnabled(b ? false : true);

        txtAllDiscuT.setEnabled(b ? false : true);
    }

    @Override
    public void onClick(View v) {

        int vId = v.getId();

        if (vId == R.id.txtAllDiscuT) {

            unselectAllFields();

            txtAllDiscuT.setSelected(true);

            txtAllDiscuT.setEnabled(false);

            getTopicList(true);

        } else if (vId == R.id.txtLatestDisc) {

            unselectAllFields();

            txtLatestDisc.setSelected(true);

            txtLatestDisc.setEnabled(false);
        } else if (vId == R.id.activityPlus) {
            Intent intent = new Intent(getActivity(), CreateTopicAct.class);
            getActivity().startActivityForResult(intent, Constants.REFRESH);

        }
    }
}
