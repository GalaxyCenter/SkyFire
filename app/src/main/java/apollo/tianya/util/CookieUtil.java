package apollo.tianya.util;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.cookie.Cookie;
import cz.msebera.android.httpclient.impl.cookie.BasicClientCookie;

/**
 * Created by Texel on 2016/7/13.
 */
public class CookieUtil {

    public static Cookie[] parse(String source) {
        ArrayList<Cookie> list = null;
        Cookie[] cookies = null;
        Pattern pattern = null;
        Matcher matcher = null;
        String key = null;
        String value = null;

        list = new ArrayList<Cookie>();
        pattern = Pattern.compile("([^=]+)=([^\\;]+);\\s?");
        matcher = pattern.matcher(source);
        while (matcher.find()) {
            key = matcher.group(1);
            value = matcher.group(2);

            list.add(new BasicClientCookie(key, value));
        }

        cookies = (Cookie[]) list.toArray(new Cookie[list.size()]);
        return cookies;
    }

    public static String getCookieValue(Cookie[] cookies, String cookieName, String key) {
        if (cookies == null) {
            return "";
        }

        String cookieStr = null;
        String value = "";
        try {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())) {
                    cookieStr = cookie.getValue();
                    cookieStr = unescape(cookieStr);
                    break;
                }
            }

            if (cookieStr == null || "".equals(cookieStr)) {
                return "";
            }

            String validKey = key + "=";

            StringTokenizer st = new StringTokenizer(cookieStr, "&");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.indexOf(validKey) != -1) {
                    value = token.substring(token.indexOf("=") + 1, token
                            .length());

                    value = unescape(value);
                    break;
                }

            }
        } catch (Exception ex) {
            value = "";
        }

        return value;
    }

    public static String getString(Cookie[] cookies, String name) {
        String value = "";
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (name.equals(cookie.getName())) {
                value = cookie.getValue();
                value = unescape(value);
                break;
            }
        }
        return value;
    }

    public static int getInt32(Cookie[] cookies, String name) {
        int val = 0;
        String temp = null;

        temp = getString(cookies, name);
        val = Integer.parseInt(temp);
        return val;
    }

    public static String getString(Cookie[] cookies, String name, String key) {
        String value = null;

        value = getString(cookies, name);
        value = getString(value, key);
        return value;
    }

    public static int getInt32(Cookie[] cookies, String name, String key) {
        int val = 0;
        String temp = null;

        temp = getString(cookies, name, key);
        val = Integer.parseInt(temp);
        return val;
    }

    public static int getInt32(String value, String key) {
        int val = 0;
        String temp = null;

        temp = getString(value, key);
        val = Integer.parseInt(temp);
        return val;
    }

    public static String getString(String value, String key) {
        String validKey = key + "=";

        // 对cookieStr进行解析
        StringTokenizer st = new StringTokenizer(value, "&");
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.indexOf(validKey) != -1) {
                value = token.substring(token.indexOf("=") + 1, token.length());

                value = unescape(value);
                break;
            }
        }
        return value;
    }

    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j)
                    || Character.isUpperCase(j)) {
                tmp.append(j);
            } else if (j < 256) {
                tmp.append("%");
                if (j < 16) {
                    tmp.append("0");
                }
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(
                            src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }

        return tmp.toString();
    }
}