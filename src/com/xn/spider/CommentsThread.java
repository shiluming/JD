package com.xn.spider;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.xn.utils.CommentsDeal;
import com.xn.utils.FileUtils;
import com.xn.utils.StringUtils;

/**
 * ���ۻ�ȡ  ���߳��Ѿ������   jd�Ǳ߻�ȡ���������������ƣ�����ÿ10ҳ�����������Ƶȴ���ʱ��
 * <p>Description: </p>
 *
 * @author ����¥��
 * @date 2016��4��13��
 */
public class CommentsThread extends Thread {

	private Logger logger = Logger.getLogger(CommentsThread.class);
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private String url;
	private int counts;
	private String itemid;
	private String path;
	
	public CommentsThread(CloseableHttpClient httpClient,String url, int counts,String itemid,String path) {
		//this.httpClient = httpClient;
		this.url = url;
		this.counts = counts%10==0?counts/10:counts/10+1;
		this.itemid = itemid;
		this.path = path;
	}
	@Override
	public void run() {
		HttpResponse response = null;
		
		System.out.println("=======44444444444444444444======"+counts);
		for(int i=0;i<counts;i++) {
			String tempUrl = url.replace("||", String.valueOf(i));
			String resultUrl = tempUrl.replace("%**%", itemid);
			HttpGet get = new HttpGet(resultUrl);
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
			get.addHeader("Cookie","user-key=edf9c4ed-834d-4dc2-87f3-e10c8369237a; cn=0; __jdv=122270672|direct|-|none|-; unpl=V2_ZzNtbUtSRBBxChIDch1VBGJRRQhLVhYUJwkVBigZCwBhABoJclRCFXIUR1FnGV4UZwYZWEJcQhVFCHZUSxBdDGEAEG1DVktGfFpEUH9OWgwwAxdbElMRRUULR2RzKV8EZgMaXEtRRRdFOEFkSyldNWYzE20PVEIUdQBHXX0fXjVm; mt_subsite=||70%2C1460529661; areaId=1; __jda=122270672.2001908773.1453121338.1460529855.1460541540.8; __jdc=122270672; ipLoc-djd=1-72-4137-0; ipLocation=%u5317%u4EAC; __jdu=2001908773");
			try {
				
				response = httpClient.execute(get);
				int state = response.getStatusLine().getStatusCode();
				String temp = "";
				
				HttpEntity entity = response.getEntity();
				
				temp = EntityUtils.toString(entity);
				get.releaseConnection();//�ͷ�����
				int counts = 0;
				while(temp.isEmpty() && counts <= 20) {
					System.out.println("=========temp============Ϊ��  "+ i + " ��ʼ����ִ��,������"+ counts);
					sleep(3000);
					response = httpClient.execute(get);
					entity = response.getEntity();
					temp = EntityUtils.toString(entity);
					get.releaseConnection();
					counts++;
				}
				List<String> list = CommentsDeal.dealWithURLJson(temp);
				//д�����ļ��Ĵ���
				for(String s : list) {
					logger.info(""+s);
					FileUtils.writer(s, path);
				}
					
				logger.info(Thread.currentThread().getName() + " ��ʼ ץȡ "+i+" ҳ���ݣ� �õ����ַ�����   "+temp.length() + "  state: "+ state +"+ +"+resultUrl);
			
				
				
				
			
				
			} catch (IOException  e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				
			}
			
			
		}
			
		
		
		try {
				httpClient.close();
				
				logger.info("  HttpClient ������Դ���ѹرգ� �̣߳� "+Thread.currentThread().getName()+" ����....");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
