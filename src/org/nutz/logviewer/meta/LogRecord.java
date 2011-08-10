package org.nutz.logviewer.meta;

import java.util.Date;

import lombok.Data;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Data
@Table("log_${name}")
public class LogRecord {

	@Id
	private long id;
	private Date time;
	private String level;
	@ColDefine(type=ColType.VARCHAR,width=150)
	private String extInfo;
	@ColDefine(type=ColType.VARCHAR,width=20000)
	private String message;
	
}
