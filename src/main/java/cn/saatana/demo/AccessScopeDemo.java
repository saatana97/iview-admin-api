package cn.saatana.demo;

import java.util.List;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.util.JdbcConstants;

/**
 * 数据级权限示例
 *
 * @author 向文可
 *
 */
public class AccessScopeDemo {
	public static void main(String[] args) {
		String sql1 = "select a.* from user a left join role b on a.role = b.id where name = 'admin'";
		String sql2 = "select a.* from user a where name = 'admin'";
		String sql3 = "select * from user where name = 'admin'";
		String sql4 = "select a.*,b.* from user a inner join role b on a.role = b.id";
		System.out.println(scope(sql1));
		System.out.println();
		System.out.println(scope(sql2));
		System.out.println();
		System.out.println(scope(sql3));
		System.out.println();
		System.out.println(scope(sql4));
	}

	public static String scope(String sql) {
		SQLSelectStatement select = (SQLSelectStatement) SQLUtils.parseStatements(sql, JdbcConstants.MYSQL).get(0);
		SQLTableSource table = select.getSelect().getQueryBlock().getFrom();
		if (table instanceof SQLJoinTableSource) {
			do {
				SQLJoinTableSource join = (SQLJoinTableSource) table;
				table = join.getLeft();
			} while (table instanceof SQLJoinTableSource);
		}
		String tableAliasName = table.computeAlias();
		List<Integer> userAccessScopes = null;// null表示不进行数据级筛选；空集合表示没有任何权限
		// List<Integer> userAccessScopes = Arrays.asList(new Integer[] { 1, 2, 3 });
		return SQLUtils.addCondition(sql, generateAccessScopesSql(userAccessScopes, tableAliasName),
				SQLBinaryOperator.BooleanAnd, true, JdbcConstants.MYSQL);
	}

	public static String generateAccessScopesSql(List<Integer> scopes, String tableAliasName) {
		StringBuilder sb = new StringBuilder();
		sb.append(tableAliasName);
		sb.append(".scope ");
		if (scopes == null) {
			sb.append("is null");
		} else {
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
}
