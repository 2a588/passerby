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

package com.icuscn.passerby.project;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.pageview.PageViewInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.icuscn.passerby.common.model.Project;
//import com.hongson.xing.my.favorite.FavoriteService;
//import com.hongson.xing.my.like.LikeService;
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
