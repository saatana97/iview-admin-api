package cn.saatana.module.system.menu.entity;

import cn.saatana.core.common.BaseEntity;
import cn.saatana.core.utils.tree.TreeNode;
import cn.saatana.core.utils.tree.Treeable;
import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "menu")
public class Menu extends BaseEntity implements Treeable<Menu> {
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
	public TreeNode<Menu> convertToTreeNode() {
		return new TreeNode<>(getId() + "", getTitle(), getParentId(), this, getSort(), true, false);
	}

	@Override
	public String uniqueCode() {
		return this.getId() + "";
	}

	@Override
	public void formatChildren(List<Menu> children) {
		this.setChildren(children);
	}

	@JsonGetter
	public String getParentId() {
		String res = null;
		if (parent != null) {
			res = parent.getId() + "";
		}
		return res;
	}

	@JsonSetter
	public void setParentId(String parentId) {
		if (!StringUtils.isEmpty(parentId)) {
			if (this.parent == null) {
				this.parent = new Menu();
			}
			this.parent.setId(parentId);
		}
	}

	@JsonIgnore
	public Menu getParent() {
		return parent;
	}

	@JsonProperty
	public void setParent(Menu parent) {
		this.parent = parent;
	}
}
