package com.ishrae.app.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.MyChapterInfo;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class MyChapter extends Fragment implements Callback,View.OnClickListener {

    private View view;

    private TextView activityTitle;

    private TextView txtChapterName;
    private TextView txtAbbreviation;
    private TextView txtAddress;
    private TextView txtParentChapter;
    private TextView txtRegion;
    private TextView txtState;
    private TextView txtContact1;
    private TextView txtContact2;
    private TextView txtTelephone1;
    private TextView txtTelephone2;
    private TextView txtEmail1;
    private TextView txtEmail2;
    private TextView txtFax;
    private TextView txtWebsite;
    private TextView txtDescription;

    private static final int REQUEST_PHONE_CALL = 1;

    private LinearLayout llMyChapterMain;

    private JSONObject loginUserToken;

    private NetworkResponse resp;

    private MyChapterInfo myChapterInfo;
    private int requestPermissionForPhoto = 0;
    private String phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_chapter, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        txtChapterName = (TextView) view.findViewById(R.id.txtChapterName);
        txtAbbreviation = (TextView) view.findViewById(R.id.txtAbbreviation);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtParentChapter = (TextView) view.findViewById(R.id.txtParentChapter);
        txtRegion = (TextView) view.findViewById(R.id.txtRegion);
        txtState = (TextView) view.findViewById(R.id.txtState);
        txtContact1 = (TextView) view.findViewById(R.id.txtContact1);
        txtContact2 = (TextView) view.findViewById(R.id.txtContact2);
        txtTelephone1 = (TextView) view.findViewById(R.id.txtTelephone1);
        txtTelephone2 = (TextView) view.findViewById(R.id.txtTelephone2);
        txtEmail1 = (TextView) view.findViewById(R.id.txtEmail1);
        txtEmail2 = (TextView) view.findViewById(R.id.txtEmail2);
        txtFax = (TextView) view.findViewById(R.id.txtFax);
        txtWebsite = (TextView) view.findViewById(R.id.txtWebsite);
        txtDescription = (TextView) view.findViewById(R.id.txtDescription);

        llMyChapterMain = (LinearLayout) view.findViewById(R.id.llMyChapterMain);
        txtEmail1.setOnClickListener(this);
        txtEmail2.setOnClickListener(this);
        txtTelephone1.setOnClickListener(this);
        txtTelephone2.setOnClickListener(this);
        getMyChapterInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.my_chapter));
    }

    private void getMyChapterInfo() {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createMyChapterInfoCmd(loginUserToken);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.MY_CHAPTER_INFO_URL, params.toString(), true);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getLoginUserToken() {
        try {
            JSONObject jsonObject = new JSONObject(SharedPref.getUserModelJSON(getActivity()));
            loginUserToken = jsonObject.getJSONObject("logintokan");
        } catch (JSONException e) {
            e.printStackTrace();
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

                    myChapterInfo = (MyChapterInfo) Util.getJsonToClassObject(resp.getJsonObject().toString(), MyChapterInfo.class);
                    if (myChapterInfo != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setData();
                            }
                        });
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setData() {
        llMyChapterMain.setVisibility(View.VISIBLE);

        txtChapterName.setText("" + myChapterInfo.ChapterName);
        txtAbbreviation.setText("" + myChapterInfo.Abbreviation);
        txtAddress.setText("" + myChapterInfo.Address);
        txtParentChapter.setText("" + myChapterInfo.ParentChapter);
        txtRegion.setText("" + myChapterInfo.Region);
        txtState.setText("" + myChapterInfo.State);
        txtContact1.setText("" + myChapterInfo.ContactPerson1);
        txtContact2.setText("" + myChapterInfo.ContactPerson2);
        if(!TextUtils.isEmpty(myChapterInfo.Telephone1))
        txtTelephone1.setText("" + myChapterInfo.Telephone1);
        if(!TextUtils.isEmpty(myChapterInfo.Telephone2))
        txtTelephone2.setText("" + myChapterInfo.Telephone2);
        txtEmail1.setText("" + myChapterInfo.EmailId1);
        txtEmail2.setText("" + myChapterInfo.EmailId2);
        if (TextUtils.isEmpty(myChapterInfo.Fax))
            myChapterInfo.Fax = "";
        txtFax.setText("" + myChapterInfo.Fax);
        if (TextUtils.isEmpty(myChapterInfo.Website))
            myChapterInfo.Website = "";
        txtWebsite.setText("" + myChapterInfo.Website);
        if (TextUtils.isEmpty(myChapterInfo.Description))
            myChapterInfo.Description = "";
        txtDescription.setText("" + Html.fromHtml(myChapterInfo.Description));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        //TODO..
    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.txtEmail1)
        {
            if(!TextUtils.isEmpty( myChapterInfo.EmailId1))
            Util. sendMail(getActivity(), myChapterInfo.EmailId1,"","");

        }
        else if(vId==R.id.txtEmail2)
        {
            if(!TextUtils.isEmpty( myChapterInfo.EmailId2))
                Util. sendMail(getActivity(), myChapterInfo.EmailId2,"","");

        }
        else if(vId==R.id.txtTelephone1)
        {
            phoneNumber = myChapterInfo.Telephone1;
            makeCallWithPermissionCehck();
        }
        else if(vId==R.id.txtTelephone2)
        {
            phoneNumber = myChapterInfo.Telephone2;
            makeCallWithPermissionCehck();

        }
    }

    private void makeCallWithPermissionCehck() {
        if(!TextUtils.isEmpty(phoneNumber))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
            }
            else
            {
                Util.makeCall(getActivity(),phoneNumber);
            }
        } else {
            Util.makeCall(getActivity(),phoneNumber);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Util.makeCall(getActivity(),phoneNumber);
                }
                break;
            }
        }
    }

}
