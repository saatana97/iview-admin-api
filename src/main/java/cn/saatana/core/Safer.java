package cn.saatana.core;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.saatana.core.auth.entity.AuthorizationInformation;
import cn.saatana.core.auth.entity.Authorizer;

/**
 * 应用安全类
 *
 * @author 向文可
 *
 */
@Component
public class Safer {
	private static Map<String, AuthorizationInformation> loginInfo = new ConcurrentHashMap<>();

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
	 * 用户登录
	 *
	 * @param user
	 *            用户
	 * @return token
	 */
	public static AuthorizationInformation login(Authorizer user) {
		HttpServletRequest request = currentRequest();
		HttpServletResponse response = currentResponse();
		String token = generateToken();
		AuthorizationInformation userInfo = new AuthorizationInformation(token, request.getSession().getId(), user);
		loginInfo.put(token, userInfo);
		Cookie cookie = new Cookie("token", token);
		cookie.setPath("/");
		response.addCookie(cookie);
		return userInfo;
	}

	/**
	 * 注销当前用户
	 */
	public static void logout() {
		String token = scanToken();
		loginInfo.remove(token);
	}

	/**
	 * 获取当前授权信息
	 *
	 * @return
	 */
	public static AuthorizationInformation currentAuthInfo() {
		String token = scanToken();
		return loginInfo.get(token);
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
		return loginInfo.get(token);
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
