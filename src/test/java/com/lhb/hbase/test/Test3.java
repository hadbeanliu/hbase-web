package com.lhb.hbase.test;

import com.rongji.cms.webservice.client.json.ArticleClient;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticleFilter;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsListResult;
import com.rongji.cms.webservice.domain.WsPage;

public class Test3 {

	public static void main(String[] args) {
		
		CmsClientFactory fac = new CmsClientFactory("http://cms.work.net", "00000002", "A7dCV37Ip96%86");
		ArticleClient client = fac.getArticleClient();
		WsArticleFilter filter=new WsArticleFilter();
		filter.setCaIds("2016120519000007");
		WsPage page=new WsPage();
		page.setPageSize(10);
		page.setCurrPage(0);
		try {
			WsListResult<ArticleVo> ar=client.findArticleVos(filter, page);
			ar.getList().forEach(x->System.out.println(x.get("pubDate_title")+"--"+x.get("title")));
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

}
