package azaza.myapplication.Settings;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.List;

import azaza.myapplication.Model.GoogleCalendarsItem;

/**
 * Created by Alex on 13.07.2015.
 */
public class LoadSettings extends Activity {

    public static int SYNC_CALENDAR = 0;
    public static int SHOW_ALARM_WINDOW = 1;
    public static List<GoogleCalendarsItem> CALENDAR_LIST;

    private static LoadSettings instance = null;

    private LoadSettings() {
    }

    public static LoadSettings getInstance() {
        if (instance == null) {
            instance = new LoadSettings();
        }
        return instance;
    }

    public void savePreferences(SharedPreferences settings) {
        // получить доступ к объекту Editor, чтобы изменить общие настройки.
        SharedPreferences.Editor editor = settings.edit();
        // задать новые базовые типы в объекте общих настроек.
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        editor.putString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_ID, "primary");
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
        editor.putInt(SettingsConst.PREF_ACCOUNT_AUTO_SING_IN, 1);
        editor.putInt(SettingsConst.PREF_ACCOUNT_FIRST_START, 1);
        editor.putLong(SettingsConst.PREF_ACCOUNT_USER_IMAGE, 0);
        editor.commit();
    }

    public void loadPreferences(SharedPreferences settings) {
        try {
          int  FRIST_START = settings.getInt(SettingsConst.PREF_ACCOUNT_FIRST_START, 0);
            if (FRIST_START == 0) {
                savePreferences(settings);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            savePreferences(settings);
        }
//
//        SYNC_CALENDAR = settings.getInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
//        GOOGLE_CALENDAR_ID = settings.getString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_ID, "primary");
//        SHOW_ALARM_WINDOW = settings.getInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
//        AUTO_SIGN_IN = settings.getInt(SettingsConst.PREF_ACCOUNT_AUTO_SING_IN, 1);
//        USER_ACCOUNT = settings.getString(SettingsConst.PREF_ACCOUNT_NAME, null);
//        USER_IS_LOGIN = settings.getInt(SettingsConst.PREF_ACCOUNT_USER_IS_LOGIN, 0);
    }

}
