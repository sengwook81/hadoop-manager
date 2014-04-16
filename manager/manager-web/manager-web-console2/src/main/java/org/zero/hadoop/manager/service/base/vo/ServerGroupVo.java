package org.zero.hadoop.manager.service.base.vo;

import org.zero.hadoop.manager.util.BaseVo;

/**
 * 서버 그룹 정보 테이블 매핑 Vo
 * @author SengWook
 *
org.zero.hadoop.manager.service.base.vo.ServerGroupVo
 */
public class ServerGroupVo extends BaseVo{
	

	private String group_Id;
	private String group_Name;
	private String group_Desc;
	
	
	public ServerGroupVo() { }
	
	public ServerGroupVo(String group_Id) {
		super();
		this.group_Id = group_Id;
	}
	
	public String getGroup_Id() {
		return group_Id;
	}
	public void setGroup_Id(String group_Id) {
		this.group_Id = group_Id;
	}
	public String getGroup_Name() {
		return group_Name;
	}
	public void setGroup_Name(String group_Name) {
		this.group_Name = group_Name;
	}
	public String getGroup_Desc() {
		return group_Desc;
	}
	public void setGroup_Desc(String group_Desc) {
		this.group_Desc = group_Desc;
	}
	@Override
	public String toString() {
		return "ServerGroupVo [group_Id=" + group_Id + ", group_Name=" + group_Name + ", group_Desc=" + group_Desc + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
