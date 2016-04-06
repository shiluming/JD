package com.xn.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * ���۴�����
 * @author Administrator
 *
 */
public class CommentsDeal {

	private static Logger logger = Logger.getLogger(CommentsDeal.class);
	
	public CommentsDeal() {
		
	}
	
	public static List<String> dealWithURLJson(String json) {
		List<String> arrayList = new ArrayList<>();
		if(json.isEmpty()) {
			logger.info("URL �� JSON Ϊ��ֵ������ʧ�ܣ���");
			return null;
		}
		String reg = "(?<=\\().*(?=\\))";
		String reJson = StringUtils.getMatches(reg, json);
		JSONObject object = JSONObject.parseObject(reJson);
		JSONArray commentsArray = (JSONArray) object.get("comments");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<commentsArray.size();i++) {
			arrayList.add(commentsArray.get(i).toString());
		}
		return arrayList;
	}
}
