package com.xn.spider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xn.utils.DBUtils;
import com.xn.utils.StringUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 爬虫调度类
 * @author Administrator
 * 2016年2月14日16:48:48
 */
public class SpiderSchedule implements PageProcessor{

	private static Logger logger = Logger.getLogger(SpiderSchedule.class);

	
	
	private Site site = Site.me().setTimeOut(1000)
			.setUserAgent("Mozilla/5.0 (X11; Fedora; Linux x86_64; rv:41.0) Gecko/20100101 Firefox/41.0")
			.setCharset("gbk");
	
	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	@Override
	public void process(Page page) {
		Connection con = null;
		String url = page.getUrl().get();
		String regItem = ".*item.jd.com/\\d+.html";
		String regList = ".*list.jd.com/list.html?.*";
		String regSearch = ".*search.jd.com/Search?.*";
		String regWant = "[\\w:/]+(?<!search)\\.jd\\.com.*";
		boolean isItem = StringUtils.isMatches(regItem,url);
		boolean isList = StringUtils.isMatches(regList, url);
		boolean isSearch = StringUtils.isMatches(regSearch, url);
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		con = DBUtils.getConnection();
		if(con==null || isSearch) {
			page.setSkip(true);
			logger.error(url + " 抓取失败，忽略！！");
		}
//		try {
//			con.setAutoCommit(false);
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
		if(isItem) {
			
			try {
				
				pst1 = con.prepareStatement("insert into tb_fetchurls (fetchurl,itemname,itemid,fetchtime,threadname,others)"
							+ " values (?,?,?,?,?,?)");
				//pst2 = con.prepareStatement("insert into tb_iteminfo (itemid,itemname,price,commentcounts,shop) values(?,?,?,?,?)");
				con.setAutoCommit(false);
				String itemname = page.getHtml().xpath("//div[@id='name']/h1/tidyText()").get();
				String itemid = page.getHtml().xpath("//div[@class='fl']/span[2]/tidyText()").get();
				pst1.setString(1, url);
				pst1.setString(2, itemname);
				pst1.setString(3, itemid);
				pst1.setString(4, new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").format(new Date()));
				pst1.setString(5, Thread.currentThread().getName());
				pst1.setString(6, "");
				pst1.execute();
				pst1 = con.prepareStatement("insert into tb_iteminfo (itemid,itemname,price,commentcounts,shop) values(?,?,?,?,?)");
				JSONObject o = itemInfo(itemid);
				pst1.setString(1, itemid);
				pst1.setString(2, itemname);
				pst1.setString(3, o.getString("price"));
				pst1.setString(4, o.getString("CommentCount"));
				pst1.setString(5, o.getString("seller"));
				pst1.execute();
				con.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error( " 商品URL ： " + url + "  获取数据失败！！");
				logger.error(e.toString());
			} finally{
				try {
					con.close();
					pst1.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(" 数据库连接关闭失败！！！");
					logger.error(e);
				}
			}
			
		} else if(isList) {
			List<String> urls1 = page.getHtml().css("div.ml-wrap").links().regex(".*item.jd.*").all();
			List<String> urls2 = page.getHtml().xpath("//*[@id=\"J_bottomPage\"]")
					.links().all();
			page.addTargetRequests(urls2);
			page.addTargetRequests(urls1);
		
		} else {
		
			Selectable selectable = page.getHtml().links();
			Selectable sel = selectable.regex(".*\\.jd\\.com.*");
			ArrayList<String> list = (ArrayList<String>) sel.all();
			for(String s : list) {
				page.addTargetRequest(StringUtils.BloomDuplic(s));
			}
			//page.addTargetRequests(page.getHtml().links().regex(".*\\.jd\\.com.*").all());
			try {
				pst1 = con.prepareStatement("insert into tb_url (url,time,threadname) values (?,?,?)");
				pst1.setString(1, url);
				pst1.setString(2, new SimpleDateFormat("yyyy:MM:dd hh:mm:ss").format(new Date()));
				pst1.setString(3, Thread.currentThread().getName());
				pst1.execute();
				
			} catch (SQLException e) {
				try {
					pst1.close();
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					logger.error(" 数据库连接关闭失败！！！");
				}
				e.printStackTrace();
				logger.error( " URL ： " + url + "  插入数据库失败！！");
			}
		}
	}
	
	public JSONObject itemInfo(String itemid) {
		HttpClient httpClient = HttpClients.createDefault();
		//HttpGet priceGet = new HttpGet("http://p.3.cn/prices/get?type=1&area=17_1458_43387&pdtk=&pduid=1681236374&pdbp=0&skuid=J_"+itemid+"&callback=cnp");
		//1317239,J_845322,J_2015887,J_1708035039,J_1544044,J_1989852,J_1251208,J_1997070,J_1938912,J_1806805991,J_188074,J_1703690562,J_1343117300,J_1448043,J_1031257,J_1197453,J_1244139,J_1654530,J_732836,J_1722540,J_10020084729,J_996957,J_1246836,J_695469
		HttpGet priceGet = new HttpGet("http://p.3.cn/prices/mgets?skuIds=J_"+itemid+"&callback=jQuery775321&_=1459827026631");
		HttpGet commentCounts = new HttpGet("http://club.jd.com/clubservice.aspx?method=GetCommentsCount&referenceIds="+itemid+"&callback=jQuery1165193&_=1455601442522");
		HttpGet shopGet = new HttpGet("http://chat1.jd.com/api/checkChat?&callback=jQuery4374894&pid="+itemid+"&returnCharset=utf-8&_=1447635649656");
		JSONObject o = null;
		try {
			HttpResponse r1 = httpClient.execute(priceGet);
			HttpResponse r2 = httpClient.execute(commentCounts);
			HttpResponse r3 = httpClient.execute(shopGet);
			
			String price = EntityUtils.toString(r1.getEntity());
			String commentsCount = EntityUtils.toString(r2.getEntity());
			String shop = EntityUtils.toString(r3.getEntity());
			
			price = StringUtils.getMatches("(?<=\\[).*(?=\\])", price);
			commentsCount = StringUtils.getMatches("(?<=\\[).*(?=\\])", commentsCount);
			shop = StringUtils.getMatches("(?<=\\().*(?=\\))", shop);
		
			JSONObject oprice = JSON.parseObject(price);
			JSONObject oCommentsCount = JSON.parseObject(commentsCount);
			JSONObject oShop = JSON.parseObject(shop);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("price", oprice.get("p"));
			map.put("CommentCount", oCommentsCount.get("CommentCount"));
			map.put("seller", oShop.get("seller"));
			o = new JSONObject(map);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("商品信息获取失败");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("商品信息获取失败");
		} finally{
			priceGet.releaseConnection();
			commentCounts.releaseConnection();
			shopGet.releaseConnection();
		}
		return o;
	}
}
