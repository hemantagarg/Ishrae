package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.activity.DashboardActivity;
import com.ishrae.app.adapter.HomeMenuAdapter;
import com.ishrae.app.adapter.ViewPagerAdapter;
import com.ishrae.app.model.HomeMenuModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.tempModel.TmpSocialModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;
import com.roughike.bottombar.BottomBar;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Nss Solutions on 21-03-2017.
 */
public class HomeFrag extends Fragment implements View.OnClickListener {

    private View view;

    public static BottomBar bottomBar;

    private TextView activityTitle;

    private AutoScrollViewPager vpBannerSlides;
    private CirclePageIndicator indicator;

//    private LinearLayout llMyIshrae;
//    private LinearLayout llMyProfile;
//    private LinearLayout llMyChapters;
//    private LinearLayout llHQDetails;
//    private LinearLayout llNewsEvents;
//    private LinearLayout llProgram;
//
//    private LinearLayout llShop;
//    private LinearLayout llElearning;
//    private LinearLayout llForum;

    private Fragment fragment = null;
    private TmpSocialModel tmpSocialModel;
    private TextView txtSponsorName;
    private TextView txtSponsorMessage;
    private ImageView imgSponsor;

    private DashboardActivity dashboardActivity;

    private FrameLayout fmBanner;
    private CardView cvBannersContainer;
    private CardView cvPresident;
    private RecyclerView rvNavMenuHome;
    private ArrayList<HomeMenuModel> menuModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        cvBannersContainer = (CardView) view.findViewById(R.id.cvBannersContainer);
        cvPresident = (CardView) view.findViewById(R.id.cvPresident);
        vpBannerSlides = (AutoScrollViewPager) view.findViewById(R.id.vpBannerSlides);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);

//        llMyIshrae = (LinearLayout) view.findViewById(R.id.llMyIshrae);
//        llMyProfile = (LinearLayout) view.findViewById(R.id.llMyProfile);
//        llMyChapters = (LinearLayout) view.findViewById(R.id.llMyChapters);
//        llHQDetails = (LinearLayout) view.findViewById(R.id.llHQDetails);
//        llNewsEvents = (LinearLayout) view.findViewById(R.id.llNewsEvents);
//        llProgram = (LinearLayout) view.findViewById(R.id.llProgram);
//
//        llElearning = (LinearLayout) view.findViewById(R.id.llElearning);
//        llForum = (LinearLayout) view.findViewById(R.id.llForum);
        fmBanner = (FrameLayout) view.findViewById(R.id.fmBanner);
        rvNavMenuHome = (RecyclerView) view.findViewById(R.id.rvNavMenuHome);

//        llShop = (LinearLayout) view.findViewById(R.id.llShop);
        txtSponsorName = (TextView) view.findViewById(R.id.txtSponsorName);
        txtSponsorMessage = (TextView) view.findViewById(R.id.txtSponsorMessage);

        imgSponsor = (ImageView) view.findViewById(R.id.imgSponsor);


