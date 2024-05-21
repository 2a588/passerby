package com.icuscn.passerby.index;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.interceptor.AuthCacheClearInterceptor;
import com.icuscn.passerby.common.model.Download;
import com.icuscn.passerby.common.model.Feedback;
import com.icuscn.passerby.common.model.Project;
import com.icuscn.passerby.common.model.Share;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.icuscn.passerby.download.DownloadService;
import com.jfinal.core.Path;

import java.util.List;

/**
 * 首页控制器
 */
@Path(value = "/", viewPath = "/index")
public class IndexController extends BaseController {
	
	@Inject
	IndexService srv;
	
	@Inject
	DownloadService downloadSrv;

	public void index() {
		List<Project> projectList = srv.getProjectList();
		List<Share> shareList = srv.getShareList();
		List<Feedback> feedbackList = srv.getFeedbackList();
		List<Download> downloadList = downloadSrv.getDownloadList();

		setAttr("projectList", projectList);
		setAttr("shareList", shareList);
		setAttr("feedbackList", feedbackList);
		setAttr("downloadList", downloadList);

		render("index.html");
	}

	@Before(AuthCacheClearInterceptor.class)
	public void clear() {
		srv.clearCache();
		redirect("/");
	}
}
