package azaza.myapplication;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import azaza.myapplication.DataBase.DataBaseProviderModern;
import azaza.myapplication.GlobalData.PhoneData;
import azaza.myapplication.Libs.Contacts.GetContactPhoto;
import azaza.myapplication.Libs.Google.AddEventCalendar;
import azaza.myapplication.Libs.Image.SetImageRadius;
import azaza.myapplication.Manager.MyAlarmManager;
import azaza.myapplication.Model.Note;
import azaza.myapplication.Reciver.MyAlarmReceiver;
import azaza.myapplication.Settings.LoadSettings;
import azaza.myapplication.Settings.SettingsConst;

/**
 * Created by Alex on 05.06.2015.
 */
public class CallActivity extends Activity {

    LinearLayout setAlarmwindow, addNote;
    TextView phone, date, contact, alarmText;
    ImageButton speak;
    EditText comment;
    String text = "";
    DataBaseProviderModern db = new DataBaseProviderModern();
    ArrayList<String> results;
    ImageView contactImageView;
    AlertDialog.Builder builder;
    View multiPickerLayout;

    long timeMili;

    int year, month, day, hour, minute;
    static int yearSet, monthSet, daySet, hourSet, minuteSet;
    public static int ID = 1;

    Calendar calendar;
    long time;

    public com.google.api.services.calendar.Calendar mService;
    GoogleAccountCredential credential;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        this.getWindow().setGravity(Gravity.BOTTOM);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);


        contactImageView = (ImageView) findViewById(R.id.contactImage);
        speak = (ImageButton) findViewById(R.id.button);
        phone = (TextView) findViewById(R.id.Phone);
        date = (TextView) findViewById(R.id.Date);
        comment = (EditText) findViewById(R.id.comment);
        setAlarmwindow = (LinearLayout) findViewById(R.id.SetAlarmWindow);
        addNote = (LinearLayout) findViewById(R.id.addNote);


        contact = (TextView) findViewById(R.id.Contact);

        alarmText = (TextView) findViewById(R.id.textTimeDate);


        phone.setText(PhoneData.PHONE);
        date.setText(PhoneData.DATE);

        if (PhoneData.IMAGE != null) {
            contactImageView.setVisibility(View.VISIBLE);
            contactImageView.setImageBitmap(SetImageRadius.getRoundedCornersImage(GetContactPhoto.getContactPhoto(this, PhoneData.PHONE), 50));
        } else {
            contactImageView.setImageResource(R.drawable.ic_account_plus);
        }

        if (PhoneData.CONTACT != "") {
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


        SharedPreferences settings = getSharedPreferences("CallManager", Context.MODE_PRIVATE);
        credential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(settings.getString(SettingsConst.PREF_ACCOUNT_NAME, null));

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

        if (settings.getString(SettingsConst.PREF_ACCOUNT_NAME, null) == null) {
            chooseAccount();
        }

    }

    public void onSave(View v) {

        Note note = new Note(ID++, PhoneData.PHONE, PhoneData.CONTACT, comment.getText().toString(), timeMili, false, true);
        setAlarm(note);
        text = (comment.getText().toString());
        db.addRec(this, 1,"All", PhoneData.PHONE, PhoneData.CONTACT, PhoneData.DATE, text, timeMili, 0, PhoneData.myTYPE, 1);

        // Load from Settings
        if (LoadSettings.SYNC_CALENDAR == 1) {
            if (PhoneData.CONTACT == null) {
                PhoneData.CONTACT = "";
            }
            new AddEventCalendar(this, text, PhoneData.CONTACT + " " + PhoneData.PHONE, timeMili).execute();
        }

        this.finish();
        showToast();

    }


    public void onCLose(View v) {
        this.finish();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("results", results);
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
    public void setDateTimePiker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        multiPickerLayout = inflater.inflate(R.layout.dialog_pickers, null, false);


        final DatePicker multiPickerDate = (DatePicker) multiPickerLayout.findViewById(R.id.multipicker_date);
        final TimePicker multiPickerTime = (TimePicker) multiPickerLayout.findViewById(R.id.multipicker_time);
        multiPickerTime.setIs24HourView(true);
        multiPickerTime.setCurrentHour(hour);
        multiPickerTime.setCurrentMinute(minute + 1);

        DialogInterface.OnClickListener dialogButtonListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
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
                        alarmText.setText(convertDate(timeMili * 1000));
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
        builder.show();

    }


    public void onAddAlarm(View view) {
        setDateTimePiker();
    }

    public static String convertDate(long dateInMilliseconds) {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        String result = df.format(dateInMilliseconds);
        return result;
    }

    public void addNoteAlarm(View view) {
        addNote.setVisibility(View.VISIBLE);
    }

    public void showToast() {
        //создаем и отображаем текстовое уведомление
        Toast toast = Toast.makeText(getApplicationContext(),
                "Saved",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode == RESULT_OK) {
                    // refreshResults();
                } else {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings = getSharedPreferences("CallManager", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(SettingsConst.PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                        //   refreshResults();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    //  mStatusText.setText("Account unspecified.");
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    //    refreshResults();
                } else {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void chooseAccount() {
        startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }


    private boolean isGooglePlayServicesAvailable() {
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        CallActivity.this,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

}
