package com.icuscn.passerby.login;

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
