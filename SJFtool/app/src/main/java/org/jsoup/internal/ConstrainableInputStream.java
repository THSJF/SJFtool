package org.jsoup.internal;

import java.io.*;
import java.net.*;
import java.nio.*;

public final class ConstrainableInputStream extends BufferedInputStream {
    private static final int DefaultSize = 1024 * 32;
    private final boolean capped;
    private final int maxSize;
    private long startTime;
    private long timeout = 0;
	private int remaining;
    private boolean interrupted;

    private ConstrainableInputStream(InputStream in, int bufferSize, int maxSize) {
        super(in, bufferSize);
        this.maxSize = maxSize;
        remaining = maxSize;
        capped = maxSize != 0;
        startTime = System.nanoTime();
    }

    public static ConstrainableInputStream wrap(InputStream in, int bufferSize, int maxSize) {
        return in instanceof ConstrainableInputStream
            ? (ConstrainableInputStream) in
            : new ConstrainableInputStream(in, bufferSize, maxSize);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (interrupted || capped && remaining <= 0)
            return -1;
        if (Thread.interrupted()) {
			interrupted = true;
            return -1;
        }
        if (expired())
            throw new SocketTimeoutException("Read timeout");
        if (capped && len > remaining)
            len = remaining; 
        try {
            final int read = super.read(b, off, len);
            remaining -= read;
            return read;
        } catch (SocketTimeoutException e) {
            return 0;
        }
    }

    public ByteBuffer readToByteBuffer(int max) throws IOException {
		final boolean localCapped = max > 0;         final int bufferSize = localCapped && max < DefaultSize ? max : DefaultSize;
        final byte[] readBuffer = new byte[bufferSize];
        final ByteArrayOutputStream outStream = new ByteArrayOutputStream(bufferSize);
        int read;
        int remaining = max;
        while (true) {
            read = read(readBuffer);
            if (read == -1) break;
            if (localCapped) {
				if (read >= remaining) {
                    outStream.write(readBuffer, 0, remaining);
                    break;
                }
                remaining -= read;
            }
            outStream.write(readBuffer, 0, read);
        }
        return ByteBuffer.wrap(outStream.toByteArray());
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        remaining = maxSize - markpos;
    }

    public ConstrainableInputStream timeout(long startTimeNanos, long timeoutMillis) {
        this.startTime = startTimeNanos;
        this.timeout = timeoutMillis * 1000000;
        return this;
    }

    private boolean expired() {
        if (timeout == 0)
            return false;
        final long now = System.nanoTime();
        final long dur = now - startTime;
        return (dur > timeout);
    }
}
