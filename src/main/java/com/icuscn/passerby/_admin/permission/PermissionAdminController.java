package com.icuscn.passerby._admin.permission;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.model.Permission;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 权限管理
 */
@Path(value = "/admin/permission", viewPath = "/permission")
public class PermissionAdminController extends BaseController {

	@Inject
	PermissionAdminService srv;

	public void index() {
		Page<Permission> permissionPage = srv.paginate(getParaToInt("p", 1));
		srv.replaceControllerPrefix(permissionPage, "com.icuscn.passerby._admin.", "...");
		boolean hasRemovedPermission = srv.markRemovedActionKey(permissionPage);
		setAttr("permissionPage", permissionPage);
		setAttr("hasRemovedPermission", hasRemovedPermission);
		render("index.html");
	}

	public void sync() {
		Ret ret = srv.sync();
		renderJson(ret);
	}

	public void edit() {
		keepPara("p");	// 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
		Permission permission = srv.findById(getParaToInt("id"));
		setAttr("permission", permission);
		render("edit.html");
	}

	public void update() {
		Ret ret = srv.update(getBean(Permission.class));
		renderJson(ret);
	}

	public void delete() {
		Ret ret = srv.delete(getParaToInt("id"));
		renderJson(ret);
	}
}