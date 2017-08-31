package service.test;

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

	public static void main(String[] args) {
	
		 ApplicationContext app=new ClassPathXmlApplicationContext("spring-hbase.xml");
		 ItemService itemService=app.getBean(ItemService.class);

		 Item item=itemService.get("headlines", TableUtil.IdReverse("2017082119002933"));
		 
		 System.out.println(new Gson().toJson(item));

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
