package cn.saatana.core.role.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonGetter;

import cn.saatana.core.common.CommonEntity;
import cn.saatana.core.menu.entity.Menu;

@Entity
@Table(name = "role")
public class Role extends CommonEntity {
	private static final long serialVersionUID = 1L;
	@Column(name = "name")
	private String name;
	@Column(name = "code")
	private String code;
	@Column(name = "description")
	private String description;
	@Where(clause = WHERE_CLAUSE)
	@OrderBy("sort,title")
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "r_role_menu")
	private Set<Menu> menus = new HashSet<>();

	public Set<Menu> getMenus() {
		return menus;
	}

	public void setMenus(Set<Menu> menus) {
		this.menus = menus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonGetter
	public String getMenusName() {
		StringBuilder sb = new StringBuilder();
		this.menus.forEach(item -> {
			sb.append(item.getTitle());
			sb.append(",");
		});
		if (this.menus.size() != 0) {
			sb.setLength(sb.length() - 1);
		}
		return sb.toString();
	}
}
