package com.xn.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xn.model.LogModel;
import com.xn.model.PageBean;
import com.xn.model.User;
import com.xn.service.InterfaceService;
import com.xn.service.SystemLogService;
import com.xn.service.UserService;
import com.xn.utils.PageUtils;
import com.xn.utils.StringUtils;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private InterfaceService interfaceService;
	
	@Resource
	private UserService userService;
	
	@Resource
	private SystemLogService systemLogService;
	
	@RequestMapping("/more")
	public ModelAndView more() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage","common/introduce.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/login")
	public String login(User user, HttpServletRequest request) {
		final LogModel lModel = new LogModel();
		User resultUser=userService.login(user);
		lModel.setIp(request.getRemoteAddr());
		//String address = getAddressByIP(request.getRemoteAddr());
		String address = "11";
		if(address.startsWith("目前")) {
			lModel.setAddress("localhost");
		} else {
			//lModel.setAddress(address);
		}
		lModel.setUsername(user.getUserName());
		lModel.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		lModel.setType("pc");
		if(resultUser.getId()==null){
			request.setAttribute("user", user);
			lModel.setOthers("登录失败");
			request.setAttribute("errorMsg", "用户名或密码错误！");
			systemLogService.add(lModel);
			return "login";
		}else{
			HttpSession session=request.getSession();
			lModel.setOthers("登录成功");
			session.setAttribute("currentUser", resultUser);
			systemLogService.add(lModel);
			return "redirect:/main.jsp";
		}
	}
	
	@RequestMapping(value="/loginForApp",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String loginForApp(HttpServletRequest request) {
		String id = request.getParameter("id");
		boolean open = interfaceService.open(id);
		if(!open) {
			return "登录失败，接口已关闭"; 
		}
		String username = request.getParameter("name");
		String password = request.getParameter("pwd");
		LogModel logModel = new LogModel();
		User user = new User();
		user.setUserName(username);
		user.setPassWord(password);
		User resultUser = userService.login(user);
		logModel.setUsername(username);
		logModel.setType("APP");
		logModel.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
		if(resultUser.getId()!=null) {
			logModel.setOthers("登录成功");
			systemLogService.add(logModel);
			return "登录成功";
		} else {
			logModel.setOthers("登录失败");
			systemLogService.add(logModel);
			return "登录失败";
		}
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.invalidate();
		return "redirect:/login.jsp";
	}
	
	@RequestMapping(value="/test")
	@ResponseBody
	public String test() {
		JSON o = new JSONObject();
		Map<String, String> m = new HashMap<>();
		m.put("test", "hello");
		return JSON.toJSONString(m);
	}
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page
			,User s_user,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		HttpSession session=request.getSession();
		if(StringUtils.isEmpty(page)){
			page="1";
			session.setAttribute("s_user", s_user);
		}else{
			s_user=(User) session.getAttribute("s_user");
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),10);
		List<User> userList=userService.find(pageBean, s_user);
		int total=userService.count(s_user);
		String pageCode=PageUtils.getPagation(request.getContextPath()+"/user/list.do", total, Integer.parseInt(page), 5);
		mav.addObject("pageCode", pageCode);
		mav.addObject("userList", userList);
		mav.addObject("mainPage", "user/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/preSave")
	public ModelAndView preSave(@RequestParam(value="id",required=false)String id){
		ModelAndView mav=new ModelAndView();
		mav.addObject("mainPage", "user/save.jsp");
		mav.addObject("modeName", "用户管理");
		mav.setViewName("main");
		if(StringUtils.isNotEmpty(id)){
			mav.addObject("actionName", "用户修改");
			User user=userService.loadById(Integer.parseInt(id));
			mav.addObject("user", user);
		}else{
			mav.addObject("actionName", "用户添加");			
		}
		return mav;
	}
	
	@RequestMapping("/save")
	public String save(User user){
		if(user.getId()==null){
			userService.add(user);			
		}else{
			userService.update(user);
		}
		return "redirect:/user/list.do";
	}
	
	@RequestMapping(value="/delete",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		userService.delete(Integer.parseInt(id));
		result.put("success", true);
		return JSON.toJSONString(result);
	}
	
	public String getAddressByIP(String ip)
	{ 
	  try
	  {
	    URL url = new URL( "http://ip.qq.com/cgi-bin/searchip?searchip1=" + ip); 
	    URLConnection conn = url.openConnection(); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK")); 
	    String line = null; 
	    StringBuffer result = new StringBuffer(); 
	    while((line = reader.readLine()) != null)
	    { 
	      result.append(line); 
	    } 
	    reader.close(); 
	    ip = result.substring(result.indexOf( "该IP所在地为：" ));
	    ip = ip.substring(ip.indexOf( "：") + 1);
	    String province = ip.substring(6, ip.indexOf("省"));
	    String city = ip.substring(ip.indexOf("省") + 1, ip.indexOf("市"));
	    System.out.println(province + " " + city);
	    return province+" " + city;
	  }
	  catch( IOException e)
	  { 
	    return "读取失败"; 
	  }
	  
	}
}
