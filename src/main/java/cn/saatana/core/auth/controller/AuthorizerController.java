package cn.saatana.core.auth.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
import cn.saatana.core.utils.RedisService;

@RestController
@RequestMapping("/authorizer")
@LogOparetion("登陆授权")
public class AuthorizerController extends CommonController<AuthorizerService, AuthorizerRepository, Authorizer> {
	@Autowired
	private RedisService redis;

	@Guest
	@RequestMapping("redis")
	public String redisTest(String str) throws ParseException {
		String res = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,sss");
		if (redis.exists(str)) {
			System.out.println("获取缓存");
			res = (String) redis.get(str);
			if (System.currentTimeMillis() - sdf.parse(res).getTime() > 30000) {
				System.out.println("清空缓存");
				redis.remove(str);
			}
		} else {
			System.out.println("更新缓存");
			res = sdf.format(new Date());
			redis.set(str, res);
		}
		return res;
	}

	@Guest
	@PostMapping("login")
	@LogOparetion("登陆")
	public Res<AuthorizationInformation> login(@RequestBody Authorizer user) {
		Authorizer entity = service.getByUsername(user.getUsername());
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

	@PostMapping("invalid")
	@LogOparetion(value = "验证授权码", ignore = true)
	public Res<Boolean> invalid(String token) {
		return Res.ok(Safer.getAuthorizerByToken(token) != null);
	}

	@PostMapping("checkRepeat/{username}")
	@LogOparetion(value = "用户名校验", ignore = true)
	public Res<Boolean> checkRepeat(@PathVariable String username) {
		return Res.ok(service.getByUsername(username) == null);
	}
}
