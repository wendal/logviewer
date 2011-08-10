package org.nutz.logviewer.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.nutz.dao.Sqls;
import org.nutz.dao.TableName;
import org.nutz.lang.Streams;
import org.nutz.lang.Strings;
import org.nutz.logviewer.meta.LogRecord;

public class DefalutLogAnalyzer extends AbstratLogAnalyzer {
	
	private static final SimpleDateFormat dateA = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static final SimpleDateFormat dateB = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
	private static final SimpleDateFormat dateC = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS");
	
	private static final int batchSize = 1000;
	
	@Override
	public void analysis(InputStream ins) {
		try {
			if(Strings.isBlank(getCategory()))
				setCategory("common");
			TableName.set(getCategory());
			getDao().create(LogRecord.class, false);
			//getDao().execute(Sqls.createf("CALL FT_CREATE_INDEX('PUBLIC', 'log_%s', NULL)",getCategory()));
			BufferedReader br = Streams.buffr(Streams.utf8r(ins));
			LogRecord record = null;
			List<LogRecord> res = new ArrayList<LogRecord>(batchSize + 100);
			StringBuilder msg = new StringBuilder();
			while(br.ready()){
				String line = br.readLine();
				if(Strings.isBlank(line) || (!line.startsWith("$: ") || !line.matches("^\\$: ([0-9]{4}[-/][0-9]{2}[-/][0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2})(,[0-9]{3}){0,1} > .+$"))) {
					if(msg.length() < 20000)
						msg.append('\n').append(msg);
					//else
					//	System.out.println(line);
					continue;
				}
				//看来是新纪录,把上一条记录保存下来先
				if(record != null) {
					if(msg.length() > 20000)
						msg.setLength(20000);
					record.setMessage(msg.toString());
					res.add(record);
					if(res.size() >= batchSize) {
						getDao().fastInsert(res);
						res.clear();
					}
				}
				record = new LogRecord();
				msg.setLength(0);
				int first_p = line.indexOf(" > ");
				int msg_start_p = first_p;
				//处理时间
				String date_str = Strings.trim(line.substring(3,first_p));
				if(date_str.indexOf('/') > 0)
					record.setTime(dateB.parse(date_str));
				else if(date_str.indexOf(',') > 0)
					record.setTime(dateC.parse(date_str));
				else
					record.setTime(dateA.parse(date_str));
				int secen_p = line.indexOf(" > ",first_p+1);
				if(secen_p > 0) { //看来有Level,处理Level
					String level_str = line.substring(first_p+3,secen_p);
					record.setLevel(Strings.trim(level_str).toUpperCase());
					msg_start_p = secen_p;
					int third_p = line.indexOf(" > ",secen_p + 1);
					if (third_p > 0) { //看来有附加信息
						String extInfo = line.substring(secen_p + 3, third_p);
						record.setExtInfo(extInfo);
						msg_start_p = third_p;
					}
				}
				String msg_str = line.substring(msg_start_p + 3);
				msg.append(msg_str);
			}
			if(res.size() > 0)
				getDao().fastInsert(res);
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
		//执行回调
		if(getCallback() != null)
			getCallback().run();
	}
}
