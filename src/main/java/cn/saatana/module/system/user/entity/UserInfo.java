package cn.saatana.module.system.user.entity;

import lombok.Data;

@Data
public class UserInfo {
	private String sessionId;
	private String token;
	private User auth;

	public UserInfo() {
	}

	public UserInfo(String token, String sessionId, User auth) {
		this.token = token;
		this.sessionId = sessionId;
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "sessionId=" + sessionId + ", token=" + token + ", auth=" + auth.getUsername();
	}

}
