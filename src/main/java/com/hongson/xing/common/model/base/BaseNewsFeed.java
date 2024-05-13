package com.hongson.xing.common.model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseNewsFeed<M extends BaseNewsFeed<M>> extends Model<M> implements IBean {

	public void setId(Integer id) {
		set("id", id);
	}
	
	public Integer getId() {
		return getInt("id");
	}

	public void setAccountId(Integer accountId) {
		set("accountId", accountId);
	}
	
	public Integer getAccountId() {
		return getInt("accountId");
	}

	public void setRefType(Integer refType) {
		set("refType", refType);
	}
	
	public Integer getRefType() {
		return getInt("refType");
	}

	public void setRefId(Integer refId) {
		set("refId", refId);
	}
	
	public Integer getRefId() {
		return getInt("refId");
	}

	public void setRefParentType(Integer refParentType) {
		set("refParentType", refParentType);
	}
	
	public Integer getRefParentType() {
		return getInt("refParentType");
	}

	public void setRefParentId(Integer refParentId) {
		set("refParentId", refParentId);
	}
	
	public Integer getRefParentId() {
		return getInt("refParentId");
	}

	public void setCreateAt(java.util.Date createAt) {
		set("createAt", createAt);
	}
	
	public java.util.Date getCreateAt() {
		return get("createAt");
	}

}