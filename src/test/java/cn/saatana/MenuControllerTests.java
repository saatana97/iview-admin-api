package cn.saatana;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.saatana.core.menu.controller.MenuController;
import cn.saatana.core.menu.entity.Menu;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuControllerTests {
	@Autowired
	private MenuController c;

	@Test
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
}
