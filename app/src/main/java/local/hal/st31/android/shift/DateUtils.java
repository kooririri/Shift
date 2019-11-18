package local.hal.st31.android.shift;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static String date2String(Date date,String format){
        SimpleDateFormat sdf =   new SimpleDateFormat( format );
        return sdf.format(date);
    }

    public static Date string2Date(String dateStr,String format){
        SimpleDateFormat sdf =   new SimpleDateFormat( format );
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
