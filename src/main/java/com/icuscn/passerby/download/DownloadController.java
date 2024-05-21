
package com.icuscn.passerby.download;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.interceptor.AuthCacheClearInterceptor;
import com.icuscn.passerby.common.interceptor.FrontAuthInterceptor;
import com.icuscn.passerby.common.kit.IpKit;
import com.icuscn.passerby.common.model.Account;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;

/**
 * 下载控制器
 */
@Path("/download")
//@Before(FrontAuthInterceptor.class)  //登陆拦截的注解
public class DownloadController extends BaseController {
	
	@Inject
	DownloadService srv;

	/**
	 * 下载
	 */
	public void index() {
		Account loginAccount = getLoginAccount();
		loginAccount = new Account();
		loginAccount.setId(1);
		String ip = IpKit.getRealIp(getRequest());
		Ret ret = srv.download(loginAccount, getPara("file"), ip);
		if (ret.isOk()) {
			String fullFileName = ret.getAs("fullFileName");
			renderFile(fullFileName);
		} else {
			renderError(404);
		}
	}

	/**
	 * 清缓存
	 */
	@Before(AuthCacheClearInterceptor.class)
	public void clear() {
		srv.clearCache();
		redirect("/");
	}
}
