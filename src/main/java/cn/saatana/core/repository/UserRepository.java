package cn.saatana.core.repository;

import org.springframework.stereotype.Component;

import cn.saatana.core.entity.User;

@Component
public interface UserRepository extends CommonRepository<User> {

	User getByUsername(String username);

}
