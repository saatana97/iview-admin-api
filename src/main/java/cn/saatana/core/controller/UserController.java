package cn.saatana.core.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
import cn.saatana.core.utils.MD5Encoder;

@RestController
@RequestMapping("/user")
public class UserController extends CommonController<UserService, UserRepository, User> {

	@Guest
	@PostMapping("login")
	public Res<UserInfo> login(@Validated User user, BindingResult result) {
		validationHandler(result);
		User entity = service.getByUsername(user.getUsername());
		if (entity != null && MD5Encoder.encode(user.getPassword()).equals(entity.getPassword())) {
			UserInfo userInfo = Safer.login(entity);
			entity.setLoginDate(new Date());
			service.update(entity, false);
			return Res.ok("登陆成功", userInfo);
		} else {
			return Res.error("用户名或密码不匹配", null);
		}
	}

	@RequestMapping("logout")
	public Res<String> logout() {
		Safer.logout();
		return Res.ok("你已经注销登录");
	}

	@Admin
	@Override
	@PostMapping("create")
	public Res<User> create(@Validated User user, BindingResult result) throws UnsupportedEncodingException {
		user.setPassword(MD5Encoder.encode(user.getPassword()));
		return super.create(user, result);
	}

	@Override
	@PostMapping("update")
	public Res<User> update(@Validated User user, BindingResult result) {
		user.setPassword(MD5Encoder.encode(user.getPassword()));
		return super.update(user, result);
	}
}
