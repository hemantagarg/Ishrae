package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.PendingPostEventListModel;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class PollOfDayResultRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<PendingPostEventListModel> list;

    OnLoadMoreListener onLoadMoreListener;
    private static final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
private View.OnClickListener onClickListener;

    public PollOfDayResultRowAdapter(Context mContext, ArrayList<PendingPostEventListModel> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.list = list;
        this.onClickListener = onClickListener;
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
            ProgressViewHolder vh = new ProgressViewHolder(view);
            return vh;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_of_day_result_row, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;
            final PendingPostEventListModel pendingPostEventListModel = list.get(position);
            holder.txtType.setText("" +pendingPostEventListModel.Type);
            holder.txtAvailableCount.setText("" +pendingPostEventListModel.AvaliableDays);
            holder.txtHeading.setText("" +pendingPostEventListModel.Heading);

            if(pendingPostEventListModel.Venue==null)
                pendingPostEventListModel.Venue="";
            holder.txtVenue.setText("" +pendingPostEventListModel.Venue);
            holder.txtCreatedBy.setText("" +pendingPostEventListModel.CreatedBy);
            holder.txtUpdatedBy.setText("" +pendingPostEventListModel.UpdateddBy);
            holder.txtStartDate.setText("" +pendingPostEventListModel.Start);
            holder.txtEndDate.setText("" +pendingPostEventListModel.End);

//            Glide.with(mContext)
//                    .load(newsEvents.url)
//                    .placeholder(R.mipmap.ic_place_holder)
//                    .priority(Priority.IMMEDIATE)
//                    .error(R.mipmap.ic_place_holder)
//                    .fallback(R.mipmap.ic_place_holder)
//                    .into(holder.imvNewsImage);


        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtType;
        TextView txtAvailableCount;
        TextView txtHeading;
        TextView txtVenue;
        TextView txtCreatedBy;
        TextView txtUpdatedBy;
        TextView txtStartDate;
        TextView txtEndDate;


        public ViewHolder(View itemView) {
            super(itemView);
            txtType = (TextView) itemView.findViewById(R.id.txtType);
            txtHeading = (TextView) itemView.findViewById(R.id.txtHeading);
            txtVenue = (TextView) itemView.findViewById(R.id.txtVenue);
            txtCreatedBy = (TextView) itemView.findViewById(R.id.txtCreatedBy);
            txtAvailableCount = (TextView) itemView.findViewById(R.id.txtAvailableCount);
            txtUpdatedBy = (TextView) itemView.findViewById(R.id.txtUpdatedBy);
            txtStartDate = (TextView) itemView.findViewById(R.id.txtStartDate);
            txtEndDate = (TextView) itemView.findViewById(R.id.txtEndDate);

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
