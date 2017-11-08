package com.ishrae.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.activity.CourseDetail;
import com.ishrae.app.adapter.CategoryAdapter;
import com.ishrae.app.adapter.CourseAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.CategoriesModel;
import com.ishrae.app.model.Course;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.utilities.AppUrls;
import com.ishrae.app.utilities.Constants;
import com.ishrae.app.utilities.Util;
import com.ishrae.app.utilities.recycler_view_utilities.RecyclerItemClickListener;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Nss Solutions on 23-03-2017.
 */

public class ELearningFrag extends Fragment implements Callback, OnLoadMoreListener {

    private View view;

    private TextView activityTitle;
    private TextView txtErrorMsg;

    private BetterSpinner spSelectCategoryEL;

    private RecyclerView rvCourseList;

    private NetworkResponse resp;

    private CourseAdapter courseAdapter;
    private CategoryAdapter categoryAdapter;

    private int pageNumber = 1;
    private int categoryId;
    private int fromWhere = 0;
    private ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<>();
    private ArrayList<Course> courseArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.elearning_list_main, container, false);

        initialize();
        return view;
    }

    private void initialize() {

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        txtErrorMsg = (TextView) view.findViewById(R.id.txtErrorMsg);
        spSelectCategoryEL = (BetterSpinner) view.findViewById(R.id.spSelectCategoryEL);
        spSelectCategoryEL.setVisibility(View.GONE);
        rvCourseList = (RecyclerView) view.findViewById(R.id.rvCourseList);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rvCourseList.setLayoutManager(gridLayoutManager);

        spSelectCategoryEL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CategoriesModel categoriesModel = (CategoriesModel) adapterView.getItemAtPosition(i);
                categoryId = categoriesModel.CategoryId;
                spSelectCategoryEL.setText("");
                spSelectCategoryEL.setClickable(true);
                spSelectCategoryEL.setHint("" + categoriesModel.CategoryName);

                if (courseArrayList.size() > 0) {

                    courseArrayList.clear();
                }
                courseAdapter=null;

                getCourseList(true);
            }
        });

        rvCourseList.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Intent in = new Intent(getActivity(), CourseDetail.class);
                in.putExtra("detail", courseArrayList.get(position));
                startActivity(in);
            }
        }));

        courseAdapter = null;
        courseArrayList = new ArrayList<Course>();
        getCourseCategoryList();
    }

    private void getCourseCategoryList() {
        if (Util.isDeviceOnline(getActivity())) {
            fromWhere = 0;
            JSONObject params = CmdFactory.createGetCategoryListCmd(Constants.FLD_CATEGORY_MASTER_REQUEST);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.ELEARNING_GET_CATEGORY_LIST_URL, params.toString(), true);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getCourseList(boolean showIndicator) {
        if (Util.isDeviceOnline(getActivity())) {
            fromWhere = 1;
            JSONObject params = CmdFactory.createGetCourseListCmd("" + pageNumber, "" + categoryId);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GET_COURSE_LIST_URL, params.toString(), showIndicator);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.ASHRAE_E_Learning_Course));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtErrorMsg.setVisibility(View.VISIBLE);
                rvCourseList.setVisibility(View.GONE);
                if (courseAdapter != null && courseAdapter.isLoaded()) {
                    courseArrayList.remove(courseArrayList.size() - 1);
                    courseAdapter.notifyItemRemoved(courseArrayList.size());
                    courseAdapter.setLoaded();
                }
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Util.dismissProgressDialog();
        handleResponse(response);
    }

    private void handleResponse(final Response response) {
        final JSONObject jsonObject = Util.getObjectFromResponse(response);
        try {
            if (jsonObject != null && jsonObject.getInt(Constants.FLD_RESPONSE_CODE) == 1) {
                String responseData = jsonObject.getString(Constants.FLD_RESPONSE_DATA);
                if (responseData.length() > 0) {
                    String value = Util.decodeToken(responseData);
                    resp = new NetworkResponse();
                    resp.respStr = value;

                    if (resp.respStr != null && resp.respStr.trim().length() > 0) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (fromWhere == 0) {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<List<CategoriesModel>>() {
                                        }.getType();
                                        categoriesModelArrayList = gson.fromJson(resp.respStr.toString(), type);
                                        setCategoryAdapter();

                                    } else if (fromWhere == 1) {
                                        if (courseAdapter != null && courseAdapter.isLoaded()) {
                                            courseArrayList.remove(courseArrayList.size() - 1);
                                            courseAdapter.notifyItemRemoved(courseArrayList.size());
                                            courseAdapter.setLoaded();
                                        }

                                        Gson gson = new Gson();
                                        Type type = new TypeToken<List<Course>>() {
                                        }.getType();

                                        ArrayList<Course> courseArrayListTmp = gson.fromJson(resp.respStr.toString(), type);

                                        if (!courseArrayListTmp.isEmpty())
                                            courseArrayList.addAll(courseArrayListTmp);
                                        setAdapter();

                                    }
                                }
                            });
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setCategoryAdapter() {
        if (categoriesModelArrayList != null && categoriesModelArrayList.size() > 0) {
            categoryAdapter = new CategoryAdapter(getActivity(), R.layout.chapter_row, categoriesModelArrayList);
            spSelectCategoryEL.setAdapter(categoryAdapter);
            categoryId = categoriesModelArrayList.get(0).CategoryId;
            fromWhere = 3;
            pageNumber = 1;
            spSelectCategoryEL.setHint("");
            spSelectCategoryEL.setText("");
            spSelectCategoryEL.setHint("" + categoriesModelArrayList.get(0).CategoryName);
            courseAdapter=null;
            getCourseList(true);
            spSelectCategoryEL.setVisibility(View.VISIBLE);
        }
    }

    private void setAdapter() {
        if (courseArrayList != null && courseArrayList.size() > 0) {
            txtErrorMsg.setVisibility(View.GONE);
            rvCourseList.setVisibility(View.VISIBLE);

            if (courseAdapter == null) {
                courseAdapter = new CourseAdapter(getActivity(), courseArrayList, rvCourseList, this);
                rvCourseList.setAdapter(courseAdapter);
            } else
                courseAdapter.notifyItemInserted(courseArrayList.size()-1);
        } else {
            txtErrorMsg.setVisibility(View.VISIBLE);
            rvCourseList.setVisibility(View.GONE);

        }
    }


    @Override
    public void onLoadMore() {
        if (courseAdapter != null) {
            courseArrayList.add(null);
            courseAdapter.notifyItemInserted(courseArrayList.size() - 1);
            pageNumber += 1;
            getCourseList(false);
        }
    }
}
