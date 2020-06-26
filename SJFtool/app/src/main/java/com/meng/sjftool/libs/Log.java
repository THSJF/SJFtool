package com.meng.sjftool.libs;

import android.os.*;
import com.meng.sjftool.*;
import java.io.*;
import org.jsoup.*;

public class Log {

	public static void network(Connection.Method method, String link, String result, Object... args) {
		if (!MainActivity.instance.sjfSettings.isUseNetLog()) {
			return;
		}
		File f=new File(Environment.getExternalStorageDirectory() + "/sjfLogNetwork.log");
		try {  
			BufferedWriter writer  = new BufferedWriter(new FileWriter(f, true));  
			writer.write(TimeFormater.getTime());
			writer.write(method == Connection.Method.GET ?" get\n": " post\n");
			writer.write(link);
			if (args != null && args.length > 0) {
				writer.write("\nargs:\n");
				for (int i=0;i < args.length;i += 2) {
					writer.write(String.valueOf(args[i]));
					writer.write("=");
					writer.write(String.valueOf(args[i + 1]));
					writer.write("\n");
				}
			}
			writer.write("\nresult:\n");
			writer.write(result);
			writer.write("\n————————————————————————————————\n");
			writer.flush();  
			writer.close();  
		} catch (Exception e) {  
			MainActivity.instance.showToast(e.toString());
		} 
	}
}
