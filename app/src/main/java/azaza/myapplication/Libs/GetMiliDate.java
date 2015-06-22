package azaza.myapplication.Libs;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex on 22.06.2015.
 */
public class GetMiliDate {

    public String millisToDate(long millis){

        long yourmilliseconds  = millis*1000;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy HH:mm");
        Date resultdate = new Date(yourmilliseconds);
        return sdf.format(resultdate);

    }
}
