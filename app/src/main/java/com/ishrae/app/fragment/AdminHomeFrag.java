package com.ishrae.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.activity.CIQEventsListAct;
import com.ishrae.app.activity.PollOfDayAct;
import com.ishrae.app.adapter.AdminHomeMenuAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.HomeMenuModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.HomeStateListTmp;
import com.ishrae.app.tempModel.TmpSocialModel;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 21-03-2017.
 */
public class AdminHomeFrag extends Fragment implements View.OnClickListener, Callback {

    private View view;

    private TextView activityTitle;

    private Button btnGetPendingPost;

    private Fragment fragment = null;
    private TmpSocialModel tmpSocialModel;

    private RecyclerView rvNavMenuHomeA;
    private ArrayList<HomeMenuModel> menuModelArrayList;
    private NetworkResponse resp;
    private HomeStateListTmp homeStateListTmp;
    String[] memberNameArray;
    Integer[] memberIconIdArray;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_home, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        rvNavMenuHomeA = (RecyclerView) view.findViewById(R.id.rvNavMenuHomeA);

        btnGetPendingPost = (Button) view.findViewById(R.id.btnGetPendingPost);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rvNavMenuHomeA.setLayoutManager(gridLayoutManager);

        rvNavMenuHomeA.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                manageHomeMenuClick(position);
            }
        }));

        menuModelArrayList = new ArrayList<>();
        manageNonMemberVisibility();

        JSONObject params = CmdFactory.createHomeStateCmd();
        NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.HOMESTATSLIST_URL, params.toString(), true);
        btnGetPendingPost.setOnClickListener(this);
    }

    private void manageHomeMenuClick(int position) {
        HomeMenuModel homeMenuModel = menuModelArrayList.get(position);
        if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.my_ishrae))) {
            fragment = new MyISHAREFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.My_Profile))) {
//            DashboardActivity.bottomBar.selectTabWithId(R.id.tabMyProfile);
            fragment = new MyProfileFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.My_Chapter))) {
            fragment = new MyChapter();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.HQ_Details))) {
            fragment = new HqDetailsFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.News_Events))) {
            fragment = new NewsEventsFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.Program))) {
            fragment = new ProgramFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.Shop))) {
            fragment = new ProductListFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.Elearning))) {
            fragment = new ELearningFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.Forum))) {
            fragment = new ForumFrag();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.dashboard));
    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.btnGetPendingPost)
        {
            Intent intent = new Intent(getActivity(), CIQEventsListAct.class);
            getActivity().startActivityForResult(intent, Constants.REFRESH);
        }
    }

    public void manageNonMemberVisibility() {
        String values = SharedPref.getUserModelJSON(getActivity());

        tmpSocialModel = (TmpSocialModel) Util.getJsonToClassObject(values, TmpSocialModel.class);

        if (tmpSocialModel != null && tmpSocialModel.Roles != null && tmpSocialModel.Roles.size() > 0) {
            memberNameArray = getActivity().getResources().getStringArray(R.array.home_menu_member_items);
            memberIconIdArray = new Integer[]{R.mipmap.ic_home_my_share, R.mipmap.ic_home_my_profile, R.mipmap.ic_home_my_chapter, R.mipmap.ic_home_hq_detail, R.mipmap.ic_home_news_events, R.mipmap.ic_home_program, R.mipmap.ic_shop, R.mipmap.ic_elearning, R.mipmap.ic_forum};

            if (tmpSocialModel.Roles.get(0).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)) {
                memberNameArray = getActivity().getResources().getStringArray(R.array.home_menu_non_member_items);
                memberIconIdArray = new Integer[]{R.mipmap.ic_home_my_share, R.mipmap.ic_home_my_profile, R.mipmap.ic_home_news_events, R.mipmap.ic_shop, R.mipmap.ic_elearning, R.mipmap.ic_forum};
            } else {
                memberNameArray = getActivity().getResources().getStringArray(R.array.home_menu_admin);
                memberIconIdArray = new Integer[]{
                        R.mipmap.ic_home_totol_strength,
                        R.mipmap.ic_home_members_in_grace,
                        R.mipmap.ic_home_dormant_members,
                        R.mipmap.ic_home_new_mamber,
                        R.mipmap.ic_home_renewal_sustenance,
                        R.mipmap.ic_home_programs,
                        R.mipmap.ic_home_pending_post_event,
                        R.mipmap.ic_home_email,
                        R.mipmap.ic_home_ishrae_drive,
                        R.mipmap.ic_home_student_member,
                        R.mipmap.ic_home__student_activity,
                        R.mipmap.ic_home_cafe
                };
            }


        }
    }

    public void setMenuAdapter() {
        for (int i = 0; i < memberNameArray.length; i++) {
            HomeMenuModel homeMenuModel = new HomeMenuModel();
            homeMenuModel.iconId = memberIconIdArray[i];
            homeMenuModel.menuName = memberNameArray[i];
            if (homeStateListTmp != null && homeStateListTmp.HomeStatsList != null) {
                for (int i1 = 0; i1 < homeStateListTmp.HomeStatsList.size(); i1++) {
                    if (homeMenuModel.menuName.equalsIgnoreCase(homeStateListTmp.HomeStatsList.get(i1).Name)) {
                        if (!TextUtils.isEmpty(homeStateListTmp.HomeStatsList.get(i1).Value))
                            homeMenuModel.count = Integer.valueOf(homeStateListTmp.HomeStatsList.get(i1).Value);
                        break;
                    }
                }
            }
            menuModelArrayList.add(homeMenuModel);
        }

        AdminHomeMenuAdapter homeMenuAdapter = new AdminHomeMenuAdapter(getActivity(), menuModelArrayList);
        rvNavMenuHomeA.setAdapter(homeMenuAdapter);
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
                    homeStateListTmp = (HomeStateListTmp) Util.getJsonToClassObject(resp.respStr, HomeStateListTmp.class);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setMenuAdapter();

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

    @Override
    public void onFailure(Call call, IOException e) {
        Util.dismissProgressDialog();
    }
}
