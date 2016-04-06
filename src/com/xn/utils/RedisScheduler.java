package com.xn.utils;

import com.xn.spider.SpiderSchedule;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.Scheduler;

public class RedisScheduler implements Scheduler{

	private JedisPool pool;
	
	private static final String QUEUE_PREFIX = "queue_";
	
	private static final String SET_PREFIX = "set_";
	
	public RedisScheduler (String host,int port) {
		pool = new JedisPool(new JedisPoolConfig(), host, port);
	}
	
	@Override
	public void push(Request request, Task task) {
		
		Jedis jedis = pool.getResource();
		//使用SortedSet 进行 url 去重
		if(jedis.zrank(SET_PREFIX+task.getUUID(), request.getUrl())==null) {
			jedis.rpush(QUEUE_PREFIX+task.getUUID(), request.getUrl());
			jedis.zadd(SET_PREFIX+task.getUUID(), System.currentTimeMillis()
					, request.getUrl());
		}
	}

	@Override
	public Request poll(Task task) {
		Jedis jedis = pool.getResource();
		String url = jedis.lpop(QUEUE_PREFIX+task.getUUID());
		if(url==null) {
			return null;
		}
		return new Request(url);
	}
	public void testJedis() {
		System.out.println(pool.getResource().set("url", "hello world"));
		System.out.println("rpush : "+pool.getResource().rpush("task_jd", "www.jd.com"));
		System.out.println("zadd  : "+pool.getResource().zadd("set_jd", System.currentTimeMillis()
				, "www.jd.com"));
		System.out.println("lpop : " + pool.getResource().lpop("task_jd"));
	}
	public static void main(String[] args) {
		SpiderSchedule spider = new SpiderSchedule();
		Spider jdSpider = Spider.create(spider).addUrl("http://www.jd.com/allSort.aspx")
		.thread(20)
		//注释的是用QueueScheduler()，这时候程序没问题，能后获取到数据
		//换成了 RedisScheduler() 的时候就获取不到数据，其他代码都没改动
		//.setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000000)))
		.setScheduler(new RedisScheduler("192.168.12.153", 12002))
		.setDownloader(new HttpClientDownloader())
		.addPipeline(new ConsolePipeline())
		;
		jdSpider.start();
		
	}
	
}
