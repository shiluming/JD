package com.xn.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xn.model.IndexModel;
import com.xn.model.PageBean;
import com.xn.model.Product;
import com.xn.service.IndexService;

/**
 * 索引控制类
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/index")
public class JDIndex {

	//文件分割符
	private final static String SEPARATOR = System.getProperty("file.separator");
	@Resource
	private IndexService indexService;
	/*
	 * 导航
	 */
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage", "index/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	/*
	 * 创建索引 
	 */
	@RequestMapping(value="/create",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String createIndex(HttpServletRequest request) {
		//设置历史索引的标志位
		String id = request.getParameter("id");
		System.out.println(id);
		indexService.changeFlag(id);
		Map map = new HashMap<String,String>();
		deleteInde();
		String result = indexService.createIndex();
		map.put("result", result);
		return JSON.toJSONString(map);
	}
	@RequestMapping(value="buckup",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String buckup() {
		Map map = new HashMap<String,String>();
		String date = new SimpleDateFormat("yyyy-MM-dd hhssmm").format(new Date());
		Path path = Paths.get("lucene2");
		try {
			Stream<Path> sp = Files.list(path);
			Iterator<Path> it = sp.iterator();
			File dir = new File("lucene2"+SEPARATOR+date+SEPARATOR);
			dir.mkdirs();
			while(it.hasNext()) {
				Path p = it.next();
				Files.copy(p,new FileOutputStream(new File("lucene2"+SEPARATOR+date+SEPARATOR+p.getFileName())));
			}
			
		} catch (IOException e) {
			map.put("result", "备份失败");
			return JSON.toJSONString(map);
		}
		map.put("result", "备份成功");
		return JSON.toJSONString(map);
	}
	/**
	 * 删除索引
	 * @return
	 */
	@RequestMapping(value="delete",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String deleteInde() {
		String result = indexService.deleteIndex();
		Map map = new HashMap<String,String>();
		map.put("result", result);
		return JSON.toJSONString(map);
	}
	
	/*
	 * 返回历史的索引信息 ajax
	 */
	@RequestMapping(value="/indexLogInfo",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String currentIndexInfo(HttpServletRequest request) {
		String current = request.getParameter("current");
		JSONObject all = new JSONObject();
		JSONArray row = new JSONArray();
		JSONObject o1 = new JSONObject();
		if(current!=null) {
			List<IndexModel> list = indexService.getData(null,current);
			for(IndexModel i : list) {
				JSONObject temp = new JSONObject();
				temp.put("id", i.getId());
				temp.put("createtime", i.getCreateTime());
				temp.put("buckupdir", i.getBuckupDir());
				temp.put("indexdir", i.getIndexDir());
				temp.put("indexnum", i.getIndexNum());
				temp.put("indexuser", i.getIndexUser());
				temp.put("sparetime", i.getSpareTime());
				temp.put("isbuckup", i.isBuckup());
				row.add(temp);
			}
			all.put("rows", row);
			all.put("total", 1);
			return JSON.toJSONString(all);
		}
		int currentpage = request.getParameter("offset")==null?1:Integer.parseInt(request.getParameter("offset"));
		int showCount = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
		int total = indexService.count();
		PageBean pageBean = new PageBean(currentpage, showCount);
		
		List<IndexModel> list = indexService.getData(pageBean,null);
		for(IndexModel i : list) {
			JSONObject temp = new JSONObject();
			temp.put("id", i.getId());
			temp.put("createtime", i.getCreateTime());
			row.add(temp);
		}
		all.put("rows", row);
		all.put("total", total);
		return JSON.toJSONString(all);
	}
	@RequestMapping(value="/search",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String search(HttpServletRequest request) {
		//第一个参数要查询的Field， 第二个参数是查询的条件
		String query = request.getParameter("query");
		return indexService.search("itemname",query);
	}
	
	
	@RequestMapping("/showsearch")
	public ModelAndView showSearch(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		String query = request.getParameter("queryStr");
		List<Product> list = indexService.searchForObject("itemname", query);
		mav.addObject("list", list);
		mav.addObject("mainPage", "dataplatform/showsearch.jsp");
		mav.setViewName("main");
		mav.addObject("query",query);
		return mav;
	}
}
