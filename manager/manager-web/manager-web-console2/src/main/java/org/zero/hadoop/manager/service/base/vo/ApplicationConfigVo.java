package org.zero.hadoop.manager.service.base.vo;

import org.zero.hadoop.manager.util.BaseVo;

public class ApplicationConfigVo extends BaseVo {
	private String app_Id;
	private String config_Id;
	private String config_Name;
	private String config_Type;

	public ApplicationConfigVo() {
	}

	public ApplicationConfigVo(String app_Id, String config_Id) {
		super();
		this.app_Id = app_Id;
		this.config_Id = config_Id;
	}

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

	public String getConfig_Name() {
		return config_Name;
	}

	public void setConfig_Name(String config_Name) {
		this.config_Name = config_Name;
	}

	public String getConfig_Type() {
		return config_Type;
	}

	public void setConfig_Type(String config_Type) {
		this.config_Type = config_Type;
	}

	@Override
	public String toString() {
		return "ApplicationConfigVo [app_Id=" + app_Id + ", config_Id=" + config_Id + ", config_Name=" + config_Name + ", config_Type=" + config_Type
				+ ", toString()=" + super.toString() + "]";
	}

}
