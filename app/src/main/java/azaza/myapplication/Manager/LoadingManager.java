package azaza.myapplication.Manager;

import android.app.Activity;

/**
 * Created by Alex on 21.07.2015.
 */
public class LoadingManager {

    Activity activity;
    private static LoadingManager instance = null;

    private LoadingManager(Activity activity) {

        this.activity = activity;

    }

    public static LoadingManager getInstance(Activity activity) {
        if (instance == null) {
            instance = new LoadingManager(activity);
        }
        return instance;
    }

    public static void loadSettings(){

    }



}
