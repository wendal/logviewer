package org.nutz.logviewer.api;

import java.io.InputStream;

import org.nutz.dao.Dao;

public interface LogAnalyzer {

	void analysis(InputStream ins);
	void setCategory(String category);
	void setDao(Dao dao);
	void setCallback(Runnable runnable);
}
