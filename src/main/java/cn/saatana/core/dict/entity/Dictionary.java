package cn.saatana.core.dict.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.saatana.core.common.CommonEntity;

@Entity
@Table(name = "dict")
public class Dictionary extends CommonEntity {
	private static final long serialVersionUID = 1L;
	private String label;
	private String value;
	private String type;

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
