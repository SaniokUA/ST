package azaza.myapplication.Model;

import android.net.Uri;

/**
 * Created by Alex on 07.07.2015.
 */
public class ContactItem {

    String contactName;
    String contactNubmer;
    Uri contactImage;

    public ContactItem(String contactName, String contactNubmer, Uri contactImage){
        this.contactName = contactName;
        this.contactNubmer = contactNubmer;
        this.contactImage = contactImage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNubmer() {
        return contactNubmer;
    }

    public void setContactNubmer(String contactNubmer) {
        this.contactNubmer = contactNubmer;
    }

    public Uri getContactImage() {
        return contactImage;
    }

    public void setContactImage(Uri contactImage) {
        this.contactImage = contactImage;
    }

}
