package cn.saatana.module.system.role.controller;

import cn.saatana.module.system.user.entity.User;
import cn.saatana.module.system.user.service.UserService;
import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.module.system.role.entity.Role;
import cn.saatana.module.system.role.repository.RoleRepository;
import cn.saatana.module.system.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController extends CurdController<RoleService, RoleRepository, Role> {
	@Autowired
	private UserService authService;

	@PostMapping("/dispatch/{id}")
    public Res<List<User>> dispatch(@PathVariable String id, @RequestBody List<String> ids) {
		List<User> list = authService.findAllByIds(ids);
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
    public Res<List<User>> undispatch(@PathVariable String id, @RequestBody List<String> ids) {
		List<User> list = authService.findAllByIds(ids);
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
