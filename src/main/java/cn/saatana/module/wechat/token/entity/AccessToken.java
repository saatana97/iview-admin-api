package cn.saatana.module.wechat.token.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class AccessToken {
	@JsonProperty("access_token")
	private String accessToken;
	// @JsonProperty("refresh_token")
	// private String refreshToken;
	@JsonProperty("openid")
	private String openId;
	// private String scope;
	@JsonProperty("expires_in")
	private int expiresIn;
	private int errcode;
	private String errmsg;
	private long generate = new Date().getTime();

	public boolean success() {
		return this.errcode == 0;
	}
}
