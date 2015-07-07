package azaza.myapplication.Reciver;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import azaza.myapplication.GlobalData.PhoneData;
import azaza.myapplication.Libs.Contacts.GetContactInfo;
import azaza.myapplication.Model.ContactItem;

/**
 * Created by Alex on 05.06.2015.
 */
public class CallReceiver extends PhonecallReceiver {

    GetContactInfo getContactInfo = new GetContactInfo();
    List<ContactItem> listContactItem = new ArrayList();


    @Override
    protected void onIncomingCallStarted(Context ctx, String number, String start) {

    }

    @Override
    protected void onOutgoingCallStarted(Context ctx, String number, String start) {
    }

    @Override
    protected void onIncomingCallEnded(Context ctx, String number, String start, String end) {

        listContactItem = getContactInfo.getContactData(ctx, number);

        PhoneData.CONTACT = listContactItem.get(0).getContactName();
        PhoneData.PHONE = listContactItem.get(0).getContactNubmer();
        PhoneData.IMAGE = listContactItem.get(0).getContactImage();

        PhoneData.DATE=end.toString();
        PhoneData.myTYPE = "0";

        Intent i = new Intent();
        i.setClassName("azaza.myapplication", "azaza.myapplication.CallActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);

    }

    @Override
    protected void onOutgoingCallEnded(Context ctx, String number, String start, String end) {

        listContactItem = getContactInfo.getContactData(ctx, number);

        PhoneData.CONTACT = listContactItem.get(0).getContactName();
        PhoneData.PHONE = listContactItem.get(0).getContactNubmer();
        PhoneData.IMAGE = listContactItem.get(0).getContactImage();
        PhoneData.DATE=end.toString();
        PhoneData.myTYPE = "1";


        Intent i = new Intent();
        i.setClassName("azaza.myapplication", "azaza.myapplication.CallActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);



    }

    @Override
    protected void onMissedCall(Context ctx, String number, String start) {
    }




}