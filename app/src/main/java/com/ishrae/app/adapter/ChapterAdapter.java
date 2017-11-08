package com.ishrae.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.model.Chapter;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class ChapterAdapter extends ArrayAdapter<Chapter> {
    private Context mContext;
    public ArrayList<Chapter> list;
    private int resourceId;

    public ChapterAdapter(Context context, int resource, ArrayList<Chapter> list) {
        super(context, resource, list);
        mContext = context;
        this.list = list;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.txtChapterName = (TextView) view.findViewById(R.id.txtChapterName);
            view.setTag(holder);

        } else {

            holder = (ViewHolder) view.getTag();
        }

        holder.txtChapterName.setText("" + list.get(position).Text);
        return view;
    }

    public class ViewHolder {
        TextView txtChapterName;
    }
}
