package com.meng.sjftool.bilibili.javabean;

import android.os.*;
import java.io.*;
import java.util.*;

public class CustomSentense2 implements Serializable {

	private static final long serialVersionUID = 9961L; 

	public ArrayList<String> sentences;

	private static CustomSentence instance = null;

	public static CustomSentence getInstance() {
		return instance;
	}

	private CustomSentense2() {
	}

	public static void load() {
		try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Environment.getExternalStorageDirectory() + "/config.bin"));
            instance = (CustomSentence) ois.readObject();
            ois.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			instance = new CustomSentence();
        }
	}

	public static void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory() + "/config.bin"));
			oos.writeObject(instance);
			oos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
