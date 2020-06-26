package org.jsoup.helper;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.*;
import java.util.zip.*;
import javax.net.ssl.*;
import org.jsoup.*;
import org.jsoup.internal.*;
import org.jsoup.parser.*;

import static org.jsoup.internal.Normalizer.lowerCase;

public class HttpConnection implements Connection {
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String DEFAULT_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";
    private static final String USER_AGENT = "User-Agent";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    public static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
    private static final int HTTP_TEMP_REDIR = 307; // http/1.1 temporary redirect, not in Java's set.
    private static final String DefaultUploadType = "application/octet-stream";

    public static Connection connect(String url) {
        Connection con = new HttpConnection();
        con.url(url);
        return con;
    }

    public static Connection connect(URL url) {
        Connection con = new HttpConnection();
        con.url(url);
        return con;
    }

    public HttpConnection() {
        req = new Request();
        res = new Response();
    }

	private static String encodeUrl(String url) {
        try {
            URL u = new URL(url);
            return encodeUrl(u).toExternalForm();
        } catch (Exception e) {
            return url;
        }
	}

    static URL encodeUrl(URL u) {
        try {
            String urlS = u.toExternalForm();
            urlS = urlS.replace(" ", "%20");
            final URI uri = new URI(urlS);
            return new URL(uri.toASCIIString());
        } catch (URISyntaxException | MalformedURLException e) {
            return u;
        }
    }

    private static String encodeMimeName(String val) {
        if (val == null)
            return null;
        return val.replace("\"", "%22");
    }

    private Connection.Request req;
    private Connection.Response res;

    public Connection url(URL url) {
        req.url(url);
        return this;
    }

