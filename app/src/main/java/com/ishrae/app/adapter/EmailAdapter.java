package com.ishrae.app.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.ishrae.app.R;
import com.ishrae.app.model.EmailModel;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;

import java.util.ArrayList;

/**
 * Created by raj on 4/16/2017.
 */

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<EmailModel> list;
    public EmailAdapter(Context mContext, ArrayList<EmailModel> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public EmailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_row, parent, false);
        return new EmailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EmailAdapter.ViewHolder holder, final int position) {
        EmailModel emailModel = list.get(position);

        holder.txtMailFrom.setText("" + emailModel.MailFrom);
        holder.txtMailSubject.setText("" + emailModel.MailSubject);
        String date = Util.convertDateTimeFormat(emailModel.MailDate, Constants.SERVER_DATE_FORMAT_COMING, Constants.DATE_FORMAT_FOR_SHOWING);
        holder.txtMailDate.setText("" + date);
        if (!TextUtils.isEmpty(emailModel.ShortImagePath))
            Glide.with(mContext)
                    .load(emailModel.ShortImagePath)
                    .priority(Priority.IMMEDIATE)
                    .error(R.mipmap.ic_email_circle1)
                    .fallback(R.mipmap.ic_email_circle1)
                    .into(holder.imgEmail);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMailFrom;
        TextView txtMailSubject;
        TextView txtMailDate;

        ImageView imgEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMailFrom = (TextView) itemView.findViewById(R.id.txtMailFrom);
            txtMailSubject = (TextView) itemView.findViewById(R.id.txtMailSubject);
            txtMailDate = (TextView) itemView.findViewById(R.id.txtMailDate);
            imgEmail = (ImageView) itemView.findViewById(R.id.imgEmail);
        }
    }
}

