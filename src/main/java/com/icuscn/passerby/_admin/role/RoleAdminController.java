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

package com.icuscn.passerby._admin.role;

import com.icuscn.passerby._admin.permission.PermissionAdminService;
import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.model.Permission;
import com.icuscn.passerby.common.model.Role;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 角色管理控制器
 */
@Path(value = "/admin/role", viewPath = "/role")
public class RoleAdminController extends BaseController {

	@Inject
	RoleAdminService srv;
	
	@Inject
    PermissionAdminService permissionAdminSrv;

	public void index() {
		Page<Role> rolePage = srv.paginate(getParaToInt("p", 1));
		setAttr("rolePage", rolePage);
		render("index.html");
	}

	public void add() {
		render("add_edit.html");
	}

	@Before(RoleAdminValidator.class)
	public void save() {
		Role role = getBean(Role.class);
		Ret ret = srv.save(role);
		renderJson(ret);
	}

	public void edit() {
		keepPara("p");	// 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
		Role role = srv.findById(getParaToInt("id"));
		setAttr("role", role);
		render("add_edit.html");
	}

	/**
	 * 提交修改
	 */
	@Before(RoleAdminValidator.class)
	public void update() {
		Role role = getBean(Role.class);
		Ret ret = srv.update(role);
		renderJson(ret);
	}

	public void delete() {
		Ret ret = srv.delete(getParaToInt("id"));
		renderJson(ret);
	}

	/**
	 * 分配权限
	 */
	public void assignPermissions() {
		Role role = srv.findById(getParaToInt("id"));
		List<Permission> permissionList = permissionAdminSrv.getAllPermissions();
		srv.markAssignedPermissions(role, permissionList);
		LinkedHashMap<String, List<Permission>> permissionMap = srv.groupByController(permissionList);

		setAttr("role", role);
		setAttr("permissionMap", permissionMap);
		render("assign_permissions.html");
	}

	/**
	 * 添加权限
	 */
	public void addPermission() {
		Ret ret = srv.addPermission(getParaToInt("roleId"), getParaToInt("permissionId"));
		renderJson(ret);
	}

	/**
	 * 删除权限
	 */
	public void deletePermission() {
		Ret ret = srv.deletePermission(getParaToInt("roleId"), getParaToInt("permissionId"));
		renderJson(ret);
	}
}
