package kr.bottomtab.pettner;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferencesManager {
    private static MySharedPreferencesManager instance;
    public static MySharedPreferencesManager getInstance() {
        if (instance == null) {
            instance = new MySharedPreferencesManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private MySharedPreferencesManager() {
        mPrefs = PreferenceManager.getDefaultSharedPreferences(kr.bottomtab.pettner.BaseApplication.getContext());
        mEditor = mPrefs.edit();
    }

    public static final String KEY_ID = "id";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_DOGSELECT = "dogselect";
    public static final String KEY_HOSPITALSELECT="hospitalselect";
    public static final String KEY_BADGECOUNT="badgecount";

    public void setId(String id) {
        mEditor.putString(KEY_ID, id);
        mEditor.commit();
    }

    public String getId() {
        return mPrefs.getString(KEY_ID,"");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();
    }

    public String getPassword() {
        return mPrefs.getString(KEY_PASSWORD, "");
    }

    public void setDogselect(String id) {
        mEditor.putString(KEY_DOGSELECT, id);
        mEditor.commit();
    }

    public String getDogselect() {
        return mPrefs.getString(KEY_DOGSELECT,"");
    }

    public void setHospitalselect(String id) {
        mEditor.putString(KEY_HOSPITALSELECT, id);
        mEditor.commit();
    }

    public String getHospitalselect() {
        return mPrefs.getString(KEY_HOSPITALSELECT,"");
    }

    public void setBadgecount(int id) {
        mEditor.putInt(KEY_BADGECOUNT, id);
        mEditor.commit();
    }

    public int getBadgecount() {
        return mPrefs.getInt(KEY_BADGECOUNT,0);
    }



    public boolean isBackupSync() {
        return mPrefs.getBoolean("perf_sync", false);
    }
}
