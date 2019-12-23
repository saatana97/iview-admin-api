package cn.saatana.module.system.dict.entity;

import cn.saatana.core.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "dict")
public class Dictionary extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String type;
	private String code;
	private String label;
	private Integer value;
	private Integer sort;

	public Dictionary() {
	}

	public Dictionary(String code, Integer value) {
		this.code = code;
		this.value = value;
	}
}
