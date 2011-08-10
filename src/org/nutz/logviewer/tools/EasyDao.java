package org.nutz.logviewer.tools;

import org.apache.commons.dbcp.BasicDataSource;
import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.ioc.impl.PropertiesProxy;

/**
 * 易于配置的NutDao,适合非Web环境使用
 * @author wendal1985@gmail.com
 *
 */
public class EasyDao extends NutDao {
	
	private static Dao me;
	
	public static final Dao me() {
		if (me == null)
			me = new  EasyDao();
		return me;
	}

	public EasyDao() {
		BasicDataSource bs = new BasicDataSource();
		PropertiesProxy pp = new PropertiesProxy();
		pp.setPaths("db.properties");
		bs.setDriverClassName(pp.get("driver", "org.hsqldb.jdbcDriver"));
		bs.setUrl(pp.get("url", "jdbc:hsqldb:file:data/logdb"));
		bs.setUsername(pp.get("username", "sa"));
		bs.setPassword(pp.get("password", "sa"));
		setDataSource(bs);
	}
}
