package com.icuscn.passerby._admin.feedback;

import com.icuscn.passerby.common.account.AccountService;
import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.model.Feedback;
import com.icuscn.passerby.common.model.FeedbackReply;
import com.jfinal.aop.Inject;
//import com.icuscn.passerby.feedback.FeedbackService;
import com.icuscn.passerby.index.IndexService;
//import com.icuscn.passerby.my.feedback.MyFeedbackValidator;
//import project.com.icuscn.passerby.ProjectService;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.List;

/**
 * 反馈管理控制器
 * 
 * 注意：sql 语句与业务逻辑要写在业务层，在此仅由于时间仓促偷懒的做法
 *     后续版本会改掉这样的用法，请小伙伴们不要效仿
 */
@Path(value = "/admin/feedback", viewPath = "/feedback")
public class FeedbackAdminController extends BaseController {

	@Inject
	FeedbackAdminService srv;
	
//	@Inject
//	FeedbackService feedbackSrv;
//
//	@Inject
//	ProjectService projectSrv;
//
	@Inject
AccountService accountSrv;
	
	@Inject
	IndexService indexSrv;

	public void index() {
		Page<Feedback> feedbackPage = srv.paginate(getParaToInt("p", 1));
		setAttr("feedbackPage", feedbackPage);
		render("index.html");
	}

	/**
	 * 创建
	 */
	public void add() {
		//setAttr("projectList", projectSrv.getAllProject("id, name"));    // 关联项目下拉列表
		render("add_edit.html");
	}

	/**
	 * 提交创建
	 */
//	@Before(MyFeedbackValidator.class)
//	public void save() {
//		Feedback feedback = getBean(Feedback.class);
//		Ret ret = srv.save(getLoginAccountId(), feedback);
//		renderJson(ret);
//	}

	/**
	 * 修改
	 */
	public void edit() {
		keepPara("p");	// 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
		//setAttr("projectList", projectSrv.getAllProject("id, name"));    // 关联项目下拉列表
		setAttr("feedback", srv.edit(getParaToInt("id")));
		render("add_edit.html");
	}

	/**
	 * 提交修改
	 */
//	@Before(MyFeedbackValidator.class)
//	public void update() {
//		Feedback feedback = getBean(Feedback.class);
//		Ret ret = srv.update(feedback);
//		renderJson(ret);
//	}

	/**
	 * 锁定，目前先做成使用举报量 report 值锁定
	 */
//	public void lock() {
//		Ret ret = srv.lock(getParaToInt("id"));
//
//		feedbackSrv.clearHotFeedbackCache();	// 清缓存
//		indexSrv.clearCache();
//		renderJson(ret);
//	}
//
//	/**
//	 * 解除锁定
//	 */
//	public void unlock() {
//		Ret ret = srv.unlock(getParaToInt("id"));
//
//		feedbackSrv.clearHotFeedbackCache();		// 清缓存
//		indexSrv.clearCache();
//		renderJson(ret);
//	}

	/**
	 * 删除 feedback
	 */
	public void delete() {
		Ret ret = srv.delete(getParaToInt("id"));
		renderJson(ret);
	}

	/**
	 * 获取 feedback 的 reply 列表
	 */
	public void getReplyList() {
		int feedbackId = getParaToInt("feedbackId");
		Feedback feedback = srv.getById(feedbackId);
		List<FeedbackReply> feedbackReplyList = srv.getReplyList(feedbackId);
		accountSrv.join("accountId", feedbackReplyList, "nickName");

		setAttr("feedback", feedback);
		setAttr("feedbackReplyList", feedbackReplyList);
		setAttr("feedbackId", feedbackId);
		render("reply.html");
	}

	/**
	 * 获取 feedback 的 reply
	 */
	public void getReply() {
		int replyId = getParaToInt("replyId");
		Ret ret = srv.getReply(replyId);
		renderJson(ret);
	}

	/**
	 * 删除 feedback reply
	 */
	public void deleteReply() {
		int replyId = getParaToInt("replyId");
		Ret ret = srv.deleteReply(replyId);
		renderJson(ret);
	}
}



