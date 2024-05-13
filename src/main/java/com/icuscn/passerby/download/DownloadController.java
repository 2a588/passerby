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
@Before(FrontAuthInterceptor.class)
public class DownloadController extends BaseController {
	
	@Inject
	DownloadService srv;

	/**
	 * 下载
	 */
	public void index() {
		Account loginAccount = getLoginAccount();
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
