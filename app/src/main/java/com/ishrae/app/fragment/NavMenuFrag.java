package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.activity.DashboardActivity;
import com.ishrae.app.adapter.NavMenuAdapter;
import com.ishrae.app.custom.CircleImageView;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.tempModel.UserDetailsTemp;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.DividerItemDecorationGray;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;


/**
 * Created by Nss Solutions on 16-11-2016.
 */

public class NavMenuFrag extends Fragment implements View.OnClickListener {

    private View view;

    private TextView txtUsername;

    private CircleImageView imvUserImage;

    private RecyclerView rvNavMenuList;

    private NavMenuAdapter adapter;

    private FragmentDrawerListener drawerListener;

    private DrawerLayout mDrawerLayout;
    private View containerView;

    private ActionBarDrawerToggle mDrawerToggle;

    private String[] navigationDrawerItems;

    private UserDetailsTemp userDetailsTemp;

    private RelativeLayout relProfileSideBar;

    public void setDrawerListener(DashboardActivity listener) {
        this.drawerListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.nav_menu, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(SharedPref.getUserModelJSON(getActivity()), UserDetailsTemp.class);

        txtUsername = (TextView) view.findViewById(R.id.txtUsername);

        imvUserImage = (CircleImageView) view.findViewById(R.id.imvUserImage);

        relProfileSideBar = (RelativeLayout) view.findViewById(R.id.relProfileSideBar);

        rvNavMenuList = (RecyclerView) view.findViewById(R.id.rvNavMenuList);
        if (Util.getUserRole(getActivity()).equalsIgnoreCase(Constants.ROLL_NON_MEMBER)) {
            navigationDrawerItems = getResources().getStringArray(R.array.nav_drawer_items_non_member);
        } else if (Util.getUserRole(getActivity()).equalsIgnoreCase(Constants.ROLL_MEMBER)) {
            navigationDrawerItems = getResources().getStringArray(R.array.nav_drawer_items);
        } else
            navigationDrawerItems = getResources().getStringArray(R.array.nav_drawer_items_admin);


        rvNavMenuList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNavMenuList.addItemDecoration(new DividerItemDecorationGray(getActivity()));
        adapter = new NavMenuAdapter(getActivity(), navigationDrawerItems);
        rvNavMenuList.setAdapter(adapter);

        rvNavMenuList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, navigationDrawerItems[position]);
                mDrawerLayout.closeDrawer(containerView);
            }
        }));
        relProfileSideBar.setOnClickListener(this);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
                setData();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    @Override
    public void onClick(View view) {
        int vId=view.getId();
        if(vId==R.id.relProfileSideBar)
        {
            drawerListener.onDrawerItemSelected(view, navigationDrawerItems[0]);
            mDrawerLayout.closeDrawer(containerView);
        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, String menuName);
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private void setData() {
        if (userDetailsTemp.userprofile != null) {
            txtUsername.setText("" + userDetailsTemp.userprofile.FullName);
            if (!TextUtils.isEmpty(userDetailsTemp.userprofile.ProfileImage))
                Glide.with(getActivity())
                        .load(userDetailsTemp.userprofile.ProfileImage).placeholder(R.mipmap.ic_default_user)
                        .priority(Priority.IMMEDIATE)
                        .error(R.mipmap.ic_default_user)
                        .fallback(R.mipmap.ic_default_user)
                        .into(imvUserImage);
        }
    }
}
