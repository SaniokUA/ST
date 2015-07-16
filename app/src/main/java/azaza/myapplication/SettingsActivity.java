package azaza.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import azaza.myapplication.Manager.CalendarManager;
import azaza.myapplication.Settings.EditSettings;
import azaza.myapplication.Settings.LoadSettings;

/**
 * Created by Alex on 03.07.2015.
 */
public class SettingsActivity extends ActionBarActivity {

    Toolbar toolbar;
    Switch syncGoogle, showAlarmWindow;
    public static SharedPreferences settings;
    Spinner selectCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getPreferences(Context.MODE_PRIVATE);

        LoadSettings loadSettings = LoadSettings.getInstance();
        loadSettings.loadPreferences(settings);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        syncGoogle = (Switch) findViewById(R.id.switchSync);
        showAlarmWindow = (Switch) findViewById(R.id.switchAlarmWindow);

        selectCalendar = (Spinner) findViewById(R.id.spinnerCalendar);

        CalendarManager calendarManager = new CalendarManager(LoadSettings.CALENDAR_LIST);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calendarManager.getAllCalendarsName() );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        selectCalendar.setAdapter(adapter);



        if (LoadSettings.SYNC_CALENDAR == 1) {
            syncGoogle.setChecked(true);
        } else {
            syncGoogle.setChecked(false);
        }

        if (LoadSettings.SHOW_ALARM_WINDOW == 1) {
            showAlarmWindow.setChecked(true);
        } else {
            showAlarmWindow.setChecked(false);
        }
    }

    public void onChangeSwitchSync(View view) {
        if (LoadSettings.SYNC_CALENDAR == 1) {
            EditSettings.offSyncGoogleCalendar(settings);
        } else {
            EditSettings.onnSyncGoogleCalendar(settings);
        }
    }

    public void onChangeSwitchShowAlarmWindow(View view) {
        if (LoadSettings.SHOW_ALARM_WINDOW == 1) {
            EditSettings.offShowAlarmWindow(settings);
        } else {
            EditSettings.onnShowAlarmWindow(settings);
        }
    }

}
