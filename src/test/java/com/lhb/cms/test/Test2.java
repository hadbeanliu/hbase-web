package com.lhb.cms.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.google.gson.Gson;

public class Test2 {

	private static Map<String, String> map1 = new HashMap<String, String>();
	private static Map<String, String> map2 = new HashMap<String, String>();

	static {

		map1.put("1", "a");
		map1.put("2", "b");
		map2.put("name", "jack");
		map2.put("age", "30");

	}

	public static void clear() {
		
			
			map1.clear();
		
		

	}

	public static void init() {
		synchronized (map1) {
			map1.put("1", ""+System.currentTimeMillis());
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		clear();
		
		map1.put("2", "b");
		}
	}

	public static String get(String key) {
		return map1.get(key);
	}

	public static void main(String[] args) {
		String ss="abcd";
		System.out.println(ss.substring(1));
	}

	public static void do1() {
		String ss="abcd";
		System.out.println(ss.substring(1));
	}

}

class RecMeta {

	private String name;
	private double value;

	public RecMeta(String name) {
		this.name = name;
	}

	public RecMeta(String name, double value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double span) {
		this.value = span;
	}

}
