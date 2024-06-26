package com.icuscn.passerby.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDocument<M extends BaseDocument<M>> extends Model<M> implements IBean {

	public void setMainMenu(Integer mainMenu) {
		set("mainMenu", mainMenu);
	}
	
	public Integer getMainMenu() {
		return getInt("mainMenu");
	}

	public void setSubMenu(Integer subMenu) {
		set("subMenu", subMenu);
	}
	
	public Integer getSubMenu() {
		return getInt("subMenu");
	}

	public void setTitle(String title) {
		set("title", title);
	}
	
	public String getTitle() {
		return getStr("title");
	}

	public void setContent(String content) {
		set("content", content);
	}
	
	public String getContent() {
		return getStr("content");
	}

	public void setUpdateAt(java.util.Date updateAt) {
		set("updateAt", updateAt);
	}
	
	public java.util.Date getUpdateAt() {
		return get("updateAt");
	}

	public void setCreateAt(java.util.Date createAt) {
		set("createAt", createAt);
	}
	
	public java.util.Date getCreateAt() {
		return get("createAt");
	}

	public void setPublish(Integer publish) {
		set("publish", publish);
	}
	
	public Integer getPublish() {
		return getInt("publish");
	}

}
