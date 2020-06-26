package com.meng.sjftool.libs;

import java.io.*;
import java.util.zip.*;
import org.jsoup.*;

public class NetWorkUtil {
	
	public static byte[] uncompress(byte[] inputByte) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inputByte.length);
        try {
            Inflater inflater = new Inflater(true);
            inflater.setInput(inputByte);
            byte[] buffer = new byte[4 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] output = outputStream.toByteArray();
        outputStream.close();
        return output;
    }
}
