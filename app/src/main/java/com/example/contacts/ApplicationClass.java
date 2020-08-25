package com.example.contacts;

import android.app.Application;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;

import java.util.List;

public class ApplicationClass extends Application {

    public static final String APPLICATION_ID = "1B380DA6-55A8-EDC3-FF7D-86E6CEA28900";
    public static final String API_KEY = "3940CD09-36D4-49E4-9AE4-C52FE6473563";
    public static final String SERVER_URL = "https://api.backendless.com";
    public static BackendlessUser user;
    //linking user to contact
    public  static List<Contact> contacts;

    @Override
    public void onCreate() {
        super.onCreate();

        Backendless.setUrl( SERVER_URL );
        Backendless.initApp( getApplicationContext(),
                APPLICATION_ID,
                API_KEY );

    }
}
