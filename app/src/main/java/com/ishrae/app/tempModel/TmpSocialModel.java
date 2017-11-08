package com.ishrae.app.tempModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raj on 4/17/2017.
 */

public class TmpSocialModel {
    public ArrayList<Databean> SocialMediaList;
    public ArrayList<BannerListDatabean> BannerList;
    public PresidentMessageDatabean PresidentMessage;
    public List<String> Roles;
    public List<HomeStatsListBean> HomeStatsList;

    public static class HomeStatsListBean {
        /**
         * Name : Total Strength
         * Value : 100
         * ImagePath :
         * SortingOrder : 1
         */

        public String Name;
        public String Value;
        public String ImagePath;
        public int SortingOrder;


    }


    public class Databean {
        public String Name;
        public String URL;
        public String ImagePath;

    }

    public class BannerListDatabean {
        public String ImageUrl;
        public String ImagePath;

    }

    public class PresidentMessageDatabean {
        public String Name;
        public String LongMessege;
        public String ImagePath;

    }


}
