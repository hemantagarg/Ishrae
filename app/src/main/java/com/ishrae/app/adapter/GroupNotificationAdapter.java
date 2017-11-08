package com.ishrae.app.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.GroupNotificationModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.util.List;

/**
 * Created by linux on 16/12/16.
 */

public class GroupNotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private final int VIEW_PROG = 2;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private Context context;
    private View.OnClickListener onClickListener;
    public List<GroupNotificationModel> data;
    public GroupNotificationAdapter(Context context, View.OnClickListener onClickListener, List<GroupNotificationModel> data, RecyclerView recyclerView) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.data=data;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        recyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                });
    }

    @Override
    public int getItemViewType(int position) {
//        return data.get(position) != null ? TYPE_ITEM : VIEW_PROG;
        return  TYPE_ITEM;
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.group_notification_layout_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
if(holder1 instanceof ViewHolder)
{
    ViewHolder viewHolder= (ViewHolder) holder1;
   GroupNotificationModel dataBean =data.get(position);

//    viewHolder.rbNoti;
    viewHolder.  txtNotiTitle.setText(dataBean.Heading);
    viewHolder.  txtNotiDescr.setText(dataBean.Message);

    viewHolder. txtNotiStartDate.setText(context.getResources().getString(R.string.Start_Date)+" "+Util.convertDateTimeFormat(dataBean.StartDate, Constants.SERVER_DATE_FORMAT_COMING1, Constants.DATE_FORMAT_FOR_SHOWING));
    viewHolder.  txtNotiEndDate.setText(context.getResources().getString(R.string.End_Date)+" "+Util.convertDateTimeFormat(dataBean.EndDate, Constants.SERVER_DATE_FORMAT_COMING1, Constants.DATE_FORMAT_FOR_SHOWING));

//    Drawable cardBgColor=context.getResources().getDrawable(R.drawable.border_box_checkout);
//
//    viewHolder.llNotiRowContainer.setBackground(cardBgColor);

}


    }


    public void setLoaded() {
        loading = false;
    }
    public boolean isLoaded() {
        return loading;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txtNotiTitle;
        public TextView txtNotiDescr;
        public TextView txtNotiStartDate;
        public TextView txtNotiEndDate;


        public ViewHolder(View itemView) {
            super(itemView);
          txtNotiTitle= (TextView) itemView.findViewById(R.id.txtNotiTitle);
         txtNotiDescr= (TextView) itemView.findViewById(R.id.txtNotiDescr);
            txtNotiStartDate= (TextView) itemView.findViewById(R.id.txtNotiStartDate);
            txtNotiEndDate= (TextView) itemView.findViewById(R.id.txtNotiEndDate);
        }

    }

}
