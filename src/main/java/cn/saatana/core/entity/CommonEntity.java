package cn.saatana.core.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import cn.saatana.core.Safer;

@MappedSuperclass
public abstract class CommonEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_DELETED = 1;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "update_date")
	private Date updateDate;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "create_by")
	private User createBy;
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "update_by")
	private User updateBy;
	@Column(name = "data_status")
	private int dataStatus = 0;

	public void preCreate() {
		UserInfo userInfo = Safer.currentUserInfo();
		if (userInfo != null) {
			this.setCreateBy(userInfo.getUser());
			this.setCreateDate(new Date());
		}
	}

	public void preUpdate() {
		UserInfo userInfo = Safer.currentUserInfo();
		if (userInfo != null) {
			this.setUpdateBy(userInfo.getUser());
			this.setUpdateDate(new Date());
		}
	}

	public void preDelete() {
		preUpdate();
		this.dataStatus = STATUS_DELETED;
	}

	public void preRestore() {
		preUpdate();
		this.dataStatus = STATUS_NORMAL;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public User getCreateBy() {
		return createBy;
	}

	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	public User getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(User updateBy) {
		this.updateBy = updateBy;
	}

	@JsonIgnore
	public int getDataStatus() {
		return dataStatus;
	}

	@JsonProperty
	public void setDataStatus(int dataStatus) {
		this.dataStatus = dataStatus;
	}
}
