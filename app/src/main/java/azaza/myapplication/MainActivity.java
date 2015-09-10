package azaza.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import azaza.library.SwipeActionAdapter;
import azaza.library.SwipeDirections;


import azaza.myapplication.Adapter.ListItemAdapter;
import azaza.myapplication.DataBase.DataBaseProviderModern;
import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Libs.GetMiliDate;
import azaza.myapplication.Libs.Google.LoadProfile;
import azaza.myapplication.Menu.MaterialMenu;
import azaza.myapplication.Model.ListItem;
import azaza.myapplication.Settings.LoadSettings;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener, SwipeActionAdapter.SwipeActionListener {

    ListView listView;
    DataBaseProviderModern db = new DataBaseProviderModern();
    List<ListItem> data;
    Toolbar toolbar;
    ListItemAdapter adapter;
    LinearLayout emptyList;
    AlertDialog.Builder ad;
    public Drawer.Result drawerResult = null;

    SwipeActionAdapter mAdapter;

    DataBaseProviderModern dataBaseProvider = new DataBaseProviderModern();

    public static Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSettings();
        ctx = this;

        getSupportLoaderManager().initLoader(1, null, this);

        emptyList = (LinearLayout) findViewById(R.id.listIsEmpty);
        emptyList.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listMain);
//        listView.setTextFilterEnabled(true);


        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerResult = MaterialMenu.createCommonDrawer(this, toolbar);

        if (UserData.getUserName() == "") {
            LoadProfile.onLoadProfile(this, toolbar);
        }

        data = getModel();
        // ListItemAdapter
        adapter = new ListItemAdapter(this, data);
        // SwipeActionAdapter
        mAdapter = new SwipeActionAdapter(adapter);
        mAdapter.setSwipeActionListener(this)
                .setDimBackgrounds(true)
                .setListView(listView);
        listView.setAdapter(mAdapter);

        mAdapter.addBackground(SwipeDirections.DIRECTION_FAR_LEFT, R.layout.row_bg_left_far)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.row_bg_left)
                .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.row_bg_right_far)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT,R.layout.row_bg_right);

//        listView.setAdapter(mAdapter);
        //mAdapter.setListView(listView);

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

    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_list, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        EditText searchEdit = (EditText) searchView.getRootView().findViewById(R.id.search_src_text);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setTextColor(Color.WHITE);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear) {
            ad.show();
        }

        if (id == R.id.action_exit) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public List<ListItem> getModel() {


        Date date = Calendar.getInstance().getTime();
        long start = GetMiliDate.getStartOfDay(date);
        long end = GetMiliDate.getEndOfDay(date);
        List<ListItem> list = new ArrayList<ListItem>();
        Cursor c = dataBaseProvider.getMainListTaskToDay(this, start / 1000, end / 1000);
        if (c.getCount() == 0) {
            emptyList.setVisibility(View.VISIBLE);
        }
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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this, DataBaseProviderModern.CONTACT_CONTENT_URI, null, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        data = getModel();
        adapter = new ListItemAdapter(this, data);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public boolean hasActions(int position) {
        return true;
    }

    @Override
    public boolean shouldDismiss(int position, int direction) {
        return direction == SwipeDirections.DIRECTION_NORMAL_LEFT;
    }


    @Override
    public void onSwipe(int[] positionList, int[] directionList) {
        for (int i = 0; i < positionList.length; i++) {
            int direction = directionList[i];
            int position = positionList[i];
            String dir = "";

            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                    dir = "Far left";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                    dir = "Far right";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    builder.setTitle("Test Dialog").setMessage("You swiped right").create().show();
                    dir = "Right";
                    break;
            }
            Toast.makeText(
                    this,
                    dir + " swipe Action triggered on " + mAdapter.getItem(position),
                    Toast.LENGTH_SHORT
            ).show();
            mAdapter.notifyDataSetChanged();
        }
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
}
