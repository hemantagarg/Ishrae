package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.adapter.BOGAdapter;
import com.ishrae.app.adapter.CWCAdapter;
import com.ishrae.app.adapter.ChapterAdapter;
import com.ishrae.app.adapter.PagerTabAdapter;
import com.ishrae.app.adapter.RDAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.BOG;
import com.ishrae.app.model.CWC;
import com.ishrae.app.model.Chapter;
import com.ishrae.app.model.RD;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.BOGTemp;
import com.ishrae.app.tempModel.CWCTemp;
import com.ishrae.app.tempModel.ChapterTemp;
import com.ishrae.app.tempModel.CommitteeMemberTemp;
import com.ishrae.app.tempModel.RDTemp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.weiwangcn.betterspinner.library.BetterSpinner;

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

public class HqDetailsFrag extends Fragment implements TabLayout.OnTabSelectedListener, Callback, OnLoadMoreListener {

    private View view;
    private TextView activityTitle;
    private TextView txtNoData;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private RecyclerView recyclerView;
    private BetterSpinner spSelectChapter;

    private String[] tabArray;
    private JSONObject loginUserToken;
    private int pageNo = 1;
    private int financialYearId = 69;

    private NetworkResponse resp;

    /**
     * 0 = BOG
     * 1 = RD (Regional Director)
     * 2 = Chapter List
     * 3 = CWC List
     * 4 = Committee List
     * 5 = Committee Member List
     */
    private BOGAdapter bogAdapter;
    private RDAdapter rdAdapter;
    private ChapterAdapter chapterAdapter;
    private CWCAdapter cwcAdapter;
    private PagerTabAdapter adapter;
    private int fromWhere = 0;
    private int totalRecords = 0;

    private ArrayList<BOG> bogArrayList = new ArrayList<>();
    private ArrayList<RD> regionalDirectorList = new ArrayList<>();
    private ArrayList<Chapter> chapterArrayList = new ArrayList<>();
    private ArrayList<CWC> cwcArrayList = new ArrayList<>();
    private ArrayList<Chapter> committeeArrayList = new ArrayList<>();
    private ArrayList<CWC> committeeMemberList = new ArrayList<>();

    private LinearLayoutManager mLayoutManager;

