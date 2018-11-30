package cn.saatana.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	private boolean enableSafer = true;

	public boolean isEnableSafer() {
		return enableSafer;
	}

	public void setEnableSafer(boolean enableSafer) {
		this.enableSafer = enableSafer;
	}
}
