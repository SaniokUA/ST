package azaza.myapplication.Reciver;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import azaza.myapplication.GlobalData.PhoneData;

/**
 * Created by Alex on 05.06.2015.
 */
public class CallReceiver extends PhonecallReceiver {

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, String start) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, String start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, String start, String end) {


        PhoneData.DATE=end.toString();
        PhoneData.PHONE=number.toString();
        PhoneData.myTYPE = "0";
        PhoneData.CONTACT = getContactName(ctx,PhoneData.PHONE );


        Intent i = new Intent();
        i.setClassName("azaza.myapplication", "azaza.myapplication.CallActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, String start, String end) {

        PhoneData.DATE=end.toString();
        PhoneData.PHONE=number.toString();
        PhoneData.myTYPE = "1";
        PhoneData.CONTACT = getContactName(ctx,PhoneData.PHONE );

        Intent i = new Intent();
        i.setClassName("azaza.myapplication", "azaza.myapplication.CallActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);



    }

    @Override
    protected void onMissedCall(Context ctx, String number, String start) {
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }


}