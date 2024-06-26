package com.icuscn.passerby._admin.document;

import com.icuscn.passerby.common.model.Document;
import com.icuscn.passerby.document.DocumentService;
import com.jfinal.aop.Inject;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;

import java.util.Date;
import java.util.List;

/**
 * document 管理业务
 */
public class DocumentAdminService  {
	
	@Inject
    DocumentService documentSrv;
	
	private Document dao = new Document().dao();

	// 加载一级文档，即便是 publish 为 0 的也加载
	public List<Document> getDocList() {
		List<Document> docList = dao.find("select * from document where subMenu = 0 order by mainMenu asc");
		for (Document pDoc : docList) {
			loadSubDocList(pDoc);
		}
		return docList;
	}

	// 加载二级文档，文档最多分两级目录，三级甚至更多级目录直接在 content 中体现
	private void loadSubDocList(Document pDoc) {
		int mainMenu = pDoc.getMainMenu();
		String sql = "select * from document where mainMenu = ? and subMenu > 0 order by subMenu asc";
		List<Document> subDocList = dao.find(sql, mainMenu);
		pDoc.put("subDocList", subDocList);
	}

	public Document getByIds(int mainMenu, int subMenu) {
		return dao.findByIds(mainMenu, subMenu);
	}

	public Ret save(Document doc) {
		if (isExists(doc)) {
			return Ret.fail("msg", "mainMenu 与 subMenu 组合已经存在");
		}
		doc.setCreateAt(new Date());
		doc.setUpdateAt(new Date());
		doc.save();
		documentSrv.clearCache();    // 清缓存
		return Ret.ok();
	}

	public Ret update(int oldMainMenu, int oldSubMenu, Document doc) {
		// 当 mainMenu 或 subMenu 值也被修改的时候，判断一下新值是否已经存在
		if (oldMainMenu != doc.getMainMenu() || oldSubMenu != doc.getSubMenu()) {
			if (isExists(doc)) {
				return Ret.fail("msg", "mainMenu 或 subMenu 已经存在，不能使用");
			}
		}

		if (oldMainMenu != doc.getMainMenu() || oldSubMenu != doc.getSubMenu()) {
			Db.update("update document set mainMenu=?, subMenu=? where mainMenu=? and subMenu=?",
						doc.getMainMenu(), doc.getSubMenu(), oldMainMenu, oldSubMenu);
		}
		doc.setUpdateAt(new Date());
		doc.update();

		documentSrv.clearCache();    // 清缓存
		return Ret.ok();
	}

	public Ret delete(int mainMenu, int subMenu) {
		Db.update("delete from document where mainMenu=? and subMenu=? limit 1", mainMenu, subMenu);
		documentSrv.clearCache();    // 清缓存

		return Ret.ok("msg", "document 删除成功");
	}

	private boolean isExists(Document doc) {
		String sql = "select mainMenu from document where mainMenu=? and subMenu=? limit 1";
		return Db.queryInt(sql , doc.getMainMenu(), doc.getSubMenu()) != null;
	}

	final String publishSql = "update document set publish = ? where mainMenu=? and subMenu=?";

	/**
	 * 发布
	 */
	public Ret publish(int mainMenu, int subMenu) {
		Db.update(publishSql, Document.PUBLISH_YES, mainMenu, subMenu);
		documentSrv.clearCache();    // 清缓存
		return Ret.ok("msg", "发布成功");
	}

	/**
	 * 取消发布，变草稿
	 */
	public Ret unpublish(int mainMenu, int subMenu) {
		Db.update(publishSql, Document.PUBLISH_NO, mainMenu, subMenu);
		documentSrv.clearCache();    // 清缓存
		return Ret.ok("msg", "取消发布成功");
	}
}
