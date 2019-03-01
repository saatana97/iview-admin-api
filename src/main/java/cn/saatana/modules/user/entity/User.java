package cn.saatana.modules.user.entity;

import java.text.NumberFormat;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Where;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;

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

	@DateTimeFormat(pattern = "yyyy年MM月dd日")
	@JsonFormat(pattern = "yyyy年MM月dd日")
	private Date birthday;

	private String address;

	private String mobilePhone;

	private String telPhone;

	private String email;

	private Integer sex;

	private String description;

	@JsonGetter
	public String getAge() {
		String res = "不详";
		if (birthday != null) {
			res = NumberFormat.getIntegerInstance()
					.format((System.currentTimeMillis() - birthday.getTime()) / 31557600000l) + "周岁";
		}
		return res;
	}

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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
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

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
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
