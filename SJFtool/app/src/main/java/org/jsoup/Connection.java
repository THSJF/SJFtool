package org.jsoup;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.*;

public interface Connection {

    enum Method {
        GET(false), POST(true), PUT(true), DELETE(false), PATCH(true), HEAD(false), OPTIONS(false), TRACE(false);

        private final boolean hasBody;

        Method(boolean hasBody) {
            this.hasBody = hasBody;
        }

        public final boolean hasBody() {
            return hasBody;
        }
    }

    Connection url(URL url);
    Connection url(String url);
    Connection proxy(Proxy proxy);
	Connection proxy(String host, int port);
    Connection userAgent(String userAgent); 
    Connection timeout(int millis);
    Connection maxBodySize(int bytes);
    Connection referrer(String referrer);
    Connection followRedirects(boolean followRedirects);
    Connection method(Method method);
    Connection ignoreHttpErrors(boolean ignoreHttpErrors);
    Connection ignoreContentType(boolean ignoreContentType);
    Connection sslSocketFactory(SSLSocketFactory sslSocketFactory);
    Connection data(String key, long value);
    Connection data(String key, String value);
    Connection data(String key, String filename, InputStream inputStream);
	Connection data(String key, String filename, InputStream inputStream, String contentType); 
    Connection data(Collection<KeyVal> data);
    Connection data(Map<String, String> data);
    Connection data(String... keyvals);
	Connection data(Object... objs);
    KeyVal data(String key);
    Connection requestBody(String body);
    Connection header(String name, String value);
    Connection headers(Map<String,String> headers);
    Connection cookie(String name, String value);
    Connection cookies(Map<String, String> cookies);
    Connection postDataCharset(String charset);
    Response execute() throws IOException;
    Request request();
	Connection request(Request request);
    Response response();
    Connection response(Response response);

    interface Base<T extends Base> {
        URL url(); 
        T url(URL url);
        Method method();
        T method(Method method);
        String header(String name);
        List<String> headers(String name);
        T header(String name, String value);
        T addHeader(String name, String value);
        boolean hasHeader(String name);
        boolean hasHeaderWithValue(String name, String value);
        T removeHeader(String name);
        Map<String, String> headers();
        Map<String, List<String>> multiHeaders();
        String cookie(String name);
        T cookie(String name, String value);
        boolean hasCookie(String name);
        T removeCookie(String name);
        Map<String, String> cookies();
    }

    interface Request extends Base<Request> {  
        Proxy proxy();
        Request proxy(Proxy proxy); 
        Request proxy(String host, int port);  
        int timeout();
        Request timeout(int millis);
        int maxBodySize();
        Request maxBodySize(int bytes);
        boolean followRedirects();
        Request followRedirects(boolean followRedirects);
        boolean ignoreHttpErrors();
        Request ignoreHttpErrors(boolean ignoreHttpErrors);
        boolean ignoreContentType();
        Request ignoreContentType(boolean ignoreContentType);
        SSLSocketFactory sslSocketFactory();
        void sslSocketFactory(SSLSocketFactory sslSocketFactory);
        Request data(KeyVal keyval); 
        Collection<KeyVal> data();
        Request requestBody(String body);
        String requestBody();
        Request postDataCharset(String charset); 
        String postDataCharset();
    }

    interface Response extends Base<Response> {
        int statusCode(); 
        String statusMessage();
        String charset();
        Response charset(String charset);
        String contentType();
        String body();
        byte[] bodyAsBytes();
        Response bufferUp();
        BufferedInputStream bodyStream();
    }

    interface KeyVal {
        KeyVal key(String key);
        String key();
        KeyVal value(String value);
        String value();
        KeyVal inputStream(InputStream inputStream);
        InputStream inputStream();
        boolean hasInputStream(); 
        KeyVal contentType(String contentType);
        String contentType();
    }
}
