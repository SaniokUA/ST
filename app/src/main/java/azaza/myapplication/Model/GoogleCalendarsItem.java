package azaza.myapplication.Model;

/**
 * Created by Alex on 16.07.2015.
 */
public class GoogleCalendarsItem {

    String idCalendar;
    String nameCalendar;

    public GoogleCalendarsItem(String idCalendar, String nameCalendar){
        this.idCalendar = idCalendar;
        this.nameCalendar = nameCalendar;
    }

    public String getIdCalendar() {
        return idCalendar;
    }

    public void setIdCalendar(String idCalendar) {
        this.idCalendar = idCalendar;
    }

    public String getNameCalendar() {
        return nameCalendar;
    }

    public void setNameCalendar(String nameCalendar) {
        this.nameCalendar = nameCalendar;
    }


}
