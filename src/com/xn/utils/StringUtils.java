package com.xn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 根据正则判断字符串是否匹配
	 * @param reg
	 * @param str
	 * @return
	 */
	public static boolean isMatches(String reg, String str) {
		return str.matches(reg);
	}
	/**
	 * 根据正则匹配返回的字符串
	 * @param reg
	 * @param str
	 * @return
	 */
	public static String getMatches(String reg, String str) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(str);
		if(m.find()) 
			return m.group();
		else 
			return null;
	}
	/**
	 * 利用正则去掉不必要的url，只获取自己想要的url去爬取
	 * @param url
	 * @return
	 */
	public static String BloomDuplic(String url) {
		final String reg = "http://(?!search)\\w+\\.jd\\.com(?:[^\\n]+)?";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(url);
		if(m.find()) {
			return url;
		}
		return "";
	}
	
	public static boolean isEmpty(String str) {
		if(str==null || "".equals(str) || str.equals(""))
			return true;
		return false;
			
	}
	
	public static boolean isNotEmpty(String str){
		if((str!=null)&&!"".equals(str.trim())){
			return true;
		}else{
			return false;
		}
	}
}
