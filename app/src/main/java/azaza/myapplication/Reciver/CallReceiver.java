package azaza.myapplication.Reciver;

import android.content.Context;
import android.content.Intent;

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

        Intent i = new Intent();
        i.setClassName("azaza.myapplication", "azaza.myapplication.CallActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(i);
    }

    @Override
    protected void onMissedCall(Context ctx, String number, String start) {
    }

}