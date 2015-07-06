package azaza.myapplication.Menu;

/**
 * Created by Alex on 03.07.2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import azaza.myapplication.AccountActivity;
import azaza.myapplication.MainActivity;
import azaza.myapplication.R;
import azaza.myapplication.AboutActivity;
import azaza.myapplication.SettingsActivity;


public class MaterialMenu {
    public static final int ACCOUNTS_LOGOUT_ID = 110;


    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            //
        }

    }


    public static Drawer.Result createCommonDrawer(final ActionBarActivity activity, Toolbar toolbar) {

        final Drawer.Result drawerResult = new Drawer()
                .withActivity(activity)
                .withHeader(R.layout.drawer_header)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(1),
                        new PrimaryDrawerItem().withName("Account").withIcon(FontAwesome.Icon.faw_sign_in).withIdentifier(2),
                        new PrimaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(3),
                        new PrimaryDrawerItem().withName("About").withIcon(FontAwesome.Icon.faw_info).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Exit").withIcon(FontAwesome.Icon.faw_close).withIdentifier(5)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public boolean equals(Object o) {
                        return super.equals(o);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {

                        hideSoftKeyboard(activity);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    // Обработка клика
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                        if (position == 1) {

                            Intent intent = new Intent(activity, MainActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_SINGLE_TOP);
                            activity.startActivity(intent);

                        } else if (position == 2) {
                            Intent intent = new Intent(activity, AccountActivity.class);
                            activity.startActivity(intent);

                        } else if (position == 3) {
                            Intent intent = new Intent(activity, SettingsActivity.class);
                            activity.startActivity(intent);

                        } else if (position == 4) {
                            Intent intent = new Intent(activity, AboutActivity.class);
                            activity.startActivity(intent);

                        } else if (position == 5) {
                            activity.finish();
                        }
                    }
                })
                .build();

        return drawerResult;
    }

}
