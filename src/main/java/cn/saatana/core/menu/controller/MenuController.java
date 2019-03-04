package cn.saatana.core.menu.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.HasPermission;
import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.menu.entity.Menu;
import cn.saatana.core.menu.repository.MenuRepository;
import cn.saatana.core.menu.service.MenuService;
import cn.saatana.core.role.entity.Role;
import cn.saatana.core.role.service.RoleService;
import cn.saatana.core.utils.TreeUtils;
import cn.saatana.core.utils.tree.TreeNode;

@RestController
@RequestMapping("/menu")
@HasPermission("menuManager")
@LogOparetion("菜单管理")
public class MenuController extends CommonController<MenuService, MenuRepository, Menu> {
	@Autowired
	private RoleService roleService;

	@RequestMapping("menuAll")
	@Override
	public Res<List<Menu>> all() {
		return super.all();
	}

	@RequestMapping("tree")
	public Res<List<TreeNode<Menu>>> tree() {
		Set<Menu> all = new HashSet<>();
		if (Safer.isSuperAdmin()) {
			all.addAll(service.findAll());
		} else {
			Set<Role> roles = Safer.currentAuthInfo().getAuth().getRoles();
			List<Integer> menuIds = new ArrayList<>();
			roles.forEach(role -> {
				role.getMenus().forEach(menu -> {
					menuIds.add(menu.getId());
				});
			});
			all.addAll(service.findAllByIds(menuIds));
		}
		List<TreeNode<Menu>> tree = TreeUtils.buildTree(all);
		return Res.ok(tree);
	}

	@RequestMapping("tree/{roleId}")
	public Res<List<TreeNode<Menu>>> roleMenuTree(@PathVariable(required = true) Integer roleId) {
		List<TreeNode<Menu>> data = tree().getData();
		Set<Menu> menus = null;
		Role role = roleService.get(roleId);
		if (role != null) {
			menus = role.getMenus();
		}
		if (menus != null) {
			Set<Integer> menuIds = new HashSet<>();
			menus.forEach(menu -> {
				menuIds.add(menu.getId());
			});
			TreeUtils.forEachTree(data, (parent, child) -> {
				if (child.getChildren() == null || child.getChildren().size() == 0) {
					child.setChecked(menuIds.contains(child.getData().getId()));
				}
			});
		}
		return Res.ok(data);
	}

	@Override
	@HasPermission("addMenu")
	public Res<Menu> create(@RequestBody Menu entity, BindingResult result) throws UnsupportedEncodingException {
		entity.setRouter(buildRouter(entity));
		return super.create(entity, result);
	}

	@Override
	public Res<Menu> update(@RequestBody Menu entity, BindingResult result) {
		Menu menu = service.get(entity.getId());
		if (menu.getParent() != null) {
			entity.setRouter(entity.getRouter().replace(menu.getParent().getRouter(), ""));
		}
		entity.setRouter(buildRouter(entity));
		return super.update(entity, result);
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
