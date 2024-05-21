package com.icuscn.passerby.document;

import com.icuscn.passerby.common.model.Document;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * 文档业务
 */
public class DocumentService {

	private Document dao = new Document().dao();

	private final String docCacheName = "doc";
	private final String menuCacheKey = "menu";

	// 加载一级文档，只加载 publish 为发布的
	public List<Document> getMenu() {
		List<Document> docList = CacheKit.get(docCacheName, menuCacheKey);
		if (docList == null) {
			String sql = "select mainMenu, subMenu, title from document where subMenu = 0 and publish=? order by mainMenu asc";
			docList = dao.find(sql, Document.PUBLISH_YES);
			for (Document pDoc : docList) {
				loadSubMenu(pDoc);
			}
			CacheKit.put(docCacheName, menuCacheKey, docList);
		}
		return docList;
	}

	// 加载二级文档，只加载 publish 为发布的
	private void loadSubMenu(Document pDoc) {
		int mainMenu = pDoc.getMainMenu();
		String sql = "select mainMenu, subMenu, title from document where mainMenu=? and subMenu>0 and publish=? order by subMenu asc";
		List<Document> subDocList = dao.find(sql, mainMenu, Document.PUBLISH_YES);
		pDoc.put("subMenuList", subDocList);
	}

	public Document findById(int mainMenu, int subMenu) {
		// 暂不支持主菜单 doc 的显示，主菜单 doc 现在仅用于我自己来 todolist 和大纲
		if (subMenu == 0) {
			return null;
		}

		String cacheKey = buildCacheKey(mainMenu, subMenu);
		Document doc = CacheKit.get(docCacheName, cacheKey);
		if (doc == null) {
			String sql = "select * from document where mainMenu=? and subMenu=? and publish=? limit 1";
			doc = dao.findFirst(sql, mainMenu, subMenu, Document.PUBLISH_YES);
			getPreviousAndNext(doc);
			CacheKit.put(docCacheName, cacheKey, doc);
		}
		return doc;
	}
	
	/**
	 * 获取当前文档的上一小节与下一小节，用于生成文档下方的链接
	 */
	private void getPreviousAndNext(Document doc) {
		if (doc != null) {
			getPrevious(doc);
			getNext(doc);
		}
	}
	
	private void getPrevious(Document doc) {
		int mainMenu = doc.getMainMenu();
		int subMenu = doc.getSubMenu();
		
		String sql = "select mainMenu, subMenu, title from document where "
					+ "mainMenu = ? and subMenu < ? and publish = ? and subMenu != 0 order by subMenu desc limit 1";
		Document previous = dao.findFirst(sql, mainMenu, subMenu, Document.PUBLISH_YES);
		if (previous == null) {
			mainMenu--;
			sql = "select mainMenu, subMenu, title from document where "
				+ "mainMenu = ? and publish = ? and subMenu != 0 order by subMenu desc limit 1";
			previous = dao.findFirst(sql, mainMenu, Document.PUBLISH_YES);
		}
		
		doc.put("previous", previous);
	}
	
	private void getNext(Document doc) {
		int mainMenu = doc.getMainMenu();
		int subMenu = doc.getSubMenu();
		
		String sql = "select mainMenu, subMenu, title from document where "
				+ "mainMenu = ? and subMenu > ? and publish = ? and subMenu != 0 order by subMenu asc limit 1";
		Document next = dao.findFirst(sql, mainMenu, subMenu, Document.PUBLISH_YES);
		if (next == null) {
			mainMenu++;
			sql = "select mainMenu, subMenu, title from document where "
				+ "mainMenu = ? and publish = ? and subMenu != 0 order by subMenu asc limit 1";
			next = dao.findFirst(sql, mainMenu, Document.PUBLISH_YES);
		}
		
		doc.put("next", next);
	}

	private String buildCacheKey(int mainMenu, int subMenu) {
		return mainMenu + "_" + subMenu;
	}

	public void clearCache() {
		CacheKit.removeAll(docCacheName);
	}
}


