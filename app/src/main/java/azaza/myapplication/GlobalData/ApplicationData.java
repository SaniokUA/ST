package azaza.myapplication.GlobalData;


import android.app.Activity;

/**
 * Created by Alex on 27.03.2015.
 */
public class ApplicationData {


    public static Activity ActivityId;

    public static Activity getActivityId() {
        return ActivityId;
    }

    public static void setActivityId(Activity activityId) {
        ActivityId = activityId;
    }
}