package co.fastcat.shopid.util;

import org.junit.Test;

import java.io.File;

/**
 * Created by swsong on 2015. 8. 23..
 */
public class MessageDigestUtilsTest {

    @Test
    public void md5Checksum() throws Exception {
        File file = new File(getClass().getResource(getClass().getSimpleName() + ".class").getFile());
        String checksum = MessageDigestUtils.getMD5Checksum(file);
        System.out.println(file.getAbsolutePath());
        System.out.println(checksum);
    }
}
