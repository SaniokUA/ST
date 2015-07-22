package azaza.myapplication.Settings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Libs.Image.ConvertImageBase64;
import azaza.myapplication.Model.GoogleCalendarsItem;

/**
 * Created by Alex on 13.07.2015.
 */
public class LoadSettings extends Activity {

    public static String USER_ACCOUNT;
    public static String GOOGLE_CALENDAR_ID;
    public static int AUTO_SIGN_IN;
    public static int SYNC_CALENDAR;
    public static int SHOW_ALARM_WINDOW;
    public static String USER_FULL_NAME;
    public static String USER_IMAGE;
    public static List<GoogleCalendarsItem> CALENDAR_LIST;
    public static SharedPreferences settings;
    Activity activity;

    private static LoadSettings instance = null;

    private LoadSettings(Activity activity) {
        this.activity = activity;
        settings  =  activity.getSharedPreferences("CallManager", Context.MODE_PRIVATE);
    }

    public static LoadSettings getInstance(Activity activity) {
        if (instance == null) {
            instance = new LoadSettings(activity);
        }
        return instance;
    }

    public void savePreferences() {
        // получить доступ к объекту Editor, чтобы изменить общие настройки.
        SharedPreferences.Editor editor = settings.edit();
        // задать новые базовые типы в объекте общих настроек.
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        editor.putString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_ID, "primary");
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
        editor.putInt(SettingsConst.PREF_ACCOUNT_AUTO_SING_IN, 1);
        editor.putInt(SettingsConst.PREF_ACCOUNT_FIRST_START, 1);
        editor.putString(SettingsConst.PREF_ACCOUNT_USER_IMAGE, null);
        editor.putString(SettingsConst.PREF_ACCOUNT_USER_FULL_NAME, null);
        editor.commit();
    }

    public void loadPreferences() {
        try {
          int  FRIST_START = settings.getInt(SettingsConst.PREF_ACCOUNT_FIRST_START, 0);
            if (FRIST_START == 0) {
                savePreferences();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        SYNC_CALENDAR = settings.getInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        GOOGLE_CALENDAR_ID = settings.getString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_ID, "primary");
        SHOW_ALARM_WINDOW = settings.getInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
        AUTO_SIGN_IN = settings.getInt(SettingsConst.PREF_ACCOUNT_AUTO_SING_IN, 1);
        USER_ACCOUNT = settings.getString(SettingsConst.PREF_ACCOUNT_NAME, null);
        USER_FULL_NAME = settings.getString(SettingsConst.PREF_ACCOUNT_USER_FULL_NAME, null);
        USER_IMAGE = settings.getString(SettingsConst.PREF_ACCOUNT_USER_IMAGE, null);

        if(LoadSettings.USER_IMAGE != null){
            ConvertImageBase64.loadImageInBase64(LoadSettings.USER_IMAGE);
        }

        if(LoadSettings.USER_ACCOUNT != null){
            UserData.setEmail(LoadSettings.USER_ACCOUNT);
        }
    }

    public static String getUserAccount() {
        return USER_ACCOUNT;
    }

    public static void setUserAccount(String userAccount) {
        USER_ACCOUNT = userAccount;
    }

    public static String getGoogleCalendarId() {
        return GOOGLE_CALENDAR_ID;
    }

    public static void setGoogleCalendarId(String googleCalendarId) {
        GOOGLE_CALENDAR_ID = googleCalendarId;
    }

    public static int getAutoSignIn() {
        return AUTO_SIGN_IN;
    }

    public static void setAutoSignIn(int autoSignIn) {
        AUTO_SIGN_IN = autoSignIn;
    }

    public static int getSyncCalendar() {
        return SYNC_CALENDAR;
    }

    public static void setSyncCalendar(int syncCalendar) {
        SYNC_CALENDAR = syncCalendar;
    }

    public static int getShowAlarmWindow() {
        return SHOW_ALARM_WINDOW;
    }

    public static void setShowAlarmWindow(int showAlarmWindow) {
        SHOW_ALARM_WINDOW = showAlarmWindow;
    }

    public static String getUserFullName() {
        return USER_FULL_NAME;
    }

    public static void setUserFullName(String userFullName) {
        USER_FULL_NAME = userFullName;
    }

    public static String getUserImage() {
        return USER_IMAGE;
    }

    public static void setUserImage(String userImage) {
        USER_IMAGE = userImage;
    }

    public static List<GoogleCalendarsItem> getCalendarList() {
        return CALENDAR_LIST;
    }

    public static void setCalendarList(List<GoogleCalendarsItem> calendarList) {
        CALENDAR_LIST = calendarList;
    }

}
