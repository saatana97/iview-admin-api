package cn.saatana.modules.user.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;

import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.common.CommonEntity;

@Entity
@Table(name = "user")
public class User extends CommonEntity {
	private static final long serialVersionUID = 1L;
	@Where(clause = WHERE_CLAUSE)
	@OneToOne(cascade = CascadeType.ALL)
	private Authorizer authorizer;

	private String name;

	private String birthday;

	private String address;

	private String mobilePhone;

	private String telPhone;

	private String email;

	private String sex;

	private String type;

	private String description;

	public Authorizer getAuthorizer() {
		return authorizer;
	}

	public void setAuthorizer(Authorizer authorizer) {
		this.authorizer = authorizer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
