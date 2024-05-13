/**
 * 请勿将俱乐部专享资源复制给任何人，保护知识产权即是保护我们所在的行业，进而保护我们自己的利益
 * 即便是公司的同事，也请尊重 JFinal 作者的努力与付出，不要复制给同事
 * 
 * 如果你尚未加入俱乐部，请立即删除该项目，或者现在加入俱乐部：http://jfinal.com/club
 * 
 * 俱乐部将提供 jfinal-club 项目源码、直播视频、专用 QQ 群，以及作者在俱乐部定期的分享与答疑，
 * 价值远比仅仅拥有 jfinal club 项目源代码要大得多，俱乐部福利资源是不断增加的，以下是俱乐部
 * 新福利计划：
 * https://jfinal.com/club/1-2
 * 
 * JFinal 俱乐部是七年以来首次寻求外部资源的尝试，以便于创建更加高品质的产品与服务，为你带来
 * 更多、更大的价值
 * 
 * JFinal 项目的可持续性发展需要你的支持！！！
 */

package com.hongson.xing._admin.account;

import com.hongson.xing.common.kit.SensitiveWordsKit;
//import com.hongson.xing.reg.RegValidator;
import com.jfinal.core.Controller;
import com.jfinal.kit.Ret;
import com.jfinal.validate.Validator;

/**
 * AccountUpdateValidator 验证账号修改功能表单
 */
public class AccountUpdateValidator extends Validator {
	
	protected void validate(Controller c) {
		setShortCircuit(true);

		/**
		 * 验证 nickName
 		 */
		if (SensitiveWordsKit.checkSensitiveWord(c.getPara("account.nickName")) != null) {
			addError("msg", "昵称不能包含敏感词");
		}
		validateRequired("account.nickName", "msg", "昵称不能为空");
		validateString("account.nickName", 1, 19, "msg", "昵称不能超过19个字");

		String nickName = c.getPara("account.nickName").trim();
		if (nickName.contains("@") || nickName.contains("＠")) { // 全角半角都要判断
			addError("msg", "昵称不能包含 \"@\" 字符");
		}
		if (nickName.contains(" ") || nickName.contains("　")) {
			addError("msg", "昵称不能包含空格");
		}
//		//Ret ret = RegValidator.validateNickName(nickName);
//		if (ret.isFail()) {
//			addError("msg", ret.getStr("msg"));
//		}

		/**
		 * 验证 userName
		 */
		validateRequired("account.userName", "msg", "邮箱不能为空");
		validateEmail("account.userName", "msg", "邮箱格式不正确");
	}

	protected void handleError(Controller c) {
		c.setAttr("state", "fail");
		c.renderJson();
	}
}

