package com.ishrae.app.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.adapter.ForumDetailListAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.ForumModel;
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

public class CreateTopicAct extends BaseAppCompactActivity implements Callback, View.OnClickListener {

    private Toolbar toolbar;
    private TextView activityTitle;

    private Button btnSubmitTopic;

    private EditText eTxTopicName;
    private EditText eTxtTopicDescr;

    private NetworkResponse resp;

    private int fromWhere;
    private boolean isRefreshed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_topic);
        initialize();
    }


    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        activityTitle = (TextView) findViewById(R.id.activityTitle);
        btnSubmitTopic = (Button) findViewById(R.id.btnSubmitTopic);

        eTxTopicName = (EditText) findViewById(R.id.eTxTopicName);
        eTxtTopicDescr = (EditText) findViewById(R.id.eTxtTopicDescr);

        btnSubmitTopic.setOnClickListener(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Create_Topic));
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
        super.onBackPressed();
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(this, e);
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

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                                Util.showCenterToast(CreateTopicAct.this, Util.getMessageFromJObj(jsonObject));
                                isRefreshed = true;
                                onBackPressed();
                            }
                        }
                    });
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if (vId == R.id.btnSubmitTopic) {
            createTopic();
        }
    }

    private void createTopic() {
        if (Util.isDeviceOnline(this, true)) {
            String topicName = eTxTopicName.getText().toString().trim();
            String topic_descr = eTxtTopicDescr.getText().toString().trim();

            if (TextUtils.isEmpty(topicName)) {
                Util.showCenterToast(this, getResources().getString(R.string.Please_enter_topic_name));
            } else if (TextUtils.isEmpty(topic_descr)) {
                Util.showCenterToast(this, getResources().getString(R.string.Please_enter_topic_description));
            } else {
                JSONObject params = CmdFactory.createTopicCmd(topicName, topic_descr);
                NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.CREATE_TOPIC_URL, params.toString(), true);
            }
        }
    }
}
