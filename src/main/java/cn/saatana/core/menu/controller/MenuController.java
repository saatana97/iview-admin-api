package cn.saatana.core.menu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.menu.entity.Menu;
import cn.saatana.core.menu.repository.MenuRepository;
import cn.saatana.core.menu.service.MenuService;

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
		// TODO 将查询全部菜单改为查询当前用户有权限查看的菜单
		List<Menu> all = service.findAll();
		Map<Integer, Menu> map = new HashMap<>();
		all.forEach(item -> {
			map.put(item.getId(), item);
		});
		List<Integer> childrenId = new ArrayList<>();
		all.forEach(item -> {
			Menu parent = item.getParent();
			if (parent != null) {
				parent.getChildren().add(item);
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

	@RequestMapping("checkRepeat")
	public Res<Boolean> repeat(Menu entity) {
		return Res.ok(service.findList(entity).size() == 0);
	}
}
