package azaza.myapplication.Model;

/**
 * Created by Alex on 05.06.2015.
 */
public class ListItem {

    String type;
    String number;
    String text;
    String date;

    public ListItem(String type, String number, String date, String text){

        this.type = type;
        this.number = number;
        this.date = date;
        this.text = text;

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

}
