package com.ishrae.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ishrae.app.R;
import com.ishrae.app.activity.DashboardActivity;
import com.ishrae.app.adapter.CategoryAdapter;
import com.ishrae.app.adapter.ProductListAdapter;
import com.ishrae.app.adapter.SubCategoryAdapter;
import com.ishrae.app.cmd.CmdFactory;
import com.ishrae.app.interfaces.OnLoadMoreListener;
import com.ishrae.app.model.CategoriesModel;
import com.ishrae.app.network.NetworkManager;
import com.ishrae.app.network.NetworkResponse;
import com.ishrae.app.tempModel.TmpProductListModel;
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

public class ProductListFrag extends Fragment implements Callback, OnLoadMoreListener, View.OnClickListener {

    private View view;
    private TextView activityTitle;
    private TextView txtEmptyPL;

    private BetterSpinner spSelectCategory;
    private BetterSpinner spSelectSubCategory;

    private LinearLayout llCategoryContainer;

    private RecyclerView rvPL;

    private ArrayList<TmpProductListModel.ProductListModel> producttList;
    private ProductListAdapter mAdapter;
    private NetworkResponse resp;

    private int pageNumber = 1;
    private int categoryId;
    /**
     * 0 = category list
     * 1 = product list
     */
    private int fromWhere = 0;

    private ArrayList<CategoriesModel> categoriesModelArrayList = new ArrayList<>();
    private CategoryAdapter categoryAdapter;
    private SubCategoryAdapter subCategoryAdapter;

