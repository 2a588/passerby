package com.icuscn.passerby.common.pageview;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.icuscn.passerby.common.kit.IpKit;
import com.jfinal.core.Controller;

/**
 * 用于记录文章详情页的页面访问量 page view，用于热门文章排序
 */
public class PageViewInterceptor implements Interceptor {
	
	@Inject
	PageViewService pageViewSrv;

	public void intercept(Invocation inv) {
		inv.invoke();

		Controller c = inv.getController();

		if (c.isParaExists(0)) {
			String actionKey = inv.getActionKey();
			Integer id = c.getParaToInt();
			String ip = IpKit.getRealIp(c.getRequest());
			pageViewSrv.processPageView(actionKey, id, ip);
		}
	}
}
