package com.xn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xn.model.PageBean;
import com.xn.model.Product;
import com.xn.service.DataService;
import com.xn.utils.PageUtils;
import com.xn.utils.StringUtils;

@Controller
@RequestMapping("/data")
public class DataController {
	
	@Resource
	private DataService dataService;
	
	@RequestMapping("/search")
	public ModelAndView search(HttpServletRequest request) {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage", "dataplatform/search.jsp");
		mav.setViewName("main");
		return mav;
	}
	@RequestMapping("/priceAnaly")
	public ModelAndView priceAnaly() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage", "common/default.jsp");
		mav.setViewName("main");
		return mav;
	}
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session = request.getSession();
		if(StringUtils.isEmpty(page)) {
			page = "1";
		}
		//PageBean pageBean = new PageBean(Integer.parseInt(page), 20);
		//List<Product> list = dataService.getData(pageBean, null);
		int totalNum = dataService.count();
		String pageCode=PageUtils.getPagation(request.getContextPath()+"/data/list.do", totalNum, Integer.parseInt(page), 20);
		mav.addObject("mainPage", "dataplatform/list2.jsp");
		mav.setViewName("main");
		mav.addObject("pageCode",pageCode);
		System.out.println(mav.getViewName());
		return mav;
	}
	
	@RequestMapping("/useURL")
	public ModelAndView useURL(@RequestParam(value="page",required=false)String page,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage", "dataplatform/useURLlist.jsp");
		mav.setViewName("main");
		return mav;
	}
	@RequestMapping("/unuseURL")
	public ModelAndView unuseURL(@RequestParam(value="page",required=false)String page,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage", "dataplatform/unuseURLlist.jsp");
		mav.setViewName("main");
		return mav;
	}
	@RequestMapping(value="/getProductData",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getProductJson(HttpServletRequest request) {
		PageBean pageBean = new PageBean(0, 10);
		String page = request.getParameter("page");
		System.out.println("page = " + page);
		List<Product> list = dataService.getData(null, null);
		return JSON.toJSONString(list);
	}
	//·þÎñ¶Ë·ÖÒ³
	@RequestMapping(value="/getProductDataFromService",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String getProductJsonFromSer(HttpServletRequest request) {
		int currentpage = request.getParameter("offset")==null?1:Integer.parseInt(request.getParameter("offset"));
		int showCount = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
		int total = dataService.count();
		PageBean pageBean = new PageBean(currentpage, showCount);
		JSONObject all = new JSONObject();
		JSONArray row = new JSONArray();
		JSONObject o1 = new JSONObject();
		List<Product> list = dataService.getData(pageBean, null);
		for(Product p : list) {
			JSONObject temp = new JSONObject();
			temp.put("id", p.getId());
			temp.put("itemname", p.getItemname());
			temp.put("itemid", p.getItemid());
			temp.put("itemprice", p.getItemprice());
			temp.put("commentCounts", p.getCommentCounts());
			temp.put("itemShop", p.getItemShop());
			row.add(temp);
		}
		
		JSONObject o3 = new JSONObject();
		all.put("rows", row);
		all.put("total", total);
		return JSON.toJSONString(all);
	}
	
	@RequestMapping(value="/test1",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String test() {
		PageBean pageBean = new PageBean(0, 300);
		List<Product> list = dataService.getData(pageBean, null);
		int total = dataService.count();
		JSONObject ojb = new JSONObject();
		JSONArray rows = new JSONArray();
		for(Product p : list) {
			JSONObject temp = new JSONObject();
			temp.put("id", p.getItemname());
			rows.add(temp);
		}
		ojb.put("total", total);
		ojb.put("rows", rows);
		return JSON.toJSONString(ojb);
	}

}
