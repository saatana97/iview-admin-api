package cn.saatana.core.common;

import javax.persistence.Transient;

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
	private int index = 1;
	@Transient
	private int limit = 10;

	@JsonIgnore
	public int getIndex() {
		return index;
	}

	@JsonProperty
	public void setIndex(int index) {
		this.index = index;
	}

	@JsonIgnore
	public int getLimit() {
		return limit;
	}

	@JsonProperty
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
