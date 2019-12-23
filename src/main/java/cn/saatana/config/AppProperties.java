package cn.saatana.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
	/**
	 * 微信服务器TOKEN
	 */
	private String token = "saatana";
	/**
	 * 微信公众号appid
	 */
	private String appid;
	/**
	 * 微信公众号appsercret;
	 */
	private String appsecret;
	/**
	 * 获取access_token最大重试次数
	 */
	private int accessTokenRetryTime = 3;

	/**
	 * 是否开启授权TOKEN验证
	 */
	private boolean enableSafer = true;
	/**
	 * 是否记录请求的详细信息
	 */
	private boolean logRequestInfo = true;
	/**
	 * 是否记录操作日志
	 */
	private boolean logOparetion = true;
	/**
	 * 是否允许本地跨域请求
	 */
	private boolean allowLocalCrossDomain = true;
	/**
	 * TOKEN存活时间，默认300，单位秒，设置30以下默认永不过期
	 */
	private long tokenLife = 60l * 5;
	/**
	 * 文件默认保存目录
	 */
	private String fileBaseDir = "C:/Uploads/WechatAdmin/";
	/**
	 * 文件默认上传临时目录
	 */
	private String fileTempDir = "C:/Uploads/WechatAdmin/temp/";
}
