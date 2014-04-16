package org.zero.hadoop.manager.service.app.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

public class HadoopVo extends ServerApplicationVo{

	private boolean name_Node = false;
	private boolean secondary_Node = false;
	private boolean data_Node = false;
	
	@Override
	public String getApp_Ext_Opts() {
		List<String> appExtOps = new ArrayList<String>();
		if(name_Node)
			appExtOps.add("NN");
		if(secondary_Node)
			appExtOps.add("SN");
		if(data_Node)
			appExtOps.add("DN");
		
		return StringUtils.arrayToDelimitedString(appExtOps.toArray(new String[0]), ":");
	}

	@Override
	public void setApp_Ext_Opts(String app_Ext_Opts) {
		if(app_Ext_Opts == null || app_Ext_Opts.isEmpty()) {
			super.setApp_Ext_Opts(app_Ext_Opts);
		}
		if(app_Ext_Opts.indexOf("NN") >= 0) {
			name_Node = true;
		}
		if(app_Ext_Opts.indexOf("SN") >= 0) {
			secondary_Node = true;
		}
		if(app_Ext_Opts.indexOf("DN") >= 0) {
			data_Node = true;
		}
	}

	public boolean isName_Node() {
		return name_Node;
	}

	public void setName_Node(boolean name_Node) {
		this.name_Node = name_Node;
	}

	public boolean isSecondary_Node() {
		return secondary_Node;
	}

	public void setSecondary_Node(boolean secondary_Node) {
		this.secondary_Node = secondary_Node;
	}

	public boolean isData_Node() {
		return data_Node;
	}

	public void setData_Node(boolean data_Node) {
		this.data_Node = data_Node;
	}

	@Override
	public String toString() {
		return super.toString()+ "\n HadoopVo [name_Node=" + name_Node + ", secondary_Node=" + secondary_Node + ", data_Node=" + data_Node + "]";
	}
	
}
