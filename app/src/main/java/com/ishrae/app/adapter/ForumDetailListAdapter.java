package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.ForumModel;
import com.ishrae.app.model.SharedPref;
import com.ishrae.app.tempModel.UserDetailsTemp;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class ForumDetailListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ForumModel> list;

    OnLoadMoreListener onLoadMoreListener;

    private static final int VIEW_ITEM_CREATED_BY = 0;
    private static final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    private View.OnClickListener onClickListener;
    private int replyVisibility = View.GONE;

    private UserDetailsTemp userDetailsTemp;

    public ForumDetailListAdapter(Context mContext, ArrayList<ForumModel> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1, View.OnClickListener onClickListener, int replyVisibility) {
        this.mContext = mContext;
        this.onClickListener = onClickListener;
        this.list = list;

        this.onLoadMoreListener = onLoadMoreListener1;

        this.replyVisibility = replyVisibility;

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

        userDetailsTemp = (UserDetailsTemp) Util.getJsonToClassObject(SharedPref.getUserModelJSON(mContext), UserDetailsTemp.class);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM_CREATED_BY) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.created_by_message, parent, false);
            ViewHolderCreatedBy vh = new ViewHolderCreatedBy(view);
            return vh;

        } else if (viewType == VIEW_PROG) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            ProgressViewHolder vh = new ProgressViewHolder(view);
            return vh;

        } else {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.created_by_other_user_message, parent, false);
            return new ViewHolder(view);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {

            return VIEW_ITEM_CREATED_BY;

        } else
            return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    public void setLoaded() {
        loading = false;
    }

    public boolean isLoaded() {
        return loading;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof ViewHolderCreatedBy) {

            ForumDetailListAdapter.ViewHolderCreatedBy holder = (ViewHolderCreatedBy) holder1;
            final ForumModel forumDetailModel = list.get(position);
            holder.txtTopicFDCB.setText("" + forumDetailModel.Topic);
            holder.txtCreatedByFDCB.setText("" + forumDetailModel.CreatedBy);
            holder.txtDescriptionFDCB.setText("" + forumDetailModel.Description.trim());
            holder.txtPostedOnFDCB.setText("" + Util.convertDateTimeFormat(forumDetailModel.CreatedDate, Constants.SERVER_DATE_FORMAT_COMING, Constants.DATE_FORMAT_FOR_SHOWING));

            if (!TextUtils.isEmpty(forumDetailModel.ImageProfile)) {

                Glide.with(mContext)
                        .load(forumDetailModel.ImageProfile)
                        .placeholder(R.mipmap.ic_default_user)
                        .priority(Priority.IMMEDIATE)
                        .error(R.mipmap.ic_default_user)
                        .fallback(R.mipmap.ic_default_user)
                        .into(holder.imvFUDCB);
            }

            holder.imgReplyFDCB.setTag(forumDetailModel);
            holder.llReportCB.setTag(forumDetailModel);

            holder.imgReplyFDCB.setOnClickListener(onClickListener);
            holder.llReportCB.setOnClickListener(onClickListener);
            holder.imgReplyFDCB.setVisibility(replyVisibility);

            holder.imgReplyFDCB.setVisibility(View.GONE);

        } else if (holder1 instanceof ViewHolder) {

            ForumDetailListAdapter.ViewHolder holder = (ViewHolder) holder1;
            final ForumModel forumDetailModel = list.get(position);
            holder.txtTopicFD.setText("" + forumDetailModel.Topic);
            holder.txtCreatedByFD.setText("" + forumDetailModel.CreatedBy);
            holder.txtDescriptionFD.setText("" + forumDetailModel.Description);
            holder.txtPostedOnFD.setText("" + Util.convertDateTimeFormat(forumDetailModel.CreatedDate, Constants.SERVER_DATE_FORMAT_COMING, Constants.DATE_FORMAT_FOR_SHOWING));

            holder.imgReplyFD.setTag(forumDetailModel);
            holder.llReport.setTag(forumDetailModel);
            if (!TextUtils.isEmpty(forumDetailModel.ImageProfile)) {
                Glide.with(mContext)
                        .load(forumDetailModel.ImageProfile)
                        .placeholder(R.mipmap.ic_default_user)
                        .priority(Priority.IMMEDIATE)
                        .error(R.mipmap.ic_default_user)
                        .fallback(R.mipmap.ic_default_user)
                        .into(holder.imvFUD);
            }
            holder.imgReplyFD.setOnClickListener(onClickListener);
            holder.llReport.setOnClickListener(onClickListener);
            holder.imgReplyFD.setVisibility(replyVisibility);

            holder.imgReplyFD.setVisibility(View.GONE);

        } else if (holder1 instanceof RenewalListAdapter.ProgressViewHolder) {
            // TODO: 5/15/2017
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtTopicFD;
        TextView txtDescriptionFD;
        TextView txtPostedOnFD;
        TextView txtCreatedByFD;
        ImageView imvFUD;
        ImageView imgReplyFD;
        LinearLayout llReport;

        public ViewHolder(View itemView) {
            super(itemView);

            txtTopicFD = (TextView) itemView.findViewById(R.id.txtTopicFD);
            txtCreatedByFD = (TextView) itemView.findViewById(R.id.txtCreatedByFD);
            txtDescriptionFD = (TextView) itemView.findViewById(R.id.txtDescriptionFD);
            txtPostedOnFD = (TextView) itemView.findViewById(R.id.txtPostedOnFD);
            imvFUD = (ImageView) itemView.findViewById(R.id.imvFUD);
            imgReplyFD = (ImageView) itemView.findViewById(R.id.imgReplyFD);
            llReport = (LinearLayout) itemView.findViewById(R.id.llReport);

        }
    }

    public class ViewHolderCreatedBy extends RecyclerView.ViewHolder {

        TextView txtTopicFDCB;
        TextView txtCreatedByFDCB;
        TextView txtDescriptionFDCB;
        TextView txtPostedOnFDCB;
        ImageView imvFUDCB;
        ImageView imgReplyFDCB;
        LinearLayout llReportCB;

        public ViewHolderCreatedBy(View itemView) {
            super(itemView);

            txtTopicFDCB = (TextView) itemView.findViewById(R.id.txtTopicFDCB);
            txtDescriptionFDCB = (TextView) itemView.findViewById(R.id.txtDescriptionFDCB);
            txtCreatedByFDCB = (TextView) itemView.findViewById(R.id.txtCreatedByFDCB);
            txtPostedOnFDCB = (TextView) itemView.findViewById(R.id.txtPostedOnFDCB);
            imvFUDCB = (ImageView) itemView.findViewById(R.id.imvFUDCB);
            imgReplyFDCB = (ImageView) itemView.findViewById(R.id.imgReplyFDCB);
            llReportCB = (LinearLayout) itemView.findViewById(R.id.llReportCB);

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
