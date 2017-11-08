package com.ishrae.app.tempModel;

import java.util.List;

/**
 * Created by raj on 5/6/2017.
 */

public class RenewalTmp {

    /**
     * RenewalList : [{"PaymentID":"0/Yf8feqeu2ftkFT544iyQ==","EntryDate":"31/Oct/2015","PaymentMode":"Online","ChequeDraftno":"8781a02c9d5aa5d9fca3","Amount":2565,"RenewalDate":"23/Oct/2018"},{"PaymentID":"QJqsGyzNJHFv/kbbB/Mebg==","EntryDate":"19/May/2014","PaymentMode":"Cash","ChequeDraftno":"","Amount":0,"RenewalDate":"23/Oct/2015"},{"PaymentID":"kNOlYgGOKJqlILk70GMn6A==","EntryDate":"23/Oct/2007","PaymentMode":"Cash","ChequeDraftno":"","Amount":0,"RenewalDate":"23/Oct/2012"}]
     * TotalItems : 3
     */

    public int TotalItems;
    public List<RenewalListBean> RenewalList;


    public static class RenewalListBean {
        /**
         * PaymentID : 0/Yf8feqeu2ftkFT544iyQ==
         * EntryDate : 31/Oct/2015
         * PaymentMode : Online
         * ChequeDraftno : 8781a02c9d5aa5d9fca3
         * Amount : 2565.0
         * RenewalDate : 23/Oct/2018
         */

        public String PaymentID;
        public String EntryDate;
        public String PaymentMode;
        public String ChequeDraftno;
        public double Amount;
        public String RenewalDate;


    }
}
