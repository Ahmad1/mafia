package com.example.ahmadnemati.mfa;

import android.app.Application;

/**
 * Created by ahmad.nemati on 2/27/18.
 */

public class MFAApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceHelper.initialize(this.getSharedPreferences("Mafia_Preferences", MODE_PRIVATE));
    }
}
