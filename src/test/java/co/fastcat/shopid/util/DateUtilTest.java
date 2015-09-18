package co.fastcat.shopid.util;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by swsong on 2015. 8. 18..
 */
public class DateUtilTest {

    @Test
    public void testElapsedTime() {
        for(long l = 0 ; l < 48 * 60 * 60 * 1000; l+=1000) {
            String elapsed = DateUtil.getElapsedTimeDisplay(l);
            System.out.println(l + " : " + elapsed);
        }
    }

    @Test
    public void testUTCTime() throws ParseException {
        String str = "2015-08-22T14:42:48.523Z";
        Date date = DateUtil.getUTCDateFormat().parse(str);
        System.out.println(date);
        Date date2 = DateUtil.getUtc2LocalTime(str);
        System.out.println(date2);
    }
}
