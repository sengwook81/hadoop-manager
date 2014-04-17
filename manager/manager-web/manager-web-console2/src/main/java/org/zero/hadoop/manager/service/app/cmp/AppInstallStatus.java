package org.zero.hadoop.manager.service.app.cmp;

public class AppInstallStatus {

	public static final String NEUTRAL = "N";
	public static final String NOW_PROCESSING = "P";
	public static final String ERROR_OCCUR = "E";
	public static final String FINISH_INSTALL = "F";
	public static final String FINISH_UNINSTALL = "U";

	private String flag;
	private String status;
	
	public AppInstallStatus(String flag, String status) {
		super();
		this.flag = flag;
		this.status = status;
	}

	public String getFlag() {
		return flag;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "AppInstallStatus [flag=" + flag + ", status=" + status + "]";
	}
}
