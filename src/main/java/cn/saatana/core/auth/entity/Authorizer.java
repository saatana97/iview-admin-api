package cn.saatana.core.auth.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.saatana.core.common.CommonEntity;
import cn.saatana.core.menu.entity.Menu;
import cn.saatana.core.role.entity.Role;

@Entity
@Table(name = "authorizer")
public class Authorizer extends CommonEntity {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	@Column(name = "login_date")
	private Date loginDate;
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinTable(name = "r_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private List<Role> roles = new ArrayList<>();
	@Transient
	private List<Menu> menus = new ArrayList<>();

	public Authorizer() {
	}

	public Authorizer(String username, String password) {
		this.username = username;
		this.password = password;
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

	public List<Menu> getMenus() {
		return menus;
	}

	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
}
