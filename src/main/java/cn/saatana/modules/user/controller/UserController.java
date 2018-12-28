package cn.saatana.modules.user.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.utils.MD5Utils;
import cn.saatana.modules.user.entity.User;
import cn.saatana.modules.user.repository.UserRepository;
import cn.saatana.modules.user.service.UserService;

@RestController
@RequestMapping("/user")
@LogOparetion("用户管理")
public class UserController extends CommonController<UserService, UserRepository, User> {

	@Override
	public Res<User> create(@RequestBody @Validated User entity, BindingResult result)
			throws UnsupportedEncodingException {
		validationHandler(result);
		if (entity != null) {
			if (entity.getAuthorizer() == null) {
				entity.setAuthorizer(new Authorizer(null, "123456"));
			} else if (StringUtils.isEmpty(entity.getAuthorizer().getPassword())) {
				entity.getAuthorizer().setPassword("123456");
			}
			if (entity.getAuthorizer().getPassword().length() < 32) {
				entity.getAuthorizer().setPassword(MD5Utils.encode(entity.getAuthorizer().getPassword()));
			}
		}
		return super.create(entity, result);
	}

	@Override
	public Res<User> update(@RequestBody @Validated User entity, BindingResult result) {
		validationHandler(result);
		if (entity != null) {
			if (entity.getAuthorizer() == null) {
				entity.setAuthorizer(new Authorizer(null, "123456"));
			} else if (StringUtils.isEmpty(entity.getAuthorizer().getPassword())) {
				entity.getAuthorizer().setPassword("123456");
			}
			if (entity.getAuthorizer().getPassword().length() < 32) {
				entity.getAuthorizer().setPassword(MD5Utils.encode(entity.getAuthorizer().getPassword()));
			}
		}
		return super.update(entity, result);
	}

}
