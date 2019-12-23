package cn.saatana.module.system.user.controller;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.Guest;
import cn.saatana.module.system.user.entity.UserInfo;
import cn.saatana.module.system.user.entity.User;
import cn.saatana.module.system.user.pojo.RepwdVo;
import cn.saatana.module.system.user.repository.UserRepository;
import cn.saatana.module.system.user.service.UserService;
import cn.saatana.core.common.CurdController;
import cn.saatana.core.common.Res;
import cn.saatana.core.utils.MD5Utils;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController extends CurdController<UserService, UserRepository, User> {

	@Guest
	@PostMapping("login")
	@ApiOperation("用户登录")
	public Res<UserInfo> login(@RequestBody User user) {
		Res<UserInfo> res = Res.error(null);
		if(StringUtils.isEmpty(user.getUsername()) && !StringUtils.isEmpty(user.getCode())){
			String openId = "oRwbN5vw8GHu7HaN-Rj8inHKeGPw";
			User auth = service.getByOpenId(openId);
			if(auth==null){
				auth = new User(openId);
				service.create(auth);
			}else if(!auth.isNormal()){
				service.restore(auth);
			}
			res = Res.ok(Safer.login(auth));
		}else{
			User entity = service.getByUsername(user.getUsername());
			if (user.getPassword() != null) {
				if (user.getPassword().length() < 32) {
					user.setPassword(MD5Utils.encode(user.getPassword()));
				}
				if (entity != null && entity.getPassword().equals(user.getPassword())) {
					UserInfo userInfo = Safer.login(entity);
					entity.setLoginDate(new Date());
					service.update(entity, false);
					res = Res.ok("登陆成功", userInfo);
				}else{
					res = Res.error("用户名或密码不匹配", null);
				}
			}
		}
		return res;
	}

	@RequestMapping("logout")
	@ApiOperation("用户注销")
	public Res<String> logout() {
		Safer.logout();
		return Res.ok("你已经注销登录");
	}

	@ApiOperation("权限鉴定")
	@RequestMapping("permission/{permission}")
	public Res<Boolean> hasPermission(@PathVariable String permission) {
		return Res.ok(Safer.isSuperAdmin() || Safer.hasPromission(permission));
	}

	@ApiOperation("修改密码")
	@PostMapping("repwd")
	public Res<Void> repwd(@RequestBody @Validated RepwdVo vo){
		Res res = null;
		User user = service.get(vo.getId());
		if(user.getPassword().equals(MD5Utils.encode(vo.getPass()))){
			user.setPassword(MD5Utils.encode(vo.getNewPwd()));
			service.update(user);
			res = Res.ok(null);
		}else{
			res = Res.error("原密码校验失败");
		}
		return res;
	}

	@Override
	public Res<User> create(@RequestBody @Validated User entity, BindingResult result)
			throws UnsupportedEncodingException {
		validationHandler(result);
		if (entity != null) {
			if (StringUtils.isEmpty(entity.getPassword())) {
				entity.setPassword("123456");
			}
			if (entity.getPassword().length() < 32) {
				entity.setPassword(MD5Utils.encode(entity.getPassword()));
			}
			if (entity.getOrg() != null && StringUtils.isEmpty(entity.getOrg().getId())) {
				entity.setOrg(null);
			}
		}
		return super.create(entity, result);
	}

	@Override
	public Res<User> update(@RequestBody @Validated User entity, BindingResult result) {
		validationHandler(result);

		return super.update(entity, result);
	}
}