//        llMyIshrae.setOnClickListener(this);
//        llMyProfile.setOnClickListener(this);
//        llMyChapters.setOnClickListener(this);
//        llHQDetails.setOnClickListener(this);
//        llNewsEvents.setOnClickListener(this);
//        llProgram.setOnClickListener(this);
//
//        llShop.setOnClickListener(this);
//        llElearning.setOnClickListener(this);
//        llForum.setOnClickListener(this);

        int he = (SharedPref.getScreenW(getActivity()) / 2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, he);
        fmBanner.setLayoutParams(params);
        setViewPager();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);

        rvNavMenuHome.setLayoutManager(gridLayoutManager);

        rvNavMenuHome.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                manageHomeMenuClick(position);
            }
        }));
        menuModelArrayList = new ArrayList<>();
        manageNonMemberVisibility();
    }

    private void manageHomeMenuClick(int position) {
        HomeMenuModel homeMenuModel = menuModelArrayList.get(position);
        if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.my_ishrae))) {
            fragment = new MyISHAREFrag();

        } else if (homeMenuModel.menuName.equalsIgnoreCase(getResources().getString(R.string.My_Profile))) {
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
            dashboardActivity = new DashboardActivity();
            fragment = new ForumFrag();
            dashboardActivity.selectBottomBar();
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
        }
    }


    private void setViewPager() {
        String values = SharedPref.getUserModelJSON(getActivity());
        tmpSocialModel = (TmpSocialModel) Util.getJsonToClassObject(values, TmpSocialModel.class);

        if (tmpSocialModel != null) {
            if (tmpSocialModel.BannerList != null && tmpSocialModel.BannerList.size() > 0) {
                cvBannersContainer.setVisibility(View.VISIBLE);
                vpBannerSlides.setAdapter(new ViewPagerAdapter(getActivity(), tmpSocialModel.BannerList));
                vpBannerSlides.startAutoScroll();
                vpBannerSlides.setInterval(5000);
                vpBannerSlides.setBorderAnimation(false);
                vpBannerSlides.setCurrentItem(0);
                indicator.setViewPager(vpBannerSlides);
                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float arg1, int arg2) {
                        //TODO..
                    }

                    @Override
                    public void onPageSelected(int arg0) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int arg0) {
                    }
                });
            } else {
                cvBannersContainer.setVisibility(View.GONE);
            }
            if (tmpSocialModel.PresidentMessage != null) {
                cvPresident.setVisibility(View.VISIBLE);
                txtSponsorName.setText(tmpSocialModel.PresidentMessage.Name);
                txtSponsorMessage.setText(tmpSocialModel.PresidentMessage.LongMessege);

                if (!TextUtils.isEmpty(tmpSocialModel.PresidentMessage.ImagePath))
                    Glide.with(getActivity())
                            .load(tmpSocialModel.PresidentMessage.ImagePath)
                            .placeholder(R.mipmap.ic_place_holder)
                            .priority(Priority.IMMEDIATE)
                            .error(R.mipmap.ic_place_holder)
                            .into(imgSponsor);
            } else {
                cvPresident.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.dashboard));
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == R.id.llMyIshrae) {
//            fragment = new MyISHAREFrag();
//
//        } else if (view.getId() == R.id.llMyProfile) {
//            DashboardActivity.bottomBar.selectTabWithId(R.id.tabMyProfile);
//            fragment = new MyProfileFrag();
//
//        } else if (view.getId() == R.id.llMyChapters) {
//            fragment = new MyChapter();
//
//        } else if (view.getId() == R.id.llHQDetails) {
//            fragment = new HqDetailsFrag();
//
//        } else if (view.getId() == R.id.llNewsEvents) {
//            fragment = new NewsEventsFrag();
//
//        } else if (view.getId() == R.id.llProgram) {
//            fragment = new ProgramFrag();
//
//        } else if (view.getId() == R.id.llShop) {
//            fragment = new ProductListFrag();
//
//        } else if (view.getId() == R.id.llElearning) {
//            fragment = new ELearningFrag();
//
//        } else if (view.getId() == R.id.llForum) {
//            fragment = new ForumFrag();
//        }
//
//        if (fragment != null) {
//            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//            fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
//        }
    }

    public void manageNonMemberVisibility() {
        if (tmpSocialModel != null && tmpSocialModel.Roles != null && tmpSocialModel.Roles.size() > 0) {
            String[] memberNameArray = getActivity().getResources().getStringArray(R.array.home_menu_member_items);
            Integer[] memberIconIdArray = new Integer[]{R.mipmap.ic_home_my_share, R.mipmap.ic_home_my_profile, R.mipmap.ic_home_my_chapter, R.mipmap.ic_home_hq_detail, R.mipmap.ic_home_news_events, R.mipmap.ic_home_program, R.mipmap.ic_shop, R.mipmap.ic_elearning, R.mipmap.ic_forum};
            if (tmpSocialModel.Roles.get(0).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)||tmpSocialModel.Roles.get(0).equalsIgnoreCase(Constants.ROLL__STUDENT)) {
                memberNameArray = getActivity().getResources().getStringArray(R.array.home_menu_non_member_items);
                memberIconIdArray = new Integer[]{R.mipmap.ic_home_my_share, R.mipmap.ic_home_my_profile, R.mipmap.ic_home_news_events, R.mipmap.ic_shop, R.mipmap.ic_elearning, R.mipmap.ic_forum};
            }
            for (int i = 0; i < memberNameArray.length; i++) {
                HomeMenuModel homeMenuModel = new HomeMenuModel();
                homeMenuModel.iconId = memberIconIdArray[i];
                homeMenuModel.menuName = memberNameArray[i];
                menuModelArrayList.add(homeMenuModel);
            }

            HomeMenuAdapter homeMenuAdapter = new HomeMenuAdapter(getActivity(), menuModelArrayList);
            rvNavMenuHome.setAdapter(homeMenuAdapter);
        }
    }
}
