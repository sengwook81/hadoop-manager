package org.zero.hadoop.manager.service.app.processor;

import java.util.Calendar;

import org.zero.hadoop.manager.service.app.vo.ServerApplicationVo;

public class ProcessMonitorVo extends ServerApplicationVo {
	private String group_Id;
	private String server_Id;
	private String server_Name;
	private String app_Id;
	private String app_Ext_Opts;
	private String process_Status;
	private String process_Data;
	private Long process_Time;

	public ProcessMonitorVo(ServerApplicationVo item, String process_Status, String process_Data) {
		this.group_Id = item.getGroup_Id();
		this.server_Id = item.getServer_Id();
		this.server_Name = item.getServer_Name();
		this.app_Id = item.getApp_Id();
		this.process_Status = process_Status;
		this.process_Data = process_Data;
		this.process_Time = Calendar.getInstance().getTimeInMillis();
	}

	public String getGroup_Id() {
		return group_Id;
	}

	public String getServer_Id() {
		return server_Id;
	}

	public String getServer_Name() {
		return server_Name;
	}

	public String getApp_Id() {
		return app_Id;
	}

	public String getApp_Ext_Opts() {
		return app_Ext_Opts;
	}

	public String getProcess_Status() {
		return process_Status;
	}

	public String getProcess_Data() {
		return process_Data;
	}

	public Long getProcess_Time() {
		return process_Time;
	}
}
