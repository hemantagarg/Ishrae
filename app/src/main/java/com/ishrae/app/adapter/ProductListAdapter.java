package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.tempModel.TmpProductListModel;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class ProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<TmpProductListModel.ProductListModel> list;

    private View.OnClickListener onClickListener;

    OnLoadMoreListener onLoadMoreListener;
    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 5;
    private int lastVisibleItem;
    private boolean loading;
    private int totalItemCount;

    public ProductListAdapter(Context mContext, ArrayList<TmpProductListModel.ProductListModel> list, View.OnClickListener onClickListener1, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1) {
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

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_row, parent, false);
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

            ProductListAdapter.ViewHolder holder = (ViewHolder) holder1;

            final TmpProductListModel.ProductListModel product = list.get(position);

            holder.txtProductName.setText("" + product.ProductName);
            holder.txtProductPrice.setText(mContext.getResources().getString(R.string.Rs) + " " + product.MemberPrice);

            Glide.with(mContext)
                    .load(product.ThumbImagePath)
                    .priority(Priority.IMMEDIATE)

                    .into(holder.imvProductImg);

            holder.btnBuy.setTag(product);
            holder.btnBuy.setOnClickListener(onClickListener);
            holder.llProductContainer.setTag(product);
            holder.llProductContainer.setOnClickListener(onClickListener);

        } else if (holder1 instanceof ProductListAdapter.ProgressViewHolder) {
            // TODO: 5/15/2017
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtProductName;
        TextView txtProductPrice;
        ImageView imvProductImg;
        LinearLayout llProductContainer;
        Button btnBuy;

        public ViewHolder(View itemView) {
            super(itemView);

            txtProductName = (TextView) itemView.findViewById(R.id.txtProductName);
            txtProductPrice = (TextView) itemView.findViewById(R.id.txtProductPrice);
            imvProductImg = (ImageView) itemView.findViewById(R.id.imvProductImg);
            btnBuy = (Button) itemView.findViewById(R.id.btnBuy);
            llProductContainer = (LinearLayout) itemView.findViewById(R.id.llProductContainer);
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
