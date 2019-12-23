package cn.saatana.module.system.org.entity;

import cn.saatana.core.common.BaseEntity;
import cn.saatana.core.utils.tree.TreeNode;
import cn.saatana.core.utils.tree.Treeable;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织机构
 *
 * @author 向文可
 *
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "children" })
@Entity
@Table(name = "org")
public class Org extends BaseEntity implements Treeable<Org> {
	private static final long serialVersionUID = 1L;
	private String title;

	private String code;

	private Integer type;

	private Integer level;
	@Where(clause = WHERE_CLAUSE)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Org parent;

	@Transient
	private List<Org> children = new ArrayList<>();

	@Override
	public TreeNode<Org> convertToTreeNode() {
		return new TreeNode<>(getId() + "", getTitle(), getParentId(), this, 0, true, false);
	}

	@Override
	public void formatChildren(List<Org> children) {
		this.setChildren(children);
	}

	@Override
	public String uniqueCode() {
		return getId() + "";
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
				this.parent = new Org();
			}
			this.parent.setId(parentId);
		}
	}

	@JsonIgnore
	public Org getParent() {
		return parent;
	}

	@JsonProperty
	public void setParent(Org parent) {
		this.parent = parent;
	}
}
