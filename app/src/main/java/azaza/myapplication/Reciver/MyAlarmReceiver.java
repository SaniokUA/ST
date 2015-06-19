package azaza.myapplication.Reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import azaza.myapplication.DataBase.Note;
import azaza.myapplication.Service.MyAlarmService;

public class MyAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getExtras().getInt(Note.ID);
        long time = intent.getLongExtra(Note.DATE, 0L);
        String title = intent.getStringExtra(Note.TITLE);
        String text = intent.getStringExtra(Note.DESCRIPTION);

        Intent service1 = new Intent(context, MyAlarmService.class);

        service1.putExtra("id", id);
        service1.putExtra("time", time);
        service1.putExtra("title", title);
        service1.putExtra("description", text);

        context.startService(service1);

    }
}
