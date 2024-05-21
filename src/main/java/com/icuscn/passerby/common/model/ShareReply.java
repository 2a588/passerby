package com.icuscn.passerby.common.model;

import com.icuscn.passerby.common.model.base.BaseShareReply;
import com.icuscn.passerby.common.safe.JsoupFilter;

/**
 * 分享回复
 */
@SuppressWarnings("serial")
public class ShareReply extends BaseShareReply<ShareReply> {

	/**
	 * 举报达到屏蔽的次数，达到这个数直接屏蔽帖子
	 */
	public static final int REPORT_BLOCK_NUM = 3;

	/**
	 * 过滤 title content 字段的 html 标记，防止 XSS 攻击
	 * 将回车换行转换成 <br> 标记便于 html 中显示换行
	 */
	protected void filter(int filterBy) {
		String content = getContent().trim().replaceAll("\r\n", "<br>").replaceAll("\n", "<br>").replaceAll("\r", "<br>");
		content = JsoupFilter.filterArticleContent(content);
		setContent(content);
	}
}
