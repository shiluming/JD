package com.xn.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xn.dao.DataDao;
import com.xn.dao.IndexDao;
import com.xn.model.IndexModel;
import com.xn.model.PageBean;
import com.xn.model.Product;
import com.xn.service.IndexService;

@Service("indexService")
public class IndexServiceImpl implements IndexService{

	private Logger logger = Logger.getLogger(IndexServiceImpl.class);
	private Directory dir;
	@Resource
	private DataDao dataDao;
	@Resource
	private IndexDao indexDao;
	
	@Override
	public String createIndex() {
		List<Product> list = dataDao.getData(null, null);
		long start = System.currentTimeMillis();
		try {
			IndexModel iModel = new IndexModel();
			dir = FSDirectory.open(Paths.get("lucene2"));
			iModel.setIndexDir("lucene2");
			iModel.setCreateTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
			iModel.setBuckup(false);
			iModel.setCurrent(true);
			IndexWriter writer = getWriter();
			for(Product p : list ) {
				Document doc = new Document();
				doc.add(new StringField("id", String.valueOf(p.getId()), Field.Store.YES));
				doc.add(new StringField("itemid", p.getItemid(), Field.Store.YES));
				doc.add(new TextField("itemname", p.getItemname(), Field.Store.YES));
				doc.add(new StringField("itemprice", p.getItemprice(), Field.Store.YES));
				doc.add(new StringField("commentCounts", p.getCommentCounts(), Field.Store.YES));
				doc.add(new StringField("itemShop", p.getItemShop(), Field.Store.YES));
				doc.add(new StringField("url", p.getOthers(), Field.Store.YES));
				writer.addDocument(doc);
			}
			iModel.setIndexNum(writer.numDocs());
			iModel.setSpareTime(System.currentTimeMillis()-start+" 毫秒");
			logger.info(" 成功创建 : "+writer.numDocs() +" 条商品索引 , 总共花费了 ： " + (System.currentTimeMillis()-start) + " 毫秒");
			
			writer.close();
			indexDao.add(iModel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			logger.info(" 创建 商品索引失败 !!");
			return "失败";
		} finally{
			
		}
		return "创建成功";
	}
	
	private IndexWriter getWriter() throws IOException {
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();//中文分词器
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir, iwc);
		return writer;
	}

	@Override
	public void addIndex(IndexModel iModel) {
		
	}

	@Override
	public int count() {
		return indexDao.count();
	}

	@Override
	public List<IndexModel> getData(PageBean pageBean,String current) {
		return indexDao.getData(pageBean,current);
	}

	@Override
	public List<Product> searchForObject(String searchField,String query) {
		IndexReader reader = null;
		String json = null;
		List<Product> all = new ArrayList<>();
		try {
			dir = FSDirectory.open(Paths.get("lucene2"));
			reader = DirectoryReader.open(dir);
			IndexSearcher iSearch = new IndexSearcher(reader);
			Term t = new Term(searchField,query);
			Query q = new TermQuery(t);
			TopDocs hits_ = iSearch.search(q, 100);
			SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();//中文分词器
			//2016年4月2日16 加的代码
			QueryParser parser = new QueryParser(searchField, analyzer);
			Query query_ = parser.parse(query);
			TopDocs hits = iSearch.search(query_, 100);
			//end 
			//
			QueryScorer scorer = new QueryScorer(query_);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			SimpleHTMLFormatter simpleHTMLFormater = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
			Highlighter higlighter = new Highlighter(simpleHTMLFormater,scorer);
			higlighter.setTextFragmenter(fragmenter);
			//
			for(ScoreDoc scoreDoc : hits.scoreDocs) {
				Document d = iSearch.doc(scoreDoc.doc);
				Product obj = new Product();
				obj.setId(Integer.parseInt(d.get("id")));
				obj.setItemname(d.get("itemname"));
				String temp = d.get("itemname");
				if(temp!=null) {
					TokenStream tokenStream = analyzer.tokenStream("primaryname", new StringReader(temp));
					String primaryname = higlighter.getBestFragment(tokenStream, temp);
					obj.setOthers(primaryname);
				}
				obj.setComments(d.get("url"));
				obj.setItemprice(d.get("itemprice"));
				obj.setCommentCounts(d.get("commentCounts"));
				obj.setItemShop(d.get("itemShop"));
				all.add(obj);
			}
		} catch (IOException | InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return all;
	}
	/**
	 * 搜索返回的是字符串
	 */
	@Override
	public String search(String searchField,String query) {
		IndexReader reader = null;
		String json = null;
		try {
			//dir = FSDirectory.open(Paths.get("d://lucene2"));
			dir = FSDirectory.open(Paths.get("lucene2"));
			reader = DirectoryReader.open(dir);
			IndexSearcher iSearch = new IndexSearcher(reader);
			Term t = new Term(searchField,query);
			Query q = new TermQuery(t);
			TopDocs hits = iSearch.search(q, 100);//只查询前面一百条数据
			//
			QueryScorer scorer = new QueryScorer(q);
			Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
			SimpleHTMLFormatter simpleHTMLFormater = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
			Highlighter higlighter = new Highlighter(simpleHTMLFormater,scorer);
			higlighter.setTextFragmenter(fragmenter);
			//
			JSONArray all = new JSONArray();
			SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();//中文分词器
			for(ScoreDoc scoreDoc : hits.scoreDocs) {
				Document d = iSearch.doc(scoreDoc.doc);
				JSONObject obj = new JSONObject();
				obj.put("id", d.get("id"));
				obj.put("name", d.get("itemname"));
				String temp = d.get("itemname");
				if(temp!=null) {
					TokenStream tokenStream = analyzer.tokenStream("primaryname", new StringReader(temp));
					String primaryname = higlighter.getBestFragment(tokenStream, temp);
					obj.put("primayname", primaryname);
					System.out.println(primaryname);
				}
				obj.put("url",d.get("url"));
				obj.put("price", d.get("itemprice"));
				obj.put("counts", d.get("commentCounts"));
				obj.put("shop", d.get("itemShop"));
				all.add(obj);
			}
			json = JSON.toJSONString(all);
		} catch (IOException | InvalidTokenOffsetsException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
		
	}

	@Override
	public String deleteIndex() {
		File file = new File("lucene2");
		File[] fileList = file.listFiles();
		if(fileList.length>0) {
			for(int i=0;i<fileList.length;i++) {
				fileList[i].delete();
			}
			return "删除索引成功";
		} else {
			return "没有索引，删除失败";
		}
	}

	@Override
	public void changeFlag(String id) {
		indexDao.changeFlag(id);
		
	}
}
