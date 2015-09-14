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

    private Activity context;
    private List<ListItem> list;

    public ListItemAdapter(Activity context, List<ListItem> list) {
        super(context, R.layout.row_bg, list);
        this.context = context;
        this.list = list;

    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflator = context.getLayoutInflater();
        View view = inflator.inflate(R.layout.row_bg, null);

        String id = String.valueOf(list.get(position).getId());
        ((TextView) view.findViewById(R.id.listId)).setText(id);

        ((TextView) view.findViewById(R.id.listCategory)).setText(list.get(position).getCategory());
        ((TextView) view.findViewById(R.id.listText)).setText(list.get(position).getText());

        if (list.get(position).getMarked() == 0) {
            ImageView markImage = ((ImageView) view.findViewById(R.id.listMarkImage));
            markImage.setVisibility(View.GONE);
        }

        return view;
    }
}

