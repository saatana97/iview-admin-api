package cn.saatana.module.system.role.repository;

import cn.saatana.core.common.CurdRepository;
import cn.saatana.module.system.role.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CurdRepository<Role> {

}
