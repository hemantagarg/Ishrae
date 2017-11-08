package com.ishrae.app.model;

import java.io.Serializable;

/**
 * Created by raj on 4/16/2017.
 */

public class CalendarEventModel implements Serializable {

    /**
     * id : 6783
     * title : DCI CWC Installation Ceremony for the Society year 2017-18
     * start : /Date(1491071400000)/
     * end : /Date(1491071400000)/
     * url : home/CalenderEvent/6783
     * description : We have the pleasure of inviting you for the installation ceremony of Chapter Working Committee of Delhi Chapter of ISHRAE for the Society Year 2017-18 on 1st April 2017 at Jacaranda Hall, India Habitat Centre, Lodhi Road from 7:00 P.M. onwards followed by Cocktail & dinner.
     */

    public int id;
    public String title;
    public String start;
    public String end;
    public String url;
    public String description;
    public Boolean IsLike;

}
