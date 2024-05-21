package com.icuscn.passerby.common;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.icuscn.passerby._admin.auth.AdminAuthInterceptor;
import com.icuscn.passerby._admin.auth.AdminAuthKit;
import com.icuscn.passerby._admin.common.PjaxInterceptor;
import com.icuscn.passerby._admin.permission.PermissionDirective;
import com.icuscn.passerby._admin.role.RoleDirective;
import com.icuscn.passerby.common.handler.UrlSeoHandler;
import com.icuscn.passerby.common.interceptor.LoginSessionInterceptor;
import com.icuscn.passerby.common.kit.DruidKit;
import com.icuscn.passerby.common.model.*;
import com.icuscn.passerby.login.LoginService;
//import com.icuscn.passerby.my.friend.FriendInterceptor;
import com.jfinal.config.*;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.JsonRender;
import com.jfinal.server.undertow.UndertowServer;
import com.jfinal.template.Engine;

import java.sql.Connection;

/**
 * JFinalClubConfig
 */
public class PasserbyConfig extends JFinalConfig {
	
	// 使用 jfinal-undertow 时此处仅保留声明，不能有加载代码
	private static Prop p;
	
	private WallFilter wallFilter;
	
	/**
	 * 启动入口，运行此 main 方法可以启动项目，此 main 方法
	 * 可以放置在任意的 Class 类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		UndertowServer.start(PasserbyConfig.class);
	}
	
	/**
	 * PropKit.useFirstFound(...) 使用参数中从左到右最先被找到的配置文件
	 * 从左到右依次去找配置，找到则立即加载并立即返回，后续配置将被忽略
	 */
	static void loadConfig() {
		if (p == null) {
			p = PropKit.useFirstFound("jfinal-club-config-pro.txt", "passerby-config-dev.txt");
		}
	}
	
	public void configConstant(Constants me) {
		loadConfig();
		
		me.setDevMode(p.getBoolean("devMode", false));
		me.setJsonFactory(MixedJsonFactory.me());
		
		// 支持 Controller、Interceptor、Validator 之中使用 @Inject 注入业务层，并且自动实现 AOP
		me.setInjectDependency(true);
		
		// 是否对超类中的属性进行注入
		me.setInjectSuperClass(true);
	}
	
	/**
	 * 路由拆分到 FrontRoutes 与 AdminRoutes 之中配置的好处：
	 * 1：可分别配置不同的 baseViewPath 与 Interceptor
	 * 2：避免多人协同开发时，频繁修改此文件带来的版本冲突
	 * 3：避免本文件中内容过多，拆分后可读性增强
	 * 4：便于分模块管理路由
	 */
	public void configRoute(Routes me) {
		// 改用扫描路由，手动注册路由不再使用，FrontRoutes、AdminRoutes 已被删除
		// me.add(new FrontRoutes());
		// me.add(new AdminRoutes());
		
		/**
		 * 扫描后台路由
		 */
		me.add(new Routes() {
			public void config() {
				// 添加后台管理拦截器，将拦截在此方法中注册的所有 Controller
				addInterceptor(new AdminAuthInterceptor());
				addInterceptor(new PjaxInterceptor());
				
				setBaseViewPath("/_view/_admin");
				
				// 如果被扫描的包在 jar 文件之中，需要添加如下配置：
				// undertow.hotSwapClassPrefix = com.icuscn.passerby._admin.
				scan("com.icuscn.passerby._admin.");
			}
		});
		
		/**
		 * 扫描前台路由
		 * 
		 * 注意：
		 * 1：scan(...) 方法要添加过滤，过滤掉后台路由，否则后台路由会被扫描到，
		 *    造成 baseViewPath 以及 routes 级别的拦截器配置错误
		 *    
		 * 2: 由于 scan(...) 内部避免了重复扫描同一个类，所以需要将扫描前台路由代码
		 *    放在扫描后台路由之前才能验证没有过滤造成的后果
		 */
		me.add(new Routes() {
			public void config() {
				setBaseViewPath("/_view");
				
				// 如果被扫描的包在 jar 文件之中，需要添加如下配置：
				// undertow.hotSwapClassPrefix = com.icuscn.passerby.
				scan("com.icuscn.passerby.", className -> {
					// className 为当前正扫描的类名，返回 true 时表示过滤掉当前类不扫描
					return className.startsWith("com.icuscn.passerby._admin.");
				});
			}
		});
	}
	
