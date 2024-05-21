package com.icuscn.passerby.project;

import com.icuscn.passerby.common.model.Project;
import com.icuscn.passerby.common.safe.JsoupFilter;
import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * ProjectService
 */
public class ProjectService {

	private Project dao = new Project().dao();

	public Page<Project> paginate(int pageNumber) {
		Page<Project> projectPage = dao.template("project.paginate", Project.REPORT_BLOCK_NUM).paginate(pageNumber, 16);
		// 列表页显示 content 的摘要信息需要过滤为纯文本，去除所有标记
		JsoupFilter.filterArticleList(projectPage.getList(), 50, 120);
		return projectPage;
	}

	public Project findById(int projectId) {
		return dao.template("project.findById", projectId, Project.REPORT_BLOCK_NUM).findFirst();
	}

	public Project findById(int projectId, String columns) {
		Kv para = Kv.by("columns", columns).set("id", projectId).set("report", Project.REPORT_BLOCK_NUM);
		return dao.template("project.findByIdWithColumns", para).findFirst();
	}

	public List<Project> getHotProject() {
		return dao.template("project.getHotProject", Project.REPORT_BLOCK_NUM).findByCache("hotProject", "hotProject");
	}

	public void clearHotProjectCache() {
		CacheKit.remove("hotProject", "hotProject");
	}

	/**
	 * 暂时用于个人空间的创建、更新 share、feedback 模块，用于显示关联项目的下拉列表，将来改成按热度排序
	 * 项目数量多了以后考虑用输入框配合 autocomplete 提示输入来实现
	 */
	public List<Project> getAllProject(String columns) {
		Kv para = Kv.by("columns", columns).set("report", Project.REPORT_BLOCK_NUM);
		return dao.template("project.getAllProject", para).find();
	}
}




