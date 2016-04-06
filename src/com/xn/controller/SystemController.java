package com.xn.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xn.dao.SystemLog;
import com.xn.model.LogModel;
import com.xn.model.PageBean;
import com.xn.utils.PageUtils;
import com.xn.utils.StringUtils;

@Controller
@RequestMapping("/system")
public class SystemController {
	
	@Resource
	private SystemLog systemLog;
	
	@RequestMapping("/list")
	public ModelAndView list(@RequestParam(value="page",required=false)String page
			,String query,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		if(StringUtils.isEmpty(page)) {
			page = "1";
		}
		PageBean pageBean=new PageBean(Integer.parseInt(page),10);
		List<LogModel> list=systemLog.list(pageBean, query);
		int total=systemLog.count(query);
		String pageCode=PageUtils.getPagation(request.getContextPath()+"/system/list.do", total, Integer.parseInt(page), 10);
		mav.addObject("pageCode", pageCode);
		mav.addObject("list", list);
		mav.addObject("mainPage", "system/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping(value="/delete",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String delete(@RequestParam(value="id")String id,HttpServletResponse response)throws Exception{
		JSONObject result=new JSONObject();
		systemLog.delete(Integer.parseInt(id));
		result.put("success", true);
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value="/moniterData",produces = {"application/json;charset=UTF-8"})
	@ResponseBody
	public String moniterData() {
		JSONObject obj = new JSONObject();
		Runtime r = Runtime.getRuntime();
		Properties props = System.getProperties();
        InetAddress addr = null;
        try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String ip = addr.getHostAddress();
        obj.put("id", ip);
        Map<String, String> map = System.getenv();
        long JVM_mermory = r.totalMemory();
        long JVM_mermory_use = r.freeMemory();
        obj.put("JVM_mermory", JVM_mermory);				//jvm内存总量
        obj.put("JVM_mermory_use", JVM_mermory_use);		//空余的jvm内存量
        
        String os_name = props.getProperty("os.name");
		obj.put("os_name", os_name);						//操作系统名称
		Sigar sigar = new Sigar();
        try {
			Mem mem = sigar.getMem();
			long mermory_all = (mem.getTotal() / 1024L);		//内存总量
			long mermory_use = (mem.getUsed() / 1024L);			//使用内存量
			long mermory_free = (mem.getFree() / 1024L);		//空余内存量
			obj.put("mermory_all", mermory_all);
			obj.put("mermory_use", mermory_use);
			obj.put("mermory_free", mermory_free);
			//cpu 信息
			CpuInfo infos[] = sigar.getCpuInfoList();
	        CpuPerc cpuList[] = null;
	        cpuList = sigar.getCpuPercList();
	        obj.put("cpu_counts", infos.length);
	        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
	            CpuInfo info = infos[i];
	            String cpu_type = info.getModel();// 获得CPU的类别，如：Celeron
	            String cpu_use =  CpuPerc.format(cpuList[i].getCombined());
	            String cpu_free = CpuPerc.format(cpuList[i].getIdle());
	            obj.put("cpu_type"+i, cpu_type);
	            obj.put("cpu_use"+i, cpu_use);
	            obj.put("cpu_free"+i, cpu_free);
	        }
	        
		} catch (SigarException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return obj.toString();
	}
}
