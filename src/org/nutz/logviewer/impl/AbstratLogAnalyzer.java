package org.nutz.logviewer.impl;

import lombok.Data;

import org.nutz.dao.Dao;
import org.nutz.logviewer.api.LogAnalyzer;

@Data
public abstract class AbstratLogAnalyzer implements LogAnalyzer {

	private String category;
	private Dao dao;
	private Runnable callback;

}
