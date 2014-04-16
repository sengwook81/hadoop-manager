package org.zero.hadoop.manager.service.app.vo;

import org.zero.hadoop.manager.util.BaseVo;

public class ServerApplicationVo extends BaseVo {

	private String group_Id;
	private String server_Id;
	private String server_Name;
	private String app_Id;
	private String app_Ext_Opts;
	private String install_Flag;
	private String install_Status;
	
	private boolean row_Flag = false;
	
	public String getGroup_Id() {
		return group_Id;
	}
	public void setGroup_Id(String group_Id) {
		this.group_Id = group_Id;
	}
	public String getServer_Id() {
		return server_Id;
	}
	public void setServer_Id(String server_Id) {
		this.server_Id = server_Id;
	}
	public String getServer_Name() {
		return server_Name;
	}
	public void setServer_Name(String server_Name) {
		this.server_Name = server_Name;
	}
	public String getApp_Id() {
		return app_Id;
	}
	public void setApp_Id(String app_Id) {
		this.app_Id = app_Id;
	}
	public String getApp_Ext_Opts() {
		return app_Ext_Opts;
	}
	public void setApp_Ext_Opts(String app_Ext_Opts) {
		this.app_Ext_Opts = app_Ext_Opts;
	}
	public boolean isRow_Flag() {
		return row_Flag;
	}
	public void setRow_Flag(boolean row_Flag) {
		this.row_Flag = row_Flag;
	}
	public String getInstall_Status() {
		return install_Status;
	}
	public void setInstall_Status(String install_Status) {
		this.install_Status = install_Status;
	}
	public String getInstall_Flag() {
		return install_Flag;
	}
	public void setInstall_Flag(String install_Flag) {
		this.install_Flag = install_Flag;
	}
	
	@Override
	public String toString() {
		return "ServerApplicationVo [group_Id=" + group_Id + ", server_Id=" + server_Id + ", server_Name=" + server_Name + ", app_Id=" + app_Id
				+ ", app_Ext_Opts=" + app_Ext_Opts + ", install_Flag=" + install_Flag + ", install_Status=" + install_Status + ", row_Flag="
				+ row_Flag + "]";
	}
	
	
}
