package cn.saatana.modules.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.common.CommonController;
import cn.saatana.modules.user.entity.User;
import cn.saatana.modules.user.repository.UserRepository;
import cn.saatana.modules.user.service.UserService;

@RestController
@RequestMapping("/user")
@LogOparetion("用户管理")
public class UserController extends CommonController<UserService, UserRepository, User> {
}
