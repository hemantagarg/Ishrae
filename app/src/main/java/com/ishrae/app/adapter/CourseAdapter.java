package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.Course;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<Course> list;


    OnLoadMoreListener onLoadMoreListener;
    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem;
    private boolean loading;
    private int totalItemCount;

    public CourseAdapter(Context mContext, ArrayList<Course> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {
        this.mContext = mContext;
        this.list = list;
        this.onLoadMoreListener = onLoadMoreListener1;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (onLoadMoreListener != null) {

                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? TYPE_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }

    public boolean isLoaded() {
        return loading;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_PROG) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            ProductListAdapter.ProgressViewHolder vh = new ProductListAdapter.ProgressViewHolder(view);
            return vh;
        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.elearning_list_row, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof CourseAdapter.ViewHolder) {
            CourseAdapter.ViewHolder holder = (ViewHolder) holder1;

            Course course = list.get(position);

            holder.txtCourseTitle.setText("" + course.CourseName);
            holder.txtCourseDes.setText("" + course.ShortDescription);
            holder.txtMemberFee.setText(" " + new DecimalFormat("##.##").format(Double.parseDouble(course.MemberPrice)));

            Glide.with(mContext)
                    .load(course.ThumbImage)
                    .placeholder(R.mipmap.ic_place_holder)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_place_holder)
                    .fallback(R.mipmap.ic_place_holder)
                    .into(holder.imgElearning);
        } else if (holder1 instanceof CourseAdapter.ProgressViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCourseTitle;
        TextView txtCourseDes;
        TextView txtMemberFee;
        ImageView imgElearning;

        public ViewHolder(View itemView) {
            super(itemView);
            txtCourseTitle = (TextView) itemView.findViewById(R.id.txtCourseTitle);
            txtCourseDes = (TextView) itemView.findViewById(R.id.txtCourseDes);
            txtMemberFee = (TextView) itemView.findViewById(R.id.txtMemberFee);
            imgElearning = (ImageView) itemView.findViewById(R.id.imgElearning);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
