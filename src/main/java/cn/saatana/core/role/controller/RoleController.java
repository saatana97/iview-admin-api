package cn.saatana.core.role.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.auth.service.AuthorizerService;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.role.entity.Role;
import cn.saatana.core.role.repository.RoleRepository;
import cn.saatana.core.role.service.RoleService;

@RestController
@RequestMapping("/role")
@LogOparetion("角色管理")
public class RoleController extends CommonController<RoleService, RoleRepository, Role> {
	@Autowired
	private AuthorizerService authService;

	@PostMapping("/dispatch/{id}")
	@LogOparetion("分配用户角色")
	public Res<List<Authorizer>> dispatch(@PathVariable Integer id, @RequestBody List<Integer> ids) {
		List<Authorizer> list = authService.findAllByIds(ids);
		Role role = service.get(id);
		list.forEach(item -> {
			if (!item.getRoles().contains(role)) {
				item.getRoles().add(role);
			}
		});
		authService.updateAll(list);
		return Res.ok(list);
	}

	@PostMapping("/undispatch/{id}")
	@LogOparetion("移除用户角色")
	public Res<List<Authorizer>> undispatch(@PathVariable Integer id, @RequestBody List<Integer> ids) {
		List<Authorizer> list = authService.findAllByIds(ids);
		Role role = service.get(id);
		list.forEach(item -> {
			if (item.getRoles().contains(role)) {
				item.getRoles().remove(role);
			}
		});
		authService.updateAll(list);
		return Res.ok(list);
	}
}
