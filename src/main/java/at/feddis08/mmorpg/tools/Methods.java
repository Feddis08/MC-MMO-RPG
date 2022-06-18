package at.feddis08.mmorpg.tools;

import at.feddis08.mmorpg.database.objects.MailObject;
import at.feddis08.mmorpg.database.objects.PlayerObject;

import java.util.Calendar;

public class Methods {
    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR);
    }
}
