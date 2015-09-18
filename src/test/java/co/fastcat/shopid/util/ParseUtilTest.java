package co.fastcat.shopid.util;

import org.junit.Test;

/**
 * Created by swsong on 2015. 8. 18..
 */
public class ParseUtilTest {

    @Test
    public void testHumanSizeOverMB() {
        for(long l = 1000*1000* 900; l < 2000 * 1000 * 1000; l+= 100 * 1000000) {
            System.out.println(l + " : " + ParseUtil.toHumanSizeOverMB(l));
        }
    }
}
