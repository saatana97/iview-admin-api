package cn.saatana.core.role.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.role.entity.Role;
import cn.saatana.core.role.repository.RoleRepository;
import cn.saatana.core.role.service.RoleService;

@RestController
@RequestMapping("/role")
@LogOparetion("角色管理")
public class RoleController extends CommonController<RoleService, RoleRepository, Role> {

}
