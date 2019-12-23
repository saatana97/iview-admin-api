package cn.saatana.module.wechat;

import cn.saatana.config.AppProperties;
import cn.saatana.core.utils.HttpUtils;
import cn.saatana.module.wechat.token.entity.AccessToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Wechat {
	private static final Logger log = Logger.getLogger(Wechat.class.getName());
	private static AccessToken accessToken;
	private static AppProperties app;

	@Autowired
	public void setAppProperties(AppProperties app) {
		Wechat.app = app;
	}

	public static String getOpenIdByCode(String code) {
		String res = null;
		try {
			String str = HttpUtils.get(String.format(
					"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
					app.getAppid(), app.getAppsecret(), code), null, String.class);
			AccessToken accessToken = new ObjectMapper().reader().forType(AccessToken.class).readValue(str);
			if (accessToken != null && accessToken.success()) {
				res = accessToken.getOpenId();
			} else {
				log.warning("通过CODE换取OPENID失败：\t" + accessToken.getErrcode() + "\t" + accessToken.getErrmsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static String getAccessToken() {
		String res = "";
		int retry = app.getAccessTokenRetryTime();
		if (accessToken == null || accessToken.getGenerate() + accessToken.getExpiresIn() * 1000
				- System.currentTimeMillis() <= 60000) {
			do {
				Wechat.accessToken = HttpUtils
						.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
								+ app.getAppid() + "&secret=" + app.getAppsecret(), null, AccessToken.class);
			} while (retry-- > 0 && Wechat.accessToken.getErrcode() != 0);
		}
		if (accessToken != null) {
			res = accessToken.getAccessToken();
		} else {
			log.warning("获取access_token失败");
		}
		return res;
	}
}
