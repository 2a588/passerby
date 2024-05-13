package com.icuscn.passerby._admin.project;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.model.Project;
import com.jfinal.aop.Inject;
import com.icuscn.passerby.index.IndexService;
//import com.icuscn.passerby.my.project.MyProjectValidator;
//import project.com.icuscn.passerby.ProjectService;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;

/**
 * 项目管理控制器
 */
@Path(value = "/admin/project", viewPath = "/project")
public class ProjectAdminController extends BaseController {

	@Inject
	ProjectAdminService srv;
	
//	@Inject
//	ProjectService projectSrv;
	
	@Inject
	IndexService indexSrv;

	public void index() {
		Page<Project> projectPage = srv.paginate(getParaToInt("p", 1));
		setAttr("projectPage", projectPage);
		render("index.html");
	}

	/**
	 * 创建
	 */
	public void add() {
		render("add_edit.html");
	}

	/**
	 * 提交创建
	 */
//	@Before(MyProjectValidator.class)
//	public void save() {
//		Project project = getBean(Project.class);
//		Ret ret = srv.save(getLoginAccountId(), project);
//		renderJson(ret);
//	}

	/**
	 * 修改
	 */
	public void edit() {
		keepPara("p");	// 保持住分页的页号，便于在 ajax 提交后跳转到当前数据所在的页
		setAttr("project", srv.edit(getParaToInt("id")));
		render("add_edit.html");
	}

	/**
	 * 提交修改
	 */
//	@Before(MyProjectValidator.class)
//	public void update() {
//		Project project = getBean(Project.class);
//		Ret ret = srv.update(project);
//		renderJson(ret);
//	}

	/**
	 * 锁定
	 */
//	public void lock() {
//		Ret ret = srv.lock(getParaToInt("id"));
//
//		projectSrv.clearHotProjectCache();
//		indexSrv.clearCache();
//		renderJson(ret);
//	}

	/**
	 * 解除锁定
	 */
//	public void unlock() {
//		Ret ret = srv.unlock(getParaToInt("id"));
//
//		projectSrv.clearHotProjectCache();
//		indexSrv.clearCache();
//		renderJson(ret);
//	}

	/**
	 * 删除 project
	 */
	public void delete() {
		Ret ret = srv.delete(getParaToInt("id"));
		renderJson(ret);
	}
}


