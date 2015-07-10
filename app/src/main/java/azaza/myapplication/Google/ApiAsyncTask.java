package azaza.myapplication.Google;

import android.os.AsyncTask;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import azaza.myapplication.CallActivity;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private CallActivity mActivity;
    String textNode;
    String contact;
    long date;

    private static final DateFormat DF;

    static {
        DF = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.getDefault());
        DF.setTimeZone(TimeZone.getTimeZone("EEST"));
    }
    /**
     * Constructor.
     *
     * @param activity MainActivity that spawned this task.
     */
    public ApiAsyncTask(CallActivity activity, String textNode, String contact, long date) {
        this.mActivity = activity;
        this.textNode = textNode;
        this.contact = contact;
        this.date = date;
    }

    /**
     * Background task to call Google Calendar API.
     *
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {

            getDataFromApi(contact, textNode, date);

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {

            availabilityException.getConnectionStatusCode();

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    CallActivity.REQUEST_AUTHORIZATION);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     *
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<String> getDataFromApi(String title, String description, long date) throws IOException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        List<String> eventStrings = new ArrayList<String>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
        }

        setEvent(title, description, date);

        return eventStrings;
    }

    public void setEvent(String title, String description, long date) throws IOException {

        Event event = new Event()
                .setSummary(title)
                .setDescription(description);

        DateTime startDateTime = new DateTime(DF.format(new Date(date*1000)));
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone(getTimeZone());
        event.setStart(start);

        DateTime endDateTime = new DateTime(DF.format(new Date(date*1000)));
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone(getTimeZone());
        event.setEnd(end);

        String calendarId = "primary";
        event = mActivity.mService.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

    public String getTimeZone() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();
        String setZone = timeZone.getID();
        return setZone;
    }

}