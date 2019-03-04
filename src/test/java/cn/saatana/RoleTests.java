package cn.saatana;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;

import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.auth.service.AuthorizerService;
import cn.saatana.core.role.entity.Role;
import cn.saatana.core.role.service.RoleService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleTests {
	@Autowired
	private RoleService roleService;
	@Autowired
	private AuthorizerService authService;

	@Test
	public void accessScopeTest() {
		Authorizer auth = authService.get(7);
		System.out.println(auth.getAccessScopes());
	}

	public void cascadeTest() {
		roleService.findPage(new Role()).getContent().forEach(role -> {
			System.out.println("分页查询出来的角色的菜单\t" + role.getMenusName());
		});
		roleService.findList(new Role()).forEach(role -> {
			System.out.println("条件查询出来的角色的菜单\t" + role.getMenusName());
		});
		roleService.findAll().forEach(role -> {
			System.out.println("查询全部出来的角色的菜单\t" + role.getMenusName());
		});
		System.out.println("主键查询出来的角色的菜单\t" + roleService.get(1).getMenusName());
	}

	public void menuTest() {
		Role role = roleService.get(1);
		System.out.println("通过角色ID查询\t" + role.getMenusName());
		Authorizer auth = authService.get(1);
		auth.getRoles().forEach(item -> {
			System.out.println("通过用户关联角色查询\t" + item.getMenusName());
		});
		System.out.println("使用fastjson转换\t" + JSON.toJSONString(auth));
	}

}
