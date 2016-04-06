package com.xn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.xn.spider.SpiderSchedule;
import com.xn.utils.RedisScheduler;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;

@Controller
@RequestMapping("/spider")
public class SpiderController {

	
	
	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView mav = new ModelAndView();
		mav.addObject("mainPage", "spider/list.jsp");
		mav.setViewName("main");
		return mav;
	}
	
	@RequestMapping("/start")
	public ModelAndView start() {
		ModelAndView mav = new ModelAndView();
		//JedisPool pool = new JedisPool(new JedisPoolConfig(),"192.168.12.153", 12002);
		//System.out.println("####"+pool);
		SpiderSchedule spider = new SpiderSchedule();
		Spider jdSpider = Spider.create(spider).addUrl("http://list.jd.com/list.html?cat=737%2C794%2C798&go=0")
		.thread(20)
		//.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
		.setScheduler(new RedisScheduler("192.168.12.153", 12002))
		.setDownloader(new HttpClientDownloader())
		.addPipeline(new ConsolePipeline())
		;
		jdSpider.start();
		
		mav.addObject("mainPage","spider/list.jsp");
		mav.addObject("info","正在后台运行中...");
		mav.setViewName("main");
		return mav;
	}
}
