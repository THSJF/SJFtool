package org.jsoup.internal;

import java.net.*;
import java.util.*;


public final class StringUtil {

    static final String[] padding = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ",
        "         ", "          ", "           ", "            ", "             ", "              ", "               ",
        "                ", "                 ", "                  ", "                   ", "                    "};

    public static String join(Collection strings, String sep) {
        return join(strings.iterator(), sep);
    }

    public static String join(Iterator strings, String sep) {
        if (!strings.hasNext())
            return "";

        String start = strings.next().toString();
        if (!strings.hasNext())             return start;

        StringBuilder sb = StringUtil.borrowBuilder().append(start);
        while (strings.hasNext()) {
            sb.append(sep);
            sb.append(strings.next());
        }
        return StringUtil.releaseBuilder(sb);
    }

    public static boolean isWhitespace(int c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\f' || c == '\r';
    }

    public static URL resolve(URL base, String relUrl) throws MalformedURLException {
		if (relUrl.startsWith("?"))
            relUrl = base.getPath() + relUrl;
		if (relUrl.indexOf('.') == 0 && base.getFile().indexOf('/') != 0) {
            base = new URL(base.getProtocol(), base.getHost(), base.getPort(), "/" + base.getFile());
        }
        return new URL(base, relUrl);
    }

    private static final Stack<StringBuilder> builders = new Stack<>();

    public static StringBuilder borrowBuilder() {
        synchronized (builders) {
            return builders.empty() ?
                new StringBuilder(MaxCachedBuilderSize) :
                builders.pop();
        }
    }

    public static String releaseBuilder(StringBuilder sb) {
        String string = sb.toString();

        if (sb.length() > MaxCachedBuilderSize)
            sb = new StringBuilder(MaxCachedBuilderSize);         else
            sb.delete(0, sb.length()); 
        synchronized (builders) {
            builders.push(sb);

            while (builders.size() > MaxIdleBuilders) {
                builders.pop();
            }
        }
        return string;
    }

    private static final int MaxCachedBuilderSize = 8 * 1024;
    private static final int MaxIdleBuilders = 8;
}
