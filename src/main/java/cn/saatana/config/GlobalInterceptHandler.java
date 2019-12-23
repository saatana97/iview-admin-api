package cn.saatana.config;

import cn.saatana.core.Safer;
import cn.saatana.core.annotation.Admin;
import cn.saatana.core.annotation.Guest;
import cn.saatana.core.annotation.HasPermission;
import cn.saatana.core.common.Res;
import cn.saatana.core.utils.IPUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
public class GlobalInterceptHandler extends HandlerInterceptorAdapter {
	private final Logger log = Logger.getLogger("GlobalInterceptHandler");
	@Autowired
	private AppProperties appProp;
	@Autowired
	private TextProperties textProp;
	private final ObjectMapper JSON = new ObjectMapper();

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
			String authId = Safer.currentAuthId();
			if (authId == null) {
				if (StringUtils.isEmpty(Safer.scanToken())) {
					// 未登录授权的提示语
					JSON.writeValue(response.getWriter(),Res.of(HttpStatus.UNAUTHORIZED.value(), textProp.getUnauthorizedMessage(), null));
				} else {
					// 登录信息失效的提示语
					JSON.writeValue(response.getWriter(),Res.of(HttpStatus.UNAUTHORIZED.value(), textProp.getInvalidTokenMessage(), null));
				}
				result = false;
			} else if ((needSuperAdmin(handler) && !Safer.isSuperAdmin()) || !hasPersisson(handler, authId)) {
				// 没有访问权限的提示语
				JSON.writeValue(response.getWriter(),Res.of(HttpStatus.UNAUTHORIZED.value(),
						textProp.getNoAccessMessage(), textProp.getNoAccessMessage()));
				result = false;
			}
		}
		if (result && appProp.isLogRequestInfo()) {
			// 授权通过就记录请求信息
			logRequestInfo(request, result);
		}
		return result;
	}

	private boolean hasPersisson(Object handler, String authId) {
		boolean result = Safer.isSuperAdmin();
		if (!result && handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Class<?> controller = method.getBeanType();
			HasPermission canno = controller.getAnnotation(HasPermission.class);
			HasPermission manno = method.getMethodAnnotation(HasPermission.class);
			if (canno != null && !StringUtils.isEmpty(canno.value())) {
				// 控制器指定了权限
				result = Safer.hasPermission(authId, canno.value(), canno.logic());
			}
			if (manno != null && !StringUtils.isEmpty(manno.value())) {
				// 方法指定了权限
				result = Safer.hasPermission(authId, manno.value(), manno.logic());
			}
			if ((canno == null || StringUtils.isEmpty(canno.value()))
					&& (manno == null || StringUtils.isEmpty(manno.value()))) {
				// 都没有指定权限
				result = true;
			}
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

	private boolean needSuperAdmin(Object handler) {
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
