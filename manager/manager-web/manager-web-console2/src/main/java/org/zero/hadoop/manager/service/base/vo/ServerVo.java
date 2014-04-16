package org.zero.hadoop.manager.service.base.vo;

import org.zero.hadoop.manager.util.BaseVo;

/**
 * 서버 정보 테이블 매핑 Vo
 * 
 * @author SengWook
 * 
 */
public class ServerVo extends BaseVo {

	private String server_Id;
	private String server_Name;
	private String host_Info;
	private String user_Id;
	private String user_Pwd;
	private String group_Id;

	public ServerVo() {
	}

	public ServerVo(String server_Id, String group_Id) {
		super();
		this.server_Id = server_Id;
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

	public String getHost_Info() {
		return host_Info;
	}

	public void setHost_Info(String host_Info) {
		this.host_Info = host_Info;
	}

	public String getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}

	public String getUser_Pwd() {
		return user_Pwd;
	}

	public void setUser_Pwd(String user_Pwd) {
		this.user_Pwd = user_Pwd;
	}

	public String getGroup_Id() {
		return group_Id;
	}

	public void setGroup_Id(String group_Id) {
		this.group_Id = group_Id;
	}

	@Override
	public String toString() {
		return "ServerVo [server_Id=" + server_Id + ", server_Name=" + server_Name + ", host_Info=" + host_Info + ", user_Id=" + user_Id
				+ ", user_Pwd=" + user_Pwd + ", group_Id=" + group_Id + ", toString()=" + super.toString() + "]";
	}
}
