package com.icuscn.passerby.document;

import com.icuscn.passerby.common.controller.BaseController;
import com.icuscn.passerby.common.interceptor.AuthCacheClearInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Inject;
import com.icuscn.passerby.common.model.Document;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Path;

import java.util.List;

/**
 * 文档控制器
 */
@Path(value = "/doc", viewPath = "/document")
public class DocumentController extends BaseController {
	
	@Inject
	DocumentService srv;

	@ActionKey("/doc")
	@Before(DocumentSeo.class)
	public void doc() {
		Integer mainMenu = getParaToInt(0);
		Integer subMenu = getParaToInt(1);
		if (mainMenu == null && subMenu == null) {
			mainMenu = 1;
			subMenu = 1;
		}
		if (mainMenu == null || subMenu == null) {
			renderError(404);
		}

		Document doc = srv.findById(mainMenu, subMenu);
		if (doc == null) {
			renderError(404);
		}

		List<Document> menu = srv.getMenu();
		setAttr("mainMenuList", menu);
		setAttr("doc", doc);
		render("index.html");
	}
	
	public void ajaxContent() {
		Integer mainMenu = getInt(0);
		Integer subMenu = getInt(1);
		Document doc = srv.findById(mainMenu, subMenu);
		if (doc != null) {
			setAttr("doc", doc);
			render("_content.html");
		} else {
			renderError(404);
		}
	}
	
	/**
	 * 有时会手动改数据库，需要清下缓存
	 */
	@Before(AuthCacheClearInterceptor.class)
	public void clear() {
		srv.clearCache();
	}
}


