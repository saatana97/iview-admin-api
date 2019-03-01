package cn.saatana.core;

import java.util.Arrays;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSONObject;

import cn.saatana.config.AppProperties;
import cn.saatana.core.auth.entity.AuthorizationInformation;
import cn.saatana.core.auth.entity.Authorizer;
import cn.saatana.core.utils.RedisService;

/**
 * 应用安全类
 *
 * @author 向文可
 *
 */
@Component
public class Safer {
	private static AppProperties appProp;
	private static RedisService redis;

	@Autowired
	public void setRedis(RedisService redis) {
		Safer.redis = redis;
	}

	@Autowired
	public void setAppProp(AppProperties appProp) {
		Safer.appProp = appProp;
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
	public static String restore(String token, AuthorizationInformation auth) {
		if (auth == null) {
			auth = getAuthorizerByToken(token);
		}
		if (auth == null) {
			redis.remove(token);
		} else {
			redis.set(token, auth, appProp.getTokenLife());
		}
		return token;
	}

	/**
	 * 用户登录
	 *
	 * @param user
	 *            用户
	 * @return token
	 */
	public static AuthorizationInformation login(Authorizer user) {
		HttpServletRequest request = currentRequest();
		String token = generateToken();
		AuthorizationInformation userInfo = new AuthorizationInformation(token, request.getSession().getId(), user);
		HttpServletResponse response = currentResponse();
		Cookie cookie = new Cookie("token", token);
		cookie.setPath("/");
		response.addCookie(cookie);
		restore(token, userInfo);
		return userInfo;
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
		return currentAuthId() == 1;
	}

	/**
	 * 获取当前授权信息
	 *
	 * @return
	 */
	public static AuthorizationInformation currentAuthInfo() {
		return getAuthorizerByToken(scanToken());
	}

	/**
	 * 获取当前授权者ID
	 *
	 * @return
	 */
	public static Integer currentAuthId() {
		Integer res = 0;
		AuthorizationInformation authInfo = currentAuthInfo();
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
	public static AuthorizationInformation getAuthorizerByToken(String token) {
		AuthorizationInformation res = null;
		Object obj = redis.get(token);
		if (obj != null) {
			res = ((JSONObject) obj).toJavaObject(AuthorizationInformation.class);
		}
		return res;
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
