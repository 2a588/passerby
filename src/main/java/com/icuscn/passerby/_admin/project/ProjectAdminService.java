package com.icuscn.passerby._admin.project;

import com.icuscn.passerby.common.model.Project;
//import com.icuscn.passerby.my.project.MyProjectService;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

import java.util.Date;

/**
 * project 管理业务
 */
public class ProjectAdminService {
	
//	@Inject
//	MyProjectService myProjectSrv;

	private Project dao = new Project().dao();

	/**
	 * 项目分页
	 */
	public Page<Project> paginate(int pageNum) {
		return dao.paginate(pageNum, 10, "select *", "from project order by id desc");
	}

	/**
	 * 判断项目名称是否存在
	 * @param projectId 当前 project 对象的 id 号，如果 project 对象还未创建，提供一个小于 0 的值即可
	 * @param name 项目名
	 */
	public boolean exists(int projectId, String name) {
		name = name.toLowerCase().trim();
		String sql = "select id from project where lower(name) = ? and id != ? limit 1";
		Integer id = Db.queryInt(sql, name, projectId);
		return id != null;
	}

	/**
	 * 创建项目
	 */
	public Ret save(int accountId, Project project) {
		if (exists(-1, project.getName())) {
			return Ret.fail("msg", "项目名称已经存在，请输入别的名称");
		}

		project.setAccountId(accountId);
		project.setName(project.getName().trim());
		project.setCreateAt(new Date());
		project.save();
		return Ret.ok("msg", "创建成功");
	}

	public Project edit(int id) {
		return dao.findById(id);
	}

	public Ret update(Project project) {
		if (exists(project.getId(), project.getName())) {
			return Ret.fail("msg", "项目名称已经存在，请输入别的名称");
		}

		project.setName(project.getName().trim());
		project.update();
		return Ret.ok("msg", "修改成功");
	}

	/**
	 * 锁定
	 */
	public Ret lock(int id) {
		Db.update("update project set report = report + ? where id=?", Project.REPORT_BLOCK_NUM, id);
		return Ret.ok("msg", "锁定成功");
	}

	/**
	 * 解除锁定
	 */
	public Ret unlock(int id) {
		Db.update("update project set report = 0 where id=?", id);
		return Ret.ok("msg", "解除锁定成功");
	}

	/**
	 * 删除 project
	 */
	public Ret delete(int projectId) {
		Integer accountId = Db.queryInt("select accountId from project where id=? limit 1", projectId);
		if (accountId != null) {
			//myProjectSrv.delete(accountId, projectId);
			return Ret.ok("msg", "project 删除成功");
		} else {
			return Ret.fail("msg", "project 删除失败");
		}
	}
}
