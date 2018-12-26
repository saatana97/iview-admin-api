package cn.saatana.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "text")
public class TextProperties {
	/**
	 * 未登录授权的提示语
	 */
	private String unauthorizedMessage = "未经授权的访问";
	/**
	 * 未登录授权的返回数据
	 */
	private String unauthorizedData = "请登录后再尝试访问本资源";
	/**
	 * 登录信息失效的提示语
	 */
	private String invalidTokenMessage = "你的登录信息已失效";
	/**
	 * 登录信息失效的返回数据
	 */
	private String invalidTokenData = "请重新登录再尝试访问本资源";
	/**
	 * 无权访问的提示语
	 */
	private String noAccessMessage = "无权访问";
	/**
	 * 无权访问的返回数据
	 */
	private String noAccessData = "只有管理员才有权访问当前资源";

	public String getUnauthorizedMessage() {
		return unauthorizedMessage;
	}

	public void setUnauthorizedMessage(String unauthorizedMessage) {
		this.unauthorizedMessage = unauthorizedMessage;
	}

	public String getUnauthorizedData() {
		return unauthorizedData;
	}

	public void setUnauthorizedData(String unauthorizedData) {
		this.unauthorizedData = unauthorizedData;
	}

	public String getInvalidTokenMessage() {
		return invalidTokenMessage;
	}

	public void setInvalidTokenMessage(String invalidTokenMessage) {
		this.invalidTokenMessage = invalidTokenMessage;
	}

	public String getInvalidTokenData() {
		return invalidTokenData;
	}

	public void setInvalidTokenData(String invalidTokenData) {
		this.invalidTokenData = invalidTokenData;
	}

	public String getNoAccessMessage() {
		return noAccessMessage;
	}

	public void setNoAccessMessage(String noAccessMessage) {
		this.noAccessMessage = noAccessMessage;
	}

	public String getNoAccessData() {
		return noAccessData;
	}

	public void setNoAccessData(String noAccessData) {
		this.noAccessData = noAccessData;
	}

}
