package azaza.myapplication.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;

import java.util.Calendar;
import java.util.GregorianCalendar;

import azaza.myapplication.AlarmActivity;
import azaza.myapplication.Model.Note;
import azaza.myapplication.R;
import azaza.myapplication.Settings.LoadSettings;

public class MyAlarmService extends Service {
    private NotificationManager mManager;
    private String title;
    private String text;
    private String contact;
    private long time;
    String contactData;
    public static SharedPreferences settings;

    public MyAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        // TODO Auto-generated method stub
        super.onCreate();
    }

    //  @SuppressWarnings("static-access")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int id = intent.getExtras().getInt(Note.ID);
        long t = intent.getLongExtra(Note.DATE, 0L);
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(t * 1000);
        time = c.getTimeInMillis();

        title = intent.getStringExtra(Note.TITLE);
        text = intent.getStringExtra(Note.DESCRIPTION);
        contact = intent.getStringExtra(Note.CONTACT);

        mManager = (NotificationManager) this.getApplicationContext().
                getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this.getApplicationContext(), AlarmActivity.class);

        mIntent.putExtra("id", id);
        mIntent.putExtra("time", time);
        mIntent.putExtra("contact", contact);
        mIntent.putExtra("title", title);
        mIntent.putExtra("description", text);

        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Load settings and check


        if (LoadSettings.SHOW_ALARM_WINDOW == 1) {
            startActivity(mIntent);
        }

        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (contact != null) {
            contactData = contact;
        } else {
            contactData = title;
        }

        Notification notification = new Notification.Builder(this.getApplicationContext())
                .setContentTitle(contactData)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pendingNotificationIntent)
                .getNotification();

        mManager.notify(id, notification);


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
