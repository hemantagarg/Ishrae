package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class CmnRcyViewFrag extends Fragment implements Callback {

    private View view;

    private TextView activityTitle;

    private int value;

    private JSONObject loginUserToken;

    private int pageNo = 1;
    private int recordPerPage = 10;
    private int financialYearId = 69;

    private NetworkResponse resp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cmn_rcy_view, container, false);
        value = getArguments().getInt("value");
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        if (value == 0) {
            //TODO.. BOG
            activityTitle.setText(getResources().getString(R.string.bog));
            getBOGList();

        } else if (value == 1) {
            //TODO.. REGIONAL DIRECTOR
            activityTitle.setText(getResources().getString(R.string.regional_director));
        } else if (value == 2) {
            //TODO.. ALL INDIA CHAPTER
            activityTitle.setText(getResources().getString(R.string.all_india_chapter));
        } else if (value == 3) {
            //TODO.. HQ Committees
            activityTitle.setText(getResources().getString(R.string.hq_committees));
        }
    }

    private void getBOGList() {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createBOGCmd(loginUserToken, pageNo, recordPerPage);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.BOG_URL, params.toString(), true);
        } else {
            //TODO.. show retry alert
        }
    }

    private void getLoginUserToken() {
        try {
            JSONObject jsonObject = new JSONObject(SharedPref.getUserModelJSON(getActivity()));
            loginUserToken = jsonObject.getJSONObject(Constants.FLD_LOGIN_TOKAN);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;

                    //TODO.. handle response here with adapter
                }
            } else {
                //TODO.. show error
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.dismissProgressDialog();
    }

}
