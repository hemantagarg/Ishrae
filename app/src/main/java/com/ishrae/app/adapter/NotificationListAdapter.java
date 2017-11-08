package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.tempModel.NotificationModelTmp;
import com.ishrae.app.tempModel.TmpProductListModel;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class NotificationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<NotificationModelTmp.FcmNotificationEntitysBean> list;

    private View.OnClickListener onClickListener;

    OnLoadMoreListener onLoadMoreListener;
    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem;
    private boolean loading;
    private int totalItemCount;

    public NotificationListAdapter(Context mContext, ArrayList<NotificationModelTmp.FcmNotificationEntitysBean> list, View.OnClickListener onClickListener1, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {

        this.mContext = mContext;
        this.list = list;
        this.onClickListener = onClickListener1;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_PROG) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_item, parent, false);
            ProgressViewHolder vh = new ProgressViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout_row, parent, false);
            return new ViewHolder(view);
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            NotificationListAdapter.ViewHolder holder = (ViewHolder) holder1;
            final NotificationModelTmp.FcmNotificationEntitysBean notificationEntitysBean = list.get(position);
            holder.txtNotiTitle.setText("" + notificationEntitysBean.Title);
            holder.txtNotiDescr.setText(notificationEntitysBean.Message);
        } else if (holder1 instanceof NotificationListAdapter.ProgressViewHolder) {
            // TODO: 5/15/2017
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNotiTitle;
        TextView txtNotiDescr;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNotiTitle = (TextView) itemView.findViewById(R.id.txtNotiTitle);
            txtNotiDescr = (TextView) itemView.findViewById(R.id.txtNotiDescr);
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
