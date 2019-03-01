package cn.saatana.core.menu.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.saatana.core.common.AccessScopeable;
import cn.saatana.core.common.CommonEntity;

@Entity
@Table(name = "menu")
public class Menu extends CommonEntity implements AccessScopeable {
	private static final long serialVersionUID = 1L;

	private String code;

	private String title;

	@JSONField(serialize = false)
	@JsonIgnore
	@Where(clause = WHERE_CLAUSE)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Menu parent;

	private String icon;

	private String router;

	private Integer sort;

	@Transient
	private Set<Menu> children = new HashSet<>();

	public Set<Menu> getChildren() {
		return children;
	}

	public void setChildren(Set<Menu> children) {
		this.children = children;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@JsonGetter
	public Integer getParentId() {
		Integer res = null;
		if (parent != null) {
			res = parent.getId();
		}
		return res;
	}

	public Menu getParent() {
		return parent;
	}

	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public Integer getScope() {
		return this.scope;
	}

	@Override
	public void setScope(Integer scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		// return "{title:" + title + ", id:" + getId() + "}";
		return JSONObject.toJSONString(this);
	}
}
