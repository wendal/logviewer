package org.nutz.logviewer;

import org.nutz.dao.Sqls;
import org.nutz.dao.TableName;
import org.nutz.lang.Stopwatch;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.logviewer.impl.LogManager;
import org.nutz.logviewer.meta.LogRecord;
import org.nutz.logviewer.tools.EasyDao;

public class Main {
	
	private static final Log log = Logs.get();

	public static void main(String[] args) {
		//TableName.set("logviewer");
		//EasyDao.me().create(LogRecord.class, true);
		//List<LogRecord> list = EasyDao.me().query(LogRecord.class, null, null);
		//System.out.println(list.toString());
		
		log.info("XXX");
		EasyDao.me().execute(Sqls.create("CREATE ALIAS IF NOT EXISTS FT_INIT FOR \"org.h2.fulltext.FullTextLucene.init\""));
		EasyDao.me().execute(Sqls.create("CALL FT_INIT()"));
		TableName.set("nginx");
		EasyDao.me().drop(LogRecord.class);
		Stopwatch sw = Stopwatch.begin();
		LogManager.loadLogFiles("nginx", "mylogs");
		sw.stop();
		long totol = EasyDao.me().count(LogRecord.class);
		System.out.printf("Time use %dms, count=%d",sw.getDuration(),totol);
	}

}
