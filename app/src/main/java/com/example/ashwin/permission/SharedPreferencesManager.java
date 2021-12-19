package com.example.ashwin.permission;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashwin on 24/8/16.
 */
public class SharedPreferencesManager {
    private Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static final String PREFERENCES = "my_preferences";

    public SharedPreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mContext = context;
    }

    // Check if user has denied contacts permission with never ask again
    private static final String NEVER_ASK_FOR_CONTACTS_PERMISSION = "Never Ask For Contacts Permission";

    public Boolean getNeverAskForContactsPermission() {
        return  mSharedPreferences.getBoolean(NEVER_ASK_FOR_CONTACTS_PERMISSION, false);
    }

    public void setNeverAskForContactsPermission(boolean askForContactsPermission) {
        mSharedPreferences.edit().putBoolean(NEVER_ASK_FOR_CONTACTS_PERMISSION, askForContactsPermission).commit();
    }
}
