package azaza.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

import azaza.myapplication.DataBase.DB;
import azaza.myapplication.DataBase.Note;
import azaza.myapplication.GlobalData.PhoneData;
import azaza.myapplication.Manager.MyAlarmManager;
import azaza.myapplication.Reciver.MyAlarmReceiver;

/**
 * Created by Alex on 05.06.2015.
 */
public class CallActivity extends Activity{

    LinearLayout setAlarmwindow;
    Switch switchAlarmWindow;
    TextView phone, date;
    ImageButton speak;
    Button setDate, setTime;
    EditText comment, editDate, editTime;
    String text;
    DB db = new DB(this);
    ArrayList<String> results;

    int year, month, day, hour, minute;


    DatePicker datePicker;
    Calendar calendar;
    long time;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);



        speak = (ImageButton) findViewById(R.id.button);
        phone = (TextView) findViewById(R.id.Phone);
        date = (TextView) findViewById(R.id.Date);
        comment = (EditText) findViewById(R.id.comment);
        setAlarmwindow  = (LinearLayout) findViewById(R.id.SetAlarmWindow);
        switchAlarmWindow = (Switch) findViewById(R.id.switchAlarm);

        setDate = (Button) findViewById(R.id.setDate);
        setTime = (Button) findViewById(R.id.setTime);

        editDate = (EditText) findViewById(R.id.editDate);
        editTime = (EditText) findViewById(R.id.editTime);


        phone.setText(PhoneData.PHONE);
        date.setText(PhoneData.DATE);


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


    }

    public void onSave(View v){

        Note note = new Note(1, "Title111", "Desc111", time, false, true);
        setAlarm(note);


        db.open();
        text = (comment.getText().toString());
        db.addRec(PhoneData.myTYPE, PhoneData.PHONE, PhoneData.DATE, text);
        this.finish();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


    public void onCLose(View v){
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

    public void onSwitch(View v){
        if(switchAlarmWindow.isChecked()){
            setAlarmwindow.setVisibility(View.VISIBLE);
        }else{
            setAlarmwindow.setVisibility(View.GONE);
        }
    }

    public void setDate(View view) {
        showDialog(1);
    }
    public void setTime(View view) { showDialog(2); }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        calendar = Calendar.getInstance();
        getTimeMili(calendar);
        if (id == 1) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, myDateListener, year, month, day);

        }
        if (id == 2) {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            return new TimePickerDialog(this, myTimeListener, hour, minute,  DateFormat.is24HourFormat(this));
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            showDate(arg1, arg2+1, arg3);
        }
    };
    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            showTime(hourOfDay, minute);
        }
    };

    private void showDate(int year, int month, int day) {

        editDate.setText(new StringBuilder().append(day).append("/").append(month).append("/").append(year));


    }

    private void showTime(int hour, int minute) {
        editTime.setText(new StringBuilder().append(hour).append(":")
                .append(minute));
    }

    private void setAlarm(Note note) {
        MyAlarmManager.setAlarm(this, MyAlarmReceiver.class, note);
    }

    public long getTimeMili(Calendar calendar){

        //Calendar calendar1 = new GregorianCalendar( year, month, day, hour, minute );

        time = calendar.getTimeInMillis() / 1000;

         return time;
    }
}
