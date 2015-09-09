package azaza.myapplication.Libs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Alex on 22.06.2015.
 */
public class GetMiliDate{


    public String millisToDate(long millis){

        long yourmilliseconds  = millis*1000;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);

    }

    public long curDate(){
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTimeInMillis();
        return today;
    }

    public static long getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }
    public static long getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }

    public static String millisToDateConvert(long millis){

        long yourmilliseconds  = millis;
        //SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy HH:mm:ss,SSS");
        Date resultdate = new Date(yourmilliseconds);
        return resultdate.toString();

    }
//
//    public long endDay(){
//        //return startDay() + (24 * 60 * 60);
//    }

}