	/**
	 * 配置模板引擎，通常情况只需配置共享的模板函数
	 */
	public void configEngine(Engine me) {
		me.setDevMode(p.getBoolean("engineDevMode", false));
		
		// 开启压缩功能，常用配置参数有: ' ' 与 '\n'
		// me.setCompressorOn('\n');
		
		// 配置用于主菜单的 URL，导航到 https://jfinal.com
		me.addSharedObject("SSL", p.get("SSL"));
		
		/**
		 * 用于在页面中使用 field 表达式代替 static field 表达式，节省代码量
		 * 
		 * 例如：
		 *    Feedback.REPORT_BLOCK_NUM
		 *    可代替
		 *    model.common.com.icuscn.passerby.Feedback::REPORT_BLOCK_NUM
		 */
		me.addSharedObject("Feedback", new Feedback());
		me.addSharedObject("Project", new Project());
		me.addSharedObject("Share", new Share());
		me.addSharedObject("Document", new Document());

		// 添加角色、权限指令
		me.addDirective("role", RoleDirective.class);
		me.addDirective("permission", PermissionDirective.class);
		me.addDirective("perm", PermissionDirective.class);		// 配置一个别名指令

		// 添加角色、权限 shared method
		me.addSharedMethod(AdminAuthKit.class);

		me.addSharedFunction("/_view/common/__layout.html");
		me.addSharedFunction("/_view/common/_paginate.html");

		me.addSharedFunction("/_view/_admin/common/__admin_layout.html");
		me.addSharedFunction("/_view/_admin/common/_admin_paginate.html");
	}
	
	/**
	 * 抽取成独立的方法，便于 _Generator 中重用该方法，减少代码冗余
	 */
	public static DruidPlugin getDruidPlugin() {
		loadConfig();
		return new DruidPlugin(p.get("jdbcUrl"), p.get("user"), p.get("password"));
	}
	
	public void configPlugin(Plugins me) {
		DruidPlugin druidPlugin = getDruidPlugin();
		wallFilter = new WallFilter();			// 加强数据库安全
		wallFilter.setDbType("mysql");
		druidPlugin.addFilter(wallFilter);
		druidPlugin.addFilter(new StatFilter());	// 添加 StatFilter 才会有统计数据
		me.add(druidPlugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
		arp.setTransactionLevel(Connection.TRANSACTION_READ_COMMITTED);
		_MappingKit.mapping(arp);
		// 强制指定复合主键的次序，避免不同的开发环境生成在 _MappingKit 中的复合主键次序不相同
		arp.setPrimaryKey("document", "mainMenu,subMenu");
		me.add(arp);
		arp.setShowSql(p.getBoolean("devMode", false));
		
		arp.getEngine().setToClassPathSourceFactory();
		arp.addSqlTemplate("/com/icuscn/passerby/common/_all_sqls.sql");
		
		me.add(new EhCachePlugin());
		me.add(new Cron4jPlugin(p));
	}
	
	public void configInterceptor(Interceptors me) {
		me.add(new LoginSessionInterceptor());
	}
	
	public void configHandler(Handlers me) {
		me.add(DruidKit.getDruidStatViewHandler()); // druid 统计页面功能
		me.add(new UrlSeoHandler());			 	// index、detail 两类 action 的 url seo
	}
	
	/**
	 * 本方法会在 jfinal 启动过程完成之后被回调，详见 jfinal 手册
	 */
	public void onStart() {
		// 调用不带参的 renderJson() 时，排除对 loginAccount、remind 的 json 转换
		JsonRender.addExcludedAttrs(
				LoginService.loginAccountCacheName,
				LoginSessionInterceptor.remindKey
//				FriendInterceptor.followNum,
//				FriendInterceptor.fansNum,
//				FriendInterceptor.friendRelation
		);
		
		// 让 druid 允许在 sql 中使用 union
		// https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter
		wallFilter.getConfig().setSelectUnionCheck(false);
	}
}






