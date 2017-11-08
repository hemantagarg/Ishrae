package com.ishrae.app.tempModel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by raj on 5/13/2017.
 */

public class NotificationModelTmp {


    /**
     * FcmNotificationEntitys : [{"id":"Di++3UkpzYqi59r+SgfLhQ==","Topic":"test","Title":"test 8","Message":"test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8\r\ntest 8test 8test 8test 8test 8test 8test 8test 8test 8test 8","ImagePath":""},{"id":"AjN0ZsImguYWJd+rHZriVQ==","Topic":"test","Title":"test 7","Message":"test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7test 7\r\ntest 7test 7test 7test 7test 7test 7test 7test 7test 7\r\ntest 7","ImagePath":""}]
     * TotalItems : 2
     */

    public int TotalItems;
    public List<FcmNotificationEntitysBean> FcmNotificationEntitys;


    public static class FcmNotificationEntitysBean implements Serializable {
        /**
         * id : Di++3UkpzYqi59r+SgfLhQ==
         * Topic : test
         * Title : test 8
         * Message : test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8
         * test 8test 8test 8test 8test 8test 8test 8test 8test 8test 8
         * ImagePath :
         */

        public String id;
        public String Topic;
        public String Title;
        public String Message;
        public String ImagePath;

    }
}