    public Connection url(String url) {
		try {
            req.url(new URL(encodeUrl(url)));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + url, e);
        }
        return this;
    }

    public Connection proxy(Proxy proxy) {
        req.proxy(proxy);
        return this;
    }

    public Connection proxy(String host, int port) {
        req.proxy(host, port);
        return this;
    }

    public Connection userAgent(String userAgent) {
		req.header(USER_AGENT, userAgent);
        return this;
    }

    public Connection timeout(int millis) {
        req.timeout(millis);
        return this;
    }

    public Connection maxBodySize(int bytes) {
        req.maxBodySize(bytes);
        return this;
    }

    public Connection followRedirects(boolean followRedirects) {
        req.followRedirects(followRedirects);
        return this;
    }

    public Connection referrer(String referrer) {
		req.header("Referer", referrer);
        return this;
    }

    public Connection method(Method method) {
        req.method(method);
        return this;
    }

    public Connection ignoreHttpErrors(boolean ignoreHttpErrors) {
		req.ignoreHttpErrors(ignoreHttpErrors);
		return this;
	}

    public Connection ignoreContentType(boolean ignoreContentType) {
        req.ignoreContentType(ignoreContentType);
        return this;
    }

	public Connection data(String key, long value) {
		data(key, String.valueOf(value));
		return this;
	}

    public Connection data(String key, String value) {
        req.data(KeyVal.create(key, value));
        return this;
    }

    public Connection sslSocketFactory(SSLSocketFactory sslSocketFactory) {
	    req.sslSocketFactory(sslSocketFactory);
	    return this;
    }

    public Connection data(String key, String filename, InputStream inputStream) {
        req.data(KeyVal.create(key, filename, inputStream));
        return this;
    }

    @Override
    public Connection data(String key, String filename, InputStream inputStream, String contentType) {
        req.data(KeyVal.create(key, filename, inputStream).contentType(contentType));
        return this;
    }

    public Connection data(Map<String, String> data) {
		for (Map.Entry<String, String> entry : data.entrySet()) {
            req.data(KeyVal.create(entry.getKey(), entry.getValue()));
        }
        return this;
    }

	public Connection data(Object... keyvals) {
		for (int i = 0; i < keyvals.length; i += 2) {
            String key =String.valueOf(keyvals[i]);
            String value = String.valueOf(keyvals[i + 1]);
			if (key.equals("Referer")) {
				req.header(key, value);
			} else {
				req.data(KeyVal.create(key, value));
			}
        }
        return this;
    }

    public Connection data(String... keyvals) {
		for (int i = 0; i < keyvals.length; i += 2) {
            String key = keyvals[i];
            String value = keyvals[i + 1];
			req.data(KeyVal.create(key, value));
        }
        return this;
    }

    public Connection data(Collection<Connection.KeyVal> data) {
		for (Connection.KeyVal entry: data) {
            req.data(entry);
        }
        return this;
    }

    public Connection.KeyVal data(String key) {
		for (Connection.KeyVal keyVal : request().data()) {
            if (keyVal.key().equals(key))
                return keyVal;
        }
        return null;
    }

    public Connection requestBody(String body) {
        req.requestBody(body);
        return this;
    }

    public Connection header(String name, String value) {
        req.header(name, value);
        return this;
    }

    public Connection headers(Map<String,String> headers) {
		for (Map.Entry<String,String> entry : headers.entrySet()) {
            req.header(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Connection cookie(String name, String value) {
        req.cookie(name, value);
        return this;
    }

    public Connection cookies(Map<String, String> cookies) {
		for (Map.Entry<String, String> entry : cookies.entrySet()) {
            req.cookie(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Connection.Response execute() throws IOException {
        res = Response.execute(req);
        return res;
    }

    public Connection.Request request() {
        return req;
    }

    public Connection request(Connection.Request request) {
        req = request;
        return this;
    }

    public Connection.Response response() {
        return res;
    }

    public Connection response(Connection.Response response) {
        res = response;
        return this;
    }

    public Connection postDataCharset(String charset) {
        req.postDataCharset(charset);
        return this;
    }

    @SuppressWarnings({"unchecked"})
    private static abstract class Base<T extends Connection.Base> implements Connection.Base<T> {
        URL url;
        Method method;
        Map<String, List<String>> headers;
        Map<String, String> cookies;

        private Base() {
            headers = new LinkedHashMap<>();
            cookies = new LinkedHashMap<>();
        }

        public URL url() {
            return url;
        }

        public T url(URL url) {
			this.url = url;
            return (T) this;
        }

        public Method method() {
            return method;
        }

        public T method(Method method) {
			this.method = method;
            return (T) this;
        }

        public String header(String name) {
			List<String> vals = getHeadersCaseInsensitive(name);
            if (vals.size() > 0) {
                return StringUtil.join(vals, ", ");
            }

            return null;
        }

        @Override
        public T addHeader(String name, String value) {
			value = value == null ? "" : value;
            List<String> values = headers(name);
            if (values.isEmpty()) {
                values = new ArrayList<>();
                headers.put(name, values);
            }
            values.add(fixHeaderEncoding(value));
            return (T) this;
        }

        @Override
        public List<String> headers(String name) {
			return getHeadersCaseInsensitive(name);
        }

        private static String fixHeaderEncoding(String val) {
            try {
                byte[] bytes = val.getBytes("ISO-8859-1");
                if (!looksLikeUtf8(bytes))
                    return val;
                return new String(bytes, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return val;
            }
        }

        private static boolean looksLikeUtf8(byte[] input) {
            int i = 0;
            // BOM:
            if (input.length >= 3 && (input[0] & 0xFF) == 0xEF && (input[1] & 0xFF) == 0xBB & (input[2] & 0xFF) == 0xBF) {
                i = 3;
            }
            int end;
            for (int j = input.length; i < j; ++i) {
                int o = input[i];
                if ((o & 0x80) == 0) {
                    continue; // ASCII
                }
                // UTF-8 leading:
                if ((o & 0xE0) == 0xC0) {
                    end = i + 1;
                } else if ((o & 0xF0) == 0xE0) {
                    end = i + 2;
                } else if ((o & 0xF8) == 0xF0) {
                    end = i + 3;
                } else {
                    return false;
                }
                if (end >= input.length)
                    return false;
                while (i < end) {
                    i++;
                    o = input[i];
                    if ((o & 0xC0) != 0x80) {
                        return false;
                    }
                }
            }
            return true;
        }

        public T header(String name, String value) {
			removeHeader(name);
            addHeader(name, value);
            return (T) this;
        }

        public boolean hasHeader(String name) {
			return !getHeadersCaseInsensitive(name).isEmpty();
        }

        public boolean hasHeaderWithValue(String name, String value) {
			List<String> values = headers(name);
            for (String candidate : values) {
                if (value.equalsIgnoreCase(candidate))
                    return true;
            }
            return false;
        }

        public T removeHeader(String name) {
			Map.Entry<String, List<String>> entry = scanHeaders(name);
            if (entry != null)
                headers.remove(entry.getKey());
            return (T) this;
        }

        public Map<String, String> headers() {
            LinkedHashMap<String, String> map = new LinkedHashMap<>(headers.size());
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String header = entry.getKey();
                List<String> values = entry.getValue();
                if (values.size() > 0)
                    map.put(header, values.get(0));
            }
            return map;
        }

        @Override
        public Map<String, List<String>> multiHeaders() {
            return headers;
        }

        private List<String> getHeadersCaseInsensitive(String name) {
			for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                if (name.equalsIgnoreCase(entry.getKey()))
                    return entry.getValue();
            }

            return Collections.emptyList();
        }

        private Map.Entry<String, List<String>> scanHeaders(String name) {
            String lc = lowerCase(name);
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                if (lowerCase(entry.getKey()).equals(lc))
                    return entry;
            }
            return null;
        }

        public String cookie(String name) {
			return cookies.get(name);
        }

        public T cookie(String name, String value) {
			cookies.put(name, value);
            return (T) this;
        }

        public boolean hasCookie(String name) {
			return cookies.containsKey(name);
        }

        public T removeCookie(String name) {
			cookies.remove(name);
            return (T) this;
        }

        public Map<String, String> cookies() {
            return cookies;
        }
    }

    public static class Request extends HttpConnection.Base<Connection.Request> implements Connection.Request {
        private Proxy proxy;
        private int timeoutMilliseconds;
        private int maxBodySizeBytes;
        private boolean followRedirects;
        private Collection<Connection.KeyVal> data;
        private String body = null;
        private boolean ignoreHttpErrors = false;
        private boolean ignoreContentType = false;
		private String postDataCharset = DataUtil.defaultCharset;
        private SSLSocketFactory sslSocketFactory;

        Request() {
            timeoutMilliseconds = 30000; // 30 seconds
            maxBodySizeBytes = 1024 * 1024 * 2; // 2MB
            followRedirects = true;
            data = new ArrayList<>();
            method = Method.GET;
            addHeader("Accept-Encoding", "gzip");
            addHeader(USER_AGENT, DEFAULT_UA);
		}

        public Proxy proxy() {
            return proxy;
        }

        public Request proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Request proxy(String host, int port) {
            this.proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
            return this;
        }

        public int timeout() {
            return timeoutMilliseconds;
        }

        public Request timeout(int millis) {
			timeoutMilliseconds = millis;
            return this;
        }

        public int maxBodySize() {
            return maxBodySizeBytes;
        }

        public Connection.Request maxBodySize(int bytes) {
			maxBodySizeBytes = bytes;
            return this;
        }

        public boolean followRedirects() {
            return followRedirects;
        }

        public Connection.Request followRedirects(boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }

        public boolean ignoreHttpErrors() {
            return ignoreHttpErrors;
        }

        public SSLSocketFactory sslSocketFactory() {
            return sslSocketFactory;
        }

        public void sslSocketFactory(SSLSocketFactory sslSocketFactory) {
            this.sslSocketFactory = sslSocketFactory;
        }

        public Connection.Request ignoreHttpErrors(boolean ignoreHttpErrors) {
            this.ignoreHttpErrors = ignoreHttpErrors;
            return this;
        }

        public boolean ignoreContentType() {
            return ignoreContentType;
        }

        public Connection.Request ignoreContentType(boolean ignoreContentType) {
            this.ignoreContentType = ignoreContentType;
            return this;
        }

        public Request data(Connection.KeyVal keyval) {
			data.add(keyval);
            return this;
        }

        public Collection<Connection.KeyVal> data() {
            return data;
        }

        public Connection.Request requestBody(String body) {
            this.body = body;
            return this;
        }

        public String requestBody() {
            return body;
        }

        public Connection.Request postDataCharset(String charset) {
			if (!Charset.isSupported(charset)) throw new IllegalCharsetNameException(charset);
            this.postDataCharset = charset;
            return this;
        }

        public String postDataCharset() {
            return postDataCharset;
        }
    }

    public static class Response extends HttpConnection.Base<Connection.Response> implements Connection.Response {
        private static final int MAX_REDIRECTS = 20;
        private static final String LOCATION = "Location";
        private int statusCode;
        private String statusMessage;
        private ByteBuffer byteData;
        private InputStream bodyStream;
        private HttpURLConnection conn;
        private String charset;
        private String contentType;
        private boolean executed = false;
        private boolean inputStreamRead = false;
        private int numRedirects = 0;
        private Connection.Request req;

        Response() {
            super();
        }

        private Response(Response previousResponse) throws IOException {
            super();
            if (previousResponse != null) {
                numRedirects = previousResponse.numRedirects + 1;
                if (numRedirects >= MAX_REDIRECTS)
                    throw new IOException(String.format("Too many redirects occurred trying to load URL %s", previousResponse.url()));
            }
        }

        static Response execute(Connection.Request req) throws IOException {
            return execute(req, null);
        }

        static Response execute(Connection.Request req, Response previousResponse) throws IOException {
			String protocol = req.url().getProtocol();
            if (!protocol.equals("http") && !protocol.equals("https"))
                throw new MalformedURLException("Only http & https protocols supported");
            final boolean methodHasBody = req.method().hasBody();
            final boolean hasRequestBody = req.requestBody() != null;
			String mimeBoundary = null;
            if (req.data().size() > 0 && (!methodHasBody || hasRequestBody))
                serialiseRequestUrl(req);
            else if (methodHasBody)
                mimeBoundary = setOutputContentType(req);
            long startTime = System.nanoTime();
            HttpURLConnection conn = createConnection(req);
            Response res = null;
            try {
                conn.connect();
                if (conn.getDoOutput())
                    writePost(req, conn.getOutputStream(), mimeBoundary);

                int status = conn.getResponseCode();
                res = new Response(previousResponse);
                res.setupFromConnection(conn, previousResponse);
                res.req = req;
                if (res.hasHeader(LOCATION) && req.followRedirects()) {
                    if (status != HTTP_TEMP_REDIR) {
                        req.method(Method.GET);
                        req.data().clear();
                        req.requestBody(null);
                        req.removeHeader(CONTENT_TYPE);
                    }

                    String location = res.header(LOCATION);
                    if (location.startsWith("http:/") && location.charAt(6) != '/')
                        location = location.substring(6);
                    URL redir = StringUtil.resolve(req.url(), location);
                    req.url(encodeUrl(redir));

                    for (Map.Entry<String, String> cookie : res.cookies.entrySet()) {
                        req.cookie(cookie.getKey(), cookie.getValue());
                    }
                    return execute(req, res);
                }
                if ((status < 200 || status >= 400) && !req.ignoreHttpErrors())
					throw new IOException("HTTP error fetching URL" + status + req.url().toString());
                String contentType = res.contentType();
                if (contentType != null && !req.ignoreContentType() && !contentType.startsWith("text/"))
                    throw new RuntimeException("Unhandled content type. Must be text/*, application/xml, or application/*+xml" + contentType + req.url().toString());
                res.charset = DataUtil.getCharsetFromContentType(res.contentType);
                if (conn.getContentLength() != 0 && req.method() != Connection.Method.HEAD) {
                    res.bodyStream = null;
                    res.bodyStream = conn.getErrorStream() != null ? conn.getErrorStream() : conn.getInputStream();
                    if (res.hasHeaderWithValue(CONTENT_ENCODING, "gzip")) {
                        res.bodyStream = new GZIPInputStream(res.bodyStream);
                    } else if (res.hasHeaderWithValue(CONTENT_ENCODING, "deflate")) {
                        res.bodyStream = new InflaterInputStream(res.bodyStream, new Inflater(true));
                    }
                    res.bodyStream = ConstrainableInputStream.wrap(res.bodyStream, DataUtil.bufferSize, req.maxBodySize()).timeout(startTime, req.timeout());
                } else {
                    res.byteData = DataUtil.emptyByteBuffer();
                }
            } catch (IOException e) {
                if (res != null) res.safeClose();
                throw e;
            }
            res.executed = true;
            return res;
        }

        public int statusCode() {
            return statusCode;
        }

        public String statusMessage() {
            return statusMessage;
        }

        public String charset() {
            return charset;
        }

        public Response charset(String charset) {
            this.charset = charset;
            return this;
        }

        public String contentType() {
            return contentType;
        }

        private void prepareByteData() {
			if (byteData == null) {
				try {
                    byteData = DataUtil.readToByteBuffer(bodyStream, req.maxBodySize());
                } catch (Exception e) {

                } finally {
                    inputStreamRead = true;
                    safeClose();
                }
            }
        }

        public String body() {
            prepareByteData();
            String body;
            if (charset == null)
                body = Charset.forName(DataUtil.defaultCharset).decode(byteData).toString();
            else
                body = Charset.forName(charset).decode(byteData).toString();
            ((Buffer)byteData).rewind();
            return body;
        }

        public byte[] bodyAsBytes() {
            prepareByteData();
            return byteData.array();
        }

        @Override
        public Connection.Response bufferUp() {
            prepareByteData();
            return this;
        }

        @Override
        public BufferedInputStream bodyStream() {
			inputStreamRead = true;
            return ConstrainableInputStream.wrap(bodyStream, DataUtil.bufferSize, req.maxBodySize());
        }

        private static HttpURLConnection createConnection(Connection.Request req) throws IOException {
            final HttpURLConnection conn = (HttpURLConnection) (req.proxy() == null ?req.url().openConnection() : req.url().openConnection(req.proxy()));
            conn.setRequestMethod(req.method().name());
            conn.setInstanceFollowRedirects(false);
            conn.setConnectTimeout(req.timeout());
            conn.setReadTimeout(req.timeout() / 2);
            if (req.sslSocketFactory() != null && conn instanceof HttpsURLConnection)
                ((HttpsURLConnection) conn).setSSLSocketFactory(req.sslSocketFactory());
            if (req.method().hasBody())
                conn.setDoOutput(true);
            if (req.cookies().size() > 0)
                conn.addRequestProperty("Cookie", getRequestCookieString(req));
            for (Map.Entry<String, List<String>> header : req.multiHeaders().entrySet()) {
                for (String value : header.getValue()) {
                    conn.addRequestProperty(header.getKey(), value);
                }
            }
            return conn;
        }

        private void safeClose() {
            if (bodyStream != null) {
                try {
                    bodyStream.close();
                } catch (IOException e) {

                } finally {
                    bodyStream = null;
                }
            }
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }

        private void setupFromConnection(HttpURLConnection conn, HttpConnection.Response previousResponse) throws IOException {
            this.conn = conn;
            method = Method.valueOf(conn.getRequestMethod());
            url = conn.getURL();
            statusCode = conn.getResponseCode();
            statusMessage = conn.getResponseMessage();
            contentType = conn.getContentType();

            Map<String, List<String>> resHeaders = createHeaderMap(conn);
            processResponseHeaders(resHeaders);
            if (previousResponse != null) {
                for (Map.Entry<String, String> prevCookie : previousResponse.cookies().entrySet()) {
                    if (!hasCookie(prevCookie.getKey()))
                        cookie(prevCookie.getKey(), prevCookie.getValue());
                }
                previousResponse.safeClose();
            }
        }

        private static LinkedHashMap<String, List<String>> createHeaderMap(HttpURLConnection conn) {

            final LinkedHashMap<String, List<String>> headers = new LinkedHashMap<>();
            int i = 0;
            while (true) {
                final String key = conn.getHeaderFieldKey(i);
                final String val = conn.getHeaderField(i);
                if (key == null && val == null)
                    break;
                i++;
                if (key == null || val == null)
                    continue; // skip http1.1 line
                if (headers.containsKey(key))
                    headers.get(key).add(val);
                else {
                    final ArrayList<String> vals = new ArrayList<>();
                    vals.add(val);
                    headers.put(key, vals);
                }
            }
            return headers;
        }

        void processResponseHeaders(Map<String, List<String>> resHeaders) {
            for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
                String name = entry.getKey();
                if (name == null)
                    continue; // http/1.1 line
                List<String> values = entry.getValue();
                if (name.equalsIgnoreCase("Set-Cookie")) {
                    for (String value : values) {
                        if (value == null)
                            continue;
                        TokenQueue cd = new TokenQueue(value);
                        String cookieName = cd.chompTo("=").trim();
                        String cookieVal = cd.consumeTo(";").trim();
						cookie(cookieName, cookieVal);
                    }
                }
                for (String value : values) {
                    addHeader(name, value);
                }
            }
        }

        private static String setOutputContentType(final Connection.Request req) {
            String bound = null;
            if (req.hasHeader(CONTENT_TYPE)) {
				if (req.header(CONTENT_TYPE).contains(MULTIPART_FORM_DATA) && !req.header(CONTENT_TYPE).contains("boundary")) {
                    bound = DataUtil.mimeBoundary();
                    req.header(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + bound);
                }
            } else if (needsMultipart(req)) {
                bound = DataUtil.mimeBoundary();
                req.header(CONTENT_TYPE, MULTIPART_FORM_DATA + "; boundary=" + bound);
            } else {
                req.header(CONTENT_TYPE, FORM_URL_ENCODED + "; charset=" + req.postDataCharset());
            }
            return bound;
        }

        private static void writePost(final Connection.Request req, final OutputStream outputStream, final String bound) throws IOException {
            final Collection<Connection.KeyVal> data = req.data();
            final BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream, req.postDataCharset()));
            if (bound != null) {
                for (Connection.KeyVal keyVal : data) {
                    w.write("--");
                    w.write(bound);
                    w.write("\r\n");
                    w.write("Content-Disposition: form-data; name=\"");
                    w.write(encodeMimeName(keyVal.key())); // encodes " to %22
                    w.write("\"");
                    if (keyVal.hasInputStream()) {
                        w.write("; filename=\"");
                        w.write(encodeMimeName(keyVal.value()));
                        w.write("\"\r\nContent-Type: ");
                        w.write(keyVal.contentType() != null ? keyVal.contentType() : DefaultUploadType);
                        w.write("\r\n\r\n");
                        w.flush(); // flush
                        DataUtil.crossStreams(keyVal.inputStream(), outputStream);
                        outputStream.flush();
                    } else {
                        w.write("\r\n\r\n");
                        w.write(keyVal.value());
                    }
                    w.write("\r\n");
                }
                w.write("--");
                w.write(bound);
                w.write("--");
            } else if (req.requestBody() != null) {
                w.write(req.requestBody());
            } else {
                boolean first = true;
                for (Connection.KeyVal keyVal : data) {
                    if (!first)
                        w.append('&');
                    else
                        first = false;
                    w.write(URLEncoder.encode(keyVal.key(), req.postDataCharset()));
                    w.write('=');
                    w.write(URLEncoder.encode(keyVal.value(), req.postDataCharset()));
                }
            }
            w.close();
        }

        private static String getRequestCookieString(Connection.Request req) {
            StringBuilder sb = StringUtil.borrowBuilder();
            boolean first = true;
            for (Map.Entry<String, String> cookie : req.cookies().entrySet()) {
                if (!first)
                    sb.append("; ");
                else
                    first = false;
                sb.append(cookie.getKey()).append('=').append(cookie.getValue());
            }
            return StringUtil.releaseBuilder(sb);
        }

        private static void serialiseRequestUrl(Connection.Request req) throws IOException {
            URL in = req.url();
            StringBuilder url = StringUtil.borrowBuilder();
            boolean first = true;
            url
                .append(in.getProtocol())
                .append("://")
                .append(in.getAuthority())
                .append(in.getPath())
                .append("?");
            if (in.getQuery() != null) {
                url.append(in.getQuery());
                first = false;
            }
            for (Connection.KeyVal keyVal : req.data()) {
				if (!first)
                    url.append('&');
                else
                    first = false;
                url
                    .append(URLEncoder.encode(keyVal.key(), DataUtil.defaultCharset))
                    .append('=')
                    .append(URLEncoder.encode(keyVal.value(), DataUtil.defaultCharset));
            }
            req.url(new URL(StringUtil.releaseBuilder(url)));
            req.data().clear();
        }
    }

    private static boolean needsMultipart(Connection.Request req) {
        for (Connection.KeyVal keyVal : req.data()) {
            if (keyVal.hasInputStream())
                return true;
        }
        return false;
    }

    public static class KeyVal implements Connection.KeyVal {
        private String key;
        private String value;
        private InputStream stream;
        private String contentType;

        public static KeyVal create(String key, String value) {
            return new KeyVal().key(key).value(value);
        }

        public static KeyVal create(String key, String filename, InputStream stream) {
            return new KeyVal().key(key).value(filename).inputStream(stream);
        }

        private KeyVal() {}

        public KeyVal key(String key) {
			this.key = key;
            return this;
        }

        public String key() {
            return key;
        }

        public KeyVal value(String value) {
			this.value = value;
            return this;
        }

        public String value() {
            return value;
        }

        public KeyVal inputStream(InputStream inputStream) {
			this.stream = inputStream;
            return this;
        }

        public InputStream inputStream() {
            return stream;
        }

        public boolean hasInputStream() {
            return stream != null;
        }

        @Override
        public Connection.KeyVal contentType(String contentType) {
			this.contentType = contentType;
            return this;
        }

        @Override
        public String contentType() {
            return contentType;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}

