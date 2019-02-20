package cn.saatana.core.role.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.saatana.core.auth.entity.Authorizer;
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
	@ManyToMany
	@JoinTable(name = "r_role_menu")
	private List<Menu> menus = new ArrayList<>();

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
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

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
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
