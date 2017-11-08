package com.ishrae.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.adapter.OptionRowAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.PollOfDayModel;
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
 * Created by Nss Solutions on 24-04-2017.
 */

public class PollOfDayAct extends Fragment implements Callback, View.OnClickListener, OnLoadMoreListener {

    private Toolbar toolbar;
    private TextView activityTitle;
    TextView txtPollQue;
    TextView txtNext;
    TextView txtEmptyPoll;
    LinearLayout llNextQ;
    LinearLayout llPollOfDayContainer;
    LinearLayout llResult;

    private RecyclerView rvPollOfTheDayOptions;

    private NetworkResponse resp;

    private int fromWhere;
    private boolean isRefreshed;
    private int pageNumber = 1;

    private ArrayList<PollOfDayModel> questionAnswerList;
    private OptionRowAdapter optionRowAdapter;

    private int currentSelectedPos;
    private String answerId;
    private int whichApi=0;
    private int NEXT_API=1;

    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.poll_of_the_day, container, false);
        initialize();
        return view;
    }


    private void initialize() {
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        activityTitle = (TextView) getActivity(). findViewById(R.id.activityTitle);

        txtPollQue = (TextView)  view.findViewById(R.id.txtPollQue);
        txtNext = (TextView)  view.findViewById(R.id.txtNext);
        txtEmptyPoll = (TextView)  view.findViewById(R.id.txtEmptyPoll);

        llNextQ = (LinearLayout)  view.findViewById(R.id.llNextQ);
        llResult = (LinearLayout) view. findViewById(R.id.llResult);
        llPollOfDayContainer = (LinearLayout) view. findViewById(R.id.llPollOfDayContainer);

        llNextQ.setOnClickListener(this);
        llResult.setOnClickListener(this);

        rvPollOfTheDayOptions = (RecyclerView) view. findViewById(R.id.rvPollOfTheDayOptions);

        rvPollOfTheDayOptions.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvPollOfTheDayOptions.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                for (int i = 0; i < questionAnswerList.get(currentSelectedPos-1).getOpinionPollChoices().size(); i++) {
                    questionAnswerList.get(currentSelectedPos-1).getOpinionPollChoices().get(i).setSelected(false);
                    optionRowAdapter.notifyItemChanged(i);
                }
                questionAnswerList.get(currentSelectedPos-1).getOpinionPollChoices().get(position).setSelected(! questionAnswerList.get(currentSelectedPos-1).getOpinionPollChoices().get(position).getSelected());
                optionRowAdapter.notifyItemChanged(position);
            }
        }));
        getOpenionPollList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Poll_Of_Day));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
          getActivity().  onBackPressed();
        }
        return true;
    }

//    @Override
//    public void onBackPressed() {
//        if (isRefreshed)
//            getActivity().setResult(Constants.REFRESH);
//        super.onBackPressed();
//    }

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

                  getActivity().  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(whichApi==NEXT_API)
                            {
                                if (txtNext.getText().toString().trim().equalsIgnoreCase(getResources().getString(R.string.Finish))) {
                                    getActivity().onBackPressed();
                                    Util.startActivity(getActivity(),PollResultAct.class);

                                }else
                                setQuetionData(questionAnswerList.get(currentSelectedPos));
                            }
                            else
                            {
                            if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<List<PollOfDayModel>>() {
                                }.getType();
                                questionAnswerList = gson.fromJson(resp.respStr.toString(), type);
                                if(questionAnswerList.size()>0) {
                                    setQuetionData(questionAnswerList.get(currentSelectedPos));
                                    llPollOfDayContainer.setVisibility(View.VISIBLE);
                                }
                                else
                                    txtEmptyPoll.setVisibility(View.VISIBLE);
                            }
                            else
                                txtEmptyPoll.setVisibility(View.VISIBLE);
                        }
                        }
                    });
                }
            }
            else
            {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llPollOfDayContainer.setVisibility(View.GONE);
                        txtEmptyPoll.setVisibility(View.VISIBLE);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setQuetionData(PollOfDayModel pollOfDayModel) {
        optionRowAdapter = new OptionRowAdapter(getActivity(), (ArrayList<PollOfDayModel.OpinionPollChoicesBean>) pollOfDayModel.getOpinionPollChoices());
        rvPollOfTheDayOptions.setAdapter(optionRowAdapter);
        txtPollQue.setText(pollOfDayModel.getQuestion());

        if (questionAnswerList.size() > currentSelectedPos) {
            currentSelectedPos++;
            if (questionAnswerList.size() == currentSelectedPos) {
                txtNext.setText(getResources().getString(R.string.Finish));
            }
        }
    }


    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.llNextQ) {
                answerId="";
                for (int i = 0; i < questionAnswerList.get(currentSelectedPos-1).getOpinionPollChoices().size(); i++) {
                    if(questionAnswerList.get(currentSelectedPos-1).getOpinionPollChoices().get(i).getSelected()) {
                        answerId = questionAnswerList.get(currentSelectedPos - 1).getOpinionPollChoices().get(i).getId();
                        break;
                    }
                }

            if(!TextUtils.isEmpty(answerId))
            {
                optionAttemptApi(true);
            }
            else
            {
                Util.showDefaultAlert(getActivity(),getResources().getString(R.string.Please_select_at_least_one_option),null);
            }
        }
        else if(vId==R.id.llResult)
        {

            Util.startActivity(getActivity(),PollResultAct.class);
        }
    }

    private void getOpenionPollList(boolean showProIndicator) {
        if (Util.isDeviceOnline(getActivity(), true)) {
            whichApi=0;
            JSONObject params = CmdFactory.getOpenionPollCmd("" + pageNumber);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GET_ACTIVE_OPINION_POLL_LIST_URL, params.toString(), showProIndicator);
        } else {
            // TODO: 6/18/2017  
        }
    }

    private void optionAttemptApi(boolean showProIndicator) {
        if (Util.isDeviceOnline(getActivity(), true)) {
            whichApi=NEXT_API;
            JSONObject params = CmdFactory.setOpenionPollCmd("" + answerId);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.SET_OPINION_POLL_URL, params.toString(), showProIndicator);
        } else {
            // TODO: 6/18/2017
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Constants.REFRESH) {
            getOpenionPollList(true);
        }

    }

    @Override
    public void onLoadMore() {
//        if (forumModelTemp != null && forumModelTemp.TotalItems > questionAnswerList.size()) {
//            if (optionRowAdapter != null) {
//
//                questionAnswerList.add(null);
//
//                optionRowAdapter.notifyItemInserted(questionAnswerList.size() - 1);
//
//                pageNumber += 1;
//
//                getOpenionPollList(false);
//            }
//        }
    }
}
