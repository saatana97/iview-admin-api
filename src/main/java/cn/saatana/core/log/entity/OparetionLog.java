package cn.saatana.core.log.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.saatana.core.common.CommonEntity;

/**
 * 操作日志
 *
 * @author 向文可
 *
 */
@Entity
@Table(name = "oparetion_log")
public class OparetionLog extends CommonEntity {
	private static final long serialVersionUID = 1L;
	private String controller;
	private String controllerName;
	private String method;
	private String methodName;
	private String ip;

	@Override
	public String toString() {
		return "[controller=" + controller + ", controllerName=" + controllerName + ", method=" + method
				+ ", methodName=" + methodName + ", ip=" + ip + "]";
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
}
