package com.ishrae.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.CalendarEventModel;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
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

public class NotificationDetailAct extends BaseAppCompactActivity implements Callback {


    private Toolbar toolbar;

    private TextView activityTitle;
    private TextView txtEmialTitleED;
    private TextView txtEmialSubTitleED;
    private TextView txtEmialDescrED;


    private NetworkResponse resp;
    private int fromWhere;

    EmailModel emailModel;
    CalendarEventModel calendarEventModel;
    private Bundle bundle;


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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.containsKey(Constants.FLD_RESPONSE_DATA)) {
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
