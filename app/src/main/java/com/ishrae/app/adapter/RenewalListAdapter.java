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
import com.ishrae.app.tempModel.RenewalTmp;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class RenewalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<RenewalTmp.RenewalListBean> list;

    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    public OnLoadMoreListener onLoadMoreListener;
    public int totalProductCount;

    public RenewalListAdapter(Context mContext, ArrayList<RenewalTmp.RenewalListBean> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {
        this.mContext = mContext;
        this.list = list;
        this.onLoadMoreListener = onLoadMoreListener1;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();

                if (!loading
                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    if (onLoadMoreListener != null) {

                        onLoadMoreListener.onLoadMore();
                    }
                    loading = true;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    isScrolling1.isScrolling(View.VISIBLE);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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

        } else if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.renewal_row, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    public int getItemViewType(int position) {
        if (list.size() == position) {
            return VIEW_PROG;
        }
        return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof RenewalListAdapter.ViewHolder) {

            RenewalListAdapter.ViewHolder holder = (ViewHolder) holder1;
            final RenewalTmp.RenewalListBean renewaltListModel = list.get(position);
            holder.txtEntryDate.setText("" + renewaltListModel.EntryDate);
            holder.txtPaymentMode.setText("" + renewaltListModel.PaymentMode);
            holder.txtAmount.setText("" + renewaltListModel.Amount);
            holder.txtRNDate.setText("" + renewaltListModel.RenewalDate);

        } else if (holder1 instanceof RenewalListAdapter.ProgressViewHolder) {
            // TODO: 5/15/2017
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);

            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtEntryDate;
        TextView txtPaymentMode;
        TextView txtAmount;
        TextView txtRNDate;

        public ViewHolder(View itemView) {
            super(itemView);

            txtEntryDate = (TextView) itemView.findViewById(R.id.txtEntryDate);
            txtPaymentMode = (TextView) itemView.findViewById(R.id.txtPaymentMode);
            txtAmount = (TextView) itemView.findViewById(R.id.txtAmount);
            txtRNDate = (TextView) itemView.findViewById(R.id.txtRNDate);
        }
    }
}
