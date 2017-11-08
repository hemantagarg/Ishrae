package com.ishrae.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.activity.EmailDetailAct;
import com.ishrae.app.adapter.CategoryAdapter;
import com.ishrae.app.adapter.NotificationListAdapter;
import com.ishrae.app.adapter.SubCategoryAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.CategoriesModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.NotificationModelTmp;
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
 * Created by Nss Solutions on 22-03-2017.
 */

public class NotificationFrag extends Fragment implements Callback, OnLoadMoreListener, View.OnClickListener {

    private View view;
    private TextView activityTitle;
    private TextView txtEmptyNL;


    private RecyclerView rvNL;

    private ArrayList<NotificationModelTmp.FcmNotificationEntitysBean> notificationList;
    private NotificationListAdapter mAdapter;
    private NetworkResponse resp;

    private int pageNumber = 1;
    private int categoryId;
    /**
     * 0 = category list
     * 1 = product list
     */
    private int fromWhere = 0;

    private NotificationModelTmp notificationModelTmp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.notifications, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        txtEmptyNL = (TextView) view.findViewById(R.id.txtEmptyNL);

        rvNL = (RecyclerView) view.findViewById(R.id.rvNL);
        mAdapter = null;
        notificationList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rvNL.setLayoutManager(gridLayoutManager);
        rvNL.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), EmailDetailAct.class);
                intent.putExtra(Constants.FLD_NOTIFICATION_DETAIL, notificationList.get(position));
                startActivity(intent);
            }
        }));

        getNotificationsList(true);
    }


    private void getNotificationsList(boolean showProDialog) {
        if (Util.isDeviceOnline(getActivity())) {
            fromWhere = 0;
            JSONObject params = CmdFactory.createGetNotificationListCmd("" + pageNumber, "" + categoryId);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.FCM_NOTIFICATION_LIST_URL, params.toString(), showProDialog);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.notification));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtEmptyNL.setVisibility(View.VISIBLE);
                rvNL.setVisibility(View.GONE);
                if (mAdapter != null && mAdapter.isLoaded()) {
                    notificationList.remove(notificationList.size() - 1);
                    mAdapter.notifyItemRemoved(notificationList.size());
                    mAdapter.setLoaded();
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        handleResponse(response);
    }

    private void handleResponse(final Response response) {
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
                                    if (mAdapter != null && mAdapter.isLoaded() && notificationList.size() > 0) {
                                        notificationList.remove(notificationList.size() - 1);
                                        mAdapter.notifyItemRemoved(notificationList.size());
                                        mAdapter.setLoaded();
                                    }
                                    notificationModelTmp = (NotificationModelTmp) Util.getJsonToClassObject(resp.respStr, NotificationModelTmp.class);
                                    if (notificationModelTmp != null && notificationModelTmp.FcmNotificationEntitys != null)
                                        notificationList.addAll(notificationModelTmp.FcmNotificationEntitys);
                                    setAdapter();

                                }
                            }
                        });
                    }

                }
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }


    private void setAdapter() {
        if (notificationList != null && notificationList.size() > 0) {
            txtEmptyNL.setVisibility(View.GONE);
            rvNL.setVisibility(View.VISIBLE);
            if (mAdapter == null) {
                mAdapter = new NotificationListAdapter(getActivity(), notificationList, NotificationFrag.this, rvNL, this);
                rvNL.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            txtEmptyNL.setVisibility(View.VISIBLE);
            rvNL.setVisibility(View.GONE);
        }
    }


    @Override
    public void onLoadMore() {
        if ((mAdapter != null && notificationModelTmp != null && notificationModelTmp.TotalItems > notificationList.size())) {

            notificationList.add(null);
            mAdapter.notifyItemInserted(notificationList.size() - 1);
            pageNumber += 1;
            getNotificationsList(false);
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();

    }
}