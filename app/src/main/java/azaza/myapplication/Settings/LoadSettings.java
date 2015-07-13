package azaza.myapplication.Settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;

/**
 * Created by Alex on 13.07.2015.
 */
public class LoadSettings extends Activity {

    public static int SYNC_CALENDAR = 0;
    public static String ACTIVE_CALENDAR = "primary";
    public static int SHOW_ALARM_WINDOW = 1;
    public static int AUTO_SIGN_IN = 0;
    public static int FRIST_START = 0;
    public static SharedPreferences settings;

    public LoadSettings(SharedPreferences settings) {
        this.settings = settings;
        try {
            FRIST_START =  settings.getInt(SettingsConst.PREF_ACCOUNT_FIRST_START, 0);
            if(FRIST_START == 0){
                savePreferences();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            savePreferences();
        }
        new getSettingsInBackground().doInBackground();

    }



    protected void savePreferences() {
        // получить доступ к объекту Editor, чтобы изменить общие настройки.
        SharedPreferences.Editor editor = settings.edit();
        // задать новые базовые типы в объекте общих настроек.
        editor.putInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
        editor.putString(SettingsConst.PREF_ACCOUNT_ACTIVE_CALENDAR, "primary");
        editor.putInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
        editor.putInt(SettingsConst.PREF_ACCOUNT_AUTO_SING_IN, 0);
        editor.putInt(SettingsConst.PREF_ACCOUNT_FIRST_START, 1);
        editor.commit();
    }

    private class getSettingsInBackground extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            SYNC_CALENDAR = settings.getInt(SettingsConst.PREF_ACCOUNT_SYNC_GOOGLE, 0);
            ACTIVE_CALENDAR = settings.getString(SettingsConst.PREF_ACCOUNT_ACTIVE_CALENDAR, "primary");
            SHOW_ALARM_WINDOW = settings.getInt(SettingsConst.PREF_ACCOUNT_SHOW_ALARM_WINDOW, 1);
            AUTO_SIGN_IN = settings.getInt(SettingsConst.PREF_ACCOUNT_AUTO_SING_IN, 0);
            return null;
        }
    }


}
