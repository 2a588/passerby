/**
 * 请勿将俱乐部专享资源复制给任何人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目源码、直播视频、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多，俱乐部福利资源是不断增加的，以下是俱乐部
 * 新福利计划：
 * https://jfinal.com/club/1-2
 * 
 * JFinal 俱乐部是七年以来首次寻求外部资源的尝试，以便于创建更加高品质的产品与服务，为你带来
 * 更多、更大的价值
 * 
 * JFinal 项目的可持续性发展需要你的支持！！！
 */

package com.hongson.xing._admin.auth;

import com.jfinal.aop.Inject;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.hongson.xing.common.model.Account;
import com.hongson.xing.login.LoginService;
import com.jfinal.kit.Ret;

/**
 * 后台管理员授权拦截器
 */
public class AdminAuthInterceptor implements Interceptor {

	@Inject
	AdminAuthService srv;
	
	/**
	 * 用于 sharedObject、sharedMethod 扩展中使用
	 */
	private static final ThreadLocal<Account> threadLocal = new ThreadLocal<Account>();
	
	public static Account getThreadLocalAccount() {
		return threadLocal.get();
	}

	public void intercept(Invocation inv) {
		Account loginAccount = inv.getController().getAttr(LoginService.loginAccountCacheName);
		if (loginAccount != null && loginAccount.isStatusOk()) {
			// 传递给 sharedObject、sharedMethod 扩展使用
			threadLocal.set(loginAccount);
			
			// 如果是超级管理员或者拥有对当前 action 的访问权限则放行
			if (	srv.isSuperAdmin(loginAccount.getId()) ||
					srv.hasPermission(loginAccount.getId(), inv.getActionKey())) {
				inv.invoke();
				return ;
			}
		}

		// renderError(404) 避免暴露后台管理 url，增加安全性
		if (loginAccount == null || inv.getActionKey().equals("/admin")) {
			inv.getController().renderError(404);
		}
		// renderJson 提示没有操作权限，提升用户体验
		else {
			inv.getController().renderJson(Ret.fail("msg", "没有操作权限"));
		}
	}
}

