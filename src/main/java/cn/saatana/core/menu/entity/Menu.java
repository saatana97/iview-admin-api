package cn.saatana.core.menu.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.common.CommonEntity;

@Entity
@Table(name = "menu")
public class Menu extends CommonEntity {
	private static final long serialVersionUID = 1L;

	private String code;

	private String title;

	@ManyToOne(cascade = CascadeType.REFRESH)
	private Menu parent;

	private String icon;

	private String router;

	private Integer sort;

	@Transient
	private List<Menu> children = new ArrayList<>();

	public List<Menu> getChildren() {
		return children;
	}

	public void setChildren(List<Menu> children) {
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

	@JsonIgnore
	public Menu getParent() {
		return parent;
	}

	@JsonProperty
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

	@JsonIgnore
	@Override
	public Authorizer getCreator() {
		return super.getCreator();
	}

	@JsonIgnore
	@Override
	public Authorizer getUpdator() {
		return super.getUpdator();
	}

}
