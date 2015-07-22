package azaza.myapplication.Libs.Google;

import android.app.Activity;
import android.content.Context;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import azaza.myapplication.AccountActivity;
import azaza.myapplication.GlobalData.ApplicationData;
import azaza.myapplication.GlobalData.UserData;
import azaza.myapplication.Libs.Image.ConvertImageBase64;
import azaza.myapplication.Model.GoogleCalendarsItem;
import azaza.myapplication.Settings.EditSettings;
import azaza.myapplication.Settings.LoadSettings;

import static com.google.android.gms.plus.Plus.PeopleApi;

/**
 * Created by Alex on 19.05.2015.
 */

public class Google extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 100;
    public GoogleApiClient mGoogleApiClient;
    private ConnectionResult mConnectionResult;
    private boolean mIntentInProgress;
    AccountActivity accountActivity = new AccountActivity();

    UserData userData = new UserData();

    Activity activity = ApplicationData.getActivityId();


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
//            userData.setUserID(currentPerson.getId());
//            userData.Sex = String.valueOf(currentPerson.getGender());
//            userData.Location = currentPerson.getCurrentLocation();
//            userData.Age = currentPerson.getBirthday();
            userData.setEmail(Plus.AccountApi.getAccountName(mGoogleApiClient));
//          userData.Language = currentPerson.getLanguage();
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
        getUserAccounts(accGoogle);
        //userData.setUserConnected(true);



        credential = GoogleAccountCredential.usingOAuth2(
                activity.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff())
                .setSelectedAccountName(accGoogle);

        mService = new com.google.api.services.calendar.Calendar.Builder(
                transport, jsonFactory, credential)
                .setApplicationName("Google Calendar API Android Quickstart")
                .build();

        new GetCalendars().execute();
    }

    /**
     * Sign-out from google
     */
    public void signOutFromGplus() {
        if (mGoogleApiClient==null) {
            accountActivity.logoutUser(activity);
        } else {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
           // mGoogleApiClient.disconnect();
            accountActivity.logoutUser(activity);
        }
    }

    /**
     * Revoking access from google
     */
    public void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            mGoogleApiClient.connect();
                        }

                    });
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
            InputStream in;
            try {
                in = new java.net.URL(urldisplay).openStream();
                mIcon = BitmapFactory.decodeStream(in);
                userData.setUserPhotoDrawble(mIcon);
                ConvertImageBase64.saveImageInBase64(new java.net.URL(urldisplay).openStream(), activity);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            accountActivity.loginUser(activity);
            super.onPostExecute(result);

        }
    }

    private class GetCalendars extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                calendars = mService.calendarList().list().set("showHidden", true).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            int calendarsSize = calendars.size();
            for (int i = 0; i < calendarsSize; i++) {
                listCalendar.add(get(calendars.getItems().get(i).getId(), calendars.getItems().get(i).getSummary()));
            }
            LoadSettings.CALENDAR_LIST = listCalendar;
            SharedPreferences settings = activity.getSharedPreferences("CallManager", Context.MODE_PRIVATE);
            try {
                EditSettings.saveUserCalendars(settings, listCalendar);
                EditSettings.loadUserCalendars(settings);
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.onPostExecute(aVoid);
        }

        public GoogleCalendarsItem get(String id, String summary) {
            return new GoogleCalendarsItem(id, summary);
        }
    }

}
