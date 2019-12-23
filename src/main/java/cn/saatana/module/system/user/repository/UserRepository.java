package cn.saatana.module.system.user.repository;

import cn.saatana.module.system.user.entity.User;
import cn.saatana.core.common.CurdRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CurdRepository<User> {

	User getByUsername(String username);
	
	User getByOpenId(String openId);
}
