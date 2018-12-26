package cn.saatana.core.org.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.saatana.core.common.CommonEntity;

/**
 * 组织机构
 *
 * @author 向文可
 *
 */
@Entity
@Table(name = "org")
public class Org extends CommonEntity {
	private static final long serialVersionUID = 1L;
	private String name;

	private String code;

	private String type;

	private String level;

	@ManyToOne(cascade = CascadeType.REFRESH)
	private Org parent;

	@Transient
	private List<Org> children = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Org getParent() {
		return parent;
	}

	public void setParent(Org parent) {
		this.parent = parent;
	}

	public List<Org> getChildren() {
		return children;
	}

	public void setChildren(List<Org> children) {
		this.children = children;
	}
}
