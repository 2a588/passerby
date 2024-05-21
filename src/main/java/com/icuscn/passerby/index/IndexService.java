package com.icuscn.passerby.index;

import com.icuscn.passerby.common.model.Feedback;
import com.icuscn.passerby.common.model.Project;
import com.icuscn.passerby.common.model.Share;
import com.icuscn.passerby.common.safe.JsoupFilter;
import com.jfinal.plugin.ehcache.CacheKit;

import java.util.List;

/**
 * 首页业务，主要为了方便做缓存，以及排序逻辑
 */
public class IndexService {
	
	private final String indexCacheName = "index";
	private Project projectDao = new Project().dao();
	private Share shareDao = new Share().dao();
	private Feedback feedbackDao = new Feedback().dao();

	public List<Project> getProjectList() {
		List<Project> projectList =  projectDao.template("index.getProjectList", Project.REPORT_BLOCK_NUM).findByCache(indexCacheName, "projectList");

		// 列表页显示 content 的摘要信息需要过滤为纯文本，去除所有标记
		JsoupFilter.filterArticleList(projectList, 50, 120);
		return projectList;
	}

	public List<Share> getShareList() {
		List<Share> shareList = shareDao.template("index.getShareList", Share.REPORT_BLOCK_NUM).findByCache(indexCacheName, "shareList");

		// 列表页显示 content 的摘要信息需要过滤为纯文本，去除所有标记
		JsoupFilter.filterArticleList(shareList, 50, 120);
		return shareList;
	}

	public List<Feedback> getFeedbackList() {
		List<Feedback> feedbackList = feedbackDao.template("index.getFeedbackList", Feedback.REPORT_BLOCK_NUM).findByCache(indexCacheName, "feedbackList");

		// 列表页显示 content 的摘要信息需要过滤为纯文本，去除所有标记
		JsoupFilter.filterArticleList(feedbackList, 50, 120);
		return feedbackList;
	}

	public void clearCache() {
		CacheKit.removeAll(indexCacheName);
	}
}



