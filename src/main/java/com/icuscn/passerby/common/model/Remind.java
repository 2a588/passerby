package com.icuscn.passerby.common.model;

import com.icuscn.passerby.common.model.base.BaseRemind;

/**
 * 提醒，用于在右上角弹出层显示各类提醒
 * accountId   int(11)    主键
 * referMe      int(11)    @提到我 的消息条数
 * message     int(11)    私信 消息条数，暂时不用
 */
@SuppressWarnings("serial")
public class Remind extends BaseRemind<Remind> {
    // 提醒类型
	public static final int TYPE_REFER_ME = 0;      // 非数据库字段，仅用于在业务层判断所操作的类型
    public static final int TYPE_MESSAGE = 1;       // 非数据库字段，仅用于在业务层判断所操作的类型
    public static final int TYPE_FANS = 2;          // 非数据库字段，仅用于在业务层判断所操作的类型
}
