package org.zero.hadoop.manager.service.base.vo;

import java.util.List;

/**
 * 어플리케이션 Structure 정보 테이블 매핑 Vo
 * @author SengWook
 *
 */
public class ApplicationSVo extends ApplicationVo {

	private List<ApplicationConfigVo> configs;


	@Override
	public String toString() {
		return "ApplicationSVo [configs=" + configs + ", toString()=" + super.toString() + "]";
	}


	public List<ApplicationConfigVo> getConfigs() {
		return configs;
	}


	public void setConfigs(List<ApplicationConfigVo> configs) {
		this.configs = configs;
	}
}
