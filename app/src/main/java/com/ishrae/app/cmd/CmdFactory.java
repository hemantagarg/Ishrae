package com.ishrae.app.cmd;

import com.ishrae.app.model.SharedPref;
import com.ishrae.app.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * here the parameter is creating to send with api.
 */
public class CmdFactory {

    public static JSONObject params = null;

    public static JSONObject createLoginCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_USERNAME, param[0]);
            jsonObject.put(Constants.FLD_PASSWORD, param[1]);
            jsonObject.put(Constants.FLD_DEVICE_ID, SharedPref.getDeviceToken());

            params.put("loginRequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createRegisterCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_NAME_R, param[0]);
            jsonObject.put(Constants.FLD_MOBILE_NO_R, param[1]);
            jsonObject.put(Constants.FLD_EMAIL_R, param[2]);
            jsonObject.put(Constants.FLD_PASSWORD_R, param[3]);
            jsonObject.put(Constants.FLD_COUNTRY_ID_R, param[4]);
            jsonObject.put(Constants.FLD_STATE_ID_R, param[5]);
            jsonObject.put(Constants.FLD_CITY_ID_R, param[6]);
            jsonObject.put(Constants.FLD_ADDRESS_R, param[7]);
            jsonObject.put(Constants.FLD_LOCATION_R, param[8]);
            jsonObject.put(Constants.FLD_LATITUDE_R, param[9]);
            jsonObject.put(Constants.FLD_LONGITUDE_R, param[10]);
            jsonObject.put(Constants.FLD_DEVICE_ID, SharedPref.getDeviceToken());
            params.put(Constants.FLD_NON_MEMBER_MASTER_ENTITY, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createNewsCmd(JSONObject loginToken, int pageNo, int recordPerPage) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_PAGE_NO, pageNo);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, recordPerPage);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, loginToken);

            params.put("newsRequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createBOGCmd(JSONObject loginToken, int pageNo, int recordPerPage) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_PAGE_NO, pageNo);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, recordPerPage);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, loginToken);

            params.put("bogRequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createRDCmd(JSONObject loginToken, int pageNo, int recordPerPage) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_PAGE_NO, pageNo);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, recordPerPage);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, loginToken);

            params.put("rdrequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createCWCCmd(JSONObject loginToken, int pageNo, int recordPerPage, int chapterId) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_PAGE_NO, pageNo);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, recordPerPage);
            jsonObject.put(Constants.FLD_CHAPTER_ID, chapterId);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, loginToken);

            params.put("cwcrequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createCommitteeMemberListCmd(JSONObject loginToken, int pageNo, int recordPerPage, int committeeId) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_PAGE_NO, pageNo);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, recordPerPage);
            jsonObject.put(Constants.FLD_COMMITTEE_ID, committeeId);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, loginToken);

            params.put("cmrequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createMyChapterInfoCmd(JSONObject loginToken) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, loginToken);
            params.put("myChapterDetailRequest", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject createChangePasswordCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OldPassword", param[0]);
            jsonObject.put("NewPassword", param[1]);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put("changePassword", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject createCalendarEventCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_START_DATE, param[0]);
            jsonObject.put(Constants.FLD_END_DATE, param[1]);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_CALENDAR_EVENT_MASTER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createEmailNotiCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_EMAIL_MASTER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject createForgotPasswordCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.FLD_MEMBER_SHIP_ID, param[0]);
            jsonObject.put(Constants.FLD_TYPE, param[1]);
            params.put(Constants.FLD_FORGOT_PASSWORD_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createEmaildDetailCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_ID, param[0]);
            params.put(Constants.FLD_EMAIL_DETAIL_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public static JSONObject createGetCategoryListCmd(String methodName) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(methodName, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static JSONObject createGetNotificationListCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);


            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_FCM_NOTIFICATION_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static JSONObject createGetProductListCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            jsonObject.put(Constants.FLD_CATEGORY_ID, param[1]);

            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_PRODUCT_MASTER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject getTopicList(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_DISCUSSION_TOPIC_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject createDiscussionCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_TOPICID, param[0]);
            if (Integer.valueOf(param[1]) > 0)
                jsonObject.put(Constants.FLD_PARENT_DESCRIPTION_ID, param[1]);
            jsonObject.put(Constants.FLD_DESCRIPTION, param[2]);

            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_CREATE_DISCUSSION_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject createTopicCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_TOPIC, param[0]);
            jsonObject.put(Constants.FLD_TOPIC_DESCRIPTION, param[1]);

            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_CREATE_TOPI_CREQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static JSONObject getPendingPostCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            params.put(Constants.FLD_PENDING_POST_EVENT_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static JSONObject getOpenionPollCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            params.put(Constants.FLD_OPINION_POLL_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject getOpenionPollStatusCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            params.put(Constants.FLD_OPINION_POLL_STATUS_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject logoutCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_LOGOUT_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject getFormCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_ACTIONNAME, param[0]);

            params.put(Constants.FLD_ACTION_AUTH_BY_MEMBER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static JSONObject setOpenionPollCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_ID, param[0]);
            params.put(Constants.FLD_SET_OPINION_POLL_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }



    public static JSONObject addPostEventCmd(JSONArray jsonArray, String id, String numberOfDelegate, String numberOfMemember, String presente, String descr, ArrayList<String> imagesList,String postEventTitle) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_ID, id);
            jsonObject.put(Constants.FLD_NO_OF_DELEGATES, numberOfDelegate);
            jsonObject.put(Constants.FLD_NO_OF_NON_MEMBERS, numberOfMemember);
            jsonObject.put(Constants.FLD_NO_OF_PRESENTED_TECHNICAL_PAPER,presente);
            jsonObject.put(Constants.FLD_SPEAKER_DETAILS, jsonArray);
            jsonObject.put(Constants.FLD_DESCRIPTION, descr);
            jsonObject.put(Constants.FLD_POST_EVENT_TITLE, postEventTitle);
            if(imagesList!=null&&imagesList.size()>0) {
                for (int i = 0; i < imagesList.size(); i++) {
                    if(i==0)
                        jsonObject.put(Constants.FIELD_UPLOAD_FILE, imagesList.get(i));
                    else
                        jsonObject.put(Constants.FIELD_IMAGE_+i, imagesList.get(i));
                }


            }
            params.put(Constants.FLD_ADD_POST_EVENT_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }





    public static JSONObject getDiscussionList(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_TOPICID, param[1]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_DISCUSSION_MASTER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }

    public static JSONObject createGetCourseListCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            jsonObject.put("CategoryType", param[1]);

            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());

            params.put(Constants.FLD_COURSE_MASTER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }


    public static JSONObject createRenewalListCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_PAGE_NO, param[0]);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_RENEWAL_MASTER_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
    public static JSONObject createLikeDishLikeCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            jsonObject.put(Constants.FLD_ID, param[0]);
            jsonObject.put(Constants.FLD_ISLIKE, param[1]);
            params.put(Constants.FLD_CALENDER_EVENTS_LIKE_DISLIKE_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }
    public static JSONObject createHomeStateCmd(String... param) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Constants.FLD_LOGIN_TOKAN, SharedPref.getLoginUserToken());
            params.put(Constants.FLD_HOMESTATSREQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return params;
    }



    public static JSONObject  createGroupNotificationCmd( int pageNo) {
        params = new JSONObject();
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put(Constants.FLD_PAGE_NO, pageNo);
            jsonObject.put(Constants.FLD_RECORD_PER_PAGE, Constants.RECORD_PER_PAGE_LIMIT);
            jsonObject.put(Constants.FLD_LOGIN_TOKAN,  SharedPref.getLoginUserToken());

            params.put(Constants.FLD_GROUP_NOTIFICATION_REQUEST, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

}