package org.zero.hadoop.manager.service.common.dao;

public interface SeqGeneratorDao {

	public String getLastSeq(String seqName);
	public void incSeq(String seqName);
}
