package com.ishrae.app.tempModel;

import java.util.ArrayList;

/**
 * Created by raj on 4/19/2017.
 */

public class TmpProductListModel {

    public ArrayList<ProductListModel> ProductMasterList;
    public int TotalItemsAnInt;

    /**
     * Product_Id : 12
     * CategoryId : 6
     * SubCategoryId : 6
     * ProductName : ALL ABOUT AHUS
     * ShortDescription : This book provides basic information to a HVAC engineer in selecting, specifying, installing, maintaining and balancing Air Handling Units.
     * ThumbImagePath : http://shop.ishrae.in/Content/Product_Thumb/iy52fdhaet2.jpg
     * MemberPrice : 180.0
     */
    public class ProductListModel {
        public int Product_Id;
        public int CategoryId;
        public int SubCategoryId;
        public String ProductName;
        public String ShortDescription;
        public String ThumbImagePath;
        public double MemberPrice;
        public String prduct_purchase_url;
    }

}
