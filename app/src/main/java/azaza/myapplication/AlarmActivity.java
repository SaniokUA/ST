package azaza.myapplication;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import azaza.myapplication.Model.Note;

/**
 * Created by Alex on 19.06.2015.
 */
public class AlarmActivity extends Activity {

    TextView phone, date, contact, text;
    public int id;
    MediaPlayer mMediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_alarm);

        phone = (TextView) findViewById(R.id.textPhone);
        date = (TextView) findViewById(R.id.textDate);
        contact = (TextView) findViewById(R.id.textContact);
        text = (TextView) findViewById(R.id.textDesc);

        Intent intent = this.getIntent();

        String phoneText = intent.getStringExtra("title");
        String contactText = intent.getStringExtra("contact");
        String descText = intent.getStringExtra("description");
        long time = intent.getLongExtra(Note.DATE, 0L);
        id = intent.getExtras().getInt(Note.ID);

        Calendar c = new GregorianCalendar();
        if (time + 1000 > c.getTimeInMillis() / 1000) {

            phone.setText(phoneText);
            contact.setText(contactText);
            text.setText(descText);

            try {
                Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource(this, alert);
                final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                    mMediaPlayer.setLooping(true);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                }
            } catch (IllegalStateException e) {
                //some text
            } catch (IOException e) {
                //some text
            }
        }
    }

    public void onClose(View view) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(id);
        this.finish();
    }

    public void onStopSignal(View view) {
        AlarmActivity.this.mMediaPlayer.stop();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onStop() {
        AlarmActivity.this.mMediaPlayer.stop();
        super.onStop();
    }
}

