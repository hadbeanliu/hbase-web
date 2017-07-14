package com.lhb.hbase.test;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.lhb.data.common.HtmlParser;
import com.lhb.data.common.MainWordExtractor;
import com.lhb.data.common.TableUtil;
import com.rongji.cms.webservice.client.json.ArticleClient;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticleFilter;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsPage;

public class Test {
	public static void main(String[] args) {
		
		double a=323.121321;
		System.out.println(10*a);
		System.out.println(Integer.toBinaryString(1000));
		
		String tmp="";
		try {
			
//			String content=getAr("2017062819002140");
//			System.out.println(content);
			String content="率先拿下房屋拆迁安置协议签约率<strong>100％</strong></p><p>完成房屋拆迁率<strong>100％</strong>咬紧牙关，鼓足干劲，克难攻坚，用智慧和勇气攻破最后的堡垒，确保在六月底前签约率达到 100%。";
			MainWordExtractor extractor = new MainWordExtractor();
//			String content=""
			String newContent=HtmlParser.delHTMLTag(content);
			List<String> words = extractor.simpleTokenize(newContent);
			System.out.println(words);
			char[] chars = content.toCharArray();
//			char[] chars = newContent.toCharArray();
			System.out.println("char length?" + chars.length+":: content length:"+newContent.length());
			int index = 0;
			StringBuffer sb = new StringBuffer();
			System.out.println(newContent);
			System.out.println(content);
			for(String w:words){
				if(content.indexOf(w)==-1)
				System.out.println(w+"::"+content.indexOf(w));
			}
			
			for(String w:words){
				if(content.indexOf(w)==-1)
					continue;
				char[] ch=w.toCharArray();
				boolean flag=true;
				if(index>=chars.length){
					flag=false;
					}
				while(flag){
					
					if(ch[0] == chars[index]){
						flag=false;
						for(int j=0;j<ch.length;j++){
							if(ch[j]!=chars[index+j]){
								flag=true;
								break;
							}
						}
						if(flag){
							sb.append(chars[index]);

						   index++;	
						}else {
							sb.append("<em class=\"margin-l\">").append(w).append("</em>");
							index+=ch.length;
							}
					}else{
						sb.append(chars[index]);
						index++;
					}
					if(index>=chars.length){
						flag=false;
						}
				}
				
				
			}
		if(index<chars.length)
			sb.append(content.substring(index));
			System.out.println(sb.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("---------"+tmp+"---------");
			
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
