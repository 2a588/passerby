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

package com.hongson.xing._admin.project;

import com.jfinal.aop.Inject;
import com.hongson.xing.common.model.Project;
//import com.hongson.xing.my.project.MyProjectService;
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
