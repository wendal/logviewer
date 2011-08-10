package org.nutz.logviewer.impl;

import java.io.InputStream;
import java.util.List;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.logviewer.api.LogAnalyzer;
import org.nutz.logviewer.tools.EasyDao;
import org.nutz.resource.NutResource;
import org.nutz.resource.Scans;

public class LogManager {
	
	private static final Log log = Logs.get();

	public static void loadLogFiles(String category,String...paths){
		for (String path : paths) {
			loadLogs(category, Scans.me().scan(path));
		}
	}
	
	public static void loadLogs(String category,List<NutResource> logFiles) {
		for (NutResource nutResource : logFiles) {
			try {
				loadLog(category, nutResource.getInputStream());
			}
			catch (Throwable e) {
				log.warn("Fail to load log file = "+ nutResource, e);
			}
		}
	}
	
	public static void loadLog(String category, InputStream ins) throws Throwable{
		LogAnalyzer analyzer = new DefalutLogAnalyzer();
		analyzer.setCategory(category);
		analyzer.setDao(EasyDao.me());
		analyzer.analysis(ins);
	}
}
