package com.icuscn.passerby._admin.index;

import com.icuscn.passerby.common.controller.BaseController;
import com.jfinal.aop.Inject;
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
