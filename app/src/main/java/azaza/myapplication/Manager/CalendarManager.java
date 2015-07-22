package azaza.myapplication.Manager;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Model.GoogleCalendarsItem;

/**
 * Created by Alex on 16.07.2015.
 */
public class CalendarManager {

    public static List<GoogleCalendarsItem> listCalendars;
    List<String> listName = new ArrayList<>();
    List<String> listId = new ArrayList<>();


    public CalendarManager(List<GoogleCalendarsItem> listCalendars) {

        this.listCalendars = listCalendars;

        for (int i = 0; i < listCalendars.size(); i++) {
            listName.add(listCalendars.get(i).getNameCalendar());
            listId.add(listCalendars.get(i).getIdCalendar());
        }

    }

    public String getCalendarId(String calendarName) {
        String idGoogleCalendar;
        int indexList = 0;
        indexList = listName.indexOf(calendarName);
        if (indexList == -1) {
            indexList = 0;
        }
        idGoogleCalendar = listCalendars.get(indexList).getIdCalendar();
        return idGoogleCalendar;
    }

    public int getCalendarName(String id) {
        String nameGoogleCalendar;
        int indexList = 0;
        indexList = listId.indexOf(id);
        if (indexList == -1) {
            indexList = 0;
        }
        //indexList = listCalendars.get(indexList).getIdCalendar();
        return indexList;
    }

    public ArrayList<String> getAllCalendarsName() {
        ArrayList<String> allCalendarsName = new ArrayList();
        for (int i = 0; i < listCalendars.size(); i++) {
            allCalendarsName.add(i, listCalendars.get(i).getNameCalendar());
        }
        return allCalendarsName;
    }

}
