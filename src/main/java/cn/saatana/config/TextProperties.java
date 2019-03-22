package cn.saatana.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "text")
public class TextProperties {
	@Autowired
	private AppProperties appProp;
	/**
	 * 未登录授权的提示语
	 */
	private String unauthorizedMessage = "请登录后再尝试访问当前资源";
	/**
	 * 登录信息失效的提示语
	 */
	private String invalidTokenMessage = "你的登录信息已失效";
	/**
	 * 无权访问的提示语
	 */
	private String noAccessMessage = "你无权访问当前资源";
	/**
	 * 文件超过限制时的提示语，可以使用占位符${fileMaxSize}替换文件大小限制具体数值
	 */
	private String fileSizeOutOfRangeMessage = "文件大小不能超过${fileMaxSize}KB";

	public String getFileSizeOutOfRangeMessage() {
		return fileSizeOutOfRangeMessage.replace("${fileMaxSize}", appProp.getFileMaxSize() + "");
	}

	public void setFileSizeOutOfRangeMessage(String fileSizeOutOfRangeMessage) {
		this.fileSizeOutOfRangeMessage = fileSizeOutOfRangeMessage;
	}

	public String getUnauthorizedMessage() {
		return unauthorizedMessage;
	}

	public void setUnauthorizedMessage(String unauthorizedMessage) {
		this.unauthorizedMessage = unauthorizedMessage;
	}

	public String getInvalidTokenMessage() {
		return invalidTokenMessage;
	}

	public void setInvalidTokenMessage(String invalidTokenMessage) {
		this.invalidTokenMessage = invalidTokenMessage;
	}

	public String getNoAccessMessage() {
		return noAccessMessage;
	}

	public void setNoAccessMessage(String noAccessMessage) {
		this.noAccessMessage = noAccessMessage;
	}

}
