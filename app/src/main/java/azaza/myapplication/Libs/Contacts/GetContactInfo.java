package azaza.myapplication.Libs.Contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.Model.ContactItem;

/**
 * Created by Alex on 07.07.2015.
 */
public class GetContactInfo {

    List<ContactItem> userData = new ArrayList<ContactItem>();
    Uri contactImage;

    public List<ContactItem> getContactData(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;

        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            contactImage = loadContactPhoto(phoneNumber);
        }

        userData.add(get(contactName, phoneNumber, contactImage));

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return userData;
    }

    public ContactItem get(String contactName, String contactNubmer, Uri contactImage){
        return new ContactItem( contactName, contactNubmer, contactImage);
    }

    public Uri loadContactPhoto(String phoneNumber) {
        Uri uri = Uri.withAppendedPath(Uri.parse(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI), Uri.encode(phoneNumber));
        return uri;
    }



}
