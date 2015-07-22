package azaza.myapplication.Settings;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Model.GoogleCalendarsItem;

/**
 * Created by Alex on 13.07.2015.
 */
public class EditSettings {

    public static void onnSyncGoogleCalendar(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 1);
        editor.commit();
    }

    public static void offSyncGoogleCalendar(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        editor.commit();
    }

    public static void onnShowAlarmWindow(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
        editor.commit();
    }

    public static void offShowAlarmWindow(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 0);
        editor.commit();
    }

    public static void saveUserName(SharedPreferences settings, String accountName) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_NAME, accountName);
        editor.commit();
    }

    public static void deleteUserName(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_NAME, null);
        editor.commit();
    }

    public static void saveUserImage(SharedPreferences settings, String userImage) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_USER_IMAGE, userImage);
        editor.commit();
    }

    public static void deleteUserImage(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_USER_IMAGE, null);
        editor.commit();
    }

    public static void saveUserCalendars(SharedPreferences settings, List<GoogleCalendarsItem> calendars) throws IOException {

        Gson gson = new Gson();
        String gsonCalendars = gson.toJson(calendars);

        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_LIST, gsonCalendars);
        editor.commit();

    }

    public static void loadUserCalendars(SharedPreferences settings) throws IOException {
        Gson gson = new Gson();
        List<GoogleCalendarsItem> listCalendars = new ArrayList<>();
        String gsonCalendarList = settings.getString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_LIST, null);
        listCalendars = gson.fromJson(gsonCalendarList, new TypeToken<List<GoogleCalendarsItem>>(){}.getType());
        LoadSettings.CALENDAR_LIST = listCalendars;
    }

    public static void deleteUserCalendars(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_LIST, null);
        editor.commit();
    }

    public static void saveUserActiveCalendarId(SharedPreferences settings, String CalendarId) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_ID, CalendarId);
        editor.commit();
    }

    public static void deleteUserActiveCalendarId(SharedPreferences settings) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(SettingsConst.PREF_ACCOUNT_GOOGLE_CALENDAR_ID, "primary");
        editor.commit();
    }


}
