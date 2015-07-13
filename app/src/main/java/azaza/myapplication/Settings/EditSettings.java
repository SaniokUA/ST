package azaza.myapplication.Settings;

import android.content.SharedPreferences;

/**
 * Created by Alex on 13.07.2015.
 */
 public class EditSettings {

    public static void onnSyncGoogleCalendar(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 1);
        editor.commit();

    }

    public static void offSyncGoogleCalendar(SharedPreferences settings){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        editor.commit();

    }

}
