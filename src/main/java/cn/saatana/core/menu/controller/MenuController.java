package cn.saatana.core.menu.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.menu.entity.Menu;
import cn.saatana.core.menu.repository.MenuRepository;
import cn.saatana.core.menu.service.MenuService;
import cn.saatana.core.role.entity.Role;

@RestController
@RequestMapping("/menu")
@LogOparetion("菜单管理")
public class MenuController extends CommonController<MenuService, MenuRepository, Menu> {

	@RequestMapping("menuAll")
	@Override
	public Res<List<Menu>> all() {
		return super.all();
	}

	@RequestMapping("tree")
	public Res<List<Menu>> tree() {
		Set<Menu> all = new HashSet<>();
		if (Safer.isSuperAdmin()) {
			all.addAll(service.findAll());
		} else {
			Set<Role> roles = Safer.currentAuthInfo().getAuth().getRoles();
			roles.forEach(role -> {
				all.addAll(role.getMenus());
			});
		}
		Map<Integer, Menu> map = new HashMap<>();
		all.forEach(menu -> {
			map.put(menu.getId(), menu);
		});
		List<Integer> childrenId = new ArrayList<>();
		all.forEach(item -> {
			Integer parent = item.getParentId();
			if (parent != null) {
				map.get(parent).getChildren().add(item);
				childrenId.add(item.getId());
			}
		});
		List<Menu> root = new ArrayList<>();
		map.forEach((key, value) -> {
			if (!childrenId.contains(key)) {
				root.add(value);
			}
		});
		return Res.ok(root);
	}

	@PostMapping("create")
	@Override
	public Res<Menu> create(@RequestBody Menu entity, BindingResult result) throws UnsupportedEncodingException {
		entity.setRouter(buildRouter(entity));
		return super.create(entity, result);
	}

	/**
	 * 根据上级菜单重新构建路由路径
	 *
	 * @param menu
	 * @return
	 */
	public String buildRouter(Menu menu) {
		String res = menu.getRouter();
		String base = "";
		Menu parent = menu.getParent();
		if (parent != null) {
			base = parent.getRouter();
			if (res.startsWith(base)) {
				base = "";
			}
		}
		int i = 0;
		if (base.endsWith("/")) {
			i++;
		}
		if (res.startsWith("/")) {
			i++;
		}
		switch (i) {
		case 0:
			res = base + "/" + res;
			break;
		case 1:
			res = base + res;
			break;
		case 2:
			res = (base + res).replace("//", "/");
			break;
		}
		return res;
	}

	@RequestMapping("checkRepeat")
	public Res<Boolean> repeat(Menu entity) {
		return Res.ok(service.findList(entity).size() == 0);
	}
}
