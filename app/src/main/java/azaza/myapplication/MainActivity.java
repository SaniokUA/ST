package azaza.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import azaza.myapplication.Adapter.ListItemAdapter;
import azaza.myapplication.DataBase.DataBaseProviderModern;
import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Libs.GetMiliDate;
import azaza.myapplication.Libs.Google.LoadProfile;
import azaza.myapplication.Menu.MaterialMenu;
import azaza.myapplication.Model.ListItem;
import azaza.myapplication.Settings.LoadSettings;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    ListView listViewToday, listViewTomorrow, listViewFuture, listViewNoDate;
    DataBaseProviderModern db = new DataBaseProviderModern();
    List<ListItem> data;
    Toolbar toolbar;
    ListItemAdapter adapter;
    LinearLayout emptyToday, emptyTomorrow, emptyFuture, emptyNoDate;
    AlertDialog.Builder ad;
    public Drawer.Result drawerResult = null;
    TabHost tabs;
    static int mark = 0;
    Spinner selectCategory;

    DataBaseProviderModern dataBaseProvider = new DataBaseProviderModern();

    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSettings();
        ctx = this;

        getSupportLoaderManager().initLoader(1, null, this);

        initTabs();

        emptyToday = (LinearLayout) findViewById(R.id.today);
        emptyTomorrow = (LinearLayout) findViewById(R.id.tomorrow);
        emptyFuture = (LinearLayout) findViewById(R.id.future);
        emptyNoDate = (LinearLayout) findViewById(R.id.noDate);


        listViewToday = (ListView) findViewById(R.id.listMainToday);
        listViewTomorrow = (ListView) findViewById(R.id.listMainTomorrow);
        listViewFuture = (ListView) findViewById(R.id.listMainFuture);
        listViewNoDate = (ListView) findViewById(R.id.listMainNoDate);

        listToday();
        listTomorrow();
        listFuture();
        listNoDate();


        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        selectCategory = (Spinner) toolbar.findViewById(R.id.spinner_nav);
        List<String> listCategory = new ArrayList<>();
        Cursor category = dataBaseProvider.fetchUniqueMembers();
        if (!category.moveToFirst()) {
        } else {
            do {
                listCategory.add(category.getString(category.getColumnIndex(DataBaseProviderModern.COLUMN_CATEGORY)));
            } while (category.moveToNext());
        }
        ArrayAdapter < String > adapterCategory = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCategory);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectCategory.setAdapter(adapterCategory);




        drawerResult = MaterialMenu.createCommonDrawer(this, toolbar);

        if (UserData.getUserName() == "") {
            LoadProfile.onLoadProfile(this, toolbar);
        }


        ad = new AlertDialog.Builder(this);
        ad.setTitle("Delete all items");  // заголовок
        ad.setIcon(R.drawable.ic_delete_dark);
        ad.setMessage("Do you want delete all items?"); // сообщение
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                db.delAllRec(MainActivity.this);
                getSupportLoaderManager().initLoader(0, null, MainActivity.this);
            }
        });

        ad.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        ad.setCancelable(true);
    }

    @Override
    public void onBackPressed() {
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_marked) {

            if (mark == 1) {
                mark = 0;
            } else {
                mark = 1;
            }
            listToday();
            listTomorrow();
            listFuture();
            listNoDate();

        }

        if (id == R.id.action_clear) {
            ad.show();
        }

        if (id == R.id.action_exit) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public List<ListItem> getModel(long dayStart, long dayEnd) {

        List<ListItem> list = new ArrayList<ListItem>();
        Cursor c = dataBaseProvider.getMainListTaskToDate(this, dayStart / 1000, dayEnd / 1000, mark);
        if (!c.moveToFirst()) {
        } else {
            do {
                list.add(get(c.getInt(c.getColumnIndex(DataBaseProviderModern.COLUMN_ID)), c.getInt(c.getColumnIndex(DataBaseProviderModern.COLUMN_ACTIVE)), c.getString(c.getColumnIndex(DataBaseProviderModern.COLUMN_CATEGORY)),
                        c.getString(c.getColumnIndex(DataBaseProviderModern.COLUMN_TXT)), GetMiliDate.millisToDateConvert(c.getLong(c.getColumnIndex(DataBaseProviderModern.COLUMN_ALARM_DATE))), c.getInt(c.getColumnIndex(DataBaseProviderModern.COLUMN_MARKED))));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<ListItem> getModelFuture(long dayStart) {

        List<ListItem> list = new ArrayList<ListItem>();

        Cursor c = dataBaseProvider.getMainListTaskToFuture(this, dayStart / 1000, mark);
        if (!c.moveToFirst()) {
        } else {
            do {
                list.add(get(c.getInt(c.getColumnIndex(DataBaseProviderModern.COLUMN_ID)), c.getInt(c.getColumnIndex(DataBaseProviderModern.COLUMN_ACTIVE)), c.getString(c.getColumnIndex(DataBaseProviderModern.COLUMN_CATEGORY)),
                        c.getString(c.getColumnIndex(DataBaseProviderModern.COLUMN_TXT)), GetMiliDate.millisToDateConvert(c.getLong(c.getColumnIndex(DataBaseProviderModern.COLUMN_ALARM_DATE))), c.getInt(c.getColumnIndex(DataBaseProviderModern.COLUMN_MARKED))));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public ListItem get(int id, int active, String category, String text, String alarmSignal, int marked) {
        return new ListItem(id, active, category, text, alarmSignal, marked);
    }

    public void loadSettings() {
        final LoadSettings loadSettings = LoadSettings.getInstance(this);
        Thread t = new Thread(new Runnable() {
            public void run() {
                loadSettings.loadPreferences();
            }
        });
        t.start();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, DataBaseProviderModern.CONTACT_CONTENT_URI, null, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        listToday();
        listTomorrow();
        listFuture();
        listNoDate();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    static class MyCursorLoader extends CursorLoader {
        DataBaseProviderModern db;

        public MyCursorLoader(Context context, DataBaseProviderModern db) {
            super(context);
            this.db = db;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData(ctx);
            return cursor;
        }
    }


    public void listToday() {

        Date date = Calendar.getInstance().getTime();
        long startDay = GetMiliDate.getStartOfDay(date);
        long endDay = GetMiliDate.getEndOfDay(date);

        data = getModel(startDay, endDay);
        adapter = new ListItemAdapter(this, data);
        if (adapter.getCount() == 0) {
            emptyToday.setVisibility(View.GONE);
        } else {
            emptyToday.setVisibility(View.VISIBLE);
            listViewToday.setAdapter(adapter);
        }
    }

    public void listTomorrow() {

        Date date = Calendar.getInstance().getTime();
        long startDay = GetMiliDate.getStartOfTomorrow(date);
        long endDay = GetMiliDate.getEndOfTomorrow(date);

        data = getModel(startDay, endDay);
        adapter = new ListItemAdapter(this, data);
        if (adapter.getCount() == 0) {
            emptyTomorrow.setVisibility(View.GONE);
        } else {
            emptyTomorrow.setVisibility(View.VISIBLE);
            listViewTomorrow.setAdapter(adapter);
        }
    }

    public void listFuture() {

        Date date = Calendar.getInstance().getTime();
        long startDay = GetMiliDate.getStartOfWeek(date);

        data = getModelFuture(startDay);
        adapter = new ListItemAdapter(this, data);
        if (adapter.getCount() == 0) {
            emptyFuture.setVisibility(View.GONE);
        } else {
            emptyFuture.setVisibility(View.VISIBLE);
            listViewFuture.setAdapter(adapter);
        }
    }

    public void listNoDate() {

        emptyNoDate.setVisibility(View.GONE);

    }

    public void initTabs() {

        tabs = (TabHost) findViewById(R.id.tabHost);
        tabs.setup();


        TabHost.TabSpec activeTab = tabs.newTabSpec("Active");
        activeTab.setContent(R.id.tabLayout1);
        activeTab.setIndicator("Активные");
        tabs.addTab(activeTab);

        TabHost.TabSpec passiveTab = tabs.newTabSpec("Passive");
        passiveTab.setContent(R.id.tabLayout2);
        passiveTab.setIndicator("Выполеные");
        tabs.addTab(passiveTab);

        TabHost.TabSpec failedTab = tabs.newTabSpec("Failed");
        failedTab.setContent(R.id.tabLayout3);
        failedTab.setIndicator("Проваленные");
        tabs.addTab(failedTab);

        TabWidget tabWidget = tabs.getTabWidget();
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            final ViewGroup tab = (ViewGroup) tabWidget.getChildAt(i);
            final TextView tabTextView = (TextView) tab.getChildAt(1).findViewById(android.R.id.title);
            tabTextView.setTextSize(11);
            tabTextView.setTypeface(null, Typeface.NORMAL);

            tabs.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_selected);
            tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).setBackgroundResource(R.drawable.tab_unselected);
        }

    }
}