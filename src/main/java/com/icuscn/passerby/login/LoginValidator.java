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
 */package com.icuscn.passerby.login;

import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.validate.Validator;

/**
 * ajax 登录参数验证
 */
public class LoginValidator extends Validator {

	protected void validate(Controller c) {
		setShortCircuit(true);
		
		/**
		 * 注入 Ret 对象，validateXxx(...) 方法的验证结果将被存放于该 Ret 对象之中，
		 * 以便于 handleError 中使用：
		 *     controller.renderJson(getRet());
		 * 
		 * 具体到本例，LoginController.doLogin() 中使用的 renderJson(ret)
		 * 与 LoginValidator.handleError() 中使用的 c.renderJson(getRet())
		 * 实现了返回值格式的统一（Ret 设置 state、msg 属性值），所以前端 js 可以
		 * 统一处理返回数据:
		 *     if (ret.state == "ok") {
		 *         location.href = ret.returnUrl;
		 *     } else {
		 *         alert(ret.msg);
		 *     }
		 * 
		 * 否则 Validator 层与 Service 层返回的 Ret 值格式将不同，前端 js 需要
		 * 对两种格式分别做处理
		 */
		setRet(Ret.fail());		// Ret.fail() 将设置 state : "fail" 值

		validateRequired("userName", "msg", "邮箱不能为空");
		validateEmail("userName", "msg", "邮箱格式不正确");

		validateRequired("password", "msg", "密码不能为空");
		validateCaptcha("captcha", "msg", "验证码不正确");
	}

	protected void handleError(Controller c) {
		// getRet() 与 setRet(...) 配合使用
		Ret ret = getRet();
		c.renderJson(ret);
	}
}
