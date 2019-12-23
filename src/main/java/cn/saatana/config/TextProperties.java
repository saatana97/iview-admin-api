package cn.saatana.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
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
}
