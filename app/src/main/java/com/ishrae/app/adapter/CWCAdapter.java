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
import com.ishrae.app.model.CWC;

import java.util.ArrayList;

import static com.ishrae.app.R.string.bog;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class CWCAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    public ArrayList<CWC> list;

    OnLoadMoreListener onLoadMoreListener;
    private static final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    public CWCAdapter(Context mContext, ArrayList<CWC> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {
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
        return list.get(position) != null ? VIEW_ITEM : VIEW_PROG;
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bog_row, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof ViewHolder) {

            ViewHolder holder = (ViewHolder) holder1;
            CWC cwc = list.get(position);
            holder.txtBOGName.setText("" + cwc.Name);
            holder.txtBOGCompany.setText("" + cwc.CompanyName);
            holder.txtBOGDesignation.setText("" + cwc.Designation);
            holder.txtBOGEmail.setText("" + cwc.Email);
            Glide.with(mContext)
                    .load(cwc.MemberPhoto)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_place_holder)
                    .fallback(R.mipmap.ic_place_holder)
                    .into(holder.imvBOGImage);

        } else if (holder1 instanceof ProgressViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtBOGName;
        TextView txtBOGCompany;
        TextView txtBOGDesignation;
        TextView txtBOGEmail;
        ImageView imvBOGImage;

        public ViewHolder(View itemView) {
            super(itemView);
            txtBOGName = (TextView) itemView.findViewById(R.id.txtBOGName);
            txtBOGCompany = (TextView) itemView.findViewById(R.id.txtBOGCompany);
            txtBOGDesignation = (TextView) itemView.findViewById(R.id.txtBOGDesignation);
            txtBOGEmail = (TextView) itemView.findViewById(R.id.txtBOGEmail);
            imvBOGImage = (ImageView) itemView.findViewById(R.id.imvBOGImage);
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