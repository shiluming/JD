package com.xn.test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xn.utils.DBPools;
import com.xn.utils.FileUtils;
import com.xn.utils.StringUtils;

import redis.clients.jedis.Jedis;

public class Test2 {

	public static void main(String[] args) {
		
		
		Test2 t = new Test2();
		t.test6();
		
	}
	public void test6() {
		Jedis jedis = new Jedis("localhost");
		System.out.println(jedis.get("name"));
	}
	
	public void test5()  {
		Path file = Paths.get("c:\\01\\m.log");
		Path temp = Paths.get("c:\\01");
		try {
			BufferedWriter bf = Files.newBufferedWriter(file, StandardCharsets.UTF_8
					,StandardOpenOption.WRITE);
			bf.write("hello world"+"\r\n");
			bf.write("shiluming");
			bf.write("wozaigz");
			
			bf.close();
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Files.createFile(file);
		} catch (IOException e) {
			
		}
	}
	
	public void test4() {
		String str = "{"
					+ "\"hello\":\"world\","
					+ "\"employees\":["
					+ "{\"firstName\":\"John\",\"lastname\":\"Doe\"},"
					+ "{\"firstName\":\"Aphe\",\"lastname\":\"Soe\"},"
					+ "{\"firstName\":\"Pery\",\"lastname\":\"Joe\"}"
					+ "]"
					+"}";
		
		System.out.println(str);
		JSONObject o = JSONObject.parseObject(str);
		for(String s: o.keySet()) {
			System.out.println(s);
		}
		System.out.println(o.get("hello").toString());
		System.out.println(o.get("employees").toString());
		JSONArray object =  (JSONArray) o.get("employees");
		System.out.println(object.size());
		for(int i=0;i<3;i++) {
			System.out.println(object.get(i).toString());
		}
		
	}
	
	public void test3(String itemid) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		int conts = 24313;
		int index = conts%10==0?conts/10:conts/10+1;
		for(int i=0;i<index;i++) {
			
		}
		HttpGet get = new HttpGet("http://club.jd.com/productpage/p-"+itemid+"-s-0-t-3-p-"+index+".html?callback=fetchJSON_comment98vv52023");
		System.out.println(index);
		try {
			HttpResponse response = httpClient.execute(get);
			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(entity);
			String reg = "(?<=\\().*(?=\\))";
			String reJson = StringUtils.getMatches(reg, json);
			
			System.out.println(reJson);
			
			JSONObject object = JSONObject.parseObject(reJson);
			for(String s: object.keySet()) {
				System.out.println(s);
			}
			JSONArray commentsArray = (JSONArray) object.get("comments");
			System.out.println("===========================");
			for(int i=0;i<commentsArray.size();i++) {
				System.out.println(commentsArray.get(i).toString());
			}
			
 		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			try {
				httpClient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void test1() {
		ExecutorService threadPools = Executors.newFixedThreadPool(10);
		DBPools pools = new DBPools();
		Connection con1 = pools.getConnection();
		Connection con2 = pools.getConnection();
		
		String sql = "select count(*) from tb_iteminfo";
		String sql2 = "select itemid from tb_iteminfo";
		
		try {
			PreparedStatement pst = con1.prepareStatement(sql);
			PreparedStatement pst2 = con2.prepareStatement(sql2);
			ResultSet rs = pst.executeQuery();
			int itemid=0;
			if(rs.first()) {
				itemid = Integer.parseInt(rs.getString(1));
				System.out.println(itemid);
			}
			
			ResultSet rs2 = pst2.executeQuery();
			while(rs2.next()) {
				String index = rs2.getString(2);
				Comments content = new Comments(Integer.parseInt(index),itemid);
				threadPools.execute(content);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	class Comments extends Thread{
		
		private int itemid;
		private int commentcounts;
		
		public Comments(int counts,int itemid) {
			this.itemid = counts;
			this.itemid = itemid;
		}
		public Comments() {
		}
		
		@Override
		public void run() {
			DBPools pools = new DBPools();
			int total = commentcounts%10==0?commentcounts/10:commentcounts/10+1;
			String url = "http://club.jd.com/productpage/p-"+itemid+"-s-0-t-3-p-"+1+".html?callback=fetchJSON_comment98vv52023";
			Connection con = pools.getConnection();
			String sql = "insert into tb_comments(itemid,commentcontents) values(?,?)";
			StringBuffer sb = new StringBuffer();
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet get = new HttpGet("");
			try {
				PreparedStatement pst = con.prepareStatement(sql);
				
				ResultSet rs = pst.executeQuery();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
