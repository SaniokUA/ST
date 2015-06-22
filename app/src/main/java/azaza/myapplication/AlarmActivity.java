package azaza.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Alex on 19.06.2015.
 */
public class AlarmActivity extends Activity {

    TextView phone, date, contact, text, alarmdate;
    public int id;
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        phone = (TextView) findViewById(R.id.textPhone);
        date = (TextView) findViewById(R.id.textDate);
        contact = (TextView) findViewById(R.id.textContact);
        text = (TextView) findViewById(R.id.textDesc);
        //  alarmdate = (TextView) findViewById(R.id.textAlarm);

        Intent intent = this.getIntent();

        String phoneText = intent.getStringExtra("title");
        String contactText = intent.getStringExtra("contact");
        String descText = intent.getStringExtra("description");

        phone.setText(phoneText);
        contact.setText(contactText);
        text.setText(descText);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mp = MediaPlayer.create(this, alarmSound);
        mp.start();

    }

    public void onClose(View view) {

        this.finish();

    }

    public void onStopSignal(View view) {
        mp.stop();
    }

    protected void onStop() {
        mp.stop();
        super.onStop();
    }
}

