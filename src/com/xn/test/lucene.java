package com.xn.test;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class lucene {

	private String ids[]={"1","2","3"};
	private String citys[]={"qingdao","nanjing","shanghai"};
	private String descs[]={
			"Qingdao is a beautiful city.",
			"Nanjing is a city of culture.",
			"Shanghai is a bustling city."
	};
	private static Directory dir;
	
	private IndexWriter getWriter() throws IOException {
		
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir, iwc);
		return writer;
	}
	
	public void init() {
		try {
			dir = FSDirectory.open(Paths.get("d://lucene2"));
			IndexWriter writer = getWriter();
			for(int i=0;i<ids.length;i++) {
				Document doc = new Document();
				doc.add(new StringField("id", ids[i], Field.Store.YES));
				doc.add(new StringField("city", citys[i], Field.Store.YES));
				doc.add(new TextField("desc", descs[i],Field.Store.NO));
				writer.addDocument(doc);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		lucene l = new lucene();
		l.init();
		try {
			IndexWriter writer = l.getWriter();
			System.out.println(writer.numDocs());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
