package azaza.myapplication.Manager;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Model.GoogleCalendarsItem;

/**
 * Created by Alex on 16.07.2015.
 */
public class CalendarManager {

    public static List<GoogleCalendarsItem> listCalendars;

    public CalendarManager(List<GoogleCalendarsItem> listCalendars) {

        this.listCalendars = listCalendars;

    }

    public String getCalendarId(String calendarName){
        String idGoogleCalendar;
        int indexList;
        indexList = listCalendars.indexOf(calendarName);
        idGoogleCalendar = listCalendars.get(indexList).getIdCalendar();
        return idGoogleCalendar;
    }

    public String getCalendarName(String id){
        String nameGoogleCalendar;
        int indexList;
        indexList = listCalendars.indexOf(id);
        nameGoogleCalendar = listCalendars.get(indexList).getIdCalendar();
        return nameGoogleCalendar;
    }

    public ArrayList<String> getAllCalendarsName(){
        ArrayList<String> allCalendarsName = new ArrayList();
        for(int i =0; i<listCalendars.size(); i++){
            allCalendarsName.add(i, listCalendars.get(i).getNameCalendar());
        }
        return allCalendarsName;
    }

}
