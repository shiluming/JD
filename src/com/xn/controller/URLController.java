package com.xn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xn.model.PageBean;
import com.xn.model.Product;
import com.xn.model.URLModel;
import com.xn.service.URLService;

@Controller
@RequestMapping("/url")
public class URLController {

	@Resource
	private URLService urlSerivce;
	
	@RequestMapping("usefullURL")
	@ResponseBody
	public String useFullURL() {
		int count = urlSerivce.usefullCount();
		JSONObject json = new JSONObject();
		json.put("count", count);
		return JSON.toJSONString(json);
	}
	
	@RequestMapping("unusefullURL") 
	@ResponseBody
	public String unusefullURL() {
		int count = urlSerivce.unusefullCount();
		JSONObject json = new JSONObject();
		json.put("count", count);
		return JSON.toJSONString(json);
	}
	
	//服务端分页,无效的url
		@RequestMapping(value="/unusefull",produces = {"application/json;charset=UTF-8"})
		@ResponseBody
		public String getUnuseFullJsonFromSer(HttpServletRequest request) {
			int currentpage = request.getParameter("offset")==null?1:Integer.parseInt(request.getParameter("offset"));
			int showCount = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
			int total = urlSerivce.unusefullCount();
			PageBean pageBean = new PageBean(currentpage, showCount);
			JSONObject all = new JSONObject();
			JSONArray row = new JSONArray();
			JSONObject o1 = new JSONObject();
			List<URLModel> list = urlSerivce.getunUseFullData(pageBean);
			for(URLModel p : list) {
				JSONObject temp = new JSONObject();
				temp.put("id", p.getId());
				temp.put("url", p.getUrl());
				temp.put("time", p.getTime());
				temp.put("threadname", p.getThreadname());
				row.add(temp);
			}
			
			JSONObject o3 = new JSONObject();
			all.put("rows", row);
			all.put("total", total);
			return JSON.toJSONString(all);
		}
		
		//服务端分页,有效的url
				@RequestMapping(value="/usefull",produces = {"application/json;charset=UTF-8"})
				@ResponseBody
				public String getUseFullJsonFromSer(HttpServletRequest request) {
					int currentpage = request.getParameter("offset")==null?1:Integer.parseInt(request.getParameter("offset"));
					int showCount = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
					int total = urlSerivce.usefullCount();
					PageBean pageBean = new PageBean(currentpage, showCount);
					JSONObject all = new JSONObject();
					JSONArray row = new JSONArray();
					JSONObject o1 = new JSONObject();
					List<URLModel> list = urlSerivce.getUseFullData(pageBean);
					for(URLModel p : list) {
						JSONObject temp = new JSONObject();
						temp.put("id", p.getId());
						temp.put("url", p.getUrl());
						temp.put("time", p.getTime());
						temp.put("itemid", p.getItemid());
						temp.put("threadname", p.getThreadname());
						row.add(temp);
					}
					
					JSONObject o3 = new JSONObject();
					all.put("rows", row);
					all.put("total", total);
					return JSON.toJSONString(all);
				}
}
