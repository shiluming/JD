package com.xn.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xn.service.DataAnalyseService;

@Controller
@RequestMapping("/analyse")
public class DataAnalyseController {

	@Resource
	private DataAnalyseService dataAnalyseService;

	@RequestMapping(value="/getpriceAnay",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String priceAnalyse() {
		int[] data = dataAnalyseService.priceAnalyse();
		JSONObject object = new JSONObject();
		for(int i=0;i<data.length;i++) {
			object.put("s"+i, data[i]);
		}
		return object.toJSONString();
	}
	
	@RequestMapping(value="/getFetchAnay",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String fetchAnalyse() {
		
		return null;
	}

}
