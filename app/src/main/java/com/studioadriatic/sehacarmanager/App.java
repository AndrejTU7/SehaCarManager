package com.studioadriatic.sehacarmanager;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;

import com.studioadriatic.sehacarmanager.models.User;

/**
 * Created by kristijandraca@gmail.com
 */
public class App extends Application{
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String GCM_SERVER_API_KEY = "AIzaSyBr7vfUX4Q8IFK_cop2c2omAAb45LC4qNs";
    public static final String GCM_SENDER_ID = "269397702090";
    public static final String SP_TOKEN = "sp_token";

    public static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        App.currentUser = currentUser;
    }

    public static String getCurrentToken(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(App.SP_TOKEN, "");
    }
}
