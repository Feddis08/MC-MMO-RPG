package at.feddis08.mmorpg.minecraft.tools;

import java.util.Calendar;

public class Methods {
    public static String getTime(){
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND) + " " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR);
    }
    public static Boolean checkCollision(Integer x1, Integer y1, Integer z1, Integer x2, Integer y2, Integer z2, Integer x3, Integer y3, Integer z3, Integer x4, Integer y4, Integer z4) {
        Boolean result = false;
        if (x2 >= x3 && x1 <= x4) {
            if (y1 <= y4 && y3 <= y2) {
                if (z1 <= z4 && z3 <= z2) {
                    result = true;
                }
            }
        }
        return result;
    }
}
