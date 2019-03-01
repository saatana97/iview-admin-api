package cn.saatana.core.auth.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.saatana.core.common.CommonEntity;
import cn.saatana.core.role.entity.Role;

@Entity
@Table(name = "authorizer")
public class Authorizer extends CommonEntity {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	@Column(name = "login_date")
	private Date loginDate;
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "r_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();

	public Authorizer() {
	}

	public Authorizer(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@JSONField(serialize = false)
	@JsonIgnore
	public Set<Integer> getAccessScopes() {
		Set<Integer> res = new HashSet<>();
		return res;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JSONField(serialize = false)
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JSONField(serialize = true)
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
