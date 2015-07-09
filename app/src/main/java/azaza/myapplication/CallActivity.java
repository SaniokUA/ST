package azaza.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import azaza.myapplication.DataBase.DB;
import azaza.myapplication.GlobalData.PhoneData;
import azaza.myapplication.Libs.Contacts.GetContactPhoto;
import azaza.myapplication.Libs.Image.SetImageRadius;
import azaza.myapplication.Manager.MyAlarmManager;
import azaza.myapplication.Model.Note;
import azaza.myapplication.Reciver.MyAlarmReceiver;

/**
 * Created by Alex on 05.06.2015.
 */
public class CallActivity extends Activity {

    LinearLayout setAlarmwindow;
    Switch switchAlarmWindow;
    TextView phone, date, contact, alarmText;
    ImageButton speak;
    Button setDate, setTime;
    EditText comment, editDate, editTime;
    String text;
    DB db = new DB(this);
    ArrayList<String> results;
    ImageView contactImageView;
    AlertDialog.Builder builder;
    View multiPickerLayout;

    long timeMili;

    int year, month, day, hour, minute;
    static int yearSet, monthSet, daySet, hourSet, minuteSet;
    public static int ID=1;

    Calendar calendar;
    long time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        this.getWindow().setGravity(Gravity.BOTTOM);



        multiPickerLayout = LayoutInflater.from(this).inflate(R.layout.dialog_pickers, null);
        contactImageView = (ImageView) findViewById(R.id.contactImage);
        speak = (ImageButton) findViewById(R.id.button);
        phone = (TextView) findViewById(R.id.Phone);
        date = (TextView) findViewById(R.id.Date);
        comment = (EditText) findViewById(R.id.comment);
        setAlarmwindow = (LinearLayout) findViewById(R.id.SetAlarmWindow);
        switchAlarmWindow = (Switch) findViewById(R.id.switchAlarm);

        contact = (TextView) findViewById(R.id.Contact);

        alarmText = (TextView) findViewById(R.id.textTimeDate);


        phone.setText(PhoneData.PHONE);
        date.setText(PhoneData.DATE);

        if(PhoneData.IMAGE!=null) {
            contactImageView.setVisibility(View.VISIBLE);
            contactImageView.setImageBitmap(SetImageRadius.getRoundedCornersImage(GetContactPhoto.getContactPhoto(this,PhoneData.PHONE), 50));
        }else{
            contactImageView.setVisibility(View.GONE);
        }


        if(PhoneData.CONTACT !="") {
            contact.setText(PhoneData.CONTACT);
        }


        speak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // This are the intents needed to start the Voice recognizer
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-RU");
                i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
                startActivityForResult(i, 1010);
            }
        });
        if (savedInstanceState != null) {
            results = savedInstanceState.getStringArrayList("results");

            if (results != null)
                comment.setText(results.toString());

        }

        setDateTimePiker();

    }

    public void onSave(View v) {


        Note note = new Note(ID++, PhoneData.PHONE, PhoneData.CONTACT, comment.getText().toString(), timeMili, false, true);
        setAlarm(note);
        db.open();
        text = (comment.getText().toString());
        db.addRec(PhoneData.myTYPE, PhoneData.PHONE, PhoneData.CONTACT, PhoneData.DATE, text, timeMili);
        db.close();
        this.finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void onCLose(View v) {
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1010 && resultCode == RESULT_OK) {
            results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            comment.setText(results.toString());
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("results", results);
    }

    public void onSwitch(View v) {
        if (switchAlarmWindow.isChecked()) {
            setAlarmwindow.setVisibility(View.VISIBLE);
        } else {
            setAlarmwindow.setVisibility(View.GONE);
        }
    }



    private void setAlarm(Note note) {
        MyAlarmManager.setAlarm(this, MyAlarmReceiver.class, note);
    }

    public long getTimeMili(int year, int month, int day, int hour, int minute) {

        Calendar calendar1 = new GregorianCalendar(year, month, day, hour, minute);
        time = calendar1.getTimeInMillis() / 1000;

        return time;
    }

    /**
     * Set time and date pikers with settings
     */
    public void setDateTimePiker(){

        calendar = Calendar.getInstance();

            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);

        final DatePicker multiPickerDate = (DatePicker) multiPickerLayout.findViewById(R.id.multipicker_date);
        final TimePicker multiPickerTime = (TimePicker) multiPickerLayout.findViewById(R.id.multipicker_time);
        multiPickerTime.setIs24HourView(true);
        multiPickerTime.setCurrentHour(hour);
        multiPickerTime.setCurrentMinute(minute+1);

        DialogInterface.OnClickListener dialogButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case DialogInterface.BUTTON_NEGATIVE: {
                        // user tapped "cancel"
                        dialog.dismiss();
                        break;
                    }
                    case DialogInterface.BUTTON_POSITIVE: {

                        yearSet = multiPickerDate.getYear();
                        monthSet = multiPickerDate.getMonth();
                        daySet = multiPickerDate.getDayOfMonth();
                        hourSet = multiPickerTime.getCurrentHour();
                        minuteSet = multiPickerTime.getCurrentMinute();

                        setAlarmwindow.setVisibility(View.VISIBLE);
                        timeMili = getTimeMili(yearSet, monthSet, daySet, hourSet, minuteSet);
                        alarmText.setText(convertDate(timeMili*1000));
                        dialog.dismiss();
                        break;
                    }
                    default: {
                        dialog.dismiss();
                        break;
                    }
                }
            }
        };

        builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setView(multiPickerLayout);
        builder.setPositiveButton("Set", dialogButtonListener);
        builder.setNegativeButton("Cancel", dialogButtonListener);
        builder.create();

    }

    public void onAddAlarm(View view){
        builder.show();
    }

    public static String convertDate(long dateInMilliseconds) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        String result = df.format(dateInMilliseconds);
        return result;
    }


    @Override
    protected void onStop() {
        super.onStop();
    }
}
