package com.ishrae.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.model.CategoriesModel;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 21-04-2017.
 */

public class SubCategoryAdapter extends ArrayAdapter<CategoriesModel.SubCategoriesModel> {
    private Context mContext;
    private ArrayList<CategoriesModel.SubCategoriesModel> list;
    private int resourceId;

    public SubCategoryAdapter(Context context, int resource, ArrayList<CategoriesModel.SubCategoriesModel> list) {
        super(context, resource, list);

        mContext = context;
        resourceId = resource;
        this.list = list;
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
        holder.txtChapterName.setText("" + list.get(position).CategoryName);
        return view;
    }

    public class ViewHolder {
        TextView txtChapterName;
    }
}
