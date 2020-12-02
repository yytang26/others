package util;

import com.sun.deploy.net.URLEncoder;

import java.io.UnsupportedEncodingException;


/**
 * @author:tyy
 * @date:2020/12/1
 */
public class Encodes {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";


    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException();
        }
    }
}
