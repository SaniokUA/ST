package azaza.myapplication.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import java.util.Calendar;
import java.util.GregorianCalendar;

import azaza.myapplication.DataBase.Note;
import azaza.myapplication.MainActivity;
import azaza.myapplication.R;

public class MyAlarmService extends Service {
    private NotificationManager mManager;
    private String title;
    private String text;
    private long time;

    public MyAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        // TODO Auto-generated method stub
        super.onCreate();
    }

  //  @SuppressWarnings("static-access")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        int id = intent.getExtras().getInt(Note.ID);
        long t = intent.getLongExtra(Note.DATE, 0L);
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(t * 1000);
        time = c.getTimeInMillis();

        title = intent.getStringExtra(Note.TITLE);
        text = intent.getStringExtra(Note.DESCRIPTION);

        mManager = (NotificationManager) this.getApplicationContext().
                getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent mIntent = new Intent(this.getApplicationContext(), MainActivity.class);

        mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent
                .getActivity(this.getApplicationContext(),0, mIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new Notification.Builder(this.getApplicationContext())
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound(alarmSound)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .getNotification();

        mManager.notify(id, notification);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
