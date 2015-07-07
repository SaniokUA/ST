package azaza.myapplication.Libs.Contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

import java.io.InputStream;

import azaza.myapplication.R;

/**
 * Created by Alex on 07.07.2015.
 */
public class GetContactPhoto {

    public static Bitmap getContactPhoto(Context ctx, String phoneNumber) {
        Uri phoneUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Uri photoUri = null;
        ContentResolver cr = ctx.getContentResolver();
        Cursor contact = cr.query(phoneUri,
                new String[] { ContactsContract.Contacts._ID }, null, null, null);

        if (contact.moveToFirst()) {
            long userId = contact.getLong(contact.getColumnIndex(ContactsContract.Contacts._ID));
            photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, userId);

        }
        else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_account);
            return defaultPhoto;
        }
        if (photoUri != null) {
            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(
                    cr, photoUri);
            if (input != null) {
                return BitmapFactory.decodeStream(input);
            }
        } else {
            Bitmap defaultPhoto = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_account);
            return defaultPhoto;
        }
        Bitmap defaultPhoto = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_account);
        return defaultPhoto;
    }
}
