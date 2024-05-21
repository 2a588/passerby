package com.icuscn.passerby.common.model;

import com.icuscn.passerby.common.model.base.BaseProject;
import com.icuscn.passerby.common.safe.JsoupFilter;

/**
 * 项目
 */
@SuppressWarnings("serial")
public class Project extends BaseProject<Project> {

	/**
	 * 举报达到屏蔽的次数，达到这个数直接屏蔽帖子
	 */
	public static final int REPORT_BLOCK_NUM = 3;

	/**
	 * 过滤 title content 字段的 html 标记，防止 XSS 攻击
	 */
	protected void filter(int filterBy) {
		JsoupFilter.filterProject(this);
	}
}
