package com.ishrae.app;

import android.app.Application;

import com.ishrae.app.model.SharedPref;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

/**
 * Created by Vishal.
 */

@ReportsCrashes(formKey = "", mailTo = "vishalsethia05@gmail.com")
public class AppInitialization extends Application {

    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();
        SharedPref.init(this);
    }
}
