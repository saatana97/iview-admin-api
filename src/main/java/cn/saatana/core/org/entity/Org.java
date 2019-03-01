package cn.saatana.core.org.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.saatana.core.common.AccessScopeable;
import cn.saatana.core.common.CommonEntity;

/**
 * 组织机构
 *
 * @author 向文可
 *
 */
@Entity
@Table(name = "org")
public class Org extends CommonEntity implements AccessScopeable {
	private static final long serialVersionUID = 1L;
	private String name;

	private String code;

	private String type;

	private String level;
	@JSONField(serialize = false)
	@JsonIgnore
	@Where(clause = WHERE_CLAUSE)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Org parent;

	@Transient
	private Set<Org> children = new HashSet<>();

	@JsonGetter
	public Integer getParentId() {
		Integer res = null;
		if (parent != null) {
			res = parent.getId();
		}
		return res;
	}

	@Override
	public Integer getScope() {
		Integer res = null;
		if (scope != null) {
			res = scope;
		} else {
			res = getId();
		}
		return res;
	}

	@Override
	public void setScope(Integer scope) {
		this.scope = scope;
	}

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

	public Set<Org> getChildren() {
		return children;
	}

	public void setChildren(Set<Org> children) {
		this.children = children;
	}

}
