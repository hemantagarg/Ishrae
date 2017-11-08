package com.ishrae.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.NewsEvents;

/**
 * Created by Nss Solutions on 30-03-2017.
 */

public class NewsDetails extends BaseAppCompactActivity {

    private Toolbar toolbar;
    private TextView activityTitle;

    private TextView txtNewsTitle;
    private TextView txtNewsDate;
    private TextView txtNewsDesc;
    private ImageView imvNewsImage;

    private Context mContext;

    private NewsEvents newsEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_details);
        mContext = NewsDetails.this;
        newsEvents = (NewsEvents) getIntent().getSerializableExtra("news");
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);

        txtNewsTitle = (TextView) findViewById(R.id.txtNewsTitle);
        txtNewsDate = (TextView) findViewById(R.id.txtNewsDate);
        txtNewsDesc = (TextView) findViewById(R.id.txtNewsDesc);
        imvNewsImage = (ImageView) findViewById(R.id.imvNewsImage);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        activityTitle.setText(getResources().getString(R.string.news_details));

        setData();
    }

    private void setData() {
        txtNewsTitle.setText("" + newsEvents.Title);
        txtNewsDate.setText("" + newsEvents.NewsDate);
        txtNewsDesc.setText("" + Html.fromHtml(newsEvents.Description));

        Glide.with(mContext)
                .load(newsEvents.Image)
                .placeholder(R.mipmap.ic_place_holder)
                .priority(Priority.IMMEDIATE)
                .error(R.mipmap.ic_place_holder)
                .fallback(R.mipmap.ic_place_holder)
                .into(imvNewsImage);
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
