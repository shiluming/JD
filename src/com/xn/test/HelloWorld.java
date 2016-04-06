package com.xn.test;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xn.spider.SpiderSchedule;
import com.xn.utils.StringUtils;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

public class HelloWorld {

	public static void main(String args[]) {
		
		Logger logger = Logger.getLogger(HelloWorld.class);
		logger.info("==============程序开始执行=============");
		HelloWorld h = new HelloWorld();
		h.test5();
		//h.test1();
	}
	public void test7() {
		String[] list = {
			"http://oer.jd.com/center/list.actionrd",
			"http://home.jd.com/",
			"http://vip.jd.com/",
			"http://b.jd.com/",
			"http://www.jd.com/allSort.aspx",
			"http://channel.jd.com/decoration.html",
			"http://licai.jd.com/",
			"http://search.jd.com/search?keyword=%E5%90%89%E6%99%AE&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid3=12091&wtype=1#filter",
			"http://www.jd.com"
		};
		
		String reg = "http://(?!search)\\w+\\.jd\\.com(?:[^\\n]+)?";
		for(String s : list) {
			Pattern p = Pattern.compile(reg);
			Matcher m = p.matcher(s);
			if(m.find()) {
				System.out.println(m.group());
			}
		}
		System.out.println(reg);
		
	}
	public void test6() {
		
		SpiderSchedule s = new SpiderSchedule();
		Spider.create(s).test("http://search.jd.com/search?keyword=%E5%90%89%E6%99%AE&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid3=12091&wtype=1#filter");
	}
	
	public void test5() {
		SpiderSchedule spider = new SpiderSchedule();
		Spider.create(spider).addUrl("http://www.jd.com")
		.thread(10)
		.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
		.addPipeline(new ConsolePipeline())
		.start();
	}
	public void test4(String itemid) {
		SpiderSchedule s = new SpiderSchedule();
		JSONObject o = s.itemInfo(itemid);
		System.out.println(o.getString("price") +  " : " + o.getString("CommentCount") + " : " + o.getString("seller"));
	}
	
	private String test3(String itemid) {
		HttpClient httpClient = HttpClients.createDefault();
		HttpGet priceGet = new HttpGet("http://p.3.cn/prices/get?type=1&area=17_1458_43387&pdtk=&pduid=1681236374&pdbp=0&skuid=J_"+itemid+"&callback=cnp");
		HttpGet commentCounts = new HttpGet("http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds="+itemid+"&callback=jQuery1165193&_=1455601442522");
		HttpGet shopGet = new HttpGet("http://chat1.jd.com/api/checkChat?&callback=jQuery4374894&pid="+itemid+"&returnCharset=utf-8&_=1447635649656");
		try {
			HttpResponse r1 = httpClient.execute(priceGet);
			HttpResponse r2 = httpClient.execute(commentCounts);
			HttpResponse r3 = httpClient.execute(shopGet);
			String price = EntityUtils.toString(r1.getEntity());
			String commentsCount = EntityUtils.toString(r2.getEntity());
			String shop = EntityUtils.toString(r3.getEntity());
			System.out.println(price + " :\n " + commentsCount + " :\n " + shop);
			price = StringUtils.getMatches("(?<=\\[).*(?=\\])", price);
			commentsCount = StringUtils.getMatches("(?<=\\[).*(?=\\])", commentsCount);
			shop = StringUtils.getMatches("(?<=\\().*(?=\\))", shop);
			System.out.println(price + " :\n " + commentsCount + " :\n " + shop);
			JSONObject oprice = JSON.parseObject(price);
			JSONObject oCommentsCount = JSON.parseObject(commentsCount);
			JSONObject oShop = JSON.parseObject(shop);
			System.out.println(oprice.get("p") + " : " + oCommentsCount.get("CommentCount") + " : " + oShop.get("seller"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return null;
	}
	
	private void test2() {
		String s = ",";
		long start = System.currentTimeMillis();
		String json = "cnp([{\"id\":\"J_1565976774\",\"p\":\"299.00\",\"m\":\"465.00\"}]);";
		String json2 = "{\"1\":\"a\",\"2\":\"b\"}";
		HttpClient httpClient = HttpClients.createDefault();
		String itemid = "1002123";
		HttpGet get = new HttpGet("http://p.3.cn/prices/get?type=1&area=17_1458_43387&pdtk=&pduid=1681236374&pdbp=0&skuid=J_"+itemid+"&callback=cnp");
		try {
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String str = EntityUtils.toString(entity);
			System.out.println("返回的json： " + str);
		
		
			String pricejson = StringUtils.getMatches("(?<=\\[).*(?=\\])", str);
			System.out.println("处理好的json: " + pricejson);
//			if(m.find()) {
//				String price = m.group();
//				System.out.println("处理好的json: " + m.group());
//				JSONObject o = JSON.parseObject(price);
//				System.out.println(o.get("p"));
//			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
		}
		System.out.println(System.currentTimeMillis()-start);
		
	}
	private void test1() {
		
		SpiderSchedule spider = new SpiderSchedule();
//		Spider.create(spider).addUrl("http://www.jd.com")
//			.thread(10)
//			.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
//			.addPipeline(new ConsolePipeline())
//			.start();
		//Spider.create(spider).test("http://list.jd.com/list.html?cat=652,829,854&ev=93899_583567%7c%7c583568%7c%7c583569@");
		String reg = "(?m)^([^/\\s]+\\.)?jd\\.com$";
		String reg1 = "[\\w:/]+(?<!search)\\.jd\\.com.*";
		String reg2 = "^(?!search)\\w+\\.jd\\.com(?:[^\\n]+)?$";
		String url = "http://search.jd.com/search?keyword=%E5%90%89%E6%99%AE&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid3=12091&wtype=1#filter";
		String url1 = "http://list.jd.com/list.html?cat=652,829,854&ev=93899_583567||583568||583569%40";
		String url2 = "http://www.jd.com";
		System.out.println(url1.matches(reg1));
	}
}
