package com.ishrae.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.activity.EmailDetailAct;
import com.ishrae.app.adapter.SocialItemListAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.TmpSocialModel;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class SocialFrag extends DialogFragment implements Callback {
    private View view;

    private TextView activityTitle;
    private RecyclerView rvSocial;

    private NetworkResponse resp;
    private int fromWhere;
    SocialItemListAdapter socialItemListAdapter;
    TmpSocialModel tmpSocialModel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, 0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_dialog_share, container, false);
        initialize();
        setAdapter();
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        rvSocial = (RecyclerView) view.findViewById(R.id.rvSocial);

        rvSocial.setLayoutManager(new LinearLayoutManager(getContext()));

        rvSocial.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (tmpSocialModel != null && tmpSocialModel.SocialMediaList != null && tmpSocialModel.SocialMediaList.size() > 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tmpSocialModel.SocialMediaList.get(position).URL));
                    startActivity(browserIntent);
                }
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
//        activityTitle.setText(getResources().getString(R.string.Social));
        getDialog().setTitle(getResources().getString(R.string.Social));
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
                                    try {

                                        JSONArray jsonArray = new JSONArray(resp.respStr);
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
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
        String values = SharedPref.getUserModelJSON(getActivity());
        tmpSocialModel = (TmpSocialModel) Util.getJsonToClassObject(values, TmpSocialModel.class);
        socialItemListAdapter = new SocialItemListAdapter(getActivity(), tmpSocialModel.SocialMediaList);
        rvSocial.setAdapter(socialItemListAdapter);
    }
}
