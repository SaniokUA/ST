package azaza.myapplication.Manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

import azaza.myapplication.DataBase.Note;


public class MyAlarmManager {

    public static void setAlarm(Context sender, Class receiver, int id, long time, String name, String text) {
        Intent myIntent = new Intent(sender, receiver);
        myIntent.putExtra("id", id);
        myIntent.putExtra("time", time);
        myIntent.putExtra("title", name);
        myIntent.putExtra("description", text);

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(time * 1000);
        long t = c.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sender, id, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)sender. getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, t, pendingIntent);
    }

    public static void cancelAlarm(Context sender, Class receiver, int id, long time, String name, String text) {
        Intent myIntent = new Intent(sender, receiver);
        myIntent.putExtra("id", id);
        myIntent.putExtra("time", time);
        myIntent.putExtra("title", name);
        myIntent.putExtra("description", text);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(sender, id, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)sender. getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    public static void setAlarm(Context sender, Class receiver, Note note) {
        Intent myIntent = new Intent(sender, receiver);
        myIntent.putExtra("id", note.getId());
        myIntent.putExtra("contact", note.getContact());
        myIntent.putExtra("time", note.getDate());
        myIntent.putExtra("title", note.getName());
        myIntent.putExtra("description", note.getText());

        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(note.getDate() * 1000);
        long t = c.getTimeInMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sender, note.getId(), myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)sender. getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, t, pendingIntent);
    }

    public static void cancelAlarm(Context sender, Class receiver, Note note) {
        Intent myIntent = new Intent(sender, receiver);
        myIntent.putExtra("id", note.getId());
        myIntent.putExtra("time", note.getDate());
        myIntent.putExtra("title", note.getName());
        myIntent.putExtra("description", note.getText());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(sender, note.getId(), myIntent, 0);
        AlarmManager alarmManager = (AlarmManager)sender. getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

}
