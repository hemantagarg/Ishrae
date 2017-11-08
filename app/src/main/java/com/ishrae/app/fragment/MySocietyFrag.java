package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishrae.app.R;

/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class MySocietyFrag extends Fragment {
    private View view;
    private TextView activityTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_society, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.my_society));
    }

}
