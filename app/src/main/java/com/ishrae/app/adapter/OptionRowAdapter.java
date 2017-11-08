package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.PollOfDayModel;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class OptionRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<PollOfDayModel.OpinionPollChoicesBean> list;


    public OptionRowAdapter(Context mContext, ArrayList<PollOfDayModel.OpinionPollChoicesBean> list) {
        this.mContext = mContext;
        this.list = list;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.option_layout, parent, false);
            return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;
            final PollOfDayModel.OpinionPollChoicesBean pendingPostEventListModel = list.get(position);
            holder.txtOptionName.setText("" +pendingPostEventListModel.getChoice());
            holder.imgFSCSelect.setImageResource(R.mipmap.ic_unselected);
            if(pendingPostEventListModel.getSelected())
                holder.imgFSCSelect.setImageResource(R.mipmap.ic_selected);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtOptionName;
       LinearLayout llOptionContainer;

        ImageView imgFSCSelect;


        public ViewHolder(View itemView) {
            super(itemView);
            txtOptionName = (TextView) itemView.findViewById(R.id.txtOptionName);
            llOptionContainer = (LinearLayout) itemView.findViewById(R.id.llOptionContainer);
            imgFSCSelect = (ImageView) itemView.findViewById(R.id.imgFSCSelect);

        }
    }

  
}
