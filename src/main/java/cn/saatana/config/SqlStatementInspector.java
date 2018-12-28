package cn.saatana.config;

import java.util.logging.Logger;

import org.hibernate.resource.jdbc.spi.StatementInspector;

public class SqlStatementInspector implements StatementInspector {
	private static final long serialVersionUID = 1L;
	private final Logger log = Logger.getLogger("SqlStatementInspector");

	@Override
	public String inspect(String sql) {
		log.info(sql);
		return sql;
	}

}
