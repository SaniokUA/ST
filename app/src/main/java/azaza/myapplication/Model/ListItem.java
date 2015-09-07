package azaza.myapplication.Model;

/**
 * Created by Alex on 05.06.2015.
 */
public class ListItem {

    int id;
    int active;
    String category;
    String text;
    String alarmSignal;
    int marked;

    public ListItem(int id, int active, String category, String text, String alarmSignal, int marked) {

        this.id = id;
        this.active = active;
        this.category = category;
        this.text = text;
        this.alarmSignal = alarmSignal;
        this.marked = marked;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAlarmSignal() {
        return alarmSignal;
    }

    public void setAlarmSignal(String alarmSignal) {
         this.alarmSignal = alarmSignal;
    }
    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMarked() {
        return marked;
    }

    public void setMarked(int marked) {
        this.marked = marked;
    }
}
