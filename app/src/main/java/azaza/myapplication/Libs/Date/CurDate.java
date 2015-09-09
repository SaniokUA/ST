package azaza.myapplication.Libs.Date;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Alex on 05.06.2015.
 */
public class CurDate {

   public CurDate(){

    }

    public static String getDate(){
        Date d = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");
        return format1.format(d);
    }

}
