package com.lhb.hbase.test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

import com.google.gson.Gson;
import com.lhb.data.common.HtmlParser;
import com.lhb.data.common.MainWordExtractor;
import com.lhb.data.common.TableUtil;
import com.rongji.cms.webservice.client.json.ArticleClient;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticleFilter;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsCallResult;
import com.rongji.cms.webservice.domain.WsPage;
import com.rongji.cms.webservice.domain.WsResource;

public class Test {
	public static void main(String[] args) {
		
		
		Connection conn;
		try {
			conn = ConnectionFactory.createConnection();
			Table table=conn.getTable(TableName.valueOf("headlines:item_meta_table"));
			CmsClientFactory fac = new CmsClientFactory("http://cms.work.net", "00000002", "A7dCV37Ip96%86");
			ArticleClient ac=fac.getArticleClient();
			Scan scan=new Scan();
			scan.addColumn("meta".getBytes(), "ogSite".getBytes());
			scan.addColumn("f".getBytes(), "lb".getBytes());
			scan.addColumn("p".getBytes(), "t".getBytes());
			scan.setStopRow(TableUtil.getEndKey(1, Calendar.DAY_OF_YEAR).getBytes());
			Filter filter=new SingleColumnValueFilter("meta".getBytes(), "ogSite".getBytes(), CompareOp.EQUAL, "东南网".getBytes());
			scan.setFilter(filter);
			ResultScanner rs=table.getScanner(scan);
			int cnt=0;
			Iterator<Result> it=rs.iterator();
			Map<String, Integer> count=new HashMap<>();
//			String[] ids=new String[500];
			List<Delete> delete=new ArrayList<Delete>();
			
			List<String> ids=new ArrayList<>();
			while(it.hasNext()){
				
				Result r=it.next();
				String v=Bytes.toString(r.getValue("meta".getBytes(), "ogSite".getBytes()));
				String l=Bytes.toString(r.getValue("f".getBytes(), "lb".getBytes()));
				String t=Bytes.toString(r.getValue("p".getBytes(), "t".getBytes()));

//				ids.add(TableUtil.IdReverse(Bytes.toString(r.getRow())));
//				delete.add(new Delete(r.getRow()));
				System.out.println(v+"................"+l+"...."+t);
//				if(ids.size()==50){
//					String[] deletes=new String[500]; 
//					ac.deleteArticles(ids.toArray(deletes));
//					ids.clear();
//					cnt+=50;
//					table.delete(new Delete(r.getRow()));
//					System.out.println("delete nums=="+cnt);
//
//				}
				cnt++;
			}
			
			for(String key:count.keySet())
				System.out.println(key+"..."+count.get(key));
			
			System.out.println("cnt=="+cnt);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private static String getAr(String id) throws Exception{
		
		CmsClientFactory fac=new CmsClientFactory("http://cms.work.net", "00000002", "A7dCV37Ip96%86");
		ArticleClient client = fac.getArticleClient();
		WsArticleFilter filter = new WsArticleFilter();

		filter.setArIds(id);

		WsPage page = new WsPage();

		ArticleVo article = client.findArticleVos(filter, page).getList().get(0);
			System.out.println(article.get("title"));
			String content=article.get("content").toLowerCase();
		return content;
	}

	private static void input() {

		Random random = new Random();
		String[] dy = new String[] { "liuhb@chifan.com", "sport-dota2", "yoyo@zhihu.com", "45123@douyu.com",
				"tec-all" };
		String[] graph = new String[] { "旅游", "看书", "世界", "游戏", "喝茶" };

		byte[] g = "graph".getBytes();
		byte[] bind = "bind".getBytes();
		Configuration conf = HBaseConfiguration.create();
		try {
			HTableInterface table = HConnectionManager.createConnection(conf)
					.getTable("user_behavior_table".getBytes());
			int num = 10000;
			for (int i = 0; i < num; i++) {
				String id = random.nextInt(20) + "_" + System.currentTimeMillis() + "_" + i;
				Put put = new Put(id.getBytes());
				put.add(bind, dy[random.nextInt(5)].getBytes(), Bytes.toBytes(1));
				put.add(g, graph[random.nextInt(5)].getBytes(), Bytes.toBytes(random.nextFloat()));
				table.put(put);
			}
			table.flushCommits();
			;

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private static void createTable() {
		Configuration conf = HBaseConfiguration.create();
		try {
			HBaseAdmin admin = new HBaseAdmin(conf);
			HTableDescriptor desc = new HTableDescriptor("user_behavior_table");
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
