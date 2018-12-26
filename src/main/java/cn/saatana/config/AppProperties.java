package cn.saatana.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	/**
	 * 是否开启授权TOKEN验证
	 */
	private boolean enableSafer = true;
	/**
	 * 是否记录请求的详细信息
	 */
	private boolean logRequestInfo = true;
	/**
	 * 是否允许本地跨域请求
	 */
	private boolean allowLocalCrossDomain = true;

	public boolean isAllowLocalCrossDomain() {
		return allowLocalCrossDomain;
	}

	public void setAllowLocalCrossDomain(boolean allowLocalCrossDomain) {
		this.allowLocalCrossDomain = allowLocalCrossDomain;
	}

	public boolean isLogRequestInfo() {
		return logRequestInfo;
	}

	public void setLogRequestInfo(boolean logRequestInfo) {
		this.logRequestInfo = logRequestInfo;
	}

	public boolean isEnableSafer() {
		return enableSafer;
	}

	public void setEnableSafer(boolean enableSafer) {
		this.enableSafer = enableSafer;
	}
}
