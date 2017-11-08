package com.ishrae.app.model;

import java.io.Serializable;

/**
 * Created by Nss Solutions on 24-04-2017.
 */

public class Course implements Serializable {

    public int CourseId;
    public String CourseName;
    public String ShortDescription;
    public String DetailDescription;
    public String ThumbImage;
    public String LargeImage;
    public String CategoryName;
    public String MemberPrice;
    public String DetailPageUrl;

    public Course() {
        //TODO..
    }

}
