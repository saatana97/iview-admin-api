package cn.saatana.core.dict.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.saatana.core.common.CommonEntity;

@Entity
@Table(name = "dict")
public class Dictionary extends CommonEntity {
	private static final long serialVersionUID = 1L;
	private String type;
	private String code;
	private String label;
	private String value;
	private Integer sort;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
