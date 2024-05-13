/**
 * 请勿将俱乐部专享资源复制给其他人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目文档与设计资源、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多
 * 
 * JFinal 俱乐部是五年以来首次寻求外部资源的尝试，以便于有资源创建更加
 * 高品质的产品与服务，为大家带来更大的价值，所以请大家多多支持，不要将
 * 首次的尝试扼杀在了摇篮之中
 */

package com.icuscn.passerby.common.model;

import com.icuscn.passerby.common.model.base.BaseShare;
import com.icuscn.passerby.common.safe.JsoupFilter;

/**
 * Share
 */
@SuppressWarnings("serial")
public class Share extends BaseShare<Share> {

	/**
	 * 举报达到屏蔽的次数，达到这个数直接屏蔽帖子
	 */
	public static final int REPORT_BLOCK_NUM = 3;

	/**
	 * 过滤 title content 字段的 html 标记，防止 XSS 攻击
	 */
	protected void filter(int filterBy) {
		JsoupFilter.filterTitleAndContent(this);
	}
}