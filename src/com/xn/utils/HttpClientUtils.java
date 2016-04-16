package com.xn.utils;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

public class HttpClientUtils {

	private PoolingHttpClientConnectionManager pools;
	
	private CloseableHttpClient http;
	/**
	 * 初始化方法   配置一些连接池的配置
	 * 里面好多类不知道是什么东东， 就知道是用来配置连接池的，基本都是在网上找的
	 */
	private void init() {
		SSLContext sslContext=null;   
		try {
			sslContext = SSLContexts.custom().useTLS().build();
            
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();
		pools = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
		SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
        pools.setDefaultSocketConfig(socketConfig); 
		MessageConstraints messageConstraints = MessageConstraints.custom()
	                .build();
		 ConnectionConfig connectionConfig = ConnectionConfig.custom()
	                .setMalformedInputAction(CodingErrorAction.IGNORE)
	                .setUnmappableInputAction(CodingErrorAction.IGNORE)
	                .setCharset(Consts.UTF_8)
	                .setMessageConstraints(messageConstraints)
	                .build();
		pools.setDefaultConnectionConfig(connectionConfig);
		pools.setMaxTotal(200);
		pools.setDefaultMaxPerRoute(20);
	}
	//从连接池获取连接的方法  
	public CloseableHttpClient getHttpClient() {
		init();
		http = HttpClients.custom().setConnectionManager(pools).build();
		return http;
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
	}
	
} 
/**
 * 获取评论数据的线程
 * @author Administrator
 *
 */
class CommentsThread extends Thread {
	
	private CloseableHttpClient httpClient;
	private String url;
	private int counts;
	private String itemid;
	
	public CommentsThread(CloseableHttpClient httpClient,String url, int counts,String itemid) {
		this.httpClient = httpClient;
		this.url = url;
		this.counts = counts%10==0?counts/10:counts/10+1;
		this.itemid = itemid;
	}
	@Override
	public void run() {
		CloseableHttpResponse response = null;
		
		for(int i=0;i<counts;i++) {
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
					temp = EntityUtils.toString(response.getEntity());
					
					System.out.println(Thread.currentThread().getName() + " 开始 抓取 "+i+" 页数据， 拿到了字符串：   "+temp.length() + "  state: "+ state +"+ +"+resultUrl);
				} else {
					System.out.println("==");
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
				
			}
		}
		try {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}