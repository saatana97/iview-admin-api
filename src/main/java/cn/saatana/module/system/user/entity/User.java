package cn.saatana.module.system.user.entity;

import cn.saatana.core.common.BaseEntity;
import cn.saatana.module.system.menu.entity.Menu;
import cn.saatana.module.system.org.entity.Org;
import cn.saatana.module.system.role.entity.Role;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	@Transient
	private String code;
	private String openId;
	@Column(name = "login_date")
	private Date loginDate;
	@Where(clause = WHERE_CLAUSE)
	@ManyToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinTable(name = "r_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<Role> roles = new HashSet<>();
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	private Org org;

	private String name;
	// @JsonFormat(pattern = "yyyy年MM月dd日", timezone = "GMT+8")
	private Date birthday;

	private String address;

	private String mobilePhone;

	private String telPhone;

	private String email;

	private Integer sex;

	private String description;

	public User() {
	}

	public User(String openId) {
		this.openId = openId;
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonGetter
	public Set<Menu> getMenu() {
		Set<Menu> res = new HashSet<>();
		this.roles.forEach(role -> {
			res.addAll(role.getMenus());
		});
		return res;
	}

	@JsonGetter
	public String getAge() {
		String res = "不详";
		if (birthday != null) {
			res = NumberFormat.getIntegerInstance()
					.format((System.currentTimeMillis() - birthday.getTime()) / 31557600000l) + "周岁";
		}
		return res;
	}

	@JsonIgnore
	public Set<Integer> getAccessScopes(){
		return null;
	}
}
