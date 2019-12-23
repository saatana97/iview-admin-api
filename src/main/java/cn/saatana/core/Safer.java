package cn.saatana.core;

import cn.saatana.core.annotation.HasPermission.PermissionLogic;
import cn.saatana.module.system.user.entity.UserInfo;
import cn.saatana.module.system.user.entity.User;
import cn.saatana.module.system.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 应用安全类
 *
 * @author 向文可
 *
 */
@Component
public class Safer {
	private static Map<String, UserInfo> redis = new ConcurrentHashMap<>();
	private static UserService authService;

	@Autowired
	public void setAuthService(UserService authService) {
		Safer.authService = authService;
	}

	private static String generateToken() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * 获取当前Request对象
	 *
	 * @return
	 */
	public static HttpServletRequest currentRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取当前Response对象
	 *
	 * @return
	 */
	public static HttpServletResponse currentResponse() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
	}

	/**
	 * 重新保存token刷新时间
	 *
	 * @return
	 */
	public static String restore() {
		return restore(scanToken());
	}

	/**
	 * 重新保存token刷新时间
	 *
	 * @param token
	 * @return
	 */
	public static String restore(String token) {
		return restore(token, null);
	}

	/**
	 * 刷新token寿命
	 *
	 * @param token
	 * @param auth
	 * @return
	 */
	public static String restore(String token, UserInfo auth) {
		if (auth == null) {
			auth = getAuthorizerByToken(token);
		}
		if (auth == null) {
			redis.remove(token);
		} else {
			auth.setAuth(authService.get(auth.getAuth().getId()));
			redis.put(token, auth);
		}
		return token;
	}

	/**
	 * 用户登录
	 *
	 * @param auth 用户
	 * @return 用户登录信息
	 */
	public static UserInfo login(User auth) {
		HttpServletRequest request = currentRequest();
		String token = generateToken();
		UserInfo userInfo = new UserInfo(token, request.getSession().getId(), auth);
		HttpServletResponse response = currentResponse();
		Cookie cookie = new Cookie("token", token);
		cookie.setPath("/");
		response.addCookie(cookie);
		restore(token, userInfo);
		return userInfo;
	}

	/**
	 * 判断当前登录授权者是否拥有指定权限
	 *
	 * @param permission 权限，多个用,隔开
	 * @return
	 */
	public static boolean hasPromission(String permission) {
		return hasPermission(permission, PermissionLogic.ALL);
	}

	/**
	 * 判断当前登录授权者是否拥有指定权限
	 *
	 * @param permission 权限，多个用,隔开
	 * @param logic      存在多个权限时的检验逻辑
	 * @return 是否拥有
	 */
	public static boolean hasPermission(String permission, PermissionLogic logic) {
		return hasPermission(currentAuthId(), permission, logic);
	}

	/**
	 * 判断指定ID的授权者是否拥有指定权限
	 *
	 * @param authId     授权者ID
	 * @param permission 权限，多个用,隔开
	 * @param logic      存在多个权限时的检验逻辑
	 * @return 是否拥有
	 */
	public static boolean hasPermission(String authId, String permission, PermissionLogic logic) {
		boolean res = false;
		if (StringUtils.isEmpty(permission)) {
			res = true;
		} else {
			User auth = authService.get(authId);
			if (auth.getRoles() != null) {
				Set<String> has = new HashSet<>();
				List<String> need = Arrays.asList(permission.split(","));
				auth.getRoles().forEach(role -> {
					role.getMenus().forEach(menu -> {
						has.addAll(Arrays.asList(menu.getPermission().split(",")));
					});
				});
				if (need.size() == 1) {
					res = has.contains(need.get(0));
				} else if (logic == PermissionLogic.ALL) {
					res = has.containsAll(need);
				} else if (logic == PermissionLogic.ANY) {
					for (String item : need) {
						res |= has.contains(item);
						if (res) {
							break;
						}
					}
				}
			}
		}
		return res;
	}

	/**
	 * 注销当前用户
	 */
	public static void logout() {
		redis.remove(currentAuthInfo().getToken());
	}

	/**
	 * 判断当前授权者是否为超级管理员
	 *
	 * @return
	 */
	public static boolean isSuperAdmin() {
		String authId = currentAuthId();
		return !StringUtils.isEmpty(authId) && authId.equals("1");
	}

	/**
	 * 获取当前授权信息
	 *
	 * @return
	 */
	public static UserInfo currentAuthInfo() {
		return getAuthorizerByToken(scanToken());
	}

	/**
	 * 获取当前授权者ID
	 *
	 * @return
	 */
	public static String currentAuthId() {
		String res = null;
		UserInfo authInfo = currentAuthInfo();
		if (authInfo != null) {
			res = authInfo.getAuth().getId();
		}
		return res;
	}

	/**
	 * 根据token获取授权信息
	 *
	 * @param token
	 * @return
	 */
	public static UserInfo getAuthorizerByToken(String token) {
		return redis.get(token);
	}

	/**
	 * 扫描Token，优先级：url参数>request hander>cookie
	 *
	 * @return
	 */
	public static String scanToken() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String token = request.getParameter("token");
		if (StringUtils.isEmpty(token)) {
			token = request.getHeader("Authorization");
			if (StringUtils.isEmpty(token)) {
				token = request.getHeader("token");
			}
		}
		if (StringUtils.isEmpty(token)) {
			Cookie[] cookies = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : Arrays.asList(cookies)) {
					if (cookie.getName().equals("token")) {
						token = cookie.getValue();
					}
				}
			}
		}
		if (token == null) {
			token = "";
		}
		return token;
	}
}
