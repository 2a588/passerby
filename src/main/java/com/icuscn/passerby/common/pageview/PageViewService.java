package com.icuscn.passerby.common.pageview;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.ehcache.CacheKit;
import org.joda.time.LocalDate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 维护 project_page_view、share_page_view、feedback_page_view 数据
 */
public class PageViewService {

	@SuppressWarnings("serial")
	private Map<String, String> actionKeyToCacheName = new HashMap<String, String>(){{
		// 只统计 project、share、feedback 详情页
		put("/project/detail", "projectPageView");
		put("/share/detail", "sharePageView");
		put("/feedback/detail", "feedbackPageView");
	}};

	public void updateToDataBase() {
		Date date = LocalDate.now().toDate();
		doUpdateToDataBase("project", date);
		doUpdateToDataBase("share", date);
		doUpdateToDataBase("feedback", date);
	}

	/**
	 * project_page_view(projectId, visitDate, visitCount)
	 */
	@SuppressWarnings("unchecked")
	private void doUpdateToDataBase(String articleType, Date date) {
		String cacheName = articleType + "PageView";
		List<Integer> ids = CacheKit.getKeys(cacheName);
		for (Integer id : ids) {
			Integer visitCount = CacheKit.get(cacheName, id);
			if (visitCount == null) {
				continue ;
			}
			
			// 获取以后立即清除，因为获取后的值将累加到数据表中。或许放在 for 循环的最后一行为好
			CacheKit.remove(cacheName, id);

			int n = Db.update(getUpdateSql(articleType), visitCount, id, date);
//			if (n == 0) {   // 记录不存在则插入新记录
//				Record pageView = new Record();
//				pageView.set(articleType + "Id", id);
//				pageView.set("visitDate", date);
//				pageView.set("visitCount", visitCount);
//				Db.save(articleType + "_page_view", pageView);
//			}
		}
	}

	private String getUpdateSql(String articleType) {
		return "update " + articleType + "_page_view set visitCount = visitCount + ?" +
				" where "+ articleType + "Id = ? and visitDate = ?";
	}

	/**
	 * 1：通过 actionKey + ip地址，去 pageViewId 缓存中去找 article id 是否存在
	 * 2：如果不存在，则将当前 article 的 visitCount 加 1 并缓存，否则直跳过不处理
	 * 3：不同类型的数据缓存分别为：xxxPageView
	 * 5：引入 pageViewId 缓存，是为了防止刷榜
	 */
	public void processPageView(String actionKey, Integer id, String ip) {
		if (id == null) {
			throw new IllegalArgumentException("id 值不能为 null.");
		}
		String cacheName = actionKeyToCacheName.get(actionKey);
		if (cacheName == null) {
			throw new RuntimeException("不支持的 actionKey： " + actionKey);
		}

		if (ip == null) {
			ip = "127.0.0.1";
		}

		// actionKey + ip 当成 key用于区分：project、share、feedback
		String pageViewKey = actionKey + ip;
		Integer idInCache = CacheKit.get("pageViewIp", pageViewKey);

		// 为了避免恶意刷榜，id 在 cache中不存在，或者存在但不等于 id，才去做计数，否则直接跳过
		if (idInCache == null || !id.equals(idInCache)) {
			Integer visitCount = CacheKit.get(cacheName, id);
			visitCount = (visitCount != null ? visitCount + 1 : 1);
			CacheKit.put(cacheName, id, visitCount);

			// 将当前访问者的 actionKey + ip ---> id 放入缓存
			CacheKit.put("pageViewIp", pageViewKey, id);
		}
	}
}
