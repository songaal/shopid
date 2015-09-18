package co.fastcat.shopid.util;

/**
 * Created by swsong on 2015. 8. 18..
 */
public class ParseUtil {

    public static Long parseLong(Object s) {
        return parseLong(s, null);
    }
    public static Long parseLong(Object s, Long defaultValue) {
        try {
            return new Long(Long.parseLong((String) s));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Float parseFloat(Object s) {
        return parseFloat(s, null);
    }
    public static Float parseFloat(Object s, Float defaultValue) {
        try {
            return new Float(Float.parseFloat((String) s));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Double parseDouble(Object s) {
        return parseDouble(s, null);
    }
    public static Double parseDouble(Object s, Double defaultValue) {
        try {
            return new Double(Double.parseDouble((String) s));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Integer parseInt(Object s) {
        return parseInt(s, null);
    }
    public static Integer parseInt(Object s, Integer defaultValue) {
        try {
            return new Integer(Integer.parseInt((String) s));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static Character parseChar(String s) {
        if(s != null && s.length() > 0) {
            return s.charAt(0);
        }
        return null;
    }

    public static String toHumanSize(Long fileLength) {
        if(fileLength == null) {
            return null;
        }
        if(fileLength < SizeUnit.KB) {
            return fileLength + "B";
        } else if(fileLength < SizeUnit.MB) {
            return String.format("%.1fKB", fileLength / SizeUnit.K);
        } else if(fileLength < SizeUnit.GB) {
            return String.format("%.1fMB", fileLength / SizeUnit.K / SizeUnit.K);
        } else if(fileLength >= SizeUnit.GB) {
            return String.format("%.1fGB", fileLength / SizeUnit.K / SizeUnit.K / SizeUnit.K);
        }
        return fileLength + "B";
    }

    public static String toHumanSizeOverMB(Long fileLength) {
        if(fileLength == null) {
            return null;
        }
        if(fileLength < SizeUnit.GB) {
            return String.format("%.0fMB", fileLength / 1000.0 / 1000.0);
        } else if(fileLength >= SizeUnit.GB) {
            return String.format("%.1fGB", fileLength / 1000.0 / 1000.0 / 1000.0);
        }
        return null;
    }
}
