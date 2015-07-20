package azaza.myapplication.Settings;

import android.content.SharedPreferences;

/**
 * Created by Alex on 13.07.2015.
 */
 public class EditSettings {

    static LoadSettings loadSettings = LoadSettings.getInstance();

    public static void onnSyncGoogleCalendar(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 1);
        editor.commit();
        loadSettings.loadPreferences(settings);
    }

    public static void offSyncGoogleCalendar(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        editor.commit();
        loadSettings.loadPreferences(settings);

    }

    public static void onnShowAlarmWindow(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
        editor.commit();

    }

    public static void offShowAlarmWindow(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 0);
        editor.commit();

    }

    public static void saveUserName (SharedPreferences settings, String accountName){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
    }

    public static void removeUserName (SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_NAME, null);
        editor.commit();
    }

    public static void saveUserImage (SharedPreferences settings, long userImage){
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(SettingsConst.PREF_ACCOUNT_NAME, userImage);
        editor.commit();
    }

    public static void deleteUserImage (SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(SettingsConst.PREF_ACCOUNT_NAME, 0);
        editor.commit();
    }

}
