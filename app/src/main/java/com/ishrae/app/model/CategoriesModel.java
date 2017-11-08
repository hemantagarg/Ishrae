package com.ishrae.app.model;

import java.util.ArrayList;

/**
 * Created by raj on 4/21/2017.
 */

public class CategoriesModel {

    /**
     * CategoryId : 1
     * CategoryName : Individual Courses
     * CategoryType : Course
     * NoOfRecord : 88
     */

    public int CategoryId;
    public String CategoryName;
    public String CategoryType;
    public int NoOfRecord;
    public ArrayList<SubCategoriesModel> ChildCategoryMenuMasterEntity;

    public class SubCategoriesModel {
        public int ParentCategoryId;
        public int CategoryId;
        public String CategoryName;
    }
}
