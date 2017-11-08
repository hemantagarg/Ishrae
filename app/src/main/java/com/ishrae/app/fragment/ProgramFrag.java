package com.ishrae.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ishrae.app.R;
import com.ishrae.app.activity.EmailDetailAct;
import com.ishrae.app.adapter.CalendarEventAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.model.CalendarEventModel;
import com.ishrae.app.model.Course;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class ProgramFrag extends Fragment implements Callback, CalendarListener {

    private View view;
    private TextView activityTitle;
    private TextView txtEmpty;
    private String startDate;
    private String endDate;
    Date begining, end;
    RecyclerView rvCalendarEvents;

    ArrayList<CalendarEventModel> calendarEventList;
    private CalendarEventAdapter mAdapter;
    private NetworkResponse resp;
    private int fromWhere;
    private CustomCalendarView calendar_view;

    public int currentSelectedPos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.program, container, false);
        initialize();
        return view;
    }

    private void initialize() {
        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);

        txtEmpty = (TextView) view.findViewById(R.id.txtEmpty);

        rvCalendarEvents = (RecyclerView) view.findViewById(R.id.rvCalendarEvents);

        calendar_view = (CustomCalendarView) view.findViewById(R.id.calendar_view);

        getCurrentMonthStartAndEndDate(new Date());

        startDate = Util.convertDateToFormat(begining, Constants.SERVER_DATE_FORMAT_FOR_SENDING);
        endDate = Util.convertDateToFormat(end, Constants.SERVER_DATE_FORMAT_FOR_SENDING);

//        Util.getCategoryList(getActivity(), this);

        getCalendarEventDateTime();

        rvCalendarEvents.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                currentSelectedPos=position;
                Intent intent = new Intent(getActivity(), EmailDetailAct.class);
                intent.putExtra(Constants.FLD_EVENT_DETAIL, calendarEventList.get(position));
                startActivityForResult(intent,Constants.REFRESH);
            }
        }));

        calendar_view.setCalendarListener(this);
        rvCalendarEvents.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void getCalendarEventDateTime() {
        if (Util.isDeviceOnline(getActivity(), true)) {
            JSONObject params = CmdFactory.createCalendarEventCmd(startDate, endDate);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GETCALENDEREVENTS_URL, params.toString(), true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.program));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtEmpty.setVisibility(View.VISIBLE);
                rvCalendarEvents.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {

            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;
                    if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (fromWhere == 0) {
                                    try {
                                        calendarEventList = new ArrayList<CalendarEventModel>();
                                        JSONArray jsonArray = new JSONArray(resp.respStr);
                                        if (jsonArray.length() > 0) {
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                CalendarEventModel calendarEventModel = (CalendarEventModel) Util.getJsonToClassObject(jsonArray.getJSONObject(i).toString(), CalendarEventModel.class);
                                                calendarEventList.add(calendarEventModel);

                                            }
                                            setAdapter();
                                        } else {
                                            txtEmpty.setVisibility(View.VISIBLE);
                                            rvCalendarEvents.setVisibility(View.GONE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REFRESH ) {
            calendarEventList.get(currentSelectedPos).IsLike=EmailDetailAct.isLike;
            mAdapter.notifyItemChanged(currentSelectedPos);

        }


    }
    private void setAdapter() {
        if (calendarEventList != null && calendarEventList.size() > 0) {
            txtEmpty.setVisibility(View.GONE);
            rvCalendarEvents.setVisibility(View.VISIBLE);
            Collections.sort(calendarEventList, new sortByDate());
            mAdapter = new CalendarEventAdapter(getActivity(), calendarEventList);
            rvCalendarEvents.setAdapter(mAdapter);
            setDateSelected();
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            rvCalendarEvents.setVisibility(View.GONE);
        }
    }

    private void setDateSelected() {
        if (calendarEventList != null && calendarEventList.size() > 0) {
            for (int i = 0; i < calendarEventList.size(); i++) {
                SimpleDateFormat df = new SimpleDateFormat(Constants.SERVER_DATE_FORMAT_COMING);
                try {
                    Date convertedDate = new Date();
                    convertedDate = df.parse(calendarEventList.get(i).start);
                    calendar_view.markDayAsSelectedDay(convertedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    public void getCurrentMonthStartAndEndDate(Date date) {
        Calendar calendar = getCalendarForNow(date);
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        setTimeToBeginningOfDay(calendar);
        begining = calendar.getTime();

        Calendar calendar1 = getCalendarForNow(date);
        calendar1.set(Calendar.DAY_OF_MONTH,
                calendar1.getActualMaximum(Calendar.DAY_OF_MONTH));
        setTimeToEndofDay(calendar1);
        end = calendar1.getTime();
    }

    private static Calendar getCalendarForNow(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private static void setTimeToBeginningOfDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }

    private static void setTimeToEndofDay(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
    }

    @Override
    public void onDateSelected(Date date, int isValidDate) {

    }

    @Override
    public void onMonthChanged(Date time) {
        getCurrentMonthStartAndEndDate(time);
        startDate = Util.convertDateToFormat(begining, Constants.SERVER_DATE_FORMAT_FOR_SENDING);
        endDate = Util.convertDateToFormat(end, Constants.SERVER_DATE_FORMAT_FOR_SENDING);
        if (calendarEventList == null)
            calendarEventList = new ArrayList<>();
        calendarEventList.clear();
        mAdapter = new CalendarEventAdapter(getActivity(), calendarEventList);
        rvCalendarEvents.setAdapter(mAdapter);
        getCalendarEventDateTime();
    }

    public class sortByDate implements Comparator<CalendarEventModel> {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.SERVER_DATE_FORMAT_COMING);

        public int compare(CalendarEventModel lhs, CalendarEventModel rhs) {
            int dateComparison = 0;
            try {
                dateComparison = dateFormat.parse(lhs.start).compareTo(dateFormat.parse(rhs.start));
//                if(( dateFormat.parse(lhs.start).getTime() == dateFormat.parse(rhs.start).getTime()))
//                dateComparison=0;
//                else
//                dateComparison= ( dateFormat.parse(lhs.start).getTime() > dateFormat.parse(rhs.start).getTime() ? -1 : 1);     //descending
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return dateComparison;
        }
    }
}
