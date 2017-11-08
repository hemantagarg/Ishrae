package com.ishrae.app.tempModel;

import com.ishrae.app.model.UserPersonalDetails;
import com.ishrae.app.model.UserTokenDetails;

import java.util.List;


/**
 * Created by Nss Solutions on 22-03-2017.
 */

public class UserDetailsTemp {

    public UserTokenDetails logintokan;
    public UserPersonalDetails userprofile;
    public List<String> Roles;
    public List<String> RolesTmp;
    public int GroupNotificationCount;


    public String KEY_ROLLS="Roles";
        public String KEY_ROLESTMP="RolesTmp";
    public UserDetailsTemp() {


    }

}
