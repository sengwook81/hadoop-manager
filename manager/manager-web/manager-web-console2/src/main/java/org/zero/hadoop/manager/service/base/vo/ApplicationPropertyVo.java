package org.zero.hadoop.manager.service.base.vo;

import org.zero.hadoop.manager.util.BaseVo;

public class ApplicationPropertyVo extends BaseVo {
	private String app_Id;
	private String config_Id;
	private String prop_Key;
	private String prop_Val;
	private String prop_Desc;
	private String prop_Type;
	public String getApp_Id() {
		return app_Id;
	}
	public void setApp_Id(String app_Id) {
		this.app_Id = app_Id;
	}
	public String getConfig_Id() {
		return config_Id;
	}
	public void setConfig_Id(String config_Id) {
		this.config_Id = config_Id;
	}
	public String getProp_Key() {
		return prop_Key;
	}
	public void setProp_Key(String prop_Key) {
		this.prop_Key = prop_Key;
	}
	public String getProp_Val() {
		return prop_Val;
	}
	public void setProp_Val(String prop_Val) {
		this.prop_Val = prop_Val;
	}
	public String getProp_Desc() {
		return prop_Desc;
	}
	public void setProp_Desc(String prop_Desc) {
		this.prop_Desc = prop_Desc;
	}
	public String getProp_Type() {
		return prop_Type;
	}
	public void setProp_Type(String prop_Type) {
		this.prop_Type = prop_Type;
	}
	@Override
	public String toString() {
		return "ApplicationPropertyVo [app_Id=" + app_Id + ", config_Id=" + config_Id + ", prop_Key=" + prop_Key + ", prop_Val=" + prop_Val
				+ ", prop_Desc=" + prop_Desc + ", prop_Type=" + prop_Type + "]";
	}
	
	
	
}
