package cn.saatana.module.system.role.entity;

import cn.saatana.core.common.BaseEntity;
import cn.saatana.module.system.menu.entity.Menu;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true, exclude = { "menus" })
@Entity
@Table(name = "role")
public class Role extends BaseEntity {
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
