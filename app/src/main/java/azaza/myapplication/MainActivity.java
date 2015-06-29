package azaza.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Adapter.ListItemAdapter;
import azaza.myapplication.DataBase.DB;
import azaza.myapplication.Libs.GetMiliDate;
import azaza.myapplication.Model.ListItem;


public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CM_DELETE_ID = 1;

    ListView listView;
    DB db = new DB(this);
    List<ListItem> data;
    Toolbar toolbar;
    ListItemAdapter adapter;


    GetMiliDate getMiliDate = new GetMiliDate();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);


        db.open();
        data = getModel();
        adapter = new ListItemAdapter(this, data);
        listView = (ListView) findViewById(R.id.listMain);
        listView.setAdapter(adapter);

        LayoutInflater inflator = this.getLayoutInflater();
        View view =  inflator.inflate(R.layout.list_item, null);
        ImageButton itemList;
        itemList = ((ImageButton) view.findViewById(R.id.deleteItemButton));
        itemList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListItemAdapter itemAdapter = (ListItemAdapter) parent.getAdapter();
                final int itemId = itemAdapter.getItem(position).getId();




                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + position);
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db.delRec(itemId);
                        adapter.notifyDataSetChanged();
                        getSupportLoaderManager().getLoader(0).forceLoad();
                    }
                });
                adb.show();
            }

        });

        getSupportLoaderManager().initLoader(0, null, this);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public List<ListItem> getModel() {

        List<ListItem> list = new ArrayList<ListItem>();
        Cursor c = db.getAllData();
        if (c.getCount() == 0) {
            list.add(get(1, "0", "551579", "Test", "13.13.2013", "Some Text", "1231"));
        } else {

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
        super.onStop();
        this.finish();

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
        listView = (ListView) findViewById(R.id.listMain);
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
