package com.ishrae.app.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.fragment.AdminHomeFrag;
import com.ishrae.app.fragment.CommonFormFragment;
import com.ishrae.app.fragment.EmailFrag;
import com.ishrae.app.fragment.ForumFrag;
import com.ishrae.app.fragment.GroupNotificationFrag;
import com.ishrae.app.fragment.HomeFrag;
import com.ishrae.app.fragment.HqDetailsFrag;
import com.ishrae.app.fragment.MyISHAREFrag;
import com.ishrae.app.fragment.MyProfileFrag;
import com.ishrae.app.fragment.NavMenuFrag;
import com.ishrae.app.fragment.NewsEventsFrag;
import com.ishrae.app.fragment.NotificationFrag;
import com.ishrae.app.fragment.SocialFrag;
import com.ishrae.app.model.PollOfDayResultModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.UserDetailsTemp;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 21-03-2017.
 */

public class DashboardActivity extends BaseAppCompactActivity implements NavMenuFrag.FragmentDrawerListener, View.OnClickListener, Callback {


    private Toolbar toolbar;

    private NavMenuFrag navMenuFrag;

    boolean doubleBackToExitPressedOnce = false;

    private Fragment fragment = null;

    private Context mContext;

    public static BottomBar bottomBar;

    private ImageView imgAppLogo;
    private AlertDialog dialog;

    private Dialog notificationDialog;
    private NetworkResponse resp;

    private FragmentManager fm;

    private TextView cancle;
    private TextView showAll;

    private UserDetailsTemp userDetailsTemp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        mContext = DashboardActivity.this;
        initialize();
    }

    private void initialize() {

        userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(SharedPref.getUserModelJSON(mContext), UserDetailsTemp.class);

        fm = this.getSupportFragmentManager();

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);

        imgAppLogo = (ImageView) findViewById(R.id.imgAppLogo);

        imgAppLogo.setOnClickListener(this);

        setSupportActionBar(toolbar);

        navMenuFrag = (NavMenuFrag) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        navMenuFrag.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawerLayout), toolbar);
        navMenuFrag.setDrawerListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                if (tabId == R.id.tabHome) {
                    if (Util.getUserRole(DashboardActivity.this).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)||Util.getUserRole(DashboardActivity.this).equalsIgnoreCase(Constants.ROLL__STUDENT)) {
                        fragment = new HomeFrag();
                    } else if (Util.getUserRole(DashboardActivity.this).equalsIgnoreCase(Constants.ROLL_MEMBER)) {
                        fragment = new HomeFrag();
                    } else {
                        fragment = new AdminHomeFrag();
                    }
                } else if (tabId == R.id.tabEmail) {
                    fragment = new EmailFrag();
                } else if (tabId == R.id.tabShare) {
                    fragment = new ForumFrag();
                } else if (tabId == R.id.tabNotif) {
                    fragment = new NotificationFrag();
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).commit();
                }
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tabHome) {
                    if (Util.getUserRole(DashboardActivity.this).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)||Util.getUserRole(DashboardActivity.this).equalsIgnoreCase(Constants.ROLL__STUDENT)) {
                        fragment = new HomeFrag();
                    } else if (Util.getUserRole(DashboardActivity.this).equalsIgnoreCase(Constants.ROLL_MEMBER)) {
                        fragment = new HomeFrag();
                    } else {
                        fragment = new AdminHomeFrag();
                    }

                } else if (tabId == R.id.tabEmail) {
                    fragment = new EmailFrag();

                } else if (tabId == R.id.tabShare) {
                    fragment = new ForumFrag();

                } else if (tabId == R.id.tabNotif) {
                    fragment = new EmailFrag();
                }
                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).commit();
                }
            }
        });

        String roll=Util.getUserRole(this);
        if (roll.equalsIgnoreCase(Constants.ROLL_CHAPTER)||roll.equalsIgnoreCase(Constants.ROLL__STUDENT) || roll.equalsIgnoreCase(Constants.ROLL_NON_MEMBER)) {
            bottomBar.getTabAtPosition(1).setVisibility(View.GONE);
        }
        if (roll.equalsIgnoreCase(Constants.ROLL_CHAPTER))
        {
            bottomBar.setVisibility(View.GONE);
        }

