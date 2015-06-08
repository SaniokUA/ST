package azaza.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Adapter.ListItemAdapter;
import azaza.myapplication.DataBase.DB;
import azaza.myapplication.Model.ListItem;


public class MainActivity extends ActionBarActivity  {

    ListView listView;
    DB db = new DB(this);
    List<ListItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db.open();
        data = getModel();
        ListItemAdapter adapter = new ListItemAdapter(this, data);
        listView = (ListView)findViewById(R.id.listMain);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private List<ListItem> getModel() {

        List<ListItem> list = new ArrayList<ListItem>();
        Cursor c = db.getAllData();
        if (c.getCount() == 0) {
            list.add(get("0","551579", "13.13.2013", "Some Text"));
        } else {

            if (!c.moveToFirst()) {
            } else {
                do {
                    list.add(get(c.getString(c.getColumnIndex("type")),c.getString(c.getColumnIndex("number")),
                            c.getString(c.getColumnIndex("date")), c.getString(c.getColumnIndex("txt"))));
                } while (c.moveToNext());
            }
        }
        return list;
    }

    private ListItem get(String type, String phone, String data, String text) {
        return new ListItem(type, phone, data, text);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
