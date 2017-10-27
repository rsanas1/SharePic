package com.rishi.sharepic;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

/**
 * Created by rishi on 10/26/2017.
 */

public class StarterApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable Local Datastore.
        //Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("8d922fbeede4fa5177efa841934f04c5db396aca")
                .clientKey("72469cdc16b0356c011f6f902074d0e86f4132ba")
                .server("http://18.220.78.54:80/parse/")
                .build()
        );

       /* ParseObject object = new ParseObject("ExampleObject");
        object.put("myNumber", "123");
        object.put("myString", "rob");

        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException ex) {
                if (ex == null) {
                    Log.i("Parse Result", "Successful!");
                } else {
                    Log.i("Parse Result", "Failed" + ex.toString());
                }
            }
        });*/


       // ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }
}
