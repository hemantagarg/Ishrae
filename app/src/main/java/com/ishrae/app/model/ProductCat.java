package com.ishrae.app.model;

import java.util.ArrayList;

/**
 * Created by Nss Solutions on 21-04-2017.
 */

public class ProductCat {
    public int ParentCategoryId;
    public int CategoryId;
    public String CategoryName;

    public ArrayList<ProductSubCat> ChildCategoryMenuMasterEntity;

    public ProductCat() {
        //TODO..
    }
}
