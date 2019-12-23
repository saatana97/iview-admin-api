package cn.saatana.module.system.role.service;

import cn.saatana.core.common.CurdService;
import cn.saatana.module.system.role.entity.Role;
import cn.saatana.module.system.role.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends CurdService<RoleRepository, Role> {

}
