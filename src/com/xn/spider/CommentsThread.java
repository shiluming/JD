package com.xn.spider;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.xn.utils.CommentsDeal;
import com.xn.utils.FileUtils;

public class CommentsThread extends Thread {

	private Logger logger = Logger.getLogger(CommentsThread.class);
	private CloseableHttpClient httpClient;
	private String url;
	private int counts;
	private String itemid;
	private String path;
	
	public CommentsThread(CloseableHttpClient httpClient,String url, int counts,String itemid,String path) {
		this.httpClient = httpClient;
		this.url = url;
		this.counts = counts%10==0?counts/10:counts/10+1;
		this.itemid = itemid;
		this.path = path;
	}
	@Override
	public void run() {
		CloseableHttpResponse response = null;
		
		for(int i=1;i<=counts;i++) {
			String tempUrl = url.replace("||", String.valueOf(i));
			String resultUrl = tempUrl.replace("%**%", itemid);
			HttpGet get = new HttpGet(resultUrl);
			try {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				response = httpClient.execute(get);
				int state = response.getStatusLine().getStatusCode();
				String temp = "";
				if(state==200) {
					HttpEntity entity = response.getEntity();
					//temp = EntityUtils.toString(response.getEntity());
					temp = EntityUtils.toString(entity);
					//CommentsDaoForThread commentdao = new CommentsDaoForThread();
					List<String> list = CommentsDeal.dealWithURLJson(temp);
					//写存入文件的代码
					for(String s : list) {
						logger.info(""+s);
						FileUtils.writer(s, path);
					}
					//commentdao.addComments(contents, itemid);
					//System.out.println(CommentsDeal.dealWithURLJson(temp));
					logger.info(Thread.currentThread().getName() + " 开始 抓取 "+i+" 页数据， 拿到了字符串：   "+temp.length() + "  state: "+ state +"+ +"+resultUrl);
				} 
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (IOException  e) {
				e.printStackTrace();
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
			try {
				httpClient.close();
				response.close();
				logger.info("  HttpClient 工具资源都已关闭， 线程： "+Thread.currentThread().getName()+" 结束....");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
