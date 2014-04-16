package org.zero.hadoop.manager.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.zero.hadoop.manager.service.common.dao.SeqGeneratorDao;

@Component
public class SeqManager {

	@Autowired
	SeqGeneratorDao generatorDao;
	
	
	public String getSeq(String seqName) 
	{
		if(StringUtils.isEmpty(seqName)) {
			throw new RuntimeException("Sequence Name Can't Null");
		}
		generatorDao.incSeq(seqName);
		return generatorDao.getLastSeq(seqName);
	}
}
