package com.lhb.hbase.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Put;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lhb.data.common.CataLogManager;
import com.lhb.data.common.DataGainConfiguration;
import com.lhb.data.common.TableUtil;
import com.lhb.data.dao.HbaseDaoImpl;
import com.lhb.data.meta.Hcolumn;
import com.lhb.data.plugin.PluginRepository;
import com.lhb.data.plugin.filter.ScoreFilters;
import com.lhb.data.service.ItemService;
import com.lhb.data.service.UserService;
import com.lhb.data.store.Item;
import com.lhb.data.store.User;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticleFilter;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsListResult;
import com.rongji.cms.webservice.domain.WsPage;

/**
 * 
 * 
 * @author hadoop 2016122819000242 2016122819000252 2016122819000240
 *         2016122819000353 2016122819000354 2016122819000364 2016122819000442
 *         2016122819000478 2016122819000659 2016122819000660
 *
 */
public class HbasetTemplateTest {

	@Autowired
	static
	CataLogManager manager;
	public static void main(String[] args) {
		Gson gson=new Gson();
		manager.init();
		DecimalFormat df = new DecimalFormat("#.00");
		CmsClientFactory cmsFac = new CmsClientFactory("http://cms.work.net", "00000002", "A7dCV37Ip96%86");
		 Configuration conf=DataGainConfiguration.create();
		
		 ApplicationContext app=new ClassPathXmlApplicationContext("spring-hbase.xml");
		 ItemService itemService=app.getBean(ItemService.class);
		int numPage = 1000;
		WsArticleFilter filter = new WsArticleFilter();

		WsPage page = new WsPage();
		page.setPageSize(100);
		// page.setCurrPage(0);
		try {
			for (int i = 1; i < 2000000; i++) {
				page.setCurrPage(i);
				WsListResult<ArticleVo> vos = cmsFac.getArticleClient().findArticleVos(filter, page);
				List<Put> puts=new ArrayList<>();
				for (ArticleVo vo : vos.getList()) {
					Item item=new Item();
					try{
					Map<String, String> meta=Maps.newHashMap();
					item.setCatagory(vo.get("caName"));
					item.setContent(HtmlParser.getTextFromHtml(vo.get("content")));
					item.setFirstFetchTime(vo.get("createTime"));
					item.setFirstPubTime(vo.get("pubDate"));
					item.setTitle(vo.get("title"));
					item.setCatagoryId(vo.get("caId"));
					item.setpCatagoryId(manager.findPId(vo.get("caId")));
					
					Map<String, Float> keywords = Maps.newHashMap();
					
					String[] tags = ((String) vo.get("tags")).split(",| ");
					for (String t : tags) {
						Float value=Float.valueOf(df.format(1.0f / tags.length));
						if (!StringUtils.isEmpty(t))
							keywords.put(t.trim(), value);
					}
					item.setKeyword(keywords);
					item.setMeta(meta);
			
					meta.put("author", vo.get("author_title"));
					meta.put("desc", vo.get("description_title"));
					meta.put("ogUrl", vo.get("redirectUrl"));
					meta.put("img", vo.get("image_title"));
					meta.put("ogSite", vo.get("source"));
//					System.out.println(item.getpCatagoryId());
					itemService.put("headlines", TableUtil.IdReverse(vo.get("id")), item);
					puts.add(toPut(TableUtil.IdReverse(vo.get("id")),item));
					}catch(Exception e){
						e.printStackTrace();
						System.out.println(vo.get("id")+":"+vo.get("title"));
					}

				}
				itemService.puts("headlines:item_meta_table", puts);
				System.out.println("----------------------------------"+i*100);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}

	}
	
	private static Put toPut(String id,Item item){
		Put put = new Put(id.getBytes());

		for (int i = 0; i < item.getFieldsCount(); i++) {
			if (item.isDirty(i)) {
				Hcolumn col = Item.HBASE_MAPPING.get(i);

				if (col == null) {
					throw new RuntimeException("HBase mapping for field ["
							+ item.getClass().getName() + "#"
							+ Item.ALL_FIELDS[i] + "] not found. ");
				}
				HbaseDaoImpl.addPutsAndDeletes(put, null, item.get(i), col);
			}

		}
	return put;
	}

}
