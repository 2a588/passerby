package com.icuscn.passerby.common.model;

import com.icuscn.passerby.common.model.base.BaseNewsFeed;

/**
 * NewsFeed
 * 
 * id
 * accountId         发布该动态的用户
 * refType             动态引用类型
 * refId                 动态引用所关联的 id，与 refType 配合，可唯一确定是某个表中的某条记录
 * refParentType   refId 对象的父对象，例如 share_reply 的父对象是 share
 * refParentId       refId 对象的父对象的 id
 * createAt
 */
@SuppressWarnings("serial")
public class NewsFeed extends BaseNewsFeed<NewsFeed> {

	public static final int REF_TYPE_PROJECT = 1;                    // 项目动态
	public static final int REF_TYPE_PROJECT_REPLY = 2;         // 项目回复动态，暂时不用

	public static final int REF_TYPE_SHARE = 3;                       // 分享动态
	public static final int REF_TYPE_SHARE_REPLY = 4;            // 分享回复动态

	public static final int REF_TYPE_FEEDBACK = 5;                 // 反馈动态
	public static final int REF_TYPE_FEEDBACK_REPLY = 6;      // 反馈回复动态

	public boolean isRefTypeProject() {
		return getRefType() == REF_TYPE_PROJECT;
	}

	public boolean isRefTypeProjectReply() {
		return getRefType() == REF_TYPE_PROJECT_REPLY;
	}

	public boolean isRefTypeShare() {
		return getRefType() == REF_TYPE_SHARE;
	}

	public boolean isRefTypeShareReply() {
		return getRefType() == REF_TYPE_SHARE_REPLY;
	}

	public boolean isRefTypeFeedback() {
		return getRefType() == REF_TYPE_FEEDBACK;
	}

	public boolean isRefTypeFeedbackReply() {
		return getRefType() == REF_TYPE_FEEDBACK_REPLY;
	}
}
