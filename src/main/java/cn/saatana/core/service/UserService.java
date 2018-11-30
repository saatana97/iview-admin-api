package cn.saatana.core.service;

import org.springframework.stereotype.Service;

import cn.saatana.core.entity.User;
import cn.saatana.core.repository.UserRepository;

@Service
public class UserService extends CommonService<UserRepository, User> {
	public User getByUsername(String username) {
		return repository.getByUsername(username);
	}
}
