package com.ishrae.app.utilities;

/**
 * Created by Nss Solutions on 16-11-2016.
 */

public class Constants {
 public static final String FLD_URL = "url";

   /* 100: tokentype not valid.
    101: Oops! you have logged in from another device.
    102: Cipher is not valid!
    103: Build Version is not valid!
    104: Device Type is not valid!
    0: Request is not valid
    1: Successfully Done*/

   public static int  CODE_TOKEN_EXPIRE=100;
   public static int  CODE_LOGIN_FROM_OTHER_DEVICE=101;
    public static final String CIQ_DATA = "CIQ_DATA";
    public static final String FLD_USER_PROFILE_IMAGE ="UserProfileImage" ;
    /**
     * Different kind of user rolls
     */
    public static String ROLL_NON_MEMBER = "non member";
    public static String ROLL_MEMBER = "Member";
    public static String ROLL_CHAPTER = "Chapter";
    public static String ROLL__STUDENT = "Student";


    /**
     * Different kind of user rolls
     */

    public static int MINIMUMLENGTHOFNAME = 2;

    public static final int DEFAULT_ID = 0;

    public static String FLD_SCREEN_WIDTH = "screen_w";

    public static final String FLD_TYPE = "Type";
    public static final String FLD_MEMBER_SHIP_ID = "MembershipID";

    public static final String Type_T1 = "T1";
    public static final String Type_T2 = "T2";
    public static final String Type_T3 = "T3";

    public static final String FLD_CATEGORY_ID = "CategoryId";

    public static final int RECORD_PER_PAGE_LIMIT = 10;
    public static final String FLD_TOPICID = "TopicID";
    public static final String FLD_DESCRIPTION = "Description";
    public static final String FLD_PARENT_DESCRIPTION_ID = "ParentDescriptionId";
    public static final int REFRESH = 1;

    public static String SERVER_DATE_FORMAT_FOR_SENDING = "yyyy-MM-dd";
    public static String SERVER_DATE_FORMAT_COMING1 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static String SERVER_DATE_FORMAT_COMING = "dd/MMM/yyyy";
    public static String DATE_TIME_FORMAT_FOR_SHOWING = "dd MMM,yyyy hh:mm a";
    public static String DATE_FORMAT_FOR_SHOWING = "dd MMM, yyyy";

    //common field constants
    public static String FLD_RESPONSE = "response";
    public static String FLD_ERROR_MSG = "message";

    //common constants values
    public static String VAL_SUCCESS = "Success";
    public static String VAL_UNKNOWN = "server not responding";

    //Request types
    public static String VAL_POST = "post";
    public static String VAL_GET = "get";

    //Request headers
    public static String FLD_CONTENT_TYPE = "Content-Type";
    public static String FLD_CIPHER = "cipher";
    public static String FLD_BUILD_VERSION = "buildVersion";
    public static String FLD_DEVICE_TYPE = "deviceType";

    //Request headers values
    public static String VAL_CIPHER = "vBrQZHIjqCfeGKUsYT4H_qCfeG";
    public static String VAL_CONTENT_TYPE = "application/json";
    public static String VAL_ANDROID = "android";
    public static String VAL_BUILD_VERSION = "1.1";


    // Login Parameters
    public static String FLD_USERNAME = "UserName";
    public static String FLD_PASSWORD = "Password";
    public static String FLD_DEVICE_ID = "DeviceID";

    //    Register Parameters
    public static String FLD_NAME_R = "Name";
    public static String FLD_COUNTRY_ID_R = "CountryId";
    public static String FLD_STATE_ID_R = "StateId";
    public static String FLD_CITY_ID_R = "CityId";
    public static String FLD_ADDRESS_R = "Address";
    public static String FLD_LOCATION_R = "Location";
    public static String FLD_MOBILE_NO_R = "MobileNo";
    public static String FLD_EMAIL_R = "Email";
    public static String FLD_LONGITUDE_R = "Logitude";
    public static String FLD_LATITUDE_R = "Latitude";
    public static String FLD_PASSWORD_R = "Password";

    //NewsEvents Parameters
    public static String FLD_PAGE_NO = "PageNo";
    public static String FLD_RECORD_PER_PAGE = "RecordPerPage";
    public static String FLD_FINANCIAL_YEAR_ID = "FinnancialYearID";

    public static String FLD_MEMBER_ID = "memberId";
    public static String FLD_USER_MODEL_JSON = "userModelJson";
    public static String FLD_USER_TOKEN = "userToken";
    public static String FLD_CHAPTER_ID = "ChapterID";
    public static String FLD_COMMITTEE_ID = "CommitteeID";


    public static String VAL_PASSENGER = "passenger";
    public static String VAL_SHUTTLE = "shuttle";

