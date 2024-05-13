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

package com.hongson.xing._admin.permission;

import com.jfinal.aop.Aop;
import com.hongson.xing._admin.auth.AdminAuthService;
import com.hongson.xing.common.model.Account;
import com.hongson.xing.login.LoginService;
import com.jfinal.template.Directive;
import com.jfinal.template.Env;
import com.jfinal.template.io.Writer;
import com.jfinal.template.stat.Scope;

/**
 * 界面上的权限控制功能
 * 用来控制界面上的菜单、按钮等等元素的显示
 *
 * 使用示例见模板文件： /_view/_admin/project/index.html 或者 /_view/_admin/permission/index.html
 * #permission("/admin/project/edit")
 * 		<a href="/admin/project/edit?id=#(x.id)">
 * 	  	  <i class="fa fa-pencil" title="修改"></i>
 * 		</a>
 * #end
 *
 * 别名： #perm() #end
 */
public class PermissionDirective extends Directive {

	static AdminAuthService adminAuthSrv = Aop.get(AdminAuthService.class);
	
	public void exec(Env env, Scope scope, Writer writer) {
		Account account = (Account)scope.getRootData().get(LoginService.loginAccountCacheName);
		if (account != null && account.isStatusOk()) {
			// 如果是超级管理员，或者拥有指定的权限则放行
			if (	adminAuthSrv.isSuperAdmin(account.getId()) ||
					adminAuthSrv.hasPermission(account.getId(), getPermission(scope))) {
				stat.exec(env, scope, writer);
			}
		}
	}

	/**
	 * 从 #permission 指令参数中获取 permission
	 */
	private String getPermission(Scope scope) {
		Object value = exprList.eval(scope);
		if (value instanceof String) {
			return (String)value;
		} else {
			throw new IllegalArgumentException("权限参数只能为 String 类型");
		}
	}

	public boolean hasEnd() {
		return true;
	}
}