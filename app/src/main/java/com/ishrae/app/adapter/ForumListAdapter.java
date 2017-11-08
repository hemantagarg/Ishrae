package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
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
import com.ishrae.app.model.ForumModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;


import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class ForumListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public ArrayList<ForumModel> list;
    OnLoadMoreListener onLoadMoreListener;
    private static final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public ForumListAdapter(Context mContext, ArrayList<ForumModel> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {
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
        int viewType = VIEW_PROG;
        if (list != null && list.size() > 0 && list.get(position) != null) {
            viewType = VIEW_ITEM;
        }
        return viewType;
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
            ForumListAdapter.ProgressViewHolder vh = new ForumListAdapter.ProgressViewHolder(view);
            return vh;

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_row, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof ViewHolder) {

            ForumListAdapter.ViewHolder holder = (ViewHolder) holder1;
            final ForumModel forumModel = list.get(position);
            holder.txtCreatedBy.setText(" " + forumModel.CreatedBy);
            holder.txtCreatedDate.setText("" + Util.convertDateTimeFormat(forumModel.CreatedDate, Constants.SERVER_DATE_FORMAT_COMING, Constants.DATE_FORMAT_FOR_SHOWING));
            holder.txtTopic.setText("" + forumModel.Topic);
            holder.txtDescription.setText("" + forumModel.Description);
            holder.txtActiveUserF.setText(" " + forumModel.TotalActiveUser);
            holder.txtMessageCountF.setText(" " + forumModel.TotalDiscussion);

            if (!TextUtils.isEmpty(forumModel.ImageProfile)) {

                Glide.with(mContext)
                        .load(forumModel.ImageProfile)
                        .placeholder(R.mipmap.ic_default_user)
                        .priority(Priority.IMMEDIATE)
                        .error(R.mipmap.ic_default_user)
                        .fallback(R.mipmap.ic_default_user)
                        .into(holder.imvForumUser);
            }
        } else if (holder1 instanceof ForumListAdapter.ProgressViewHolder) {
            // TODO: 5/15/2017
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtCreatedBy;
        TextView txtCreatedDate;
        TextView txtDescription;
        TextView txtTopic;
        TextView txtActiveUserF;
        TextView txtMessageCountF;
        ImageView imvForumUser;

        public ViewHolder(View itemView) {
            super(itemView);

            txtCreatedBy = (TextView) itemView.findViewById(R.id.txtCreatedBy);
            txtCreatedDate = (TextView) itemView.findViewById(R.id.txtCreatedDate);
            txtDescription = (TextView) itemView.findViewById(R.id.txtDescription);
            txtTopic = (TextView) itemView.findViewById(R.id.txtTopic);
            txtActiveUserF = (TextView) itemView.findViewById(R.id.txtActiveUserF);
            txtMessageCountF = (TextView) itemView.findViewById(R.id.txtMessageCountF);

            imvForumUser = (ImageView) itemView.findViewById(R.id.imvForumUser);

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
