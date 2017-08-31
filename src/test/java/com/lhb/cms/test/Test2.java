package com.lhb.cms.test;

import java.util.List;

import com.google.gson.Gson;
import com.lhb.data.common.HtmlParser;
import com.lhb.data.common.MainWordExtractor;
import com.rongji.cms.webservice.client.json.ArticleClient;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticleFilter;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsPage;

public class Test2 {

	public static void main(String[] args) throws Exception {
		MainWordExtractor mainWordExtor=MainWordExtractor.getInstance();
		CmsClientFactory fac = new CmsClientFactory("http://cms.work.net", "00000002", "A7dCV37Ip96%86");
		ArticleClient client = fac.getArticleClient();
		WsArticleFilter filter = new WsArticleFilter();

		filter.setArIds("2017071919001664");

		WsPage page = new WsPage();
	
		ArticleVo article = client.findArticleVos(filter, page).getList().get(0);
//		System.out.println(article.get("createTime"));
		article.keySet().forEach(x->System.out.println(x));
//		System.out.println(new Gson().toJson(article));
		String str = article.get("content");
		System.out.println(str);
//		String str="而是将所有零件移位到";
//		String str="旧的零售的关注点不在于“人”，但是如今第五轮零售的变化恰恰就是由“人”所引发的变化";
		List<String> words = mainWordExtor.simpleTokenize(HtmlParser.delHTMLTag(str));
		int index = 0;
		StringBuffer sb = new StringBuffer();
		char[] originChars = str.toCharArray();
		String lowCaseContent = str.toLowerCase();
		char[] chars = lowCaseContent.toCharArray();


		for (String w : words) {
			String tmp=lowCaseContent.substring(index);
			if (tmp.indexOf(w) == -1)
				continue;

			if (w.equals("1.5"))
				System.out.println("hehre");

			char[] ch = w.toCharArray();
			boolean flag = true;
			if (index >= chars.length) {
				flag = false;
			}
			while (flag) {

				if (ch[0] == chars[index]) {
					flag = false;
					for (int j = 1; j < ch.length; j++) {
						if (ch[j] != chars[index + j]) {
							flag = true;
							break;
						}
					}
					if (flag) {

						sb.append(originChars[index]);
						index++;

					} else {
						sb.append("<em class=\"margin-l\">").append(w).append("</em>");
						index += ch.length;
					}
				} else {

					if (chars[index] == '<' && ((index + 4) < chars.length) && chars[index + 1] == 'i'
							&& chars[index + 2] == 'm' && chars[index + 3] == 'g') {
						System.out.println(chars[index + 2] + chars[index + 3]);
						sb.append("<img");
						index += 3;

						do {
							index++;
							sb.append(originChars[index]);
						} while (chars[index] != '>');
						index++;
					} else {

						sb.append(originChars[index]);
						index++;
					}
				}
				if (index >= chars.length) {
					flag = false;
				}
			}

		}
		if (index < chars.length)
			sb.append(str.substring(index));

		System.out.println(sb.toString());
	}

}
