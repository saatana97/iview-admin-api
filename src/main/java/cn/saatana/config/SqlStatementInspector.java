package cn.saatana.config;

import java.util.Set;

import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat.Column;
import com.alibaba.druid.util.JdbcConstants;

import cn.saatana.core.Safer;
import cn.saatana.core.auth.entity.AuthorizationInformation;

@Configuration
public class SqlStatementInspector implements StatementInspector {
	private static final long serialVersionUID = 1L;
	// private final Logger log = Logger.getLogger("SqlStatementInspector");

	/**
	 * 拼接数据权限SQL
	 *
	 * @param scopes
	 *            数据权限范围
	 * @param tableAliasName
	 *            表别名
	 * @return SQL字符串
	 */
	private String generateAccessScopesSql(Set<Integer> scopes, String tableAliasName) {
		StringBuilder sb = new StringBuilder();
		sb.append(tableAliasName);
		sb.append(".scope is null ");
		if (scopes != null && scopes.size() > 0) {
			sb.append("or ");
			sb.append(tableAliasName);
			sb.append(".scope ");
			sb.append("in (");
			scopes.forEach(item -> {
				sb.append(item);
				sb.append(",");
			});
			sb.setLength(sb.length() - 1);
			sb.append(")");
		}
		return sb.toString();
	}

	@Override
	public String inspect(String sql) {
		if (sql.startsWith("select")) {
			// 获取当前登录用户信息
			AuthorizationInformation authInfo = Safer.currentAuthInfo();
			// 超级管理员可以看到任何数据
			if (authInfo != null && !Safer.isSuperAdmin()) {
				SQLSelectStatement select = (SQLSelectStatement) SQLUtils.parseStatements(sql, JdbcConstants.MYSQL)
						.get(0);
				SQLTableSource table = select.getSelect().getQueryBlock().getFrom();
				// 如果是多表查询遍历出主表
				if (table instanceof SQLJoinTableSource) {
					do {
						SQLJoinTableSource join = (SQLJoinTableSource) table;
						table = join.getLeft();
					} while (table instanceof SQLJoinTableSource);
				}
				// 获取Hibernate给表取的别名
				String tableAliasName = table.computeAlias();
				// 获取真实的表名
				MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
				select.accept(visitor);
				// for(Entry<Name, TableStat> entry : visitor.getTables().entrySet()) {
				// System.out.println(entry.getKey().getName());
				// }
				String tableRealName = visitor.getAliasMap().get(tableAliasName);
				visitor.endVisit(select);
				boolean isPrimaryKeyQuery = false;
				SQLExpr where = select.getSelect().getQueryBlock().getWhere();
				if (where != null) {
					where.accept(visitor);
					Column idCol = visitor.getColumn(tableRealName, "id");
					isPrimaryKeyQuery = idCol != null && idCol.isWhere();
				}
				// 如果是关联表或者是主键查询就不校验数据权限
				if (!tableRealName.startsWith("r_") && !isPrimaryKeyQuery) {
					sql = SQLUtils.addCondition(sql,
							generateAccessScopesSql(authInfo.getAuth().getAccessScopes(), tableAliasName),
							SQLBinaryOperator.BooleanAnd, true, JdbcConstants.MYSQL);
				}
			}
		}
		return sql;
	}
}
