package org.jsoup;

import org.jsoup.helper.*;

public class Jsoup {
	
    private Jsoup() {}

    public static Connection connect(String url) {
        return HttpConnection.connect(url);
    }
}
