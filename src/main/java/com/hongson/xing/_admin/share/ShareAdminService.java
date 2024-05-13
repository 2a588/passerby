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

package com.hongson.xing._admin.share;

import com.jfinal.aop.Inject;
import com.hongson.xing.common.model.Share;
import com.hongson.xing.common.model.ShareReply;
//import com.hongson.xing.my.share.MyShareService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;
import java.util.List;

/**
 * share 管理业务
 */
public class ShareAdminService {
	
//	@Inject
//	MyShareService myShareSrv;

	private Share dao = new Share().dao();
	private ShareReply shareReplyDao = new ShareReply().dao();

	/**
	 * share 分页
	 */
	public Page<Share> paginate(int pageNum) {
		return dao.paginate(pageNum, 10, "select *", "from share order by id desc");
	}

	/**
	 * 创建分享
	 */
	public Ret save(int accountId, Share project) {
		project.setAccountId(accountId);
		project.setTitle(project.getTitle().trim());
		project.setCreateAt(new Date());
		project.save();
		return Ret.ok("msg", "创建成功");
	}

	public Share edit(int id) {
		return dao.findById(id);
	}

	public Ret update(Share share) {
		share.update();
		return Ret.ok("msg", "修改成功");
	}

	/**
	 * 锁定
	 */
	public Ret lock(int id) {
		Db.update("update share set report = report + ? where id=?", Share.REPORT_BLOCK_NUM, id);
		return Ret.ok("msg", "锁定成功");
	}

	/**
	 * 解除锁定
	 */
	public Ret unlock(int id) {
		Db.update("update share set report = 0 where id=?", id);
		return Ret.ok("msg", "解除锁定成功");
	}

	/**
	 * 删除 share
	 */
	public Ret delete(int shareId) {
		Integer accountId = Db.queryInt("select accountId from share where id=? limit 1", shareId);
		if (accountId != null) {
			//myShareSrv.delete(accountId, shareId);
			return Ret.ok("msg", "share 删除成功");
		} else {
			return Ret.fail("msg", "share 删除失败");
		}
	}

	public Share getById(int shareId) {
		return dao.findById(shareId);
	}

	/**
	 * 获取 reply list
	 */
	public List<ShareReply> getReplyList(int shareId) {
		String sql = "select id, accountId, createAt, substring(content, 1, 30) as content from share_reply where shareId=? order by id desc";
		return shareReplyDao.find(sql, shareId);
	}

	public Ret getReply(int replyId) {
		ShareReply reply = shareReplyDao.findById(replyId);
		return Ret.ok("reply", reply);
	}

	/**
	 * 删除 share reply
	 */
	public Ret deleteReply(int shareReplyId) {
		Integer accountId = Db.queryInt("select accountId from share_reply where id=? limit 1", shareReplyId);
		if (accountId != null) {
			//myShareSrv.deleteShareReplyById(accountId, shareReplyId);
			return Ret.ok("msg", "share reply 删除成功");
		} else {
			return Ret.fail("msg", "share reply 删除失败");
		}
	}
}






