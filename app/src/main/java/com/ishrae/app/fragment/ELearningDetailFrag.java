package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.Course;
import com.ishrae.app.utilities.Util;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class ELearningDetailFrag extends Fragment implements View.OnClickListener {

    private View view;

    private TextView activityTitle;

    private TextView txtMemberFeeELD;
    private TextView txtDescrELD;
    private TextView txtTitleELD;

    private ImageView imvELImg;

    private Button btnBuyELD;

    private Course course;

    public static ELearningDetailFrag newInstance(Course course) {
        Bundle args = new Bundle();
        ELearningDetailFrag fragment = new ELearningDetailFrag();
        fragment.setArguments(args);
        fragment.course = course;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.elearning_detail, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        imvELImg = (ImageView) view.findViewById(R.id.imvELImg);

        txtMemberFeeELD = (TextView) view.findViewById(R.id.txtMemberFeeELD);
        txtDescrELD = (TextView) view.findViewById(R.id.txtDescrELD);
        txtTitleELD = (TextView) view.findViewById(R.id.txtTitleELD);

        btnBuyELD = (Button) view.findViewById(R.id.btnBuyELD);

        btnBuyELD.setOnClickListener(this);

        setData();
    }

    public void setData() {
        if (course != null) {
            Glide.with(getActivity())
                    .load(course.ThumbImage)
                    .placeholder(R.mipmap.ic_logo_300)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_logo_300)
                    .fallback(R.mipmap.ic_logo_300)
                    .into(imvELImg);
            txtTitleELD.setText("" + course.CourseName);
            txtDescrELD.setText("" + course.ShortDescription);

            txtMemberFeeELD.setText(" " + course.MemberPrice);
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
            String url = "http://elearning.ishraehq.in/Course/" + course.CourseName + "/" + course.CourseId;
            Util.openUrlOnBrowser(getActivity(), url);
        }
    }
}