    public static String FLD_RESPONSE_CODE = "responseCode";
    public static String FLD_RESPONSE_MESSAGE = "responseMessage";
    public static String FLD_RESPONSE_DATA = "responseData";
    public static String FLD_RESPONSE_OBJECT = "responseObject";
    public static String FLD_EVENT_DETAIL = "eventDetail";
    public static String FLD_NOTIFICATION_DETAIL = "notificationDetail";
    public static String FLD_MESSAGE = "message";
    public static String FLD_ERROR_CODE = "errorCode";
    public static String FLD_USER = "user";
    public static String FLD_LOGIN_TOKAN = "loginTokan";
    public static String FLD_LOGIN_tOKAN = "logintokan";

    public static String FLD_NAME = "name";
    public static String FLD_GENDER = "gender";
    public static String FLD_EMAIL = "email";
    public static String FLD_API_BASE = "apiBase";
    public static String FLD_USER_TYPE = "userType";
    public static String FLD_PHONE = "phone";
    public static String FLD_HOTEL_ID = "hotelId";
    public static String FLD_FLIGHT_ID = "flightId";
    public static String FLD_PHOTO = "photo";

    public static String FLD_SOCIAL_MEDIA_TYPE = "socialMediaType";
    public static String FLD_SOCIAL_MEDIA_ID = "socialMediaId";
    public static String FLD_SOCIAL_MEDIA_TOKEN = "socialMediaToken";
    public static String FLD_UNIQUE_DEVICE_ID = "uniqueDeviceId";
    public static String FLD_ACCESS_TOKEN = "accessToken";
    public static String FLD_DEVICE_LOCALIZED_MODEL = "deviceLocalizedModel";
    public static String FLD_DEVICE_MODAL = "deviceModal";
    public static String FLD_DEVICE_MODAL_NAME = "deviceModalName";
    public static String FLD_DEVICE_NAME = "deviceName";
    public static String FLD_DEVICE_SYSTEM_NAME = "deviceSystemName";
    public static String FLD_DEVICE_TOKEN = "deviceToken";
    public static String FLD_DONT_SHOW_ME_AGAIN= "dont_show_me_again";
    public static String FLD_ON_BORD_BUILD_VERSION = "onbordBuildVersion";
    public static String FLD_OS_VERSION = "osVersion";
    public static String FLD_PUSH_SERVICES_ENABLED = "pushServicesEnabled";
    public static String FLD_LOCATION_SERVICES_ENABLED = "locationServicesEnabled";

    public static String FLD_FB_ACCESS_TOKEN = "fbAccessToken";
    public static String FLD_CITY = "city";
    public static String FLD_STATE = "state";
    public static String FLD_LATITUDE = "latitude";
    public static String FLD_LONGITUDE = "longitude";
    public static String FLD_ZIP_CODE = "zipCode";
    public static String FLD_COUNTRY_CODE = "countryCode";
    public static String FLD_COUNTRY = "country";
    public static String FLD_TIMEZONE = "timezone";
    public static String FLD_OTP = "otp";
    public static String FLD_CITY_NAME = "cityName";
    public static String FLD_STATE_CODE = "stateCode";
    public static String FLD_COUNTRY_NAME = "countryName";
    public static String FLD_PASSENGER_ID = "passengerId";

    public static String FLD_IS_LOGIN = "isLogin";

    public static String FLD_USER_ADDRESS = "userAddress";
    public static String FLD_HEADING_DIRECTION = "headingDirection";
    public static String FLD_HEADER_TITLE = "header_title";
    public static String FLD_IS_HANDICAPPED = "isHandicapped";
    public static String FLD_AIRPORT_FLIGHT_TERMINAL_ID = "airportFlightTerminalId";
    public static String FLD_TOTAL_PASSENGER = "totalPassengers";
    public static String FLD_TOTAL_HANDICAPPED = "totalHandicapped";
    public static String FLD_BOOKING_ID = "bookingId";
    public static String FLD_RATING = "rating";
    public static String FLD_IS_NEAR_HOTEL = "isNearHotel";
    public static String FLD_IS_NEAR_AIRPORT = "isNearAirport";
    public static String FLD_BOOKING_OBJECT = "bookingObject";
    public static String FLD_BOOKING_STATUS = "bookingStatus";
    public static String FLD_HOTEL_OBJECT = "hotelObject";

    public static String JWT_SECRET_KEY = "b40c5fdd620f8352a573";

    public static String AppFolderName = "/onBord";
    public static String receivedMessages = "/receivedMessages";
    public static String tempFolderName = "/temp";

    public static String FileType_image = "image";
    public static String imageFormat = ".jpg";
    public static String fileNameForSaVeToSDCard;
    public static int heightOfVideoFile;
    public static String orientationMode;
    public static String PORTRAIT = "portrait";
    public static String LANDSCAPE = "landscape";
    public static int heightOfImageInLandScape = 0;
    public static int cardHeight = 0;
    public static String imagepath = "";
    public static final int PICK_FROM_CAMERA_VIDEO = 111;
    public static final int PICK_FROM_GALLERY_VIDEO = 4;

    public static String FLD_HISTORY_ID = "historyId";
    public static String FLD_LAST_HISTORY_ID = "lastHistoryId";