    private int lastHistoryId;
    private int chapterId;
    private int committeeId;
    private int pos = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.hq_detail, container, false);
        initialize();
        return view;
    }

    private void initialize() {

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        txtNoData = (TextView) view.findViewById(R.id.txtNoData);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        spSelectChapter = (BetterSpinner) view.findViewById(R.id.spSelectChapter);
        tabArray = getActivity().getResources().getStringArray(R.array.hq_detail_tabs);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[2]));
        tabLayout.addTab(tabLayout.newTab().setText(tabArray[3]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //Initializing viewPager
        viewPager = (ViewPager) view.findViewById(R.id.pager);
        //Creating our pager adapter
        adapter = new PagerTabAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setOnTabSelectedListener(HqDetailsFrag.this);
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        spSelectChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (fromWhere == 3) {

                    Chapter chapter = (Chapter) adapterView.getItemAtPosition(i);
                    chapterId = chapter.Value;
                    pos = i;
                    spSelectChapter.setText("");
                    spSelectChapter.setClickable(true);
                    spSelectChapter.setHint("" + chapterArrayList.get(pos).Text);

                    if (cwcArrayList != null && cwcArrayList.size() > 0) {
                        cwcArrayList.clear();
                    }
                    cwcAdapter.list = cwcArrayList;
                    cwcAdapter.notifyDataSetChanged();
                    cwcAdapter = null;


                    pageNo = 1;

                    getCWCList(true);
                } else if (fromWhere == 5) {
                    Chapter chapter = (Chapter) adapterView.getItemAtPosition(i);
                    committeeId = chapter.Value;
                    pos = i;
                    spSelectChapter.setText("");
                    spSelectChapter.setClickable(true);
                    spSelectChapter.setHint("" + committeeArrayList.get(pos).Text);

                    pageNo = 1;
                    if (committeeMemberList != null && committeeMemberList.size() > 0) {
                        committeeMemberList.clear();
                    }

                    if(cwcAdapter!=null) {
                        cwcAdapter.list = committeeMemberList;
                        cwcAdapter.notifyDataSetChanged();
                        cwcAdapter = null;
                    }
                    getCommitteeMemberList(true);
                }
            }
        });

        getBOGList(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.hq_details));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        spSelectChapter.setVisibility(View.GONE);

        if (tab.getPosition() == 0) {
            setNullAdapter();
            fromWhere = 0;
            pageNo = 1;
            getBOGList(true);
        } else if (tab.getPosition() == 1) {
            setNullAdapter();
            fromWhere = 1;
            pageNo = 1;
            getRDList(true);

        } else if (tab.getPosition() == 2) {
            setNullAdapter();

            fromWhere = 2;
            pageNo = 1;
            getChapterList();

        } else if (tab.getPosition() == 3) {
            setNullAdapter();
            fromWhere = 4;
            pageNo = 1;
            getCommitteeList();
        }
    }

    private void setNullAdapter() {
        pos = 0;
        if (bogArrayList != null && bogArrayList.size() > 0) {
            bogArrayList.clear();
            bogAdapter.list = bogArrayList;
            bogAdapter.notifyDataSetChanged();
        }

        if (regionalDirectorList != null && regionalDirectorList.size() > 0) {
            regionalDirectorList.clear();
            rdAdapter.list = regionalDirectorList;
            rdAdapter.notifyDataSetChanged();
        }

        if (chapterArrayList != null && chapterArrayList.size() > 0) {
            chapterArrayList.clear();
            if(chapterAdapter!=null) {
                chapterAdapter.list = chapterArrayList;
                chapterAdapter.notifyDataSetChanged();
            }
        }

        if (cwcArrayList != null && cwcArrayList.size() > 0) {
            cwcArrayList.clear();
            cwcAdapter.list = cwcArrayList;
            cwcAdapter.notifyDataSetChanged();
        }

        if (committeeArrayList != null && committeeArrayList.size() > 0) {
            committeeArrayList.clear();
            chapterAdapter.list = committeeArrayList;
            chapterAdapter.notifyDataSetChanged();
        }

        if (committeeMemberList != null && committeeMemberList.size() > 0) {
            committeeMemberList.clear();
            cwcAdapter.list = committeeMemberList;
            cwcAdapter.notifyDataSetChanged();
        }

        bogAdapter = null;
        rdAdapter = null;
        chapterAdapter = null;
        cwcAdapter = null;
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        //TODO..
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        //TODO..
    }

    private void getBOGList(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createBOGCmd(loginUserToken, pageNo, Constants.RECORD_PER_PAGE_LIMIT);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.BOG_URL, params.toString(), showIndicator);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getRDList(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createRDCmd(loginUserToken, pageNo, Constants.RECORD_PER_PAGE_LIMIT);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.RD_URL, params.toString(), showIndicator);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getChapterList() {
        if (Util.isDeviceOnline(getActivity())) {
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_GET, AppUrls.CHAPTER_LIST_URL, "", true);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getCWCList(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createCWCCmd(loginUserToken, pageNo, Constants.RECORD_PER_PAGE_LIMIT, chapterId);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.CWC_LIST_URL, params.toString(), showIndicator);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getCommitteeList() {
        if (Util.isDeviceOnline(getActivity())) {
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_GET, AppUrls.COMMITTEE_LIST_URL, "", true);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getCommitteeMemberList(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity())) {
            getLoginUserToken();
            JSONObject params = CmdFactory.createCommitteeMemberListCmd(loginUserToken, pageNo, Constants.RECORD_PER_PAGE_LIMIT, committeeId);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.COMMITTEE_MEMBER_LIST_URL, params.toString(), showIndicator);
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
            if (jsonObject != null && jsonObject.getInt("responseCode") == 1) {
                String responseData = jsonObject.getString("responseData");
                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (fromWhere == 0) {
                                if (bogAdapter != null && bogAdapter.isLoaded()) {

                                    bogArrayList.remove(bogArrayList.size() - 1);

                                    bogAdapter.notifyItemRemoved(bogArrayList.size());

                                    bogAdapter.setLoaded();
                                }
                                BOGTemp bogTemp = (BOGTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), BOGTemp.class);
                                bogArrayList.addAll(bogTemp.bogList);
                                totalRecords = bogTemp.TotalItems;
//                                isLoadMore = false;
                                setBOGAdapter();

                            } else if (fromWhere == 1) {
                                if (rdAdapter != null && rdAdapter.isLoaded()) {

                                    regionalDirectorList.remove(regionalDirectorList.size() - 1);

                                    rdAdapter.notifyItemRemoved(regionalDirectorList.size());

                                    rdAdapter.setLoaded();
                                }
                                RDTemp rdTemp = (RDTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), RDTemp.class);
                                regionalDirectorList.addAll(rdTemp.RDList);
                                totalRecords = rdTemp.TotalItems;
//                                isLoadMore = false;
                                setRDAdapter();

                            } else if (fromWhere == 2) {
                                ChapterTemp chapterTemp = (ChapterTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), ChapterTemp.class);
                                if(chapterArrayList!=null)
                                    chapterArrayList.clear();
                                chapterArrayList = chapterTemp.DropdownList;

                                setChapterAdapter();

                            } else if (fromWhere == 3) {

                                if (cwcAdapter != null && cwcAdapter.isLoaded()) {

                                    cwcArrayList.remove(cwcArrayList.size() - 1);

                                    cwcAdapter.notifyItemRemoved(cwcArrayList.size());

                                    cwcAdapter.setLoaded();
                                }

                                CWCTemp cwcTemp = (CWCTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), CWCTemp.class);
                                cwcArrayList.addAll(cwcTemp.CWCList);
                                totalRecords = cwcTemp.TotalItems;
