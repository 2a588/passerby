package com.icuscn.passerby.project;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.pageview.PageViewInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.icuscn.passerby.common.model.Project;
//import com.icuscn.passerby.my.favorite.FavoriteService;
//import com.icuscn.passerby.my.like.LikeService;
import com.jfinal.core.Path;
import com.jfinal.kit.Ret;

/**
 * 项目控制器
 * 项目详情页侧边栏列表放此项目的热门分享
 * 项目列表页侧边栏放热门项目
 */
@Path("/project")
@Before(ProjectSeo.class)
public class ProjectController extends BaseController {

	@Inject
	ProjectService srv;
	
//	@Inject
//	FavoriteService favoriteSrv;
//
//	@Inject
//	LikeService likeSrv;

	public void index() {
		setAttr("projectPage", srv.paginate(getParaToInt("p", 1)));
		setAttr("hotProject", srv.getHotProject());
		render("index.html");
	}

	/**
	 * 详情页添加分享与反馈按钮，便于对当前项目时行反馈与分享
	 */
	@Before(PageViewInterceptor.class)
	public void detail() {
		Project project = srv.findById(getParaToInt());
		if (project != null) {
			setAttr("project", project);
			setAttr("hotProject", srv.getHotProject());
			render("detail.html");

            setLikeAndFavoriteStatus(project);
		} else {
			renderError(404);
		}
	}

    /**
     * 如果用户已登录，则需要显示当前 article 是否已经被该用户点赞、收藏了
     */
    private void setLikeAndFavoriteStatus(Project project) {
        Ret ret = Ret.create();
//        likeSrv.setLikeStatus(getLoginAccount(), "project", project, ret);
//        favoriteSrv.setFavoriteStatus(getLoginAccount(), "project", project, ret);
        setAttr("ret", ret);
    }
}
