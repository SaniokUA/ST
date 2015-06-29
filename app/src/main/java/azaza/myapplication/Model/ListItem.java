package azaza.myapplication.Model;

/**
 * Created by Alex on 05.06.2015.
 */
public class ListItem {

    int id;
    String type;
    String contact;
    String number;
    String text;
    String date;
    String alarmSignal;

    public ListItem(int id, String type, String contact, String number, String date, String text, String alarmSignal) {

        this.id = id;
        this.type = type;
        this.contact = contact;
        this.number = number;
        this.date = date;
        this.text = text;
        this.alarmSignal = alarmSignal;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlarmSignal() {
        return alarmSignal;
    }

    public void setAlarmSignal(String alarmSignal) {
         this.alarmSignal = alarmSignal;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
