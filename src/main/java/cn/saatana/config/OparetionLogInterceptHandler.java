package cn.saatana.config;

import cn.saatana.core.Safer;
import cn.saatana.module.system.log.entity.OparetionLog;
import cn.saatana.module.system.log.service.OparetionLogService;
import cn.saatana.core.utils.IPUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

@Configuration
public class OparetionLogInterceptHandler extends HandlerInterceptorAdapter {
	private final Logger log = Logger.getLogger("GlobalInterceptHandler");
	@Autowired
	private OparetionLogService oparetionLogService;
	@Autowired
	private AppProperties appProp;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		boolean result = true;
		if (appProp.isLogOparetion() && handler instanceof HandlerMethod) {
			HandlerMethod method = (HandlerMethod) handler;
			Class<?> controller = method.getBeanType();
			ApiOperation canno = controller.getAnnotation(ApiOperation.class);
			ApiOperation manno = method.getMethodAnnotation(ApiOperation.class);
			OparetionLog oparetionLog = new OparetionLog();
			oparetionLog.setController(controller.getName());
			oparetionLog.setMethod(method.getMethod().getName());
			if (canno != null) {
				oparetionLog.setControllerName(canno.value());
			}
			if (manno != null) {
				oparetionLog.setMethodName(manno.value());
			}
			oparetionLog.setIp(IPUtils.getIP(request));
			oparetionLogService.create(oparetionLog);
			log.fine("记录操作日志：" + oparetionLog.toString());
		}
		return result;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		Safer.restore();
		super.postHandle(request, response, handler, modelAndView);
	}

}