//        if((!SharedPref.getDontShowMeAgain())&&Util.getUserRole(this).equalsIgnoreCase(Constants.ROLL_MEMBER)) {
//            GroupNotificationActivity groupNotificationActivity =new GroupNotificationActivity();
//            groupNotificationActivity.show(getSupportFragmentManager(),null);

        if (userDetailsTemp.GroupNotificationCount > 0) {

            dialog = Util.retryAlertDialog(this, getResources().getString(R.string.notification), getResources().getString(R.string.you_have_new_group_notifications), getResources().getString(R.string.cancel), getResources().getString(R.string.show_all), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment = new GroupNotificationFrag();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
//                        Util.logout(DashboardActivity.this);
                    dialog.dismiss();
                }
            });
        }
//        }

    }

    public void selectBottomBar(){
        bottomBar.selectTabWithId(R.id.tabShare);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment frg = getSupportFragmentManager().findFragmentById(R.id.fmContainer);
        frg.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDrawerItemSelected(View view, String menuNmae) {
        displayView(menuNmae);
    }

    private void displayView(String menuNmae) {
        fragment = null;
        if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.my_profile))) {
            bottomBar.selectTabWithId(R.id.tabHome);
            fragment = new MyProfileFrag();
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Mails))) {
            fragment = null;
            bottomBar.selectTabWithId(R.id.tabEmail);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.my_ishrae))) {
            bottomBar.selectTabWithId(R.id.tabHome);
            fragment = new MyISHAREFrag();
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.HQ_Details))) {
            bottomBar.selectTabWithId(R.id.tabHome);
            fragment = new HqDetailsFrag();
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.news_events))) {
            bottomBar.selectTabWithId(R.id.tabHome);
            fragment = new NewsEventsFrag();
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.notification))) {
            fragment = null;
            bottomBar.selectTabWithId(R.id.tabNotif);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.group_notification))) {
            fragment = new GroupNotificationFrag();
//            bottomBar.selectTabWithId(R.id.tabHome);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Social))) {
//            fragment = new SocialFrag();
//            bottomBar.selectTabWithId(R.id.tabShare);
            fragment=null;
            SocialFrag socialFrag=new SocialFrag();
            socialFrag.show(getSupportFragmentManager(),null);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Logout))) {

                dialog=  Util.retryAlertDialog(this, getResources().getString(R.string.app_name), getResources().getString(R.string.Are_you_sure_to_logout), getResources().getString(R.string.cancel), getResources().getString(R.string.Yes), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logout(true);
//                        Util.logout(DashboardActivity.this);
                        dialog.dismiss();
                    }
                });


        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.home))) {
            bottomBar.selectTabWithId(R.id.tabHome);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.post_event))) {
            Util.startActivity(DashboardActivity.this, CIQEventsListAct.class);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Poll_Of_Day))) {
            fragment = new PollOfDayAct();
//            Util.startActivity(DashboardActivity.this, PollOfDayAct.class);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Poll_Result))) {
            fragment = null;
            Util.startActivity(DashboardActivity.this, PollResultAct.class);
        } else if (menuNmae.equalsIgnoreCase(mContext.getResources().getString(R.string.Feedback))) {
            fragment = new CommonFormFragment();
            Bundle bundle=new Bundle();
            bundle.putString(Constants.FLD_ACTIONNAME,Constants.FLD_ACTION_FEEDBACKFORM);
            bundle.putString(Constants.FLD_HEADER_TITLE,menuNmae);
               fragment.setArguments(bundle);
//            Util.setCommonFormAct(DashboardActivity.this,Constants.FLD_ACTION_FEEDBACKFORM,menuNmae,null);
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fmContainer, fragment).addToBackStack(null).commit();
        }
    }

    private void deselectToCurrentTab() {
        bottomBar.getCurrentTab().deselect(true);
    }

    @Override
    public void onBackPressed() {
        backCountToExit();
    }



    private void backCountToExit() {

        if (fm.getBackStackEntryCount() > 1 ){

            fm.popBackStack();


        } else {
            if (doubleBackToExitPressedOnce) {
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.imgAppLogo) {
//            bottomBar.setDefaultTabPosition(0);
            Util.startActivity(mContext, PollOfDayAct.class);
        }

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
//            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                                Util.logout(DashboardActivity.this);
//                            }
                        }
                    });
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout(boolean showProIndicator) {
        if (Util.isDeviceOnline(this, true)) {
            JSONObject params = CmdFactory.logoutCmd();
            NetworkManager.requestForAPI(this, this, Constants.VAL_POST, AppUrls.LOGOUT_URL, params.toString(), showProIndicator);
        }
    }
}
