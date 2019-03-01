package cn.saatana.core.common;

import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 可进行分页查询的类
 *
 * @author 向文可
 *
 */
public abstract class PageQueryable {
	@Transient
	private int page = 1;
	@Transient
	private int limit = 10;

	@JsonIgnore
	@JSONField(serialize = false)
	public int getPage() {
		return page;
	}

	@JsonProperty
	public void setPage(int page) {
		this.page = page;
	}

	@JsonIgnore
	@JSONField(serialize = false)
	public int getLimit() {
		return limit;
	}

	@JsonProperty
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
