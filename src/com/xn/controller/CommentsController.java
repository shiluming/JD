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

	
	//�ļ��ָ��� 
	private final static String SEPARATOR = System.getProperty("file.separator");
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private DataService dataService;
	
	private Logger logger = Logger.getLogger(CommentsController.class);
	
	@RequestMapping("/start")
	public void startComments() {
		//���߳̿�ʼ��ʼץȡ���۹���
		//1. �����̳߳أ���ʼץȡ����
		int threadNum = 20;    //�߳�����
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
		logger.info("  ���� �̳߳أ� �߳����� �� " + threadNum + " ���̳߳��ж����ˣ� " + proList.size() + " ������ ");
		
	}
	@RequestMapping(value="/startSingle",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String startSingleComments(@RequestParam(value="itemid",required=true)String itemid
			,HttpServletRequest request) {
		Map map = new HashMap<String,String>();
		//���߳̿�ʼץȡ����    Ҫץ����Ʒ��id�� ��Ʒ��������
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
		map.put("result", "�ɹ�");
		return JSON.toJSONString(map);
		
	}
	/**
	 * ���۷��� 2016��4��13��21:32
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
			 * {name:"����",age:11,level:3}
			 * {name:"����",age:21,level:1}
			 * {name:"��β",age:122,level:3}
			 * {name:"����",age:12,level:2}
			 * {name:"����",age:122,level:1}
			 * 
			 * ������Ҫͳ�Ʋ�ͬ�����ж����飬��������˵�ǣ�3�飬�ֱ��� ���ݣ�2�������ڣ�2������β��1������ôͳ�ƣ�
			 * 
			 * new һ�� Set���ϣ�Set�����ǲ������ظ�ֵ���ֵģ�����ֻҪ�� m.get("name")��ֵ�ӵ�setȥ��֮����set.size()
			 * ���ܵó��ж�������
			 * 
			 */
			final String[] city = citySet.toArray(new String[0]);
			final String[] realCity = new String[city.length-1];
			for(int i=1;i<city.length;i++) {//ȥ����ֵ
				realCity[i-1]=city[i];  
			}
			int[] userProvinceNum = new int[citySet.size()];//�û�ʡ������

			final String[] level = levelSet.toArray(new String[0]);
			int[] userLevelNum = new int[levelSet.size()];//�û��ȼ�����
			
			System.out.println( " list ��С�� " + list.size() + " ����û��ȼ�int[]��С " + userLevelNum.length
					+ " level userLevelNum �Ĵ�С�� " + level.length + " , " + userLevelNum.length);
			for(int j=0;j<list.size();j++) {
				HashMap<String,String> m = list.get(j);
				String levelName = m.get("userLevelName");
				for(int i=0;i<level.length;i++) {
					if(m.get("userLevelName") == level[i] || m.get("userLevelName").equals(level[i])) {
						userLevelNum[i]++;// leve����洢����ʡ����Ϣ�������ҵ������ڸ�ʡ�ݶ�Ӧ���û�ʡ������userProvinceNum +1 
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
			System.out.println(" ����ж����û��ȼ��飺 " + level.length);
			Map<String,List<Object[]>> provinceMap = new HashMap<String,List<Object[]>>();
			for(int i=0;i<city.length;i++) {
				System.out.print( city[i] + " ������ " + userProvinceNum[i] +";");
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
				System.out.println(level[i] + " �ȼ� �� " + userLevelNum[i]);
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
			System.out.println(" �ܵ�ִ��ʱ�䣺 " + (System.currentTimeMillis()-startTime)/1000 + " ��");
			
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
