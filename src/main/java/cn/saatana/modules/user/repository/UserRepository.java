package cn.saatana.modules.user.repository;

import org.springframework.stereotype.Component;

import cn.saatana.core.common.CommonRepository;
import cn.saatana.modules.user.entity.User;

@Component
public interface UserRepository extends CommonRepository<User> {
	User getByAuthorizerId(Integer authorizerId);
}
