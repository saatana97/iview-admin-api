package cn.saatana.core.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.Admin;
import cn.saatana.core.annotation.Guest;
import cn.saatana.core.entity.Res;
import cn.saatana.core.entity.User;
import cn.saatana.core.entity.UserInfo;
import cn.saatana.core.repository.UserRepository;
import cn.saatana.core.service.UserService;

@Admin
@RestController
@RequestMapping("/user")
public class UserController extends CommonController<UserService, UserRepository, User> {

	@Guest
	@PostMapping("login")
	public Res<UserInfo> login(User user) {
		User entity = service.getByUsername(user.getUsername());
		if (entity != null && entity.getPassword().equals(user.getPassword())) {
			UserInfo userInfo = Safer.login(entity);
			entity.setLoginDate(new Date());
			service.update(entity, false);
			return Res.ok("登陆成功", userInfo);
		} else {
			return Res.error("用户名或密码不匹配", null);
		}
	}

	@Admin(false)
	@RequestMapping("logout")
	public Res<String> logout() {
		Safer.logout();
		return Res.ok("你已经注销登录");
	}
}
