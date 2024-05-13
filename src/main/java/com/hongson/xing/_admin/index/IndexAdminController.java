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

package com.hongson.xing._admin.index;

import com.jfinal.aop.Inject;
import com.hongson.xing.common.controller.BaseController;
import com.jfinal.core.Path;

/**
 * 后台管理首页
 */
@Path(value = "/admin", viewPath = "/index")
public class IndexAdminController extends BaseController {

	@Inject
	IndexAdminService srv;

	public void index() {
		setAttr("accountProfile", srv.getAccountProfile());
		setAttr("projectProfile", srv.getProjectProfile());
		setAttr("shareProfile", srv.getShareProfile());
		setAttr("feedbackProfile", srv.getFeedbackProfile());
		setAttr("permissionProfile", srv.getPermissionProfile());

		render("index.html");
	}
}
