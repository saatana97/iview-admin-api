package cn.saatana.core.auth.controller;

import java.util.Date;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.Guest;
import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.auth.entity.AuthorizationInformation;
import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.auth.repository.AuthorizerRepository;
import cn.saatana.core.auth.service.AuthorizerService;
import cn.saatana.core.common.CommonController;
import cn.saatana.core.common.Res;
import cn.saatana.core.utils.MD5Utils;

@RestController
@RequestMapping("/auth")
@LogOparetion("登陆授权")
public class AuthorizerController extends CommonController<AuthorizerService, AuthorizerRepository, Authorizer> {

	@Guest
	@PostMapping("login")
	@LogOparetion("登陆")
	public Res<AuthorizationInformation> login(@RequestBody Authorizer user) {
		Authorizer entity = service.getByUsername(user.getUsername());
		System.out.println(entity.getAccessScopes());
		if (user.getPassword() != null) {
			if (user.getPassword().length() < 32) {
				user.setPassword(MD5Utils.encode(user.getPassword()));
			}
			if (entity != null && entity.getPassword().equals(user.getPassword())) {
				AuthorizationInformation userInfo = Safer.login(entity);
				entity.setLoginDate(new Date());
				service.update(entity, false);
				return Res.ok("登陆成功", userInfo);
			}
		}
		return Res.error("用户名或密码不匹配", null);
	}

	@RequestMapping("logout")
	@LogOparetion("注销")
	public Res<String> logout() {
		Safer.logout();
		return Res.ok("你已经注销登录");
	}
}
