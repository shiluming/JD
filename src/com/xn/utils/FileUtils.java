package com.xn.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 文件工具类
 * @author Administrator
 *	2016年2月20日
 */
public class FileUtils {

	public static void main(String[] args) {
		String path = "c:\\01\\11.log";
		String dirPath = "c:\\02";
		createDir("c:\\01");
		createFile(path);
	}
	
	public static void writer(String str,String filePath) {
		Path file = Paths.get(filePath);
		BufferedWriter writer = null;
		try {
			writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8
					,StandardOpenOption.WRITE
					,StandardOpenOption.APPEND);
			writer.append(str+"\r\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * 创建文件
	 * @param path
	 */
	public static void createFile(String path) {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * 创建文件夹
	 * @param dirPath
	 */
	public static void createDir(String dirPath) {
		File file = new File(dirPath);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
}
