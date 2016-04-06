package com.xn.test;

import com.xn.spider.SpiderSchedule;
import com.xn.utils.RedisScheduler;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;

public class test {

	public static void main(String[] args) {
		SpiderSchedule spider = new SpiderSchedule();
		Spider.create(spider).addUrl("http://www.jd.com")
		.setScheduler(new RedisScheduler("localhost", 6379))
		.addPipeline(new ConsolePipeline())
		.start();
	}

}
