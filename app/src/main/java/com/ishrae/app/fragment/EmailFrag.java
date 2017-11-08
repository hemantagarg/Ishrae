package com.ishrae.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.activity.EmailDetailAct;
import com.ishrae.app.adapter.EmailAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
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
 * Created by Nss Solutions on 23-03-2017.
 */

public class EmailFrag extends Fragment implements Callback {

    private View view;

    private TextView activityTitle;
    private TextView txtEmptyEmail;
    private RecyclerView rvEmailNotifications;

    private NetworkResponse resp;
    private int fromWhere;
    ArrayList<EmailModel> emailModelArrayList;
    EmailAdapter emailListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_email, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        txtEmptyEmail = (TextView) view.findViewById(R.id.txtEmptyEmail);

        rvEmailNotifications = (RecyclerView) view.findViewById(R.id.rvEmailNotifications);

        rvEmailNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        rvEmailNotifications.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), EmailDetailAct.class);
                intent.putExtra(Constants.FLD_RESPONSE_DATA, emailModelArrayList.get(position));
                startActivity(intent);
            }
        }));

        getEmailList();
    }

    @Override
    public void onResume() {
        super.onResume();

        activityTitle.setText(getResources().getString(R.string.email));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
    }


    private void getEmailList() {

        if (Util.isDeviceOnline(getActivity(), true)) {

            JSONObject params = CmdFactory.createEmailNotiCmd();
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GETCURRENTMAILS_URL, params.toString(), true);

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

                    if (resp.respStr != null && resp.respStr.trim().length() > 0) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (fromWhere == 0) {
                                    try {
                                        emailModelArrayList = new ArrayList<EmailModel>();
                                        JSONArray jsonArray = new JSONArray(resp.respStr);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            EmailModel emailModel = (EmailModel) Util.getJsonToClassObject(jsonArray.getJSONObject(i).toString(), EmailModel.class);
                                            emailModelArrayList.add(emailModel);
                                            setEmailAdapter();
                                        }
                                        if(emailModelArrayList.size()>0)
                                        {
                                            txtEmptyEmail.setVisibility(View.GONE);
                                            rvEmailNotifications.setVisibility(View.VISIBLE);
                                        }
                                        else
                                        {
                                            txtEmptyEmail.setVisibility(View.VISIBLE);
                                            rvEmailNotifications.setVisibility(View.GONE);
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

    private void setEmailAdapter() {

        emailListAdapter = new EmailAdapter(getActivity(), emailModelArrayList);
        rvEmailNotifications.setAdapter(emailListAdapter);
    }
}
