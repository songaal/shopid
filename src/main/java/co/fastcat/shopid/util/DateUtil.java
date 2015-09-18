package co.fastcat.shopid.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by swsong on 2015. 8. 17..
 */
public class DateUtil {

    public static String getNow() {
        return getDateFormat2().format(new Date());
    }
    public static SimpleDateFormat getDateFormat() {
        return new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");
    }
    public static SimpleDateFormat getDateFormat2() {
        return new SimpleDateFormat("yyyy.MM.dd hh:mm");
    }
    public static SimpleDateFormat getShortDateFormat() {
        return new SimpleDateFormat("yyyy.MM.dd");
    }
    public static SimpleDateFormat getUTCDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    }

    public static String convertDateString(String dateString) {
        if(dateString == null) {
            return "";
        }
        return dateString.substring(0, 10).replaceAll("-", ".");
    }

    public static String getElapsedTimeDisplay(long elapsed) {
        int seconds = (int) (elapsed / 1000) % 60;
        int minutes = (int) ((elapsed / (1000 * 60)) % 60);
        int hours = (int) ((elapsed / (1000 * 60 * 60)) % 24);
        int days = (int) (elapsed / (1000 * 60 * 60 * 24));

        StringBuilder sb = new StringBuilder();
        if(days != 0) {
            return sb.append(days).append("d").append(" ").append(hours).append("h").toString();
        } else if(hours != 0){
            return sb.append(hours).append("h").append(" ").append(minutes).append("m").toString();
        } else if(minutes != 0){
            return sb.append(minutes).append("m").append(" ").append(seconds).append("s").toString();
        } else if(seconds != 0){
            return sb.append(seconds).append("s").toString();
        } else {
            return "-";
        }
    }

    public static String getShortDateString(Date date) {
        return getShortDateFormat().format(date);
    }

    public static Date getUtc2LocalTime(String string) {
        Date gmtTime = null;
        try {
            gmtTime = getUTCDateFormat().parse(string);
            Date newTime = new Date(gmtTime.getTime() + TimeZone.getDefault().getOffset(gmtTime.getTime()));
            return newTime;
        } catch (ParseException ignore) {
        }
        return null;
    }

    public static long getElapsedTime(Date startTime) {
        long diff = new Date().getTime() - startTime.getTime();
        if(diff < 0) {
            diff = 0;
        }
        return diff;
    }

}
