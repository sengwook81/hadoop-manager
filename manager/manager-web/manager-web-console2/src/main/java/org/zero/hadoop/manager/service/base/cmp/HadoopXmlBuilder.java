package org.zero.hadoop.manager.service.base.cmp;

import java.util.List;

import org.springframework.stereotype.Component;
import org.zero.hadoop.manager.service.app.vo.ApplicationConfigPropertyExtVo;

@Component("hadoop-xml")
public class HadoopXmlBuilder extends ConfigBuilder{

	@Override
	protected String buildData(List<ApplicationConfigPropertyExtVo> data) {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<?xml version=\"1.0\"\n");
		buffer.append("<?xml-stylesheet type=\"text/xsl\" href=\"configuration.xsl\"?>\n");
		buffer.append("<configuration>\n");
		for(ApplicationConfigPropertyExtVo item : data) {
			buffer.append("\t<property>\n");
			buffer.append("\t\t<name>");
			buffer.append(item.getProp_Key());
			buffer.append("</name>\n");
			buffer.append("\t\t<value>");
			buffer.append(item.getProp_Val());
			buffer.append("</value>\n");
			buffer.append("\t</property>\n");
		}
		buffer.append("</configuration>\n");
		
		return buffer.toString();
	}

}
