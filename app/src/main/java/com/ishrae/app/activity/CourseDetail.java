package com.ishrae.app.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.Course;
import com.ishrae.app.utilities.Util;

import java.text.DecimalFormat;

/**
 * Created by Nss Solutions on 24-04-2017.
 */

public class CourseDetail extends BaseAppCompactActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView activityTitle;

    private TextView txtMemberFeeELD;
    private TextView txtDescrELD;
    private TextView txtTitleELD;

    private ImageView imvELImg;

    private Button btnBuyELD;

    private Course course;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.elearning_detail);
        course = (Course) getIntent().getExtras().getSerializable("detail");
        mContext = CourseDetail.this;
        initialize();
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        activityTitle = (TextView) findViewById(R.id.activityTitle);

        imvELImg = (ImageView) findViewById(R.id.imvELImg);

        txtMemberFeeELD = (TextView) findViewById(R.id.txtMemberFeeELD);
        txtDescrELD = (TextView) findViewById(R.id.txtDescrELD);
        txtTitleELD = (TextView) findViewById(R.id.txtTitleELD);

        btnBuyELD = (Button) findViewById(R.id.btnBuyELD);

        btnBuyELD.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        activityTitle.setText(getResources().getString(R.string.course_details));

        setData();
    }

    public void setData() {
        if (course != null) {
            Glide.with(mContext)
                    .load(course.ThumbImage)
                    .placeholder(R.mipmap.ic_logo_300)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_logo_300)
                    .fallback(R.mipmap.ic_logo_300)
                    .into(imvELImg);

            txtTitleELD.setText("" + course.CourseName);
            txtDescrELD.setText(Html.fromHtml(course.DetailDescription));
            txtMemberFeeELD.setText(" " + new DecimalFormat("##.##").format(Double.parseDouble(course.MemberPrice)));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Elearning_Details));
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();
        if (vId == R.id.btnBuyELD) {
            Util.openUrlOnBrowser(mContext, course.DetailPageUrl);
        }
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
