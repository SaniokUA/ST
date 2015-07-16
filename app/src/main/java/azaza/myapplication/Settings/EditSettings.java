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
        loadSettings.loadPreferences(settings);
    }

    public static void offShowAlarmWindow(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 0);
        editor.commit();
        loadSettings.loadPreferences(settings);

    }

}
