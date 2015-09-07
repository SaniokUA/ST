package azaza.myapplication.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Libs.Contacts.GetContactPhoto;
import azaza.myapplication.Model.ListItem;
import azaza.myapplication.R;

/**
 * Created by Alex on 05.06.2015.
 */
public class ListItemAdapter extends ArrayAdapter<ListItem> {

    private Activity context;
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

        if (filteredModelItemsArray.get(position).getContact() != "" && filteredModelItemsArray.get(position).getContact() != null) {
            ((TextView) view.findViewById(R.id.listContact)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.listContact)).setText(filteredModelItemsArray.get(position).getContact());
        } else {
            ((TextView) view.findViewById(R.id.listContact)).setVisibility(View.GONE);
        }

        ((TextView) view.findViewById(R.id.listPhone)).setText(filteredModelItemsArray.get(position).getNumber());
        ((TextView) view.findViewById(R.id.listDate)).setText(filteredModelItemsArray.get(position).getDate());

        String text = filteredModelItemsArray.get(position).getText();
        if (!text.equals("")) {
            ((TextView) view.findViewById(R.id.listText)).setVisibility(View.VISIBLE);
            ((TextView) view.findViewById(R.id.listText)).setText(filteredModelItemsArray.get(position).getText());
        } else {
            ((TextView) view.findViewById(R.id.listText)).setVisibility(View.GONE);
        }

        ((TextView) view.findViewById(R.id.listAlarmSignal)).setText(filteredModelItemsArray.get(position).getAlarmSignal());

        if (filteredModelItemsArray.get(position).getType().equals("0")) {
            ((ImageView) view.findViewById(R.id.listImage)).setBackgroundResource(R.drawable.ic_arrow_top_right);
        } else {
            ((ImageView) view.findViewById(R.id.listImage)).setBackgroundResource(R.drawable.ic_arrow_bottom_left);
        }
        Bitmap imageNew = GetContactPhoto.getContactPhoto(context, filteredModelItemsArray.get(position).getNumber());
        ((ImageView) view.findViewById(R.id.contactImage)).setImageBitmap(imageNew);

        return view;
    }

    @Override
    public Filter getFilter() {
        if (textFilter == null) {
            textFilter = new ContactFilter();
        }

        return textFilter;
    }

    private class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<ListItem> filteredItems = new ArrayList<ListItem>();

                for (int i = 0, l = allModelItemsArray.size(); i < l; i++) {
                    ListItem m = allModelItemsArray.get(i);
                    try {
                        if (m.getText().toLowerCase().contains(constraint)) {
                            filteredItems.add(m);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                result.values = filteredItems;
                result.count = filteredItems.size();
            } else {
                synchronized (this) {
                    result.values = allModelItemsArray;
                    result.count = allModelItemsArray.size();
                }
            }
            return result;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            filteredModelItemsArray = (ArrayList<ListItem>) results.values;
            clear();
           // notifyDataSetChanged();
            for (int i = 0, l = filteredModelItemsArray.size(); i < l; i++)
                add(filteredModelItemsArray.get(i));
            notifyDataSetChanged();
        }
    }
}


