package com.xn.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xn.model.InterfaceModel;
import com.xn.model.User;
import com.xn.service.InterfaceService;
import com.xn.service.UserService;

@Controller
@RequestMapping("/inter")
public class InterfaceController {

	@Resource
	private InterfaceService interfaceService;
	@Resource
	private UserService userService;
	
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		List<InterfaceModel> list=interfaceService.list();
		mav.addObject("list", list);
		mav.addObject("mainPage", "interface/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	@RequestMapping(value="/register",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String register(@RequestParam(value="id",required=false)String id, HttpServletRequest request) {
		boolean open = interfaceService.open(id);
		if(!open) {
			return "注册失败，请联系接口开发者"; 
		}
		String username = request.getParameter("name");
		User user = userService.loadByUserName(username);
		if(user.getUserName() != null) {
			return "注册失败,账号已存在";
		}
		
		String password = request.getParameter("password");
		String address = request.getParameter("address");
		String tell = request.getParameter("tell");
		String role = request.getParameter("role");
		if(role==null) {
			role="查看者";
		}
		String bfm = request.getParameter("bfm");
		user.setUserName(username);
		user.setAddress(address);
		user.setPassWord(password);
		user.setRole(role);
		user.setTellPhone(tell);
		userService.add(user);//保存到数据库中
		return "注册成功";
	}
	
	@RequestMapping(value="/btn",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String btn(@RequestParam(value="id")String id,HttpServletRequest request) {
		//1 代表开， 0 代表关
		JSONObject result = new JSONObject();
		String sw = request.getParameter("sw");
		interfaceService.btn(sw, id);
		result.put("success", true);
		return JSON.toJSONString(result);
	}
}
