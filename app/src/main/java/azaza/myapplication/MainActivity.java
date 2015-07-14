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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Adapter.ListItemAdapter;
import azaza.myapplication.DataBase.DB;
import azaza.myapplication.Libs.GetMiliDate;
import azaza.myapplication.Libs.Swipe.SwipeDismissListViewTouchListener;
import azaza.myapplication.Menu.MaterialMenu;
import azaza.myapplication.Model.ListItem;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, SearchView.OnQueryTextListener {

    ListView listView;
    DB db = new DB(this);

    List<ListItem> data;
    Toolbar toolbar;
    ListItemAdapter adapter;
    TextView emptyList;
    AlertDialog.Builder ad;
    private Drawer.Result drawerResult = null;
    GetMiliDate getMiliDate = new GetMiliDate();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyList = (TextView) findViewById(R.id.idListEmpty);
        emptyList.setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.listMain);
        listView.setTextFilterEnabled(true);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerResult = MaterialMenu.createCommonDrawer(this, toolbar);

        db.open();
        data = getModel();
        adapter = new ListItemAdapter(this, data);
        listView.setAdapter(adapter);

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    int itemId = adapter.getItem(position).getId();
                                    db.delRec(itemId);
                                    adapter.remove(adapter.getItem(position));
                                    adapter = new ListItemAdapter(MainActivity.this, getModel());
                                    listView.setAdapter(adapter);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());

        ad = new AlertDialog.Builder(this);
        ad.setTitle("Delete all items");  // заголовок
        ad.setIcon(R.drawable.ic_delete_dark);
        ad.setMessage("Do you want delete all items?"); // сообщение
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                db.delAllRec();
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

        List<ListItem> list = new ArrayList<ListItem>();
        Cursor c = db.getAllData();
        if (c.getCount() == 0) {
            listView.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
            list.add(get(1, "0", "551579", "Test", "13.13.2013", "Some Text", "1231"));
        } else {
            listView.setVisibility(View.VISIBLE);
            emptyList.setVisibility(View.GONE);
            if (!c.moveToFirst()) {
            } else {
                do {
                    list.add(get(c.getInt(c.getColumnIndex("_id")), c.getString(c.getColumnIndex("type")), c.getString(c.getColumnIndex("contact")), c.getString(c.getColumnIndex("number")),
                            c.getString(c.getColumnIndex("date")), c.getString(c.getColumnIndex("txt")), getMiliDate.millisToDate(c.getLong(c.getColumnIndex("alarmDate")))));
                } while (c.moveToNext());
            }
        }
        return list;
    }

    public ListItem get(int id, String type, String contact, String phone, String data, String text, String alarmSignal) {
        return new ListItem(id, type, contact, phone, data, text, alarmSignal);
    }

    @Override
    protected void onStop() {
        db.close();
        this.finish();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new MyCursorLoader(this, db);
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
        DB db;
        public MyCursorLoader(Context context, DB db) {
            super(context);
            this.db = db;
        }
        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData();
            return cursor;
        }
    }
}
