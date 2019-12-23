package cn.saatana.module.system.menu.controller;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.HasPermission;
import cn.saatana.module.system.user.entity.User;
import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.module.system.menu.entity.Menu;
import cn.saatana.module.system.menu.repository.MenuRepository;
import cn.saatana.module.system.menu.service.MenuService;
import cn.saatana.module.system.role.entity.Role;
import cn.saatana.module.system.role.service.RoleService;
import cn.saatana.core.utils.TreeUtils;
import cn.saatana.core.utils.tree.TreeNode;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/menu")
@HasPermission("menuManager")
public class MenuController extends CurdController<MenuService, MenuRepository, Menu> {
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
			all.addAll(service.findList(new Menu()));
		} else {
			User auth = super.currentAuth();
			if (auth != null) {
				all.addAll(auth.getMenu());
			}
		}
		List<TreeNode<Menu>> tree = TreeUtils.buildTree(all);
		return Res.ok(tree);
	}

	@RequestMapping("tree/{roleId}")
	public Res<List<TreeNode<Menu>>> roleMenuTree(@PathVariable(required = true) String roleId) {
		List<TreeNode<Menu>> data = tree().getData();
		Set<Menu> menus = null;
		Role role = roleService.get(roleId);
		if (role != null) {
			menus = role.getMenus();
		}
		if (menus != null) {
			Set<String> menuIds = new HashSet<>();
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
		if(!StringUtils.isEmpty(entity.getRouter())){
			if (menu.getParent() != null) {
				entity.setRouter(entity.getRouter().replace(menu.getParent().getRouter(), ""));
			}
			entity.setRouter(buildRouter(entity));
		}
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
			parent = service.get(parent.getId());
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
}
