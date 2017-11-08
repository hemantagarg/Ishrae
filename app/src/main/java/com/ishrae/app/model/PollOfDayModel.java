package com.ishrae.app.model;

import java.util.List;

/**
 * Created by a on 6/19/2017.
 */

public class PollOfDayModel {


    /**
     * id : kCneJcUOZjs=
     * Question : Have you received copy of ISHRAE Journal(Jan-Feb'2016) by speed post?
     * OpinionPollChoices : [{"id":"zwxOrAHtPes=","Choice":"Yes","Sorting":1},{"id":"f1bksls94dSlqXk=","Choice":"No","Sorting":2}]
     */

    private String id;
    private String Question;
    private List<OpinionPollChoicesBean> OpinionPollChoices;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public List<OpinionPollChoicesBean> getOpinionPollChoices() {
        return OpinionPollChoices;
    }

    public void setOpinionPollChoices(List<OpinionPollChoicesBean> OpinionPollChoices) {
        this.OpinionPollChoices = OpinionPollChoices;
    }

    public static class OpinionPollChoicesBean {
        /**
         * id : zwxOrAHtPes=
         * Choice : Yes
         * Sorting : 1
         */

        private String id;
        private String Choice;
        private int Sorting;
        private boolean isSelected;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getChoice() {
            return Choice;
        }

        public void setChoice(String Choice) {
            this.Choice = Choice;
        }

        public int getSorting() {
            return Sorting;
        }

        public void setSorting(int Sorting) {
            this.Sorting = Sorting;
        }

        public boolean getSelected() {
            return isSelected;
        }

        public void setSelected(Boolean selected) {
            isSelected = selected;
        }
    }
}
