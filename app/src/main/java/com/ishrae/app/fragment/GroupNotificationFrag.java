package com.ishrae.app.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.activity.BaseAppCompactActivity;
import com.ishrae.app.adapter.GroupNotificationAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.GroupNotificationModel;
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
 * A simple {@link Fragment} subclass.
 */
public class GroupNotificationFrag extends Fragment implements Callback, OnLoadMoreListener, View.OnClickListener {

    private TextView activityTitle;
    private TextView txtEmptyGNL;

    private CheckBox cbGN;
    private RecyclerView rvGroupNoti;

    private GroupNotificationAdapter groupNotiAdapter;

    private Context mContext;
    private int pageNumber;
    private ArrayList<GroupNotificationModel> notificationList;
    private NetworkResponse resp;

    private   View view;


    public GroupNotificationFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_group_notification, container, false);
        mContext=getActivity();
        initialize();
        return view;
    }

    private void initialize() {

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        txtEmptyGNL = (TextView) view.findViewById(R.id.txtEmptyGNF);

        rvGroupNoti = (RecyclerView) view.findViewById(R.id.rvGNF);

        rvGroupNoti.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvGroupNoti.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                if (Util.getUserRole(CIQEventsListAct.this).equalsIgnoreCase(Constants.ROLL_CHAPTER)) {
//                    Intent in = new Intent(CIQEventsListAct.this, CIQEventsDetailAct.class);
//                    in.putExtra(Constants.CIQ_DATA, pendingPostEventList.get(position));
//                    startActivity(in);
//                }

            }
        }));

        getGroupNotification(true);
    }

    private void getGroupNotification(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity(), true)) {
            JSONObject params = CmdFactory.createGroupNotificationCmd( pageNumber);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GROUP_NOTIFICATION_LIST_URL, params.toString(), showIndicator);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.group_notification));
    }

    @Override
    public void onClick(View view) {

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

                    BaseAppCompactActivity.baseAppCompactActivity. runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<GroupNotificationModel>>() {
                                }.getType();
                                notificationList = gson.fromJson(resp.respStr.toString(), type);
                                setPendingPostAdapter();
                            }
                        }
                    });
                }
                else
                {
                    getActivity(). runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rvGroupNoti.setVisibility(View.GONE);
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setPendingPostAdapter() {
        groupNotiAdapter =new GroupNotificationAdapter(getActivity(),GroupNotificationFrag.this,notificationList,rvGroupNoti);
        rvGroupNoti.setAdapter(groupNotiAdapter);
        if(notificationList!=null&&notificationList.size()>0)
        {
            rvGroupNoti.setVisibility(View.VISIBLE);
        }
        else
        {
            rvGroupNoti.setVisibility(View.GONE);
        }
    }
    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
    }



    @Override
    public void onLoadMore() {

    }
}
