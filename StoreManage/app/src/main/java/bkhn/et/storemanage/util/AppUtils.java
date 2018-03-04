package bkhn.et.storemanage.util;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import static bkhn.et.storemanage.util.AppConstant.TAGG;

/**
 * Created by PL_itto on 2/12/2018.
 */

public class AppUtils {
    private static final String TAG = TAGG + AppUtils.class.getSimpleName();

    public static <V> boolean isNotNull(V item) {
        return item != null;
    }

    public static boolean isValidProductCode(@NonNull String data) {
        return data.matches("(.*)\\|(.*)");
    }

    public static boolean isValidSession(double start, double end, Date currentTime) {
        double current = getTimeInHours(currentTime);
        Log.w(TAG, "isValidSession: " + start + " " + end + " " + current);
        if (start <= current && end >= current)
            return true;
        return false;
    }

    public static double getTimeInHours(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.HOUR_OF_DAY) + ((double) calendar.get(Calendar.MINUTE)) / 60.0;
    }

    public static String getTimeString(double time) {
        DecimalFormat format = new DecimalFormat("##.##");
        String hour = format.format((int) time);
        String minute = format.format((time - (int) time) * 60);
        return hour + ":" + minute;
    }
    public static String longToTime(long time){
        return new Date(time).toString();
    }
}
