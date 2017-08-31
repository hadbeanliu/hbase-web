package com.lhb.hbase.dao.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class SimpleDaoTest {

	public static void main(String[] args) throws IOException {
		

		File f = new File("/home/hadoop/result/tags.json");
		System.out.println((int) f.length());
		char[] content = new char[(int) f.length()];
		int i=0;
		Map<String, Integer> map=new HashMap<>();
		
		try {
			InputStreamReader read=new InputStreamReader(new FileInputStream(f), "utf-8");
//			read.read(content);
			JsonReader jreader=new JsonReader(read);
			jreader.setLenient(true);
			jreader.beginObject();
			
			while(jreader.hasNext()){

				map.put(jreader.nextName(), jreader.nextInt());
				i+=1;
			}
			jreader.endObject();
//			Map<String, Integer> map=new Gson().fromJson(new String(content), new TypeToken<Map<String, Integer>>() {
//			}.getType());
			
			System.out.println(map.size()+"::"+map.get("饼干"));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("i============"+i);
			e.printStackTrace();
		}finally{
			System.out.println("i============"+i);
		}
		
	
		
		
	}

}
