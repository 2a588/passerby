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

package com.hongson.xing._admin.document;

import com.jfinal.aop.Inject;
import com.hongson.xing._admin.permission.Remark;
import com.hongson.xing.common.controller.BaseController;
import com.hongson.xing.common.model.Document;
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




