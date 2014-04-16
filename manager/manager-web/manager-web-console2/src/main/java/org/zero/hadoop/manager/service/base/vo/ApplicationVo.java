package org.zero.hadoop.manager.service.base.vo;

import org.zero.hadoop.manager.util.BaseVo;

/**
 * 어플리케이션 정보 테이블 매핑 Vo
 * @author SengWook
 *
 */
public class ApplicationVo extends BaseVo {

	private String app_Id;
	private String app_Name;
	private String app_Ver;
	private String app_Processor_Name;
	private String cluster_Yn;
	
	public ApplicationVo() {
		
	}
	
	public ApplicationVo(String app_Id) {
		this.app_Id = app_Id;
	}

	
	public String getApp_Id() {
		return app_Id;
	}
	public void setApp_Id(String app_Id) {
		this.app_Id = app_Id;
	}
	public String getApp_Name() {
		return app_Name;
	}
	public void setApp_Name(String app_Name) {
		this.app_Name = app_Name;
	}
	public String getApp_Ver() {
		return app_Ver;
	}
	public void setApp_Ver(String app_Ver) {
		this.app_Ver = app_Ver;
	}
	public String getCluster_Yn() {
		return cluster_Yn;
	}
	public void setCluster_Yn(String cluster_Yn) {
		this.cluster_Yn = cluster_Yn;
	}
	
	public String getName_Ver() {
		return app_Name + app_Ver;
	}

	public String getApp_Processor_Name() {
		return app_Processor_Name;
	}

	public void setApp_Processor_Name(String app_Processor_Name) {
		this.app_Processor_Name = app_Processor_Name;
	}

	@Override
	public String toString() {
		return "ApplicationVo [app_Id=" + app_Id + ", app_Name=" + app_Name + ", app_Ver=" + app_Ver + ", app_Processor_Name=" + app_Processor_Name
				+ ", cluster_Yn=" + cluster_Yn + "]";
	}
	
	
}
