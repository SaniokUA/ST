package azaza.myapplication.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Model.ListItem;
import azaza.myapplication.R;

/**
 * Created by Alex on 05.06.2015.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

    private List<ListItem> list;
    public  List<ListItem> listNew;
    private Activity context;
    List<ListItem> resListItems;
    private Filter textFilter;

    private List<ListItem> allModelItemsArray;
    private List<ListItem> filteredModelItemsArray;


    public ListItemAdapter(Activity context, List<ListItem> list) {
        super(context, R.layout.list_item, list);
        this.context = context;

        this.allModelItemsArray = new ArrayList<ListItem>();
        allModelItemsArray.addAll(list);

        this.filteredModelItemsArray = new ArrayList<ListItem>();
        filteredModelItemsArray.addAll(allModelItemsArray);
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflator = context.getLayoutInflater();
        View view = inflator.inflate(R.layout.list_item, null);

        String id = String.valueOf(filteredModelItemsArray.get(position).getId());

        ((TextView) view.findViewById(R.id.listId)).setText(id);
        ((TextView) view.findViewById(R.id.listContact)).setText(filteredModelItemsArray.get(position).getContact());
        ((TextView) view.findViewById(R.id.listPhone)).setText(filteredModelItemsArray.get(position).getNumber());
        ((TextView) view.findViewById(R.id.listDate)).setText(filteredModelItemsArray.get(position).getDate());
        ((TextView) view.findViewById(R.id.listText)).setText(filteredModelItemsArray.get(position).getText());
        ((TextView) view.findViewById(R.id.listAlarmSignal)).setText(filteredModelItemsArray.get(position).getAlarmSignal());

        if (filteredModelItemsArray.get(position).getType().equals("0")) {
            ((ImageView) view.findViewById(R.id.listImage)).setBackgroundResource(R.drawable.in);
        } else {
            ((ImageView) view.findViewById(R.id.listImage)).setBackgroundResource(R.drawable.ou);
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        if (textFilter == null){
            textFilter = new PlanetFilter();
        }

        return textFilter;
    }

    private class PlanetFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<ListItem> filteredItems = new ArrayList<ListItem>();

                for(int i = 0, l = allModelItemsArray.size(); i < l; i++)
                {
                    ListItem m = allModelItemsArray.get(i);
                    if(m.getContact().toLowerCase().contains(constraint))
                        filteredItems.add(m);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = allModelItemsArray;
                    result.count = allModelItemsArray.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredModelItemsArray = (ArrayList<ListItem>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = filteredModelItemsArray.size(); i < l; i++)
                add(filteredModelItemsArray.get(i));
            notifyDataSetInvalidated();
        }
    }
}


