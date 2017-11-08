package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.PendingPostEventListModel;
import com.ishrae.app.model.PollOfDayModel;
import com.ishrae.app.model.PollOfDayResultModel;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class PollOfResultRowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<PollOfDayResultModel.OpinionPollChoicesWithStatusBean> list;



    public PollOfResultRowAdapter(Context mContext, ArrayList<PollOfDayResultModel.OpinionPollChoicesWithStatusBean> list) {
        this.mContext = mContext;
        this.list = list;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_of_day_result_row, parent, false);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;
            final PollOfDayResultModel.OpinionPollChoicesWithStatusBean pollOfDayResultModel = list.get(position);
            holder.txtAnswerName.setText("" + pollOfDayResultModel.Choice);
            holder.txtAnswerAttampt.setText("" + pollOfDayResultModel.StatusInPercent+mContext.getResources().getString(R.string.Percentage));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAnswerName;
        TextView txtAnswerAttampt;


        public ViewHolder(View itemView) {
            super(itemView);
            txtAnswerName = (TextView) itemView.findViewById(R.id.txtAnswerName);
            txtAnswerAttampt = (TextView) itemView.findViewById(R.id.txtAnswerAttampt);

        }
    }



}
