package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.tempModel.TmpSocialModel;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Risheek on 1/31/2016.
 */
public class ViewPagerAdapter extends PagerAdapter {

    Context mContext;
    ArrayList<TmpSocialModel.BannerListDatabean> bannerListDatabeen;

    public ViewPagerAdapter(Context mContext, ArrayList<TmpSocialModel.BannerListDatabean> bannerListDatabeen) {
        this.mContext = mContext;
        this.bannerListDatabeen = bannerListDatabeen;
    }

    @Override
    public int getCount() {
        return bannerListDatabeen.size();
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view == ((ImageView) object);
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (SharedPref.getScreenW(mContext) / 2));
        final ImageView imageView = new ImageView(mContext);

        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        if (!TextUtils.isEmpty(bannerListDatabeen.get(position).ImagePath))
            Glide.with(mContext)
                    .load(bannerListDatabeen.get(position).ImagePath)
                    .placeholder(R.mipmap.ic_place_holder_720_360)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_place_holder_720_360)
                    .into(imageView);

        imageView.setLayoutParams(params);
        ((ViewPager) container).addView(imageView, 0);
        imageView.setTag(bannerListDatabeen.get(position));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TmpSocialModel.BannerListDatabean bannerListDatabean = (TmpSocialModel.BannerListDatabean) v.getTag();
                Util.openUrlOnBrowser(mContext, bannerListDatabean.ImageUrl);
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView) object);
    }
}
