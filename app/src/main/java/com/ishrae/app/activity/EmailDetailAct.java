package com.ishrae.app.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.CalendarEventModel;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.NotificationModelTmp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by raj on 4/16/2017.
 */

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class EmailDetailAct extends BaseAppCompactActivity implements Callback ,View.OnClickListener {
    private Toolbar toolbar;

    private TextView activityTitle;
    private TextView txtEmialTitleED;
    private TextView txtEmialSubTitleED;
    private TextView txtEmialDescrED;

    private LinearLayout llLikeContainer;
    private LinearLayout llShowInterest;

    private ImageView imgLike;
    private ImageView imgDisLike;

    private WebView wvEmialDescrED;

    private NetworkResponse resp;
    private int fromWhere;

    EmailModel emailModel;
    CalendarEventModel calendarEventModel;
    NotificationModelTmp.FcmNotificationEntitysBean notificationEntitysBean;
    private Bundle bundle;

    private int whichApi=0;
    private int API_LIKE=1;
    private int API_DIS_LIKE=2;
    private AlertDialog dialog;
    private boolean isRefreshed;
    public static boolean isLike;
    public ScrollView svED;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frg_email_detail);
        initialize();
    }


    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);
        txtEmialTitleED = (TextView) findViewById(R.id.txtEmialTitleED);
        txtEmialSubTitleED = (TextView) findViewById(R.id.txtEmialSubTitleED);
        txtEmialDescrED = (TextView) findViewById(R.id.txtEmialDescrED);
       llLikeContainer = (LinearLayout) findViewById(R.id.llLikeContainer);
        llShowInterest = (LinearLayout) findViewById(R.id.llShowInterest);

        svED = (ScrollView) findViewById(R.id.svED);

        imgLike= (ImageView) findViewById(R.id.imgLike);
         imgDisLike = (ImageView) findViewById(R.id.imgDisLike);

        wvEmialDescrED = (WebView) findViewById(R.id.wvEmialDescrED);

        imgLike.setOnClickListener(this);
        imgDisLike.setOnClickListener(this);
        llShowInterest.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bundle = getIntent().getExtras();
        txtEmialDescrED.setVisibility(View.VISIBLE);

        if (bundle != null) {
            if (bundle.containsKey(Constants.FLD_RESPONSE_DATA)) {
                txtEmialDescrED.setVisibility(View.GONE);
                emailModel = (EmailModel) bundle.getSerializable(Constants.FLD_RESPONSE_DATA);
                getEmailDetail();
                activityTitle.setText(getResources().getString(R.string.email_detail));
            } else if (bundle.containsKey(Constants.FLD_EVENT_DETAIL)) {
                calendarEventModel = (CalendarEventModel) bundle.getSerializable(Constants.FLD_EVENT_DETAIL);
                txtEmialTitleED.setText(calendarEventModel.title);
                String subTitle = getResources().getString(R.string.Start_Date) + " " + calendarEventModel.start + "\n" + getResources().getString(R.string.End_Date) + " " + calendarEventModel.end;
                txtEmialSubTitleED.setText(subTitle);
                txtEmialDescrED.setText("" + calendarEventModel.description);
                activityTitle.setText(getResources().getString(R.string.event_detail));
                llLikeContainer.setVisibility(View.VISIBLE);
                manageLikeDislikeColor();
                svED.setVisibility(View.VISIBLE);
            } else if (bundle.containsKey(Constants.FLD_NOTIFICATION_DETAIL)) {
                notificationEntitysBean = (NotificationModelTmp.FcmNotificationEntitysBean) bundle.getSerializable(Constants.FLD_NOTIFICATION_DETAIL);
                txtEmialTitleED.setText(notificationEntitysBean.Title);
                txtEmialSubTitleED.setVisibility(View.GONE);
                txtEmialDescrED.setText("" + notificationEntitysBean.Message);
                activityTitle.setText(getResources().getString(R.string.notification_Detail));
                svED.setVisibility(View.VISIBLE);
            }

        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(this, e);
    }


    private void getEmailDetail() {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.createEmaildDetailCmd(emailModel.id);
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.GETMAILDETAILS_URL, params.toString(), true);
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                if(whichApi==API_DIS_LIKE)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calendarEventModel.IsLike=false;
                            manageLikeDislikeColor();
                            isRefreshed=true;
                        }
                    });
                }

              else  if(whichApi==API_LIKE)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Util.showDefaultAlert(EmailDetailAct.this,getResources().getString(R.string.Thanks_for_showing_your_interest),null);
                            calendarEventModel.IsLike=true;
                            manageLikeDislikeColor();
                            isRefreshed=true;
                            isLike=true;

                        }
                    });

                }
                else
                {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;

                    if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (fromWhere == 0) {
                                    try {
                                        JSONArray jsonArray = new JSONArray(resp.respStr);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            emailModel = (EmailModel) Util.getJsonToClassObject(jsonArray.getJSONObject(i).toString(), EmailModel.class);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    txtEmialTitleED.setText(emailModel.MailSubject);
                                    txtEmialSubTitleED.setText(emailModel.MailFrom);
                                    if (!TextUtils.isEmpty(emailModel.MailBody))
                                        txtEmialDescrED.setText(Html.fromHtml(emailModel.MailBody));

                                    wvEmialDescrED. loadData(emailModel.MailBody, "text/html", "UTF-8");
                                    svED.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }

                }
                }
            }
            else
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (jsonObject != null ) {
                            Util.showDefaultAlert(EmailDetailAct.this,Util.getMessageFromJObj(jsonObject),null);
                        }
                    }
                });
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        finish();
    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.imgLike)
        {
//            if(calendarEventModel.IsLike==null||!calendarEventModel.IsLike) {
            if(calendarEventModel.IsLike==null) {
                dialog=  Util.retryAlertDialog(this, getResources().getString(R.string.app_name), getResources().getString(R.string.Do_you_like_this_Event), getResources().getString(R.string.cancel), getResources().getString(R.string.Yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        whichApi=API_LIKE;
                        makeLikeDishLike("true");
                        dialog.dismiss();
                    }
                });

            }

        }
       else  if(vId==R.id.imgDisLike)
        {
//            if(calendarEventModel.IsLike==null||calendarEventModel.IsLike)
//            {
                if(calendarEventModel.IsLike==null)
                {
                    dialog= Util.retryAlertDialog(this, getResources().getString(R.string.app_name), getResources().getString(R.string.Do_you_dislike_this_Event), getResources().getString(R.string.cancel), getResources().getString(R.string.Yes), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            whichApi=API_DIS_LIKE;
                            makeLikeDishLike("false");
                            dialog.dismiss();
                        }
                    });

                }
            }
            else if(vId==R.id.llShowInterest)
        {
            if(calendarEventModel.IsLike==null) {
                dialog=  Util.retryAlertDialog(this, getResources().getString(R.string.app_name), getResources().getString(R.string.Do_you_like_this_Event), getResources().getString(R.string.cancel), getResources().getString(R.string.Yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        whichApi=API_LIKE;
                        makeLikeDishLike("true");
                        dialog.dismiss();
                    }
                });

            }
        }


    }


    private void makeLikeDishLike(String isLike) {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.createLikeDishLikeCmd(""+calendarEventModel.id,isLike);
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.SET_CALENDER_EVENTS_LIKE_DISLIKE_URL, params.toString(), true);
        }
    }

    public void manageLikeDislikeColor()
    {
        if(calendarEventModel.IsLike==null)
        {
            imgDisLike.setColorFilter(getResources().getColor(R.color.white));
            imgLike.setColorFilter(getResources().getColor(R.color.white));
            llShowInterest.setVisibility(View.VISIBLE);
        }
       else if(!calendarEventModel.IsLike)
        {
            imgLike.setColorFilter(getResources().getColor(R.color.white));
            imgDisLike.setColorFilter(getResources().getColor(R.color.red));
            imgLike.setVisibility(View.GONE);
            isLike=calendarEventModel.IsLike;
            llShowInterest.setVisibility(View.GONE);
        }

        else if(calendarEventModel.IsLike)
        {
            imgLike.setColorFilter(getResources().getColor(R.color.toolbar_bg));
            imgDisLike.setColorFilter(getResources().getColor(R.color.white));
            imgDisLike.setVisibility(View.GONE);
            llShowInterest.setVisibility(View.GONE);
            isLike=calendarEventModel.IsLike;
        }

        imgLike.setVisibility(View.GONE);
        imgDisLike.setVisibility(View.GONE);

    }
}
