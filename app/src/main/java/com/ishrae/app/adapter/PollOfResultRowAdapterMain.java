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
import com.ishrae.app.model.PollOfDayResultModel;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class PollOfResultRowAdapterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<PollOfDayResultModel> list;

   
    

    public PollOfResultRowAdapterMain(Context mContext, ArrayList<PollOfDayResultModel> list, RecyclerView recyclerView, OnLoadMoreListener onLoadMoreListener1, View.OnClickListener onClickListener) {
        this.mContext = mContext;
        this.list = list;
        
    }

   

 
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poll_of_result_row_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, final int position) {
        if (holder1 instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) holder1;
            final PollOfDayResultModel pollOfDayResultModel = list.get(position);
            holder.txtQuePResut.setText("" + pollOfDayResultModel.Question);
            holder.rvPollOfDayResultRM.setLayoutManager(new LinearLayoutManager(mContext));

            holder.rvPollOfDayResultRM.addOnItemTouchListener(new RecyclerItemClickListener(mContext, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                }
            }));
            PollOfResultRowAdapter pollOfResultRowAdapter =new PollOfResultRowAdapter(mContext, (ArrayList<PollOfDayResultModel.OpinionPollChoicesWithStatusBean>) pollOfDayResultModel.OpinionPollChoicesWithStatus);
            holder.rvPollOfDayResultRM.setAdapter(pollOfResultRowAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuePResut;
        RecyclerView  rvPollOfDayResultRM;


        public ViewHolder(View itemView) {
            super(itemView);
            txtQuePResut = (TextView) itemView.findViewById(R.id.txtQuePResut);
            rvPollOfDayResultRM = (RecyclerView) itemView.findViewById(R.id.rvPollOfDayResultRM);

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
