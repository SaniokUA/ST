package azaza.myapplication.Libs.Google;

import android.app.Activity;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.CalendarList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Menu.MaterialMenu;
import azaza.myapplication.Model.GoogleCalendarsItem;
import azaza.myapplication.Settings.LoadSettings;

import static com.google.android.gms.plus.Plus.PeopleApi;

/**
 * Created by Alex on 17.07.2015.
 */
public class GoogleAuth extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static GoogleAuth instance = null;
    Activity activity;
    android.support.v7.widget.Toolbar toolbar;

    private GoogleAuth(Activity activity, android.support.v7.widget.Toolbar toolbar) {
        this.activity = activity;
        this.toolbar = toolbar;

    }

    public static GoogleAuth getInstance(Activity activity, android.support.v7.widget.Toolbar toolbar) {
        if (instance == null) {
            instance = new GoogleAuth(activity, toolbar);
        }
        return instance;
    }

    private static final int RC_SIGN_IN = 100;
    public GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;

    UserData userData = new UserData();


    public static SharedPreferences settings;


    List<GoogleCalendarsItem> listCalendar = new ArrayList();
    CalendarList calendars;
    public com.google.api.services.calendar.Calendar mService;
    GoogleAccountCredential credential;
    final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    private static final String[] SCOPES = {CalendarScopes.CALENDAR};

    public void getUserAccounts(String googleAcc) {
        if (googleAcc != null) {
            mGoogleApiClient = new GoogleApiClient.Builder(activity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(Plus.API)
                    .addScope(Plus.SCOPE_PLUS_LOGIN)
                    .setAccountName(googleAcc)
                    .build();
            mGoogleApiClient.connect();
        }
    }


    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(activity, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();

            }
        }
    }


    /**
     * Error connectionFailed
     * Only to more 2 Accounts Google
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), activity, 0).show();
            return;
        }
        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;
            resolveSignInError();
        }
    }

    /**
     * If user connected to Google
     * Get user info
     */
    @Override
    public void onConnected(Bundle arg0) {

        UserData.userConnected = true;
        getProfileInformation();

    }


    /**
     * Fetching user's information name, email, profile pic
     */
    public void getProfileInformation() {
        try {
            Person currentPerson = PeopleApi.getCurrentPerson(mGoogleApiClient);
            userData.setFirstName(currentPerson.getName().getGivenName());
            userData.setLastName(currentPerson.getName().getFamilyName());
            userData.setUserName(currentPerson.getDisplayName());
            userData.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));
            userData.setUserPhoto(currentPerson.getImage().getUrl());
            new LoadProfileImage().execute(userData.getUserPhoto().substring(0,
                    userData.getUserPhoto().length() - 2) + 80);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    /**
     * Sign-in into google
     */


    public void signInWithGplus(String accGoogle) {

        credential = GoogleAccountCredential.usingOAuth2(
                activity.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(accGoogle);

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

        getUserAccounts(accGoogle);

    }

    /**
     * Sign-out from google
     */
    public void signOutFromGplus() {
        if (mGoogleApiClient == null) {
        } else {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
        }
    }


    public class LoadProfileImage extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
                userData.setUserPhotoDrawble(mIcon);

                calendars = mService.calendarList().list().set("showHidden", true).execute();

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            MaterialMenu.createCommonDrawer(activity, toolbar);


            int calendarsSize = calendars.size();
            for (int i = 0; i < calendarsSize; i++) {
                listCalendar.add(get(calendars.getItems().get(i).getId(), calendars.getItems().get(i).getSummary()));
            }
            LoadSettings.CALENDAR_LIST = listCalendar;

            super.onPostExecute(result);
        }

        public GoogleCalendarsItem get(String id, String summary) {
            return new GoogleCalendarsItem(id, summary);
        }

    }


}
