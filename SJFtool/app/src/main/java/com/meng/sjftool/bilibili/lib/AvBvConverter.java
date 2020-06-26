package com.meng.sjftool.bilibili.lib;

import java.math.*;
import java.util.*;

public class AvBvConverter {
	private static AvBvConverter instance = null;

	public static AvBvConverter getInstance() {
		if (instance == null) {
			instance = new AvBvConverter();
		}
		return instance;
	}

	private AvBvConverter() {

	}

	private final int xor = 177451812;//异或用的数，可变
	private final long add = 100618342136696320L;//加减用的数，可变
	private final int[] changeArray = {11,10,3,8,4,6,2,9,5,7};//核心变换数组，BV号的重排顺序，注意到该数组中没有元素0 元素1，意味着BV号的第一、第二位（前缀）不会参与到整个加密、解密的过程中。
	private final String dictionary = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";//核心变换字典，BV号是58进制，所以字典58位，代表从0-57。
	private final String preFix = "BV";//加密过程中用到的前缀符，长度要求与changeArray最小元素值一致。

	/**
	 * 建立字典中字符与字典值之间的对应关系。
	 * @return
	 */
	private Map<Character, Integer> getWorkingMap() {
		char[] cArray = dictionary.toCharArray();
		Map<Character, Integer> map = new HashMap<>(dictionary.length());
		for (int i = 0 ; i < dictionary.length() ; ++i) {
			map.put(cArray[i], i);
		}
		return map;
	}

	/**
	 * 获取整形数组中元素的最大/最小值
	 * @param array 整形数组
	 * @param max true获取最大值， false获取最小值
	 * @return 最大、最小值
	 */

	private int getMin(int[] array) {
		int k = Integer.MAX_VALUE;
		for (int l : array) {
			k = Math.min(k, l);
		}
		return k;
	}
	private int getMax(int[] array) {
		int k =Integer.MIN_VALUE;
		for (int l : array) {
			k = Math.max(k, l);
		}
		return k;
	}

	/**
	 * 解码 BV号转到AV号
	 * @param BV号
	 * @return av号
	 */
	public long decode(String bv) {
		//参数校验，前缀一致，且长度符合changeArray最大元素+1
		if (bv.startsWith(preFix) && bv.length() == (getMax(changeArray) + 1)) {
			BigDecimal b = new BigDecimal(0);
			char[] intputArray = bv.toCharArray();
			for (int i = 0 ; i < changeArray.length ; i ++) {
				//TODO:参数校验，有可能Key取不到Value
				BigDecimal b1 = new BigDecimal(getWorkingMap().get(intputArray[changeArray[i]]));
				BigDecimal b2 = new BigDecimal(dictionary.length()).pow(i);
				b = b.add(b1.multiply(b2));
			}
			long l = b.subtract(new BigDecimal(add)).longValue();
			return l ^ xor;
		} else {
			return -1;
		}
	}

	public String encode(long av) {
		BigDecimal b = new BigDecimal(av ^ xor).add(new BigDecimal(add));
		char[] resultArray = new char[changeArray.length];
		char[] dictionaryArray = dictionary.toCharArray();
		for (int i = 0 ;  i < changeArray.length ;  i++) {
			/*
			 * 在生成的过程中，不需要考虑前缀，所以把重排数组中，前缀占用空间减去。要求：
			 * assert getExtremum(changeArray, false) == preFix.length();
			 */
			resultArray[changeArray[i] - getMin(changeArray)] = 
				dictionaryArray[
				//(b / (进制的i次幂))对进制取模
				b.divide(new BigDecimal(Math.pow(dictionary.length(), i)) , RoundingMode.DOWN).remainder(new BigDecimal(dictionary.length())).intValue()
				];
		}
		//将前缀加回。
		StringBuilder sb = new StringBuilder(preFix);
		for (char i : resultArray) {
			//拼接字符串。
			sb.append(i);
		}
		return sb.toString();
	}
}
