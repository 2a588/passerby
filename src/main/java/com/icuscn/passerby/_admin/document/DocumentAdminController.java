package com.icuscn.passerby._admin.document;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.model.Document;
import com.jfinal.aop.Inject;
import com.icuscn.passerby._admin.permission.Remark;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;

import java.util.List;

/**
 * 文档管理控制器
 * 暂不支持主菜单 doc 的显示，主菜单 doc 现在仅用于我自己来 todolist 和大纲
 */
@Path(value = "/admin/doc", viewPath = "/document")
public class DocumentAdminController extends BaseController {

	@Inject
	DocumentAdminService srv;

	@Remark("文档管理首页")
	public void index() {
		List<Document> docList = srv.getDocList();
		setAttr("docList", docList);
		render("index.html");
	}

	@Remark("创建文档")
	public void add() {
		List<Document> docList = srv.getDocList();
		setAttr("docList", docList);
		render("add_edit.html");
	}

	@Remark("创建文档提交")
	public void save() {
		Document doc = getBean(Document.class, "doc");
		Ret ret = srv.save(doc);
		renderJson(ret);
	}

	@Remark("修改文档")
	public void edit() {
		Document doc = srv.getByIds(getParaToInt("mainMenu"), getParaToInt("subMenu"));
		if (doc == null) {
			renderError(404);
		}
		setAttr("doc", doc);
		render("add_edit.html");
	}

	@Remark("修改文档提交")
	public void update() {
		Document doc = getBean(Document.class, "doc");
		Ret ret = srv.update(getParaToInt("oldMainMenu"), getParaToInt("oldSubMenu"), doc);
		renderJson(ret);
	}

	@Remark("删除文档")
	public void delete() {
		Ret ret = srv.delete(getParaToInt("mainMenu"), getParaToInt("subMenu"));
		renderJson(ret);
	}

	/**
	 * 发布
	 */
	@Remark("发布文档")
	public void publish() {
		Ret ret = srv.publish(getParaToInt("mainMenu"), getParaToInt("subMenu"));
		renderJson(ret);
	}

	/**
	 * 取消发布，成为草稿
	 */
	@Remark("取消发布文档")
	public void unpublish() {
		Ret ret = srv.unpublish(getParaToInt("mainMenu"), getParaToInt("subMenu"));
		renderJson(ret);
	}
}




