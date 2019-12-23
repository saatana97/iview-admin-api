package cn.saatana.module.system.file.entity;

import cn.saatana.core.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Getter
@Setter
@Entity
@Table(name = "resource")
public class Resource extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private String type;
	private String label;
	private String path;
	@Transient
	private MultipartFile file;
}
