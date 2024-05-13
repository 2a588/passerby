package com.icuscn.passerby._admin.feedback;

import com.icuscn.passerby.common.model.Feedback;
import com.icuscn.passerby.common.model.FeedbackReply;
//import com.icuscn.passerby.my.feedback.MyFeedbackService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;

/**
 * feedback 管理业务
 */
public class FeedbackAdminService {
	
//	@Inject
//	MyFeedbackService myFeedbackSrv;

	private Feedback dao = new Feedback().dao();
	private FeedbackReply feedbackReplyDao = new FeedbackReply().dao();

	/**
	 * feedback 分页
	 */
	public Page<Feedback> paginate(int pageNum) {
		return dao.paginate(pageNum, 10, "select *", "from feedback order by id desc");
	}

	/**
	 * 创建反馈
	 */
	public Ret save(int accountId, Feedback feedback) {
		feedback.setAccountId(accountId);
		feedback.setTitle(feedback.getTitle().trim());
		feedback.setCreateAt(new Date());
		feedback.save();
		return Ret.ok("msg", "创建成功");
	}

	public Feedback edit(int id) {
		return dao.findById(id);
	}

	public Ret update(Feedback feedback) {
		feedback.update();
		return Ret.ok("msg", "修改成功");
	}

	public Ret lock(int id) {
		Db.update("update feedback set report = report + ? where id=?", Feedback.REPORT_BLOCK_NUM, id);
		return Ret.ok("msg", "锁定成功");
	}

	public Ret unlock(int id) {
		Db.update("update feedback set report = 0 where id=?", id);
		return Ret.ok("msg", "解除锁定成功");
	}

	/**
	 * 删除 feedback
	 */
	public Ret delete(int feedbackId) {
		Integer accountId = Db.queryInt("select accountId from feedback where id=? limit 1", feedbackId);
		if (accountId != null) {
			//myFeedbackSrv.delete(accountId, feedbackId);
			return Ret.ok("msg", "feedback 删除成功");
		} else {
			return Ret.fail("msg", "feedback 删除失败");
		}
	}

	public Feedback getById(int feedbackId) {
		return dao.findById(feedbackId);
	}

	/**
	 * 获取 reply list
	 */
	public List<FeedbackReply> getReplyList(int feedbackId) {
		String sql = "select id, accountId, createAt, substring(content, 1, 30) as content from feedback_reply where feedbackId=? order by id desc";
		return feedbackReplyDao.find(sql, feedbackId);
	}

	public Ret getReply(int replyId) {
		FeedbackReply reply = feedbackReplyDao.findById(replyId);
		return Ret.ok("reply", reply);
	}

	/**
	 * 删除 feedback reply
	 */
	public Ret deleteReply(int feedbackReplyId) {
		Integer accountId = Db.queryInt("select accountId from feedback_reply where id=? limit 1", feedbackReplyId);
		if (accountId != null) {
			//myFeedbackSrv.deleteFeedbackReplyById(accountId, feedbackReplyId);
			return Ret.ok("msg", "feedback reply 删除成功");
		} else {
			return Ret.fail("msg", "feedback reply 删除失败");
		}
	}
}
