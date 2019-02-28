package cn.saatana.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.Admin;
import cn.saatana.core.annotation.Guest;
import cn.saatana.core.auth.entity.AuthorizationInformation;
import cn.saatana.core.common.Res;
import cn.saatana.core.utils.IPUtils;

@Configuration
public class GlobalInterceptHandler extends HandlerInterceptorAdapter {
	private final Logger log = Logger.getLogger("GlobalInterceptHandler");
	@Autowired
	private AppProperties appProp;
	@Autowired
	private TextProperties textProp;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = true;
		// 允许本地请求跨域访问
		if (appProp.isAllowLocalCrossDomain() && request.getRemoteHost().equals("0:0:0:0:0:0:0:1")) {
			response.addHeader("Access-Control-Allow-Origin", "*");
		}
		// 设置响应类型及编码
		response.setContentType("application/json;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		// 判断是否启用了权限系统，判断当前访问的资源是否需要登陆授权
		if (appProp.isEnableSafer() && needAuthorized(handler)) {
			AuthorizationInformation user = Safer.currentAuthInfo();
			if (user == null) {
				if (StringUtils.isEmpty(Safer.scanToken())) {
					// 未登录授权的提示语
					response.getWriter().println(JSON.toJSON(Res.of(HttpStatus.UNAUTHORIZED.value(),
							textProp.getUnauthorizedMessage(), textProp.getUnauthorizedData())));
				} else {
					// 登录信息失效的提示语
					response.getWriter().println(JSON.toJSON(Res.of(HttpStatus.UNAUTHORIZED.value(),
							textProp.getInvalidTokenMessage(), textProp.getInvalidTokenData())));
				}
				result = false;
			} else if (needAdmin(handler) && !user.getAuth().getUsername().equals("admin")) {
				// 需要管理员权限的提示语
				response.getWriter().println(JSON.toJSON(Res.of(HttpStatus.UNAUTHORIZED.value(),
						textProp.getNoAccessMessage(), textProp.getNoAccessMessage())));
				result = false;
			} else {
				// 刷新token存活时间
				Safer.restore(Safer.scanToken());
			}
		}
		if (appProp.isLogRequestInfo()) {
			// 记录请求信息
			logRequestInfo(request, result);
		}
		return result;
	}

	private boolean needAuthorized(Object handler) {
		boolean result = false;
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Class<?> controller = method.getBeanType();
			Guest canno = controller.getAnnotation(Guest.class);
			Guest manno = method.getMethodAnnotation(Guest.class);
			if (canno == null || !canno.value()) {
				if (manno == null || !manno.value()) {
					result = true;
				}
			} else if (manno != null && !manno.value()) {
				result = true;
			}
		}
		return result;
	}

	private boolean needAdmin(Object handler) {
		boolean result = false;
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Class<?> controller = method.getBeanType();
			Admin canno = controller.getAnnotation(Admin.class);
			Admin manno = method.getMethodAnnotation(Admin.class);
			if (canno != null && canno.value()) {
				if (manno == null || manno.value()) {
					result = true;
				}
			} else if (manno != null && manno.value()) {
				result = true;
			}
		}
		return result;
	}

	private synchronized void logRequestInfo(HttpServletRequest request, boolean auth) {
		log.log(Level.INFO, "==========================================================================");
		log.log(Level.INFO, "SESSIONID:\t" + request.getSession().getId());
		log.log(Level.INFO, "IP:\t" + IPUtils.getIP(request));
		log.log(Level.INFO, "USERINFO:\t" + Safer.currentAuthInfo());
		log.log(Level.INFO, "URI:\t" + request.getRequestURI());
		log.log(Level.INFO, "AUTH:\t" + auth);
		log.log(Level.INFO, "METHOD:\t" + request.getMethod());
		log.log(Level.INFO, "QUERY:\t" + request.getQueryString());
		StringBuilder sb = new StringBuilder();
		sb.append("PARAMS:\t");
		request.getParameterMap().forEach((key, value) -> {
			sb.append(key);
			sb.append("=");
			for (String str : value) {
				sb.append(str);
			}
			sb.append("\t");
		});
		log.log(Level.INFO, sb.toString());
		log.log(Level.INFO, "==========================================================================\n");
	}

}
