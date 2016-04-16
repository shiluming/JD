package com.xn.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.bag.SynchronizedSortedBag;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xn.model.Product;
import com.xn.service.DataService;
import com.xn.spider.CommentsThread;
import com.xn.utils.FileUtils;
import com.xn.utils.HttpClientUtils;
import com.xn.utils.MD5Utils;

@Controller
@RequestMapping("/comments")
public class CommentsController {

	
	//文件分隔符 
	private final static String SEPARATOR = System.getProperty("file.separator");
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private DataService dataService;
	
	private Logger logger = Logger.getLogger(CommentsController.class);
	
	@RequestMapping("/start")
	public void startComments() {
		//多线程开始开始抓取评论功能
		//1. 开个线程池，开始抓取数据
		int threadNum = 20;    //线程数量
		ExecutorService threadPools = Executors.newFixedThreadPool(threadNum);
		List<Product> proList = dataService.getDataForComments(null);
		HttpClientUtils utils = new HttpClientUtils();
		for(Product o : proList) {
			String md5id = MD5Utils.GetMD5Code(o.getItemid());
			String dirPath = "data"+SEPARATOR+md5id.substring(0, 2)+SEPARATOR+md5id.substring(2, 4);
			String path = dirPath+SEPARATOR+md5id+".json";
			FileUtils.createDir(dirPath);
			FileUtils.createFile(path);
			CommentsThread comments = new CommentsThread(utils.getHttpClient()
					, "http://club.jd.com/productpage/p-%**%-s-0-t-3-p-||.html?callback=fetchJSON_comment98vv52023" 
					, Integer.parseInt(o.getCommentCounts())
					, o.getItemid()
					,path);
			threadPools.execute(comments);
		}
		logger.info("  创建 线程池， 线程数量 ： " + threadNum + " 往线程池中丢入了： " + proList.size() + " 个任务 ");
		
	}
	@RequestMapping(value="/startSingle",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String startSingleComments(@RequestParam(value="itemid",required=true)String itemid
			,HttpServletRequest request) {
		Map map = new HashMap<String,String>();
		//单线程开始抓取评论    要抓的商品的id， 商品的评论数
		Product p = new Product();
		p.setItemid(itemid);
		List<Product> list = dataService.getDataForComments(p);
		if(list.size()>0) {
			Product resultPro = list.get(0);
			//http://club.jd.com/productpage/p-1956794-s-0-t-3-p-1.html?callback=fetchJSON_comment98vv22910
			String url = "http://club.jd.com/productpage/p-%**%-s-0-t-3-p-||.html?callback=fetchJSON_comment98vv22910";
			HttpClientUtils utils = new HttpClientUtils();
			int counts = Integer.parseInt(resultPro.getCommentCounts());
			String id = resultPro.getItemid();
			String md5id = MD5Utils.GetMD5Code(id);
			String dirPath = "data"+SEPARATOR+md5id.substring(0, 2)+SEPARATOR+md5id.substring(2, 4);
			String path = dirPath+SEPARATOR+md5id+".json";
			FileUtils.createDir(dirPath);
			FileUtils.createFile(path);
			CommentsThread comments = new CommentsThread(utils.getHttpClient()
					,url
					,counts
					,id
					,path);
			
			comments.start();
		}
		map.put("result", "成功");
		return JSON.toJSONString(map);
		
	}
	/**
	 * 评论分析 2016年4月13日21:32
	 * @return
	 */
	@RequestMapping(value="/anyComments",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String anyComments(@RequestParam(value="itemid",required=true)String itemid
			,HttpServletRequest request) {
		long startTime = System.currentTimeMillis();
		BufferedReader reader = null;
		String md5id = MD5Utils.GetMD5Code(itemid);
		String dirPath = "data"+SEPARATOR+md5id.substring(0, 2)+SEPARATOR+md5id.substring(2, 4);
		String path = dirPath+SEPARATOR+md5id+".json";
		reader = FileUtils.reader(path);
		if(reader==null) {
			return "[]";
		}
		Product p = new Product();
		p.setItemid(itemid);
		List<Product> userList = dataService.getDataForComments(p);
		List<Map<String,List<Object[]>>> result = new ArrayList<Map<String,List<Object[]>>>();
		try {
			String json = null;
			List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
			JSONObject obj = null;
			Set<String> citySet = new HashSet<String>();
			Set<String> levelSet = new HashSet<String>();
			while((json=reader.readLine())!=null) {
				obj = JSON.parseObject(json);
				HashMap<String,String> map = new HashMap<String,String>();
				String content = obj.getString("content");
				String createTime = obj.getString("creationTime");
				String isMobile = obj.getString("isMobile");
				String userLevelName = obj.getString("userLevelName");
				String userProvince = obj.getString("userProvince");
				String userRegisterTime = obj.getString("userRegisterTime");
				String nickname = obj.getString("nickname");
				map.put("content", content);
				map.put("createTime", createTime);
				map.put("isMobile", isMobile);
				map.put("userLevelName", userLevelName);
				map.put("userProvince", userProvince);
				map.put("userRegisterTime", userRegisterTime);
				map.put("nickName", nickname);
				citySet.add(userProvince);
				levelSet.add(userLevelName);
				list.add(map);
			}
			/**
			 * Map<String,String>
			 * {name:"广州",age:11,level:3}
			 * {name:"深圳",age:21,level:1}
			 * {name:"汕尾",age:122,level:3}
			 * {name:"深圳",age:12,level:2}
			 * {name:"广州",age:122,level:1}
			 * 
			 * 现在我要统计不同城市有多少组，以上面来说是：3组，分别是 广州（2），深圳（2），汕尾（1），怎么统计？
			 * 
			 * new 一个 Set集合，Set集合是不允许重复值出现的，所以只要将 m.get("name")的值扔到set去，之后用set.size()
			 * 就能得出有多少组了
			 * 
			 */
			final String[] city = citySet.toArray(new String[0]);
			final String[] realCity = new String[city.length-1];
			for(int i=1;i<city.length;i++) {//去掉空值
				realCity[i-1]=city[i];  
			}
			int[] userProvinceNum = new int[citySet.size()];//用户省份组数

			final String[] level = levelSet.toArray(new String[0]);
			int[] userLevelNum = new int[levelSet.size()];//用户等级组数
			
			System.out.println( " list 大小： " + list.size() + " 存放用户等级int[]大小 " + userLevelNum.length
					+ " level userLevelNum 的大小： " + level.length + " , " + userLevelNum.length);
			for(int j=0;j<list.size();j++) {
				HashMap<String,String> m = list.get(j);
				String levelName = m.get("userLevelName");
				for(int i=0;i<level.length;i++) {
					if(m.get("userLevelName") == level[i] || m.get("userLevelName").equals(level[i])) {
						userLevelNum[i]++;// leve数组存储的是省份信息，假设找到，就在该省份对应的用户省份组数userProvinceNum +1 
						break;
					}
				}
				for(int k=0;k<city.length;k++) {
					if(m.get("userProvince").equals(city[k]) || m.get("userProvince") == city[k]) {
						userProvinceNum[k]++;
						break;
					}
				}
			}
			System.out.println(" 算出有多少用户等级组： " + level.length);
			Map<String,List<Object[]>> provinceMap = new HashMap<String,List<Object[]>>();
			for(int i=0;i<city.length;i++) {
				System.out.print( city[i] + " 人数： " + userProvinceNum[i] +";");
			}
			List<Object[]> ll = new ArrayList<Object[]>();
			ll.add(realCity);
			Integer[] integer1 = new Integer[userProvinceNum.length-1];
			for(int i=1;i<userProvinceNum.length;i++) {
				integer1[i-1] = userProvinceNum[i];
			}
			ll.add(integer1);
			provinceMap.put("PROVINCES", ll);
			
			result.add(provinceMap);
			System.out.println(" ");
			Map<String,List<Object[]>> levelMap = new HashMap<String,List<Object[]>>();
			for(int i=0;i<level.length;i++) {
				System.out.println(level[i] + " 等级 ： " + userLevelNum[i]);
			}
			List<Object[]> lll = new ArrayList<Object[]>();
			lll.add(level);
			Integer[] integer = new Integer[userLevelNum.length];
			for(int i=0;i<userLevelNum.length;i++ ) {
				integer[i]=userLevelNum[i];
			}
			lll.add(integer);
			
			provinceMap.put("USERLEVEL", lll);
			result.add(levelMap);
			Map<String,List<Object[]>> userMap = new HashMap<String,List<Object[]>>();
			List<Object[]> us = new ArrayList<Object[]>();
			us.add(new String[]{userList.get(0).getItemname()});
			userMap.put("USER", us);
			result.add(userMap);
			System.out.println(" ");
			System.out.println(" 总的执行时间： " + (System.currentTimeMillis()-startTime)/1000 + " 秒");
			
		} catch (IOException e) {
			return "";
		} finally{
			try {
				if(reader!=null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return JSON.toJSONString(result);
	}
	public String fetchCommentsInfo(String itemid) {
		String md5id = MD5Utils.GetMD5Code(itemid);
		String dirPath = "data"+SEPARATOR+md5id.substring(0, 2)+SEPARATOR+md5id.substring(2, 4);
		String path = dirPath+SEPARATOR+md5id+".json";
		Path file = Paths.get(path);
		
		return null;
	}
}