    private TmpProductListModel tmpProductListModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.product_list_main, container, false);
        initialize();
        return view;
    }

    private void initialize() {

        activityTitle = (TextView) getActivity().findViewById(R.id.activityTitle);
        txtEmptyPL = (TextView) view.findViewById(R.id.txtEmptyPL);
        spSelectCategory = (BetterSpinner) view.findViewById(R.id.spSelectCategory);
        spSelectSubCategory = (BetterSpinner) view.findViewById(R.id.spSelectSubCategory);

        rvPL = (RecyclerView) view.findViewById(R.id.rvPL);

        llCategoryContainer = (LinearLayout) view.findViewById(R.id.llCategoryContainer);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvPL.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (producttList.get(position) == null)
                    return 2;
                return 1;
            }
        });

        spSelectCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoriesModel categoriesModel = (CategoriesModel) adapterView.getItemAtPosition(i);
                categoryId = categoriesModel.CategoryId;
                spSelectCategory.setText("");
                spSelectCategory.setClickable(true);
                spSelectCategory.setHint("" + categoriesModel.CategoryName);
                if (producttList.size() > 0) {
                    producttList.clear();
                }
                setSubCategoriesData(categoriesModel);
            }
        });


        spSelectSubCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CategoriesModel.SubCategoriesModel categoriesModel = (CategoriesModel.SubCategoriesModel) adapterView.getItemAtPosition(i);
                categoryId = categoriesModel.CategoryId;
                pageNumber = 1;
                spSelectSubCategory.setText("");
                spSelectSubCategory.setClickable(true);
                spSelectSubCategory.setHint("" + categoriesModel.CategoryName);
                if (producttList.size() > 0) {
                    producttList.clear();
                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                    mAdapter = null;
                }
                getProductList(true);

            }
        });


        rvPL.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //TODO..
            }
        }));

        mAdapter = null;
        producttList = new ArrayList<>();
        getCategoryList();
    }

    private void getCategoryList() {

        if (Util.isDeviceOnline(getActivity())) {
            fromWhere = 0;
            JSONObject params = CmdFactory.createGetCategoryListCmd(Constants.FLD_CATEGORY_MENU_MASTER_REQUEST);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GET_CATEGORY_LIST_URL, params.toString(), true);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    private void getProductList(boolean showProDialog) {
        if (Util.isDeviceOnline(getActivity())) {
            fromWhere = 1;
            JSONObject params = CmdFactory.createGetProductListCmd("" + pageNumber, "" + categoryId);
            NetworkManager.requestForAPI(getActivity(), this, Constants.VAL_POST, AppUrls.GET_PRODUCT_LIST_URL, params.toString(), showProDialog);
        } else {
            Util.showDefaultAlert(getActivity(), getResources().getString(R.string.msg_internet), null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        activityTitle.setText(getResources().getString(R.string.Shop));
    }

    @Override
    public void onFailure(Call call, IOException e) {
        Util.manageFailure(getActivity(), e);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtEmptyPL.setVisibility(View.VISIBLE);
                rvPL.setVisibility(View.GONE);
                if (mAdapter != null && mAdapter.isLoaded()) {
                    producttList.remove(producttList.size() - 1);
                    mAdapter.notifyItemRemoved(producttList.size());
                    mAdapter.setLoaded();
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
                                    if (mAdapter != null && mAdapter.isLoaded() && producttList.size() > 0) {
                                        producttList.remove(producttList.size() - 1);
                                        mAdapter.notifyItemRemoved(producttList.size());
                                        mAdapter.setLoaded();
                                    }
                                    tmpProductListModel = (TmpProductListModel) Util.getJsonToClassObject(resp.respStr, TmpProductListModel.class);

                                    if (tmpProductListModel != null && tmpProductListModel.ProductMasterList != null)
                                        producttList.addAll(tmpProductListModel.ProductMasterList);
                                    setAdapter();
                                }
                            }
                        });
                    }
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        llCategoryContainer.setVisibility(View.GONE);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void setCategoryAdapter() {
        llCategoryContainer.setVisibility(View.VISIBLE);
        categoryAdapter = new CategoryAdapter(getActivity(), R.layout.chapter_row, categoriesModelArrayList);
        spSelectCategory.setAdapter(categoryAdapter);

        if (!categoriesModelArrayList.isEmpty()) {
            setSubCategoriesData(categoriesModelArrayList.get(0));
            spSelectCategory.setHint("");
            spSelectCategory.setText("");
            spSelectCategory.setHint("" + categoriesModelArrayList.get(0).CategoryName);
        }


    }

    public void setSubCategoriesData(CategoriesModel categoriesModel) {

        if (categoriesModel != null) {
            subCategoryAdapter = new SubCategoryAdapter(getActivity(), R.layout.chapter_row, categoriesModel.ChildCategoryMenuMasterEntity);
            spSelectSubCategory.setAdapter(subCategoryAdapter);

            if (categoriesModel.ChildCategoryMenuMasterEntity != null && categoriesModel.ChildCategoryMenuMasterEntity.size() > 0) {
                pageNumber = 1;
                categoryId = categoriesModel.ChildCategoryMenuMasterEntity.get(0).CategoryId;
                spSelectSubCategory.setHint("");
                spSelectSubCategory.setText("");
                spSelectSubCategory.setHint("" + categoriesModel.ChildCategoryMenuMasterEntity.get(0).CategoryName);
                getProductList(true);
            }
        }
    }

    private void setAdapter() {

        if (producttList != null && producttList.size() > 0) {
            txtEmptyPL.setVisibility(View.GONE);
            rvPL.setVisibility(View.VISIBLE);

            if (mAdapter == null) {
                mAdapter = new ProductListAdapter(getActivity(), producttList, ProductListFrag.this, rvPL, this);
                rvPL.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            txtEmptyPL.setVisibility(View.VISIBLE);
            rvPL.setVisibility(View.GONE);
        }
    }


    @Override
    public void onLoadMore() {

        if ((mAdapter != null && tmpProductListModel != null && tmpProductListModel.TotalItemsAnInt > producttList.size())) {
            producttList.add(null);
            mAdapter.notifyItemInserted(producttList.size() - 1);
            pageNumber += 1;
            getProductList(false);
        }
    }

    @Override
    public void onClick(View v) {
        int vId = v.getId();

        if (vId == R.id.btnBuy||vId==R.id.llProductContainer) {
            TmpProductListModel.ProductListModel productListMode = (TmpProductListModel.ProductListModel) v.getTag();

            if (Util.isDeviceOnline(getActivity(), true)) {
                String url = "http://shop.ishrae.in/product/details/" + productListMode.ProductName + "/" + productListMode.Product_Id;

                if (productListMode.prduct_purchase_url != null)
                    url = productListMode.prduct_purchase_url;
                Util.setCommonFormAct(getActivity(),null,getActivity().getResources().getString(R.string.Shop),url);
//                Util.openUrlOnBrowser(getActivity(), url);
            }

        }
    }
}
