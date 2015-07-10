package azaza.myapplication;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.makeramen.roundedimageview.RoundedImageView;

import azaza.myapplication.GlobalData.ApplicationData;
import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Libs.Google.Google;

/**
 * Created by Alex on 03.07.2015.
 */
public class AccountActivity extends ActionBarActivity {

    Toolbar toolbar;
    LinearLayout loginLayout;
    TextView email, name;
    RoundedImageView userImage;
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    GoogleAccountCredential credential;
    UserData userData = new UserData();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        loginLayout = (LinearLayout) findViewById(R.id.loginUser);


        toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        final SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);


        ApplicationData.setActivityId(this);
        final Google google = new Google();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settings.getString(PREF_ACCOUNT_NAME,null) == null){
                    showGoogleAccountPicker();
                }

                google.signInWithGplus(settings.getString(PREF_ACCOUNT_NAME, null));
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                findViewById(R.id.progress_wheel).setVisibility(View.VISIBLE);

            }
        });
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google.signOutFromGplus();
            }
        });

        if (userData.isUserConnected()) {
            loginUser(this);
        }else{
            logoutUser(this);
        }

    }


    public void loginUser(Activity activity) {
        activity.findViewById(R.id.progress_wheel).setVisibility(View.GONE);
        activity.findViewById(R.id.loginUser).setVisibility(View.VISIBLE);
        email = (TextView) activity.findViewById(R.id.loginEmail);
        email.setText(userData.getEmail());
        name = (TextView) activity.findViewById(R.id.loginName);
        name.setText(userData.getUserName());
        userImage = (RoundedImageView) activity.findViewById(R.id.userImage);
        userImage.setImageBitmap(userData.getUserPhotoDrawble());
    }

    public void logoutUser(Activity activity) {
        activity.findViewById(R.id.loginUser).setVisibility(View.GONE);
        activity.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
    }


    private void showGoogleAccountPicker() {
        Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null);
        startActivityForResult(googlePicker, REQUEST_ACCOUNT_PICKER);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_ACCOUNT_PICKER && resultCode == RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            SharedPreferences settings =
                    getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(PREF_ACCOUNT_NAME, accountName);
            editor.commit();
        }
    }

}
