package cn.saatana.module.system.user.service;

import cn.saatana.module.system.user.entity.User;
import cn.saatana.module.system.user.repository.UserRepository;
import cn.saatana.core.common.CurdService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends CurdService<UserRepository, User> {
	public User getByUsername(String username) {
		return repository.getByUsername(username);
	}
	public User getByOpenId(String openId) {
		return repository.getByOpenId(openId);
	}
	
}