//                                isLoadMore = false;
                                setCWCAdapter();

                            } else if (fromWhere == 4) {

                                ChapterTemp chapterTemp = (ChapterTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), ChapterTemp.class);
                                committeeArrayList = chapterTemp.DropdownList;

                                setCommitteeAdapter();

                            } else if (fromWhere == 5) {
                                if (cwcAdapter != null && cwcAdapter.isLoaded()) {

                                    committeeMemberList.remove(committeeMemberList.size() - 1);

                                    cwcAdapter.notifyItemRemoved(committeeMemberList.size());

                                    cwcAdapter.setLoaded();
                                }

                                CommitteeMemberTemp committeeMemberTemp = (CommitteeMemberTemp) Util.getJsonToClassObject(resp.getJsonObject().toString(), CommitteeMemberTemp.class);
                                committeeMemberList.addAll(committeeMemberTemp.CMList);
                                totalRecords = committeeMemberTemp.TotalItems;
//                                isLoadMore = false;
                                setCommitteeMemberAdapter();
                            }
                        }
                    });
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_token_expire), null);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_token_expire), null);
                }
            });
        }
    }

    private void setBOGAdapter() {
        if (bogArrayList != null && bogArrayList.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            spSelectChapter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (rdAdapter == null) {
                bogAdapter = new BOGAdapter(getActivity(), bogArrayList, recyclerView, this);
                recyclerView.setAdapter(bogAdapter);
            } else {
                bogAdapter.notifyItemInserted(bogArrayList.size());
                recyclerView.invalidate();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            spSelectChapter.setVisibility(View.GONE);
        }
    }

    private void setRDAdapter() {
        if (regionalDirectorList != null && regionalDirectorList.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            spSelectChapter.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            if (rdAdapter == null) {
                rdAdapter = new RDAdapter(getActivity(), regionalDirectorList, recyclerView, this);
                recyclerView.setAdapter(rdAdapter);
            } else {
                rdAdapter.notifyItemChanged(regionalDirectorList.size());
                recyclerView.invalidate();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            spSelectChapter.setVisibility(View.GONE);
        }
    }

    private void setChapterAdapter() {
        if (chapterArrayList != null && chapterArrayList.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            spSelectChapter.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            spSelectChapter.setVisibility(View.VISIBLE);
            chapterAdapter = new ChapterAdapter(getActivity(), R.layout.chapter_row, chapterArrayList);
            spSelectChapter.setAdapter(chapterAdapter);

            chapterId = chapterArrayList.get(0).Value;
            pageNo = 1;
            fromWhere = 3;
            spSelectChapter.setHint("");
            spSelectChapter.setText("");
            spSelectChapter.setHint("" + chapterArrayList.get(pos).Text);
            getCWCList(true);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            spSelectChapter.setVisibility(View.GONE);
        }
    }

    private void setCWCAdapter() {
        if (cwcArrayList != null && cwcArrayList.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            spSelectChapter.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            if (cwcAdapter == null) {
                cwcAdapter = new CWCAdapter(getActivity(), cwcArrayList, recyclerView, this);
                recyclerView.setAdapter(cwcAdapter);
            } else {
                cwcAdapter.notifyItemInserted(cwcArrayList.size());
                recyclerView.invalidate();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            if(chapterArrayList!=null&&chapterArrayList.size()<=0)
            spSelectChapter.setVisibility(View.GONE);
        }
    }

    private void setCommitteeAdapter() {
        if (committeeArrayList != null && committeeArrayList.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            spSelectChapter.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            spSelectChapter.setVisibility(View.VISIBLE);
            chapterAdapter = new ChapterAdapter(getActivity(), R.layout.chapter_row, committeeArrayList);
            spSelectChapter.setAdapter(chapterAdapter);

            committeeId = committeeArrayList.get(0).Value;
            pageNo = 1;
            fromWhere = 5;
            spSelectChapter.setHint("");
            spSelectChapter.setText("");
            spSelectChapter.setHint("" + committeeArrayList.get(pos).Text);
            getCommitteeMemberList(true);
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            if(chapterArrayList!=null&&chapterArrayList.size()<=0)
            spSelectChapter.setVisibility(View.GONE);
        }
    }

    private void setCommitteeMemberAdapter() {
        if (committeeMemberList != null && committeeMemberList.size() > 0) {
            txtNoData.setVisibility(View.GONE);
            spSelectChapter.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            if (cwcAdapter == null) {
                cwcAdapter = new CWCAdapter(getActivity(), committeeMemberList, recyclerView, this);
                recyclerView.setAdapter(cwcAdapter);
            } else {
                cwcAdapter.notifyDataSetChanged();
                recyclerView.invalidate();
            }
        } else {
            recyclerView.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            if(committeeArrayList!=null&&committeeArrayList.size()<=0)
            spSelectChapter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.dismissProgressDialog();
    }

    @Override
    public void onLoadMore() {

        if (fromWhere == 0) {

            if (bogAdapter != null && totalRecords > bogArrayList.size()) {

                bogArrayList.add(null);

                bogAdapter.notifyItemInserted(bogArrayList.size() - 1);

                pageNo += 1;

                getBOGList(false);
            }
        } else if (fromWhere == 1) {
            if (rdAdapter != null && totalRecords > regionalDirectorList.size()) {

                regionalDirectorList.add(null);

                rdAdapter.notifyItemInserted(regionalDirectorList.size() - 1);
                pageNo = pageNo + 1;
                getRDList(false);
            }


        } else if (fromWhere == 3) {

            if (cwcAdapter != null && totalRecords > cwcArrayList.size()) {

                cwcArrayList.add(null);

                cwcAdapter.notifyItemInserted(cwcArrayList.size() - 1);
                pageNo = pageNo + 1;
                getCWCList(false);

            }
        } else if (fromWhere == 5) {
            if (cwcAdapter != null && totalRecords > committeeMemberList.size()) {


                committeeMemberList.add(null);

                cwcAdapter.notifyItemInserted(committeeMemberList.size() - 1);
                pageNo = pageNo + 1;
                getCommitteeMemberList(false);

            }

        }
    }

}
