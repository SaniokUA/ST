package azaza.myapplication.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import azaza.myapplication.Model.ListItem;
import azaza.myapplication.R;

/**
 * Created by Alex on 05.06.2015.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

private final List<ListItem> list;
private final Activity context;

public ListItemAdapter(Activity context, List<ListItem> list) {
        super(context, R.layout.list_item, list);
        this.context = context;
        this.list = list;
        }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflator = context.getLayoutInflater();
        View view =  inflator.inflate(R.layout.list_item, null);

        ((TextView) view.findViewById(R.id.listPhone)).setText(list.get(position).getNumber());
        ((TextView) view.findViewById(R.id.listDate)).setText(list.get(position).getDate());
        ((TextView) view.findViewById(R.id.listText)).setText(list.get(position).getText());

        if(list.get(position).getType().equals("0")){
            ((ImageView) view.findViewById(R.id.listImage)).setBackgroundResource(R.drawable.in);
        }else{
            ((ImageView) view.findViewById(R.id.listImage)).setBackgroundResource(R.drawable.ou);
        }

        return view;
    }
}
