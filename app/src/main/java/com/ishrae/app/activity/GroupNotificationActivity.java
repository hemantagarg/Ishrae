package com.ishrae.app.activity;


import android.app.Dialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.adapter.GroupNotificationAdapter;
import com.ishrae.app.adapter.PendingPostEventAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.GroupNotificationModel;
import com.ishrae.app.model.PendingPostEventListModel;
import com.ishrae.app.model.SharedPref;
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
import okhttp3.Response;

import static com.ishrae.app.R.id.eTextMemberIdFP;
import static com.ishrae.app.R.string.memberId;

/**
 * Created by Nss Solutions on 21-03-2017.
 */

public class GroupNotificationActivity extends AppCompatDialogFragment implements View.OnClickListener, okhttp3.Callback ,OnLoadMoreListener{

    private ImageView imgCloseGN;

    private CheckBox cbGN;
    private RecyclerView rvGroupNoti;

    private GroupNotificationAdapter groupNotiAdapter;

    private Context mContext;
    private int pageNumber;
    private ArrayList<GroupNotificationModel> notificationList;
    private NetworkResponse resp;
    private TextView txtNoGroupNoti;

    private   View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialogFullScreen);
         rootView = inflater.inflate(R.layout.group_notification, container,
                false);
        mContext=getActivity();
        initialize();
//        getDialog().getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_AppCompat_Light_Dialog_Alert);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.light_black)));
//        setContentView(R.layout.group_notification);
//        mContext = GroupNotificationActivity.this;
//
//    }

    private void initialize() {
      imgCloseGN= (ImageView) rootView.findViewById(R.id.imgCloseGN);
        rvGroupNoti = (RecyclerView) rootView. findViewById(R.id.rvGroupNoti);
      cbGN= (CheckBox)  rootView.findViewById(R.id.cbGN);

        txtNoGroupNoti= (TextView)  rootView.findViewById(R.id.txtNoGroupNoti);

        imgCloseGN.setOnClickListener(this);

        cbGN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPref.setDontShowMeAgain(getActivity(),b);
                dismiss();
            }
        });

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
    public void getGroupNotification(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity(), true)) {
            JSONObject params = CmdFactory.createGroupNotificationCmd( pageNumber);
             NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GROUP_NOTIFICATION_LIST_URL, params.toString(), showIndicator);
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgCloseGN) {
           dismiss();
        }
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
                            txtNoGroupNoti.setVisibility(View.VISIBLE);
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
        for(int i=0;i<10;i++)
        {
            GroupNotificationModel groupNotificationModel =new GroupNotificationModel();
            groupNotificationModel.Heading="Heading: "+i;
            groupNotificationModel.Message="Message: "+i;
            groupNotificationModel.StartDate="2017-07-28T18:30:00Z";
            groupNotificationModel.EndDate="2017-07-28T18:30:00Z";
            notificationList.add(groupNotificationModel);
        }

         groupNotiAdapter =new GroupNotificationAdapter(getActivity(),GroupNotificationActivity.this,notificationList,rvGroupNoti);
        rvGroupNoti.setAdapter(groupNotiAdapter);
        if(notificationList!=null&&notificationList.size()>0)
        {
            txtNoGroupNoti.setVisibility(View.GONE);
            rvGroupNoti.setVisibility(View.VISIBLE);
        }
        else
        {
            txtNoGroupNoti.setVisibility(View.VISIBLE);
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
