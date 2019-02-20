package cn.saatana.core.common;

/**
 * 支持数据级权限
 *
 * @author 向文可
 *
 */
public interface AccessScopeable {
	Integer getScope();

	void setScope(Integer scope);
}
