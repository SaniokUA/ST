package azaza.myapplication.Model;

public class Note {

    public static final String ID = "id";
    public static final String CONTACT = "contact";
    public static final String DATE = "time";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";

    private String name;



    private String contact;
    private String text;
    private long date;
    private boolean complite;
    private boolean alarm;
    private int id;
    private String image;

    public Note(int id, String name, String contact, String text, long date, boolean complite, boolean alarm) {
        this.id = id;
        this.setName(name);
        this.setContact(contact);
        this.setText(text);
        this.setDate(date);
        this.setComplite(complite);
        this.setAlarm(alarm);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isComplite() {
        return complite;
    }

    public void setComplite(boolean complite) {
        this.complite = complite;
    }

    public int getId() { return id; }

    public boolean isAlarm() { return alarm; }

    public void setAlarm(boolean alarm) { this.alarm = alarm; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
