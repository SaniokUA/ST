package azaza.myapplication.Libs.Google;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Settings.SettingsConst;

/**
 * Created by Alex on 20.07.2015.
 */
public class LoadProfile {

    public static void onLoadProfile(Activity activity, android.support.v7.widget.Toolbar toolbar){
        SharedPreferences settings =  activity.getSharedPreferences("CallManager", Context.MODE_PRIVATE);

        if (settings.getString(SettingsConst.PREF_ACCOUNT_NAME, null) != null && UserData.getUserName() =="") {
            GoogleAuth googleAuth = GoogleAuth.getInstance(activity, toolbar);
            googleAuth.signInWithGplus(settings.getString(SettingsConst.PREF_ACCOUNT_NAME, null));
        }



    }

}
