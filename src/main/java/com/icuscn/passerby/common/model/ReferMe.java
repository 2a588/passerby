package com.icuscn.passerby.common.model;

import com.icuscn.passerby.common.model.base.BaseReferMe;

/**
 * refer_me 提到我 ReferMe 模型，用于生成与我有关的记录，
 * 关联到 news_feed 表
 */
@SuppressWarnings("serial")
public class ReferMe extends BaseReferMe<ReferMe> {
	public static final int TYPE_AT_ME = 1;                 // @我
	public static final int TYPE_COMMENT_ME = 2;    // 评论我

	public static final int TYPE_SHARE_REF_MY_PROJECT = 3;          // 分享引用到我的项目
	public static final int TYPE_FEEDBACK_TO_MY_PROJECT = 4;     // 反馈到我的项目
}
