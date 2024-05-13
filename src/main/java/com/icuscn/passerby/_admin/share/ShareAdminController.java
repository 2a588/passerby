package com.icuscn.passerby._admin.share;

import com.icuscn.passerby.common.account.AccountService;
import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.model.Share;
import com.icuscn.passerby.common.model.ShareReply;
import com.jfinal.aop.Inject;
import com.icuscn.passerby.index.IndexService;
//import com.icuscn.passerby.my.share.MyShareValidator;
//import project.com.icuscn.passerby.ProjectService;
//import com.icuscn.passerby.share.ShareService;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 分享管理控制器
 */
@Path(value = "/admin/share", viewPath = "/share")
public class ShareAdminController extends BaseController {

	@Inject
	ShareAdminService srv;
	
//	@Inject
//	ShareService shareSrv;
//
//	@Inject
//	ProjectService projectSrv;
	
	@Inject
    AccountService accountSrv;
	
	@Inject
	IndexService indexSrv;

	public void index() {
		Page<Share> sharePage = srv.paginate(getParaToInt("p", 1));
		setAttr("sharePage", sharePage);
		render("index.html");
	}

	/**
	 * 创建
	 */
//	public void add() {
//		setAttr("projectList", projectSrv.getAllProject("id, name"));    // 关联项目下拉列表
//		render("add_edit.html");
//	}
//
//	/**
//	 * 提交创建
//	 */
//	@Before(MyShareValidator.class)
//	public void save() {
//		Share share = getBean(Share.class);
//		Ret ret = srv.save(getLoginAccountId(), share);
//		renderJson(ret);
//	}

	/**
	 * 修改
	 */
	public void edit() {
		keepPara("p");	// 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
		//setAttr("projectList", projectSrv.getAllProject("id, name"));    // 关联项目下拉列表
		setAttr("share", srv.edit(getParaToInt("id")));
		render("add_edit.html");
	}

	/**
	 * 提交修改
	 */
//	@Before(MyShareValidator.class)
//	public void update() {
//		Share share = getBean(Share.class);
//		Ret ret = srv.update(share);
//		renderJson(ret);
//	}
//
//	/**
//	 * 锁定
//	 */
//	public void lock() {
//		Ret ret = srv.lock(getParaToInt("id"));
//
//		shareSrv.clearHotShareCache();
//		indexSrv.clearCache();
//		renderJson(ret);
//	}

	/**
	 * 解除锁定
	 */
//	public void unlock() {
//		Ret ret = srv.unlock(getParaToInt("id"));
//
//		shareSrv.clearHotShareCache();
//		indexSrv.clearCache();
//		renderJson(ret);
//	}

	/**
	 * 删除 share
	 */
	public void delete() {
		Ret ret = srv.delete(getParaToInt("id"));
		renderJson(ret);
	}

	/**
	 * 获取 share 的 reply 列表
	 */
	public void getReplyList() {
		int shareId = getParaToInt("shareId");
		Share share = srv.getById(shareId);
		List<ShareReply> shareReplyList = srv.getReplyList(shareId);
		accountSrv.join("accountId", shareReplyList, "nickName");

		setAttr("share", share);
		setAttr("shareReplyList", shareReplyList);
		setAttr("shareId", shareId);
		render("reply.html");
	}

	/**
	 * 获取 share 的 reply
	 */
	public void getReply() {
		int replyId = getParaToInt("replyId");
		Ret ret = srv.getReply(replyId);
		renderJson(ret);
	}

	/**
	 * 删除 share reply
	 */
	public void deleteReply() {
		int replyId = getParaToInt("replyId");
		Ret ret = srv.deleteReply(replyId);
		renderJson(ret);
	}
}



