package cn.saatana;

import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.saatana.core.auth.service.AuthorizerService;
import cn.saatana.core.menu.controller.MenuController;
import cn.saatana.core.menu.entity.Menu;
import cn.saatana.core.menu.service.MenuService;
import cn.saatana.core.role.service.RoleService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuTests {
	@Autowired
	private MenuController c;
	@Autowired
	private MenuService menuService;
	@Autowired
	private AuthorizerService authService;
	@Autowired
	private RoleService roleService;

	// @Test
	public void routerBuilderTest() {
		Menu menu = new Menu();
		Menu parent = new Menu();
		menu.setParent(parent);

		menu.setRouter("user");
		parent.setRouter("/sys");
		System.out.println(c.buildRouter(menu));

		menu.setRouter("/user");
		parent.setRouter("/sys");
		System.out.println(c.buildRouter(menu));

		menu.setRouter("/sys/user");
		parent.setRouter("/sys");
		System.out.println(c.buildRouter(menu));

		menu.setParent(null);

		menu.setRouter("user");
		System.out.println(c.buildRouter(menu));

		menu.setRouter("/user");
		System.out.println(c.buildRouter(menu));
	}

	@Test
	public void cascadeTest() {
		System.out.println("-------所有菜单-------");
		List<Menu> all = menuService.findAll();
		all.forEach(item -> {
			System.out.println(item);
		});
		System.out.println("-------角色关联菜单-------");
		Set<Menu> role = roleService.get(1).getMenus();
		role.forEach(item -> {
			System.out.println(item);
		});
		System.out.println("-------用户关联角色关联菜单-------");
		authService.get(1).getRoles().forEach(item -> {
			item.getMenus().forEach(menu -> {
				System.out.println(menu);
			});
		});
	}
}
