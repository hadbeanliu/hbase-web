package com.lhb.hbase.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Stream;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lhb.data.common.CataLogManager;
import com.lhb.data.common.HtmlParser;
import com.lhb.data.common.HttpClientResource;
import com.lhb.data.common.MainWordExtractor;
import com.lhb.data.common.TextRankKeyWordExtor;
import com.lhb.data.meta.Weight;
import com.rongji.cms.webservice.client.json.ArticleClient;
import com.rongji.cms.webservice.client.json.CmsClientFactory;
import com.rongji.cms.webservice.domain.WsArticleFilter;
import com.rongji.cms.webservice.domain.WsArticleSynData.ArticleVo;
import com.rongji.cms.webservice.domain.WsPage;

public class Test2 {

	public static List<String> get() throws Exception {
		

		Gson gson=new Gson();
		CmsClientFactory fac = new CmsClientFactory("http://cms.work.net", "00000002", "A7dCV37Ip96%86");
		ArticleClient client = fac.getArticleClient();
		System.out.println(client);
		WsArticleFilter filter = new WsArticleFilter();

		filter.setArIds("2017071219004177");
		
		WsPage page = new WsPage();

		ArticleVo article= client.findArticleVos(filter, page).getList().get(0);
//		article.forEach(action);
		System.out.println(article.get("createTime"));
		System.out.println(article.get("pubDate"));
		System.out.println("isVote:"+article.get("isVote")+":"+article.get("caName"));
		String content = article.get("content").toLowerCase();
		String text = HtmlParser.delHTMLTag(content);
		String model = "NaiveBayes";
		System.out.println(text);
		MainWordExtractor mainWordExtor=MainWordExtractor.getInstance();
		List<String> words=mainWordExtor.tokenizeWithoutPart(text);
		String uri = "http://slave2:9999/mining/extractkw?biz_code=headlines" + "&ss_code=user-analys";
		// String text = "[图]杭州上万辆共享单车被弃荒野 场面蔚为壮观";
		String result = HttpClientResource.post(gson.toJson(words), uri);
//		TextRankKeyWordExtor textRank=new TextRankKeyWordExtor();
//		Map<String, Float> tr=textRank.getKeyWordWithScore(mainWordExtor.tokenizeWithoutPart(text));
//		Map<String,Weight> weights=new Gson().fromJson(result, new TypeToken<Map<String,Weight>>() {}.getType());
//		Map<String, Double> withIDF=new HashMap<>();			
//		Map<String,Double> andTFIDF=new HashMap<>();
		
		
//		Stream<Weight> stream= weights.values().stream();
		
		
		
//		double maxIDF=stream.max((x,y)->x.getIdf().compareTo(y.getIdf())).get().getIdf();
//		double minIDF=stream.min((x,y)->x.getIdf().compareTo(y.getIdf())).get().getIdf();

//		System.out.println(maxIDF);
//		System.out.println(minIDF);
		
//		double maxIDF=Comparator<T>
//		double minIDF=
//		double
		
//		Weight defautWeight=Weight.getZero();
//		tr.forEach((k,v)->{
//			Weight w=weights.getOrDefault(k, defautWeight);
//				
//			withIDF.put(k, (v*w.getIdf()));
//			
//		});
//		
		
		
		
//		
//		Map<String, Double> withIDF=new HashMap<>();
//		Map<String, Double> andTFIDF=new HashMap<>();
//
////		Collections.max(coll)
//		tfidfs.forEach(x->{
//			String word=x.getWord();
//			withIDF.put(word, x.getIdf()*tr.getOrDefault(word, 0f));
//			andTFIDF.put(word, x.getTfidf()+tr.getOrDefault(word, 0f));
//		});
		
		System.out.println("-----------"+result);
		
		System.out.println(result);

		StringTokenizer strToken = new StringTokenizer(result, "()");
		List<String> resultList = new ArrayList<>();
		List<Meta> metas = new ArrayList<>();
		while (strToken.hasMoreTokens()) {
			String tok = strToken.nextToken();
			if (tok.equals(","))
				continue;
			String[] split = tok.split(",");
			String caId = CataLogManager.findCaIdByName(split[0]);
			float value = 0.0f;
			if (split[1].length() < 5)
				value = Float.valueOf(split[1]);
			else
				value = Float.valueOf(split[1].substring(0, 5));
			metas.add(new Meta(caId, split[0], value));

			resultList.add(tok);
		}
		return resultList;
	}

	public static void main(String[] args) throws Exception {

		Map<String, Integer> test=new HashMap<>();
		test.put("a", 10);
		test.put("a", 132);
		
//		test.entrySet().stream().min(comparator)
		
		
		List<String> resultList = get().subList(0, 10);
//		List<String> resultList = new ArrayList<>();
//		resultList.add("用户体验,1");
//		resultList.add("用户体验,0.1");
//		resultList.add("用户体验,0.04");
		double[] d = new double[resultList.size()];
		int i = 0;
		double sum = 0;
		double max = 0;
		int aveg=0;
		int[] c=new int[resultList.size()];

		for (String s : resultList) {
			double v = Double.parseDouble(s.split(",")[1]);
			System.out.println(v + "~~~~~~~~~~~~~~~~~~~~");
			if(v!=0)
			c[i]=(int) Math.abs(Math.log10(v));
//			v = Math.pow(10, 1 - 1 / v);
			d[i] = v;
			i++;
		}
		for (int k = 0; k < c.length; k++) {
			aveg+=c[k];
		}
		System.out.println(aveg);
		aveg/=c.length;
		for (int k = 0; k < d.length; k++) {
			System.out.println(d[k]);
		}
		System.out.println("--------------------");

		for (double v : d) {
			max = Math.max(max, v);
		}
		for (int k = 0; k < d.length; k++) {
			System.out.println(Math.exp(d[k] - max));
			System.out.println(Math.pow(75, (d[k] - max)));
			d[k] = Math.pow(Math.PI, (d[k] - max));
		}
		for (int k = 0; k < d.length; k++) {
			sum += d[k];
		}
		for (int k = 0; k < d.length; k++) {
			d[k] = d[k] / sum;
		}
		for (int k = 0; k < d.length; k++) {
			System.out.println(d[k]);
		}
		System.out.println("-------"+aveg+"-------------" + sum + "----------" + max);

		for (int k = 0; k < d.length; k++) {
			System.out.println(d[k]);
		}
	}

}

class Meta {

	private String caId;
	private String caName;
	private float value;

	public Meta(String caId, String caName, float value) {
		this.caId = caId;
		this.caName = caName;
		this.value = value;
	}

	public String getCaId() {
		return caId;
	}

	public void setCaId(String caId) {
		this.caId = caId;
	}

	public String getCaName() {
		return caName;
	}

	public void setCaName(String caName) {
		this.caName = caName;
	}

	public double getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	@Override
	public String toString() {

		return this.caName + "," + this.value;
	}

}