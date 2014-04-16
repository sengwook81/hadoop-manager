package org.zero.hadoop.manager.service.node.vo;

public class NodeVo {
	private int node_id;
	private String node_addr;
	private String node_name;
	private String node_home;
	private String node_user;
	private String node_password;
	
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public String getNode_addr() {
		return node_addr;
	}
	public void setNode_addr(String node_addr) {
		this.node_addr = node_addr;
	}
	public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	public String getNode_home() {
		return node_home;
	}
	public void setNode_home(String node_home) {
		this.node_home = node_home;
	}
	public String getNode_user() {
		return node_user;
	}
	public void setNode_user(String node_user) {
		this.node_user = node_user;
	}
	public String getNode_password() {
		return node_password;
	}
	public void setNode_password(String node_password) {
		this.node_password = node_password;
	}
}
