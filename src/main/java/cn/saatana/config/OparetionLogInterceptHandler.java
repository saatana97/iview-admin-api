package cn.saatana.config;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.saatana.core.annotation.LogOparetion;
import cn.saatana.core.log.entity.OparetionLog;
import cn.saatana.core.log.service.OparetionLogService;
import cn.saatana.core.utils.IPUtils;

@Configuration
public class OparetionLogInterceptHandler extends HandlerInterceptorAdapter {
	private final Logger log = Logger.getLogger("GlobalInterceptHandler");
	@Autowired
	private OparetionLogService oparetionLogService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = true;
		if (handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Class<?> controller = method.getBeanType();
			LogOparetion canno = controller.getAnnotation(LogOparetion.class);
			LogOparetion manno = method.getMethodAnnotation(LogOparetion.class);
			OparetionLog oparetionLog = new OparetionLog();
			oparetionLog.setController(controller.getName());
			oparetionLog.setMethod(method.getMethod().getName());
			if (canno != null) {
				if (canno.ignore()) {
					return result;
				}
				oparetionLog.setControllerName(canno.value());
			}
			if (manno != null) {
				if (manno.ignore()) {
					return result;
				}
				oparetionLog.setMethodName(manno.value());
			}
			oparetionLog.setIp(IPUtils.getIP(request));
			oparetionLogService.create(oparetionLog);
			log.fine("记录操作日志：" + oparetionLog.toString());
		}
		return result;
	}

}
