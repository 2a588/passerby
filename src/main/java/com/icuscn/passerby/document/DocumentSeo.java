package com.icuscn.passerby.document;

import com.icuscn.passerby.common.interceptor.BaseSeoInterceptor;
import com.icuscn.passerby.common.model.Document;
import com.jfinal.core.Controller;

/**
 * Document 搜索引擎优化
 */
public class DocumentSeo extends BaseSeoInterceptor {

	@Override
	public void indexSeo(Controller c) {

	}

	@Override
	public void detailSeo(Controller c) {

	}

	@Override
	public void othersSeo(Controller c, String method) {
		if (method.equals("doc")) {
			Document doc = c.getAttr("doc");
			if (doc != null) {
				setSeoTitle(c, "JFinal 文档、资料、学习、API，" + doc.getTitle());
				setSeoKeywords(c, "JFinal 文档, JFinal 教程, JFinal API, JFinal 入门, JFinal 资料, JFinal 学习");
				setSeoDescr(c, "JFinal 官方社区文档频道，提供最新、最优质、最权威、最全面的 JFinal 在线文档、资料、例子、API，是 JFinal 入门、提升、深入学习的最好资源，文档会经常更新，尽请关注 JFinal 社区动态");
			}
		}
	}
}
