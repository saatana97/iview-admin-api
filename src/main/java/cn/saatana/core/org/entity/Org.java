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
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.saatana.core.common.AccessScopeable;
import cn.saatana.core.common.CommonEntity;
import cn.saatana.core.utils.DictUtils;

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
	private String title;

	private String code;

	private Integer type;

	private Integer level;
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

	@JsonGetter
	public String getTypeLabel() {
		return DictUtils.query("orgType", type);
	}

	@JsonGetter
	public String getLevelLabel() {
		return DictUtils.query("orgLevel", level);
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@JSONField(serialize = false)
	@JsonIgnore
	public Org getParent() {
		return parent;
	}

	@JsonProperty
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
