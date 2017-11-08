package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.BOG;
import com.ishrae.app.model.CalendarEventModel;

import java.util.ArrayList;

/**
 * Created by raj on 4/16/2017.
 */

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<CalendarEventModel> list;

    public CalendarEventAdapter(Context mContext, ArrayList<CalendarEventModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public CalendarEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_event_row, parent, false);
        return new CalendarEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarEventAdapter.ViewHolder holder, final int position) {
        CalendarEventModel calendarEventModel = list.get(position);
        holder.txtDesc.setText(calendarEventModel.description);
        holder.txtTitle.setText(calendarEventModel.title);
        holder.txtCalDate.setText(calendarEventModel.end);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle;
        TextView txtDesc;
        TextView txtCalDate;


        public ViewHolder(View itemView) {
            super(itemView);

            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDesc = (TextView) itemView.findViewById(R.id.txtDesc);
            txtCalDate = (TextView) itemView.findViewById(R.id.txtCalDate);

        }
    }
}

