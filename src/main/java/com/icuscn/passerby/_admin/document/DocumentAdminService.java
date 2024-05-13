/**
 * 请勿将俱乐部专享资源复制给任何人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目源码、直播视频、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多，俱乐部福利资源是不断增加的，以下是俱乐部
 * 新福利计划：
 * https://jfinal.com/club/1-2
 * 
 * JFinal 俱乐部是七年以来首次寻求外部资源的尝试，以便于创建更加高品质的产品与服务，为你带来
 * 更多、更大的价值
 * 
 * JFinal 项目的可持续性发展需要你的支持！！！
 */

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
