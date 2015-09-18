package azaza.myapplication.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import azaza.myapplication.Model.CategoryItem;
import azaza.myapplication.R;

/**
 * Created by Alex on 17.09.2015.
 */
public class CategoryItemAdapter extends ArrayAdapter<CategoryItem> {

    private Activity context;
    private List<CategoryItem> list;

    public CategoryItemAdapter(Activity context, List<CategoryItem> list) {
        super(context, R.layout.spinner_item_menu, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {

        LayoutInflater inflator = context.getLayoutInflater();
        View view = inflator.inflate(R.layout.spinner_item_menu, null);

        TextView categoryName = ((TextView) view.findViewById(R.id.category_name));
        categoryName.setText(list.get(position).getName());

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return initView(position, convertView);
    }

    private View initView(int position, View convertView) {
        if (convertView == null)
            convertView = View.inflate(getContext(),
                    R.layout.spinner_item_menu,
                    null);
        TextView categotyNameText = (TextView) convertView.findViewById(R.id.category_name);
        categotyNameText.setText(list.get(position).getName());
        return convertView;
    }

}
