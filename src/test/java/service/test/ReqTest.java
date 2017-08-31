package service.test;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.lhb.data.common.CataLogManager;
import com.lhb.data.common.DateUtils;
import com.lhb.data.common.HtmlParser;
import com.lhb.data.common.HttpClientResource;
import com.lhb.data.common.NumberFormat;
import com.lhb.data.common.TableUtil;
import com.lhb.data.controller.ItemController;
import com.lhb.data.store.Item;
import com.rongji.cms.webservice.client.json.ArticleClient;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticle;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsCallResult;

public class ReqTest {
	private static Logger LOG=Logger.getLogger(ItemController.class);
    private CmsClientFactory cmsFactory;
	
    
    public static void main(String[] args){
    	

    }
	
	public String test(){

		
		long begin =System.currentTimeMillis();
		// 获取表单
		Map<String, String> pageMap = new HashMap<>();

		String biz_code=pageMap.get("biz_code");
		if(StringUtils.isEmpty(biz_code)){
			LOG.warn("文章biz_code为空:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+pageMap.get("content").length()+">]");
			return "fail";
			}
		if (StringUtils.isEmpty(pageMap.get("baseUrl")) || StringUtils.isEmpty(pageMap.get("content"))
				|| pageMap.get("content").equals("此页面是否是列表页或首页？未找到合适正文内容。")) {
//			System.out.println("content or baseUrl or other"+pageMap.get("baseUrl")+":");
			return "fail";
		}
		if(pageMap.get("content").length()<19){
			LOG.warn("文章内容长度太短:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+pageMap.get("content").length()+">]");
			return "fail";
			}
		long currTime = System.currentTimeMillis();

		Item page = new Item();

		page.setContent(HtmlParser.delHTMLTag(pageMap.get("content")));
		page.setFirstFetchTime(currTime + "");
		page.setTitle(pageMap.get("title"));
		Map<String, String> meta = new HashMap<String, String>();
		meta.put("keyword", pageMap.get("keywords"));
		page.setMeta(meta);

		if (pageMap.get("tag") != null) {
			Map<String, Float> keywords = Maps.newHashMap();
			String[] tags = ((String) pageMap.get("tag")).split(",");
			float value = Float.valueOf(NumberFormat.decimalFormat(1.0f / tags.length));
			for (String t : tags) {
				if (!StringUtils.isEmpty(t))
					keywords.put(t.trim(), value);
			}
			page.setKeyword(keywords);

		}
		String url = pageMap.get("baseUrl");

		if (!StringUtils.isEmpty(pageMap.get("pubDate"))) {
			page.setFirstPubTime(DateUtils.getPubDate(pageMap.get("pubDate")));
		}
		if (!StringUtils.isEmpty(pageMap.get("tsmp"))) {
			try {
				page.setFirstPubTime(DateUtils.format(null, new Date(Long.valueOf(pageMap.get("tsmp").trim()))));
			} catch (NumberFormatException e) {

				e.printStackTrace();
				LOG.warn("时间戳格式错误:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<tsmp:" + pageMap.get("tsmp") + ">]");

				return "fail";

			}
		}

		if (!StringUtils.isEmpty(pageMap.get("author"))) {
			meta.put("author", pageMap.get("author"));
		}

		if (pageMap.get("biz_code") != null) {
			// queue.put(key, value)

			String catagory = null;
			String autoDis = pageMap.get("autoDis");
			if ("0".equals(autoDis) && StringUtils.isEmpty(pageMap.get("catagory"))){
				System.out.println("zero ---- and"+pageMap.get("catagory"));
				return "fail";
			}
			if ("1".equals(autoDis) || (StringUtils.isEmpty(pageMap.get("catagory")))) {

				String result = HttpClientResource.post(page.getContent(),
						"http://slave2:9999/mining/classify?biz_code=" + pageMap.get("biz_code")
								+ "&ss_code=user-analys&model=NaiveBayes");

				StringTokenizer token = new StringTokenizer(result, "()");
				int i = 0;
				StringBuffer tag = new StringBuffer();
				while (token.hasMoreTokens()) {
					String tok = token.nextToken();
					if (tok.equals(","))
						continue;
					tag.append(tok.split(",")[0]).append(" ");
					if (++i == 1)
						break;
				}

				catagory = tag.toString();
				page.getMeta().put("auto", "1");
			} else {
				catagory = pageMap.get("catagory");
			}

			if (catagory == null || "".equals(catagory)){
				LOG.warn("找不到该类别索引:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+catagory+">]");

				return "fail";
			}
			
//			System.out.println("time 1.............."+(System.currentTimeMillis()-begin));
			try {

				String caId = CataLogManager.findCaIdByName(catagory.trim());
				page.setCatagory(CataLogManager.getCaName(caId));
				page.setCatagoryId(caId);
				page.setpCatagoryId(CataLogManager.findPId(caId));
				if (caId == null){
					System.out.println("not find caId:");
					LOG.warn("找不到该类别ID:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+catagory+">]");

					return "fail";
				}
				ArticleClient arClient = cmsFactory.getArticleClient();
				ArticleVo ar=new ArticleVo();
				// ar.setCreateTime();

				if (page.getFirstPubTime() != null)
					if (page.getFirstPubTime().contains(":")) {
						ar.put("createTime", String.valueOf(DateUtils.toDate("yyyy-MM-dd HH:mm", page.getFirstPubTime()).getTime()));
						page.setFirstPubTime(ar.get("createTime"));

					} else {
						ar.put("createTime", String.valueOf(DateUtils.toDate("yyyy-MM-dd HH:mm", page.getFirstPubTime()).getTime()));
						page.setFirstPubTime(ar.get("createTime"));

					}
				ar.put("content",pageMap.get("html"));
				ar.put("metaKeywords",pageMap.get("keywords"));
				if (!StringUtils.isEmpty(pageMap.get("tag"))) {
					ar.put("arTags",pageMap.get("tag").trim().replaceAll(" ", "##"));
				}
				ar.put("creatorId","00000002");
				ar.put("ArTitle",page.getTitle());
				ar.put("caId",caId);
				ar.put("IsVote",page.getMeta().get("auto") == null ? "0" : "1");
				ar.put("description",pageMap.get("description"));
				String ogUrl = StringUtils.isEmpty(pageMap.get("ogUrl")) ? pageMap.get("baseUrl")
						: pageMap.get("ogUrl");

				ar.put("redirectUrl",ogUrl);
				page.getMeta().put("ogUrl", ogUrl);
				ar.put("ArAuthor",page.getMeta().get("author"));

				ar.put("status","1");
				ar.put("ArSource",pageMap.get("ogSite"));
				
				if(biz_code.equals("gov")){
					ar.put("siteId","190019");
					if(pageMap.get("wellFetch")!=null&&pageMap.get("wellFetch").equals("1")){
						
						if(pageMap.get("isDis")!=null&&pageMap.get("isDis").equals("1")){
							if(pageMap.get("index")!=null)
							    ar.put("docIndex", pageMap.get("index"));
							if(pageMap.get("index")!=null)
								ar.put("docNum", pageMap.get("arCode"));
							
						}else return "fail";
						
						
					}else return "fail";
					
				}

				page.getMeta().put("ogSite", pageMap.get("ogSite"));
//				System.out.println("time 2.............."+(System.currentTimeMillis()-begin));
				// CmsClientFactory cmsClientFactory;
				String[] rsIds = cmsFactory.getResourceClient().importOutSiteFileByUrl("190014", pageMap.get("img"));
//				System.out.println("time 3.5.............."+(System.currentTimeMillis()-begin));

				WsCallResult status = arClient.saveArticleSynData(caId, ar);

				page.getMeta().put("img", pageMap.get("img"));
//				System.out.println("time 3.............."+(System.currentTimeMillis()-begin));

				if (status.getRet()==0) {
					// System.out.println("save to cms:" + status[2]);
					page.setId(TableUtil.IdReverse(status.getRetValue()));
					if (rsIds[0].equals("0"))
						arClient.setArticleCover(status.getRetValue(), rsIds[2]);
					// System.out.println("save to hbase:" +
					// TableUtil.IdReverse(status[2]));
					// String
					// hbaseCode=client.doSend("http://192.168.16.111:10080/HWEB/item/save",
					// queue, "POST");
//					itemService.put(pageMap.get("biz_code"), TableUtil.IdReverse(status[2]), page);
//					System.out.println("time 4.............."+(System.currentTimeMillis()-begin));

					// System.out.println("save the data next ");
					return "success";
				} else {
					System.out.println("fail:::"+status.getRetValue());
					LOG.error("存储cms异常:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+status.getMsg()+">]");

					return "fail:";

				}

			}catch(NullPointerException e){
				
				LOG.error("日志采集失败:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+e.getMessage()+">]");

				return "fail";
			}catch (Exception e) {
				//
				System.out.println(pageMap.get("catagory"));
				LOG.error("日志采集失败:[title:"+pageMap.get("title")+"   url:"+pageMap.get("baseUrl")+"::<"+e.getMessage()+">]");

				 e.printStackTrace();
				 
				return "fail";
			}

		}
		System.out.println("return fail");
		return "fail";

	
		
	}
	
	
}
