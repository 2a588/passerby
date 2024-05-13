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

import com.jfinal.aop.Aop;
import com.hongson.xing.common.model.Account;

/**
 * 权限管理的 shared method 扩展
 *
 * 作为 #role、#permission 指令的补充，支持 #else 块
 *
 *
 * 使用示例：
 * #if (hasRole("权限管理员", "CEO", "CTO"))
 *   ...
 * #else
 *   ...
 * #end
 *
 * #if (hasPermission("/admin/project/edit"))
 *   ...
 * #else
 *   ...
 * #end
 */
public class AdminAuthKit {
	
	/**
	 * 注意这里与控制器和拦截器不同，不能使用 @Inject 注入
	 * 但可以使用 Aop.get(...) 实现同样的功能，代码稍多点而已
	 * 
	 * 不能使用 @Inject 注入的原因是 AdminAuthKit 工具对象
	 * 的创建并不是由 jfinal 接管的，而 controller、interceptor
	 * 的创建是由 jfinal 接管的，在接管后会自动进行注入动作
	 * 
	 * 所以，这里需要手动 Aop.get(...)
	 */
	static AdminAuthService adminAuthSrv = Aop.get(AdminAuthService.class);

	/**
	 * 当前账号是否拥有某些角色
	 */
	public boolean hasRole(String... roleNameArray) {
		Account account = AdminAuthInterceptor.getThreadLocalAccount();
		if (account != null && account.isStatusOk()) {
			if (	adminAuthSrv.isSuperAdmin(account.getId()) ||
					adminAuthSrv.hasRole(account.getId(), roleNameArray)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否拥有具体某个权限
	 */
	public boolean hasPermission(String actionKey) {
		Account account = AdminAuthInterceptor.getThreadLocalAccount();
		if (account != null && account.isStatusOk()) {
			if (	adminAuthSrv.isSuperAdmin(account.getId()) ||
					adminAuthSrv.hasPermission(account.getId(), actionKey)) {
				return true;
			}
		}

		return false;
	}
}


