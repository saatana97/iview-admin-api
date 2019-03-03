package cn.saatana.core.menu.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.saatana.core.common.CommonEntity;
import cn.saatana.core.utils.tree.TreeNode;
import cn.saatana.core.utils.tree.Treeable;

@Entity
@Table(name = "menu")
public class Menu extends CommonEntity implements Treeable<Menu> {
	private static final long serialVersionUID = 1L;

	private String code;

	private String title;

	@Where(clause = WHERE_CLAUSE)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Menu parent;

	private String icon;

	private String router;

	private Integer sort;

	private Boolean display;

	private String permission;

	@Transient
	private List<Menu> children = new ArrayList<>();

	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}

	@Override
	public TreeNode<Menu> convertToTreeNode() {
		return new TreeNode<>(getId() + "", getTitle(), getParentId(), this, getSort(), true, false);
	}

	@Override
	public String uniqueCode() {
		return this.getId() + "";
	}

	@Override
	public void formatChildren(Collection<Menu> children) {
		this.setChildren(children.parallelStream().collect(Collectors.toList()));
	}

	@JsonGetter
	public String getParentId() {
		String res = null;
		if (parent != null) {
			res = parent.getId() + "";
		}
		return res;
	}

	@JSONField(serialize = false)
	@JsonIgnore
	public Menu getParent() {
		return parent;
	}

	@JsonProperty
	public void setParent(Menu parent) {
		this.parent = parent;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

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
}
