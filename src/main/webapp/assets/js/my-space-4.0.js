// 个人空间 js

/**
 * 初始化个人空间
 */
$(document).ready(function() {
	setCurrentMySpaceMenu();
});

/**
 * 选中个人空间左侧菜单
 */
function setCurrentMySpaceMenu() {
	var pathName = location.pathname;
	if (pathName == '/my' || pathName == '/my/hot' || pathName == '/my/referMe') {
		// $("#my_space_menu [home='true']").addClass("active");
		$("#my_space_menu a[href='/my']").addClass("active");
		return ;
	}
	
	$("#my_space_menu a[href]").each(function(index, element) {
		var href = $(element).attr("href");
		if (href != '/my' && pathName.indexOf(href) != -1) {
			var currentMenu = $("#my_space_menu a[href='" + href + "']");
			currentMenu.addClass("active");
			return false;	// return false 终止后续循环，提升效率
		}
	});
}

/**
 * 选中个人空间 newsfeed 的 tab
 */
function setCurrentNewsFeedTab() {
	var url = location.pathname, navMenus = $("#newsfeed_tab a");
	if (url == '/my') {
		navMenus.eq(0).addClass("active");
	} else if (url.indexOf('/my/hot') != -1) {
		navMenus.eq(1).addClass("active");
    } else if (url.indexOf('/my/referMe') != -1) {
		navMenus.eq(2).addClass("active");
	}
}

/**
 * 设置某用户空间菜单，url: "/user/id"
 * 需要在 #define js() 中手动调用一次，默认没有调用
 */
function setCurrentUserMenu() {
	var pathName = location.pathname;
	
	$("#user_space_menu a[href]").each(function(index, element) {
		var href = $(element).attr("href");
		if (pathName.indexOf(href) != -1) {
			var currentMenu = $("#user_space_menu a[href='" + href + "']");
			currentMenu.addClass("active");
			return false;	// return false 终止后续循环，提升效率
		}
	});
}


