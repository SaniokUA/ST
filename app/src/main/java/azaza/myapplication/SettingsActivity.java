package azaza.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Switch;

import azaza.myapplication.Settings.EditSettings;
import azaza.myapplication.Settings.LoadSettings;

/**
 * Created by Alex on 03.07.2015.
 */
public class SettingsActivity extends ActionBarActivity {

    Toolbar toolbar;
    Switch syncGoogle;
    public static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getPreferences(Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        syncGoogle = (Switch) findViewById(R.id.switchSync);
        syncGoogle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onChangeSwitchSync();
            }
        });



        if (LoadSettings.SYNC_CALENDAR == 1) {
            syncGoogle.setChecked(true);
        } else {
            syncGoogle.setChecked(false);
        }


    }

    public void onChangeSwitchSync() {
        if (LoadSettings.SYNC_CALENDAR == 1) {
            EditSettings.offSyncGoogleCalendar(settings);
        } else {
            EditSettings.onnSyncGoogleCalendar(settings);
        }
    }

}
