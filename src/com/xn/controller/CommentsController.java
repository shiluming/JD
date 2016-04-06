package com.xn.controller;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
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
			String url = "http://club.jd.com/productpage/p-%**%-s-0-t-3-p-||.html?callback=fetchJSON_comment98vv52023";
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
	
	public String fetchCommentsInfo(String itemid) {
		String md5id = MD5Utils.GetMD5Code(itemid);
		String dirPath = "data"+SEPARATOR+md5id.substring(0, 2)+SEPARATOR+md5id.substring(2, 4);
		String path = dirPath+SEPARATOR+md5id+".json";
		Path file = Paths.get(path);
		
		return null;
	}
}
