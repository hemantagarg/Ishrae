package com.ishrae.app.model;

import java.util.List;

/**
 * Created by a on 6/19/2017.
 */

public class PollOfDayResultModel {


    /**
     * id : kCneJcUOZjs=
     * Question : Have you received copy of ISHRAE Journal(Jan-Feb'2016) by speed post?
     * OpinionPollChoicesWithStatus : [{"id":"zwxOrAHtPes=","Choice":"Yes","Sorting":1,"StatusInPercent":60},{"id":"f1bksls94dSlqXk=","Choice":"No","Sorting":2,"StatusInPercent":39}]
     */

    public String id;
    public String Question;
    public List<OpinionPollChoicesWithStatusBean> OpinionPollChoicesWithStatus;


    public static class OpinionPollChoicesWithStatusBean {
        /**
         * id : zwxOrAHtPes=
         * Choice : Yes
         * Sorting : 1
         * StatusInPercent : 60
         */

        public String id;
        public String Choice;
        public int Sorting;
        public int StatusInPercent;


    }
}


