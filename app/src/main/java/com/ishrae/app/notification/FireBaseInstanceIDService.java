package com.ishrae.app.notification;
 /*
 * Author       :   Vishal Jain
 * Designation  :   Android Developer
 * E-mail       :   vishal@nssolutions.in
 * Date         :   7/7/16
 * Company      :   Nss Solutions
 * Purpose      :   purpose
 * Description  :   desc.
 */

import android.content.Context;
import android.text.TextUtils;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.ishrae.app.model.SharedPref;


// this class will get called automatically since it is define in manifest file.
public class FireBaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = FireBaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        SharedPref.setDeviceToken(FireBaseInstanceIDService.this, refreshedToken);
        //register device token here with server
    }

    public static String getDeviceToken(Context context) {
        String token = SharedPref.getDeviceToken();
        if (TextUtils.isEmpty(token)) {
            token = FirebaseInstanceId.getInstance().getToken();
        }
        return token;
    }
}