    public static String FLD_SUBJECT = "subject";
    public static String FLD_CONTENT = "content";
    public static String FLD_IS_PUSH_ENABLED = "isPushEnabled";
    public static String FLD_ASK_FOR_RATING = "askForRating";
    public static String FLD_SETTINGS = "settings";

    public static String RATING = "4";

    public static String STATUS_BOOKED = "BOOKED";
    public static String STATUS_ARRIVED = "ARRIVED";
    public static String STATUS_BOARDED = "BOARDED";
    public static String STATUS_REACHED = "REACHED";
    public static String STATUS_DROPPED = "DROPPED";
    public static String STATUS_ROLLING_ASSIGNMENT = "ROLLING_ASSIGNMENT";
    public static String STATUS_CANCEL = "CANCELLED";
    public static String STATUS_RATING = "RATING";
    public static boolean IS_GPS_CANCELED_CLICKED = false;
    /*Program Screen*/
    public static final String FLD_CALENDAR_EVENT_MASTER_REQUEST = "calendareventmasterRequest";
    public static final String FLD_START_DATE = "StartDate";
    public static final String FLD_END_DATE = "EndDate";

    /* Email List Screen*/
    public static final String FLD_EMAIL_MASTER_REQUEST = "emailmasterequest";

    public static final String FLD_CATEGORY_MENU_MASTER_REQUEST = "categorymenumasterrequest";

    public static final String FLD_CATEGORY_MASTER_REQUEST = "categorymasterrequest";

    public static final String FLD_PRODUCT_MASTER_REQUEST = "productmasterrequest";
    public static final String FLD_FCM_NOTIFICATION_REQUEST = "fcmNotificationRequest";
    public static final String FLD_COURSE_MASTER_REQUEST = "coursemasterrequest";
    public static final String FLD_EMAIL_DETAIL_REQUEST = "emaildetailequest";
    public static final String FLD_FORGOT_PASSWORD_REQUEST = "forgotPasswordRequest";
    public static final String FLD_RENEWAL_MASTER_REQUEST = "renewalmasterequest";
    public static final String FLD_DISCUSSION_TOPIC_REQUEST = "discussiontopicrequest";
    public static final String FLD_DISCUSSION_MASTER_REQUEST = "discussionmasterrequest";
    public static final String FLD_CREATE_DISCUSSION_REQUEST = "creatediscussionrequest";
    public static final String FLD_HOMESTATSREQUEST = "homeStatsRequest";
    public static final String FLD_PENDING_POST_EVENT_REQUEST = "pendingPostEventRequest";
    public static final String FLD_ADD_POST_EVENT_REQUEST = "addPostEventRequest";
    public static final String FLD_CALENDER_EVENTS_LIKE_DISLIKE_REQUEST = "calenderEventsLikeDislikeRequest";

    /* Register */
    public static String FLD_NON_MEMBER_MASTER_ENTITY = "nonmembermasterentity";
    public static final String FLD_ID = "id";

   /* Create Topic*/

    public static final String FLD_CREATE_TOPI_CREQUEST = "createtopicrequest";

    public static final String FLD_TOPIC = "Topic";

    public static final String FLD_TOPIC_DESCRIPTION = "TopicDescription";

   /* Create post Event */
    public static final String FLD_NO_OF_DELEGATES="No_of_Delegates";
    public static final String FLD_POST_EVENT_TITLE="post_event_title";
    public static final String FLD_NO_OF_NON_MEMBERS="No_of_Non_members";
    public static final String FLD_NO_OF_PRESENTED_TECHNICAL_PAPER="No_of_presented_technical_paper";
    public static final String FLD_SPEAKER_DETAILS="Speaker_Details";
    public static final String FLD_SPEAKER_NAME="Speaker_Name";
    public static final String FLD_MOBLIE_NO="Moblie_No";
    public static final String FLD_EMAIL_CAPITAL="Email";

    public static final String FLD_UPLOAD_FILE="Upload_file";
    public static final String FLD_POST_EVENT_DOC="PostEventDoc";
    public static final String FIELD_FILE1="file1";
    public static final String FIELD_UPLOAD_FILE="Upload_file";
    public static final String FIELD_IMAGE_="Image_";


  /*  Get openion poll request*/

  public static final String FLD_OPINION_POLL_REQUEST ="opinionPollRequest";

  public static final String FLD_OPINION_POLL_STATUS_REQUEST ="opinionPollStatusRequest";

  public static final String FLD_ACTION_AUTH_BY_MEMBER_REQUEST ="actionAuthByMemberRequest";

  public static final String FLD_ACTIONNAME ="ActionName";
  public static final String FLD_ACTION_FEEDBACKFORM ="FeedbackForm";
  public static final String FLD_ACTION_RENEWAL ="Renewal";

    /*  set openion poll request*/

    public static final String FLD_SET_OPINION_POLL_REQUEST ="setOpinionPollRequest";
    public static final String FLD_ISLIKE ="IsLike";

     /*Group notification Request*/
     public static final String FLD_GROUP_NOTIFICATION_REQUEST="groupNotificationRequest";

     public static final String FLD_LOGOUT_REQUEST="logoutRequest";


}
