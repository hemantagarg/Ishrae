package com.ishrae.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by raj on 4/25/2017.
 */

public class ForumModel implements Serializable {

    /**
     * TopicID : 1000
     * Topic : How to do i calculate load for OT application?
     * Description : I want to have some inputs in designing the OT room AC.
     * CreatedBy : Akash  Vajpai
     * CreatedDate : 21/Feb/2017
     * TotalActiveUser : 0
     * TotalDiscussion : 2
     */

    public int TopicID;
    public String Topic;
    public String Description;
    public String CreatedBy;
    public String ImageProfile;
    public String CreatedDate;
    public int TotalActiveUser;
    public int TotalDiscussion;
    public long ParentDescriptionId;
    public long DescriptionId;

    public ArrayList<ForumModel> ChildDescriptionDetails;


}
