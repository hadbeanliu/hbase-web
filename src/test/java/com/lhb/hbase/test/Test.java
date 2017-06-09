package com.lhb.hbase.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.lhb.data.common.TableUtil;

public class Test {
      public static void main(String[] args){
    	  
    	  Configuration conf=HBaseConfiguration.create();
    	  try {
			HTableInterface table=HConnectionManager.createConnection(conf).getTable("headlines:item_meta_table");
			Delete del=new Delete(TableUtil.IdReverse("2017011619000072").getBytes());
			table.delete(del);
			table.flushCommits();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  	System.out.println(Integer.MAX_VALUE);
//    	  	org.springframework.http.converter.StringHttpMessageConverter
//    	  	org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter
    	  System.out.println(TableUtil.IdReverse("982988380999966"));
    	  
//    	  Calendar calendar=Calendar.getInstance();
//    	  System.out.println(calendar.get(Calendar.YEAR));
//    	  calendar.add(Calendar.YEAR, -1);
//    	  System.out.println(calendar.get(Calendar.YEAR));
//    	  SimpleDateFormat format=new SimpleDateFormat("yyyyMMddhhmmssSSS");
//    	  System.out.println(format.format(new Date()));
//    	  List<Put> puts=new ArrayList<>();
//    	  for(int i=0;i<50;i++){
//    		  long time=System.currentTimeMillis();
//    		  
//    		  String id=String.valueOf((10000000000000l-time));
//    		  Put p=new Put(id.getBytes());
//    		  p.add("f".getBytes(), "bid".getBytes(),String.valueOf(time).getBytes());
//    		  puts.add(p);
//    		  try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				
//				e.printStackTrace();
//			}
//    	  }
//    	Configuration conf=HBaseConfiguration.create();
//	    try {
//			HTableInterface table =HConnectionManager.createConnection(conf).getTable("test".getBytes());
////			table.put(puts);
//			Scan scan=new Scan();
//			ResultScanner scanr=table.getScanner(scan);
//			Result r=null;
//			while((r=scanr.next())!=null){
//				System.out.println(new String(r.getRow()));
//			}
//			table.flushCommits();
//			table.close();
//			
//	    } catch (IOException e) {
//			e.printStackTrace();
//		}
    	  
      }
      
      private static void input(){

    	  Random random=new Random();
    	  String[] dy=new String[]{"liuhb@chifan.com","sport-dota2","yoyo@zhihu.com","45123@douyu.com","tec-all"};
    	  String[] graph=new String[]{"旅游","看书","世界","游戏","喝茶"};
    	  
    	  byte[] g="graph".getBytes();
    	  byte[] bind="bind".getBytes();
    	  Configuration conf=HBaseConfiguration.create();
    	  try {
			HTableInterface table=HConnectionManager.createConnection(conf).getTable("user_behavior_table".getBytes());
			int num=10000;
			for(int i=0;i<num;i++){
				String id=random.nextInt(20)+"_"+System.currentTimeMillis()+"_"+i;
				Put put=new Put(id.getBytes());
				put.add(bind, dy[random.nextInt(5)].getBytes(), Bytes.toBytes(1));
				put.add(g,graph[random.nextInt(5)].getBytes(),Bytes.toBytes(random.nextFloat()));
				table.put(put);
			}
			table.flushCommits();;
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    	  
    	  
    	  
    	  
      
      }
      private static void createTable(){
    	  Configuration conf=HBaseConfiguration.create();
    	  try {
  			HBaseAdmin admin=new HBaseAdmin(conf);
  			HTableDescriptor desc=new HTableDescriptor("user_behavior_table");
  			desc.addFamily(new HColumnDescriptor("behavior".getBytes()));
  			desc.addFamily(new HColumnDescriptor("info".getBytes()));
  			desc.addFamily(new HColumnDescriptor("recommend".getBytes()));
  			desc.addFamily(new HColumnDescriptor("history".getBytes()));
  			desc.addFamily(new HColumnDescriptor("graph".getBytes()));
  			desc.addFamily(new HColumnDescriptor("bind".getBytes()));
  			admin.createTable(desc);
  			
  			admin.close();
  			
  		} catch (IOException e) {
  			
  			e.printStackTrace();
  		}
      }
}
