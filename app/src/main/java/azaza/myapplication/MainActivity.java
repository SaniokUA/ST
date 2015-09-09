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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
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


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    SwipeListView listView;
    DataBaseProviderModern db = new DataBaseProviderModern();


    List<ListItem> data;
    Toolbar toolbar;
    ListItemAdapter adapter;
    LinearLayout emptyList;
    AlertDialog.Builder ad;
    public Drawer.Result drawerResult = null;

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
        listView = (SwipeListView) findViewById(R.id.listMain);
        listView.setTextFilterEnabled(true);


        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerResult = MaterialMenu.createCommonDrawer(this, toolbar);

        if (UserData.getUserName() == "") {
            LoadProfile.onLoadProfile(this, toolbar);
        }

        data = getModel();
        adapter = new ListItemAdapter(this, data);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
            }

            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }

            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));
            }

            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    data.remove(position);
                }
                adapter.notifyDataSetChanged();
            }

        });


        listView.setAdapter(adapter);



//        SwipeMenuCreator creator = new SwipeMenuCreator() {
//
//            @Override
//            public void create(SwipeMenu menu) {
//                // create "call" item
//                SwipeMenuItem callItem = new SwipeMenuItem(
//                        getApplicationContext());
//                callItem.setWidth((100));
//                callItem.setIcon(R.drawable.ic_phone);
//                menu.addMenuItem(callItem);
//
//                // create "delete alarm" item
//                SwipeMenuItem deleteAlarmItem = new SwipeMenuItem(
//                        getApplicationContext());
//                deleteAlarmItem.setWidth((100));
//                deleteAlarmItem.setIcon(R.drawable.ic_alarm);
//                menu.addMenuItem(deleteAlarmItem);
//
//                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(
//                        getApplicationContext());
//                deleteItem.setWidth((100));
//                deleteItem.setIcon(R.drawable.ic_delete_dark);
//                menu.addMenuItem(deleteItem);
//            }
//        };


// set creator
//        listView.setMenuCreator(creator);
//
//        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                switch (index) {
//                    case 0:
////                        Intent callIntent = new Intent(Intent.ACTION_CALL);
////                        callIntent.setData(Uri.parse("tel:" + adapter.getItem(position).getNumber()));
////                        startActivity(callIntent);
//                        break;
//
//                    case 1:
//
//                        break;
//
//                    case 2:
//                        // delete
//                        int itemId = adapter.getItem(position).getId();
//                        db.delRec(MainActivity.this, itemId);
//                        adapter.remove(adapter.getItem(position));
//                        adapter = new ListItemAdapter(MainActivity.this, getModel());
//                        listView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                        break;
//                }
//                // false : close the menu; true : not close the menu
//                return false;
//            }
//        });
//
//
//        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);


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


        Date date  = Calendar.getInstance().getTime();
        long start = GetMiliDate.getStartOfDay(date);
        long end = GetMiliDate.getEndOfDay(date);
        List<ListItem> list = new ArrayList<ListItem>();
        Cursor c = dataBaseProvider.getMainListTaskToDay(this, start / 1000, end / 1000);
        if(c.getCount() == 0){
            emptyList.setVisibility(View.VISIBLE);
        }
        if (!c.moveToFirst()) {
        }else{
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
