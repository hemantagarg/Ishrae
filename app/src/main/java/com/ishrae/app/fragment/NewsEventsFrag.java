package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.adapter.NewsAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.NewsEvents;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.NewsEventTemp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class NewsEventsFrag extends Fragment implements okhttp3.Callback, OnLoadMoreListener {

    private View view;

    private TextView activityTitle;

    private ImageView imvNoNews;

    private RecyclerView rvNewsList;

    private LinearLayoutManager mLayoutManager;

    private NetworkResponse resp;

    private JSONObject loginUserToken;

    private int pageNo = 1;


    private ArrayList<NewsEvents> newsEventsArrayList = new ArrayList<>();
    private NewsAdapter adapter;

    private int totalRecords;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.news_events, container, false);
            initialize();
        }
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        imvNoNews = (ImageView) view.findViewById(R.id.imvNoNews);
        rvNewsList = (RecyclerView) view.findViewById(R.id.rvNewsList);

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvNewsList.setLayoutManager(mLayoutManager);

        getNewsList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.news_events));
    }

    private void getNewsList(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createNewsCmd(loginUserToken, pageNo, Constants.RECORD_PER_PAGE_LIMIT);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.NEWS_URL, params.toString(), showIndicator);
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

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (newsEventsArrayList.size() > 0 && adapter != null && adapter.isLoaded()) {
                                newsEventsArrayList.remove(newsEventsArrayList.size() - 1);
                                adapter.notifyItemRemoved(newsEventsArrayList.size());
                                adapter.setLoaded();
                            }

                            NewsEventTemp newsEventTemp = (NewsEventTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), NewsEventTemp.class);
                            newsEventsArrayList.addAll(newsEventTemp.NewsList);
                            totalRecords = newsEventTemp.TotalItems;
                            setAdapter();
                        }
                    });
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_token_expire), null);
                        removeBottomLoader();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_token_expire), null);
                    removeBottomLoader();
                }
            });
        }
    }

    private void removeBottomLoader() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (newsEventsArrayList.size() > 0 && adapter != null && adapter.isLoaded()) {
                    newsEventsArrayList.remove(newsEventsArrayList.size() - 1);
                    adapter.notifyItemRemoved(newsEventsArrayList.size());
                    adapter.setLoaded();
                }
            }
        });

    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
        removeBottomLoader();
    }

    private void setAdapter() {
        if (newsEventsArrayList != null && newsEventsArrayList.size() > 0) {

            if (adapter == null) {
                adapter = new NewsAdapter(getActivity(), newsEventsArrayList, rvNewsList, this);
                rvNewsList.setAdapter(adapter);
            } else {
                adapter.notifyItemInserted(newsEventsArrayList.size() - 1);
            }
        } else {
            //TODO.. no news & events
        }
    }

    @Override
    public void onLoadMore() {

        if (totalRecords > newsEventsArrayList.size()) {

            if (adapter != null) {
                newsEventsArrayList.add(null);
                adapter.notifyItemInserted(newsEventsArrayList.size() - 1);
                pageNo += 1;
                getNewsList(false);

            }
        }
    }
}
